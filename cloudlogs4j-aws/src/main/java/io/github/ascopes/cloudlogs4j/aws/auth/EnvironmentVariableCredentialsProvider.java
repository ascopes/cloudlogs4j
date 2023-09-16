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

import static java.util.Objects.requireNonNull;

import io.github.ascopes.cloudlogs4j.aws.ex.AwsException;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsMissingValueException;
import org.jspecify.annotations.Nullable;

/**
 * Credentials provider that reads from environment variables.
 *
 * <p>This accepts the following environment variables:
 *
 * <table>
 *   <thead>
 *     <tr>
 *       <th>Name</th>
 *       <th>Alternate name</th>
 *       <th>Optional</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td>{@code AWS_ACCESS_KEY_ID}</td>
 *       <td>{@code AWS_ACCESS_KEY}</td>
 *       <td>No</td>
 *     </tr>
 *     <tr>
 *       <td>{@code AWS_SECRET_ACCESS_KEY}</td>
 *       <td>{@code AWS_SECRET_KEY}</td>
 *       <td>No</td>
 *     </tr>
 *     <tr>
 *       <td>{@code AWS_SESSION_TOKEN}</td>
 *       <td>-</td>
 *       <td>Yes</td>
 *     </tr>
 *   </tbody>
 * </table>
 *
 * <p>Providing one of the required environment variables without the others will result in an
 * exception being raised. If no environment variables matching the required names are found, then
 * this will return a {@code null} value for the credentials.
 *
 * <p>The results of loading the variables are cached after being read.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class EnvironmentVariableCredentialsProvider extends LazyLoadedCredentialsProvider {
  private static final String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";
  private static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";
  private static final String AWS_ACCESS_KEY = "AWS_ACCESS_KEY";
  private static final String AWS_SECRET_ACCESS_KEY = "AWS_SECRET_ACCESS_KEY";
  private static final String AWS_SESSION_TOKEN = "AWS_SESSION_TOKEN";

  @Nullable
  @Override
  protected AwsCredentials fetchCredentials() throws AwsException {
    var accessKeyId = getEnvValue(AWS_ACCESS_KEY_ID, AWS_ACCESS_KEY);
    var secretAccessKey = getEnvValue(AWS_SECRET_KEY, AWS_SECRET_ACCESS_KEY);
    var sessionToken = getEnvValue(AWS_SESSION_TOKEN);

    if (sessionToken != null) {
      return new AwsCredentials(
          require(AWS_ACCESS_KEY_ID, accessKeyId),
          require(AWS_SECRET_ACCESS_KEY, secretAccessKey),
          sessionToken
      );
    }

    if (accessKeyId != null || secretAccessKey != null) {
      return new AwsCredentials(
          require(AWS_ACCESS_KEY_ID, accessKeyId),
          require(AWS_SECRET_ACCESS_KEY, secretAccessKey)
      );
    }

    // No envvars provided, so skip.
    return null;
  }

  @Nullable
  private String getEnvValue(String... names) {
    for (var name : names) {
      var value = System.getenv(name);
      if (value != null) {
        return value;
      }
    }

    return null;
  }

  private String require(String name, @Nullable String value) throws AwsMissingValueException {
    if (value == null) {
      throw new AwsMissingValueException("Missing required environment variable '" + name + "'");
    }
    return value;
  }
}
