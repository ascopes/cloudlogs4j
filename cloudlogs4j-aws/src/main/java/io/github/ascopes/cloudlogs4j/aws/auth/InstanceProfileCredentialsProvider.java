/*
 * Copyright (C) 2022 Ashley Scopes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ascopes.cloudlogs4j.aws.auth;

import static java.util.Objects.requireNonNullElse;

import io.avaje.jsonb.Jsonb;
import io.github.ascopes.cloudlogs4j.aws.AwsHeaders;
import io.github.ascopes.cloudlogs4j.aws.UserAgent;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsException;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsHttpResponseException;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsIoException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import org.jspecify.annotations.Nullable;

/**
 * Credential provider that queries the AWS EC2 Instance Metadata service to fetch credentials
 * associated with the EC2 instance profile.
 *
 * <p>This will attempt to re-fetch credentials once they expire.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class InstanceProfileCredentialsProvider extends LazyLoadedCredentialsProvider {

  private static final Charset RESPONSE_CHARSET = StandardCharsets.ISO_8859_1;

  private static final String AWS_EC2_METADATA_SERVICE_OVERRIDE_SYSTEM_PROPERTY
      = "com.amazonaws.sdk.ec2MetadataServiceEndpointOverride";
  private static final String AWS_EC2_METADATA_SERVICE_ENDPOINT_ENVIRONMENT_VARIABLE
      = "AWS_EC2_METADATA_SERVICE_ENDPOINT";
  private static final String AWS_METADATA_SERVICE_TIMEOUT_ENVIRONMENT_VARIABLE
      = "AWS_METADATA_SERVICE_TIMEOUT";
  private static final int AWS_METADATA_SERVICE_DEFAULT_TIMEOUT = 5_000;
  private static final String AWS_DEFAULT_EC2_METADATA_ENDPOINT = "http://169.254.169.254";
  private static final String AWS_SECURITY_CREDENTIALS_ENDPOINT
      = "/latest/meta-data/iam/security-credentials/";
  private static final Duration RESET_CREDENTIALS_OFFSET = Duration.ofMinutes(15);

  private final Jsonb jsonb;
  private final String instanceMetadataServiceHost;

  /**
   * Initialise this provider.
   *
   * @param clock the clock to use to determine when we reset credentials.
   */
  public InstanceProfileCredentialsProvider(Clock clock) {
    this(getEc2MetadataServiceHost(), clock);
  }

  /**
   * Initialise this provider.
   *
   * @param instanceMetadataServiceHost the instance metadata service host to use.
   * @param clock                       the clock to use to determine when we reset credentials.
   */
  public InstanceProfileCredentialsProvider(String instanceMetadataServiceHost, Clock clock) {
    super(clock);
    jsonb = Jsonb.builder().build();
    this.instanceMetadataServiceHost = instanceMetadataServiceHost;
  }

  @Override
  @Nullable
  protected AwsCredentials fetchCredentials() throws AwsException {
    var credentials = fetch(AWS_SECURITY_CREDENTIALS_ENDPOINT);

    if (credentials == null) {
      return null;
    }

    var securityCredentialId = credentials.split("\n")[0];
    var credentialResponse = fetch(AWS_SECURITY_CREDENTIALS_ENDPOINT + securityCredentialId);
    var credentialPayload = jsonb
        .type(InstanceMetadataCredentials.class)
        .fromJson(credentialResponse);

    var resetEpoch = credentialPayload
        .expirationDateTime()
        .minus(RESET_CREDENTIALS_OFFSET)
        .toEpochSecond() * 1_000L;

    resetCredentialsAfter(resetEpoch);

    return new AwsCredentials(
        credentialPayload.accessKey(),
        credentialPayload.secretKey(),
        credentialPayload.token()
    );
  }

  @Nullable
  private String fetch(String endpoint) throws AwsException {
    var timeout = getTimeout();

    var uri = URI.create(instanceMetadataServiceHost + endpoint);

    try {
      var conn = (HttpURLConnection) uri.toURL().openConnection();

      conn.setConnectTimeout(timeout);
      conn.setDoInput(false);
      conn.setDoOutput(true);
      conn.setReadTimeout(timeout);
      conn.setRequestMethod("GET");
      conn.setRequestProperty(AwsHeaders.ACCEPT, "*/*");
      conn.setRequestProperty(AwsHeaders.CONNECTION, "close");
      conn.setRequestProperty(AwsHeaders.USER_AGENT, UserAgent.USER_AGENT);
      conn.connect();

      var status = conn.getResponseCode();

      if (status == 200) {
        var response = readStream(conn.getInputStream());
        conn.disconnect();
        return response;

      } else {
        var errorBody = readStream(conn.getErrorStream());
        conn.disconnect();
        throw new AwsHttpResponseException("GET", uri, status, errorBody);
      }

    } catch (SocketTimeoutException ex) {
      // This means there is no instance metadata service available, so we should skip this
      // step.
      return null;

    } catch (MalformedURLException | ClassCastException ex) {
      throw new IllegalArgumentException(
          "Invalid HTTP URL provided for the instance metadata service: " + uri,
          ex
      );

    } catch (IOException ex) {
      throw new AwsIoException("Failed to fetch instance profile resource " + uri, ex);
    }
  }

  private static String getEc2MetadataServiceHost() {
    var host = System.getProperty(AWS_EC2_METADATA_SERVICE_OVERRIDE_SYSTEM_PROPERTY);
    if (host == null) {
      host = System.getenv(AWS_EC2_METADATA_SERVICE_ENDPOINT_ENVIRONMENT_VARIABLE);
    }
    return requireNonNullElse(host, AWS_DEFAULT_EC2_METADATA_ENDPOINT);
  }

  private static int getTimeout() {
    var timeoutString = System.getenv(AWS_METADATA_SERVICE_TIMEOUT_ENVIRONMENT_VARIABLE);
    return timeoutString == null
        ? AWS_METADATA_SERVICE_DEFAULT_TIMEOUT
        : Integer.parseInt(timeoutString);
  }

  private static String readStream(InputStream inputStream) throws IOException {
    try (inputStream) {
      return new String(inputStream.readAllBytes(), RESPONSE_CHARSET);
    }
  }

}
