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

import io.avaje.http.client.HttpException;
import io.github.ascopes.cloudlogs4j.aws.ec2.Ec2InstanceMetadataClient;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsException;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsHttpResponseException;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsIoException;
import org.jspecify.annotations.Nullable;

import java.net.http.HttpTimeoutException;
import java.time.Clock;
import java.time.Duration;

import static java.util.Objects.requireNonNullElse;

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

  private static final String AWS_EC2_METADATA_SERVICE_OVERRIDE_SYSTEM_PROPERTY
      = "com.amazonaws.sdk.ec2MetadataServiceEndpointOverride";
  private static final String AWS_EC2_METADATA_SERVICE_ENDPOINT_ENVIRONMENT_VARIABLE
      = "AWS_EC2_METADATA_SERVICE_ENDPOINT";
  private static final String AWS_DEFAULT_EC2_METADATA_ENDPOINT = "http://169.254.169.254";
  private static final Duration RESET_CREDENTIALS_OFFSET = Duration.ofMinutes(15);

  private final String instanceMetadataServiceEndpoint;

  /**
   * Initialise this provider.
   */
  public InstanceProfileCredentialsProvider() {
    this(getEc2MetadataServiceEndpoint(), Clock.systemUTC());
  }

  /**
   * Initialise this provider.
   *
   * @param instanceMetadataServiceEndpoint the instance metadata service host to use.
   * @param clock                           the clock to use to determine when we reset
   *                                        credentials.
   */
  public InstanceProfileCredentialsProvider(String instanceMetadataServiceEndpoint, Clock clock) {
    super(clock);
    this.instanceMetadataServiceEndpoint = instanceMetadataServiceEndpoint;
  }

  @Override
  @Nullable
  protected AwsCredentials fetchCredentials() throws AwsException {
    try {
      var client = Ec2InstanceMetadataClient.createClient(instanceMetadataServiceEndpoint);

      var credentialsList = client.getSecurityCredentials().body().split("\n");
      if (credentialsList.length == 0) {
        // No instance metadata credentials available.
        return null;
      }

      var securityCredential = client.getSecurityCredential(credentialsList[0]).body();

      resetCredentialsAfter(securityCredential.expirationDateTime()
          .minus(RESET_CREDENTIALS_OFFSET)
          .toEpochSecond() * 1_000);

      if (securityCredential.token() == null) {
        return new AwsCredentials(securityCredential.accessKey(), securityCredential.secretKey());
      } else {
        return new AwsCredentials(
            securityCredential.accessKey(),
            securityCredential.secretKey(),
            securityCredential.token()
        );
      }

    } catch (HttpException ex) {
      if (ex.getCause() instanceof HttpTimeoutException) {
        // Instance metadata service did not response, so skip it.
        return null;
      } else if (ex.getCause() != null) {
        throw new AwsIoException("Failed to call the instance metadata service", ex);
      } else {
        throw new AwsHttpResponseException(ex);
      }
    }
  }

  private static String getEc2MetadataServiceEndpoint() {
    var host = System.getProperty(AWS_EC2_METADATA_SERVICE_OVERRIDE_SYSTEM_PROPERTY);
    if (host == null) {
      host = System.getenv(AWS_EC2_METADATA_SERVICE_ENDPOINT_ENVIRONMENT_VARIABLE);
    }
    return requireNonNullElse(host, AWS_DEFAULT_EC2_METADATA_ENDPOINT);
  }
}
