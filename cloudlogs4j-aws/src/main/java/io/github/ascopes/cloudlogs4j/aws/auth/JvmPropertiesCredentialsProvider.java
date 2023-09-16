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
 * Credentials provider that reads from the JVM properties.
 *
 * <p>This accepts the following JVM properties:
 *
 * <table>
 *   <thead>
 *     <tr>
 *       <th>Name</th>
 *       <th>Optional</th>
 *     </tr>
 *   </thead>
 *   <tbody>
 *     <tr>
 *       <td>{@code aws.accessKeyId}</td>
 *       <td>No</td>
 *     </tr>
 *     <tr>
 *       <td>{@code aws.secretKey}</td>
 *       <td>No</td>
 *     </tr>
 *     <tr>
 *       <td>{@code aws.sessionToken}</td>
 *       <td>Yes</td>
 *     </tr>
 *   </tbody>
 * </table>
 *
 * <p>Providing one of the required properties without the others will result in an exception being
 * raised. If no properties matching the required names are found, then this will return a
 * {@code null} value for the credentials.
 *
 * <p>The results of loading the properties are cached after being read.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class JvmPropertiesCredentialsProvider extends LazyLoadedCredentialsProvider {
  private static final String AWS_ACCESS_KEY_ID = "aws.accessKeyId";
  private static final String AWS_SECRET_KEY = "aws.secretKey";
  private static final String AWS_SESSION_TOKEN = "aws.sessionToken";

  @Nullable
  @Override
  protected AwsCredentials fetchCredentials() throws AwsException {
    var accessKeyId = System.getProperty(AWS_ACCESS_KEY_ID);
    var secretAccessKey = System.getProperty(AWS_SECRET_KEY);
    var sessionToken = System.getProperty(AWS_SESSION_TOKEN);

    if (sessionToken != null) {
      return new AwsCredentials(
          require(AWS_ACCESS_KEY_ID, accessKeyId),
          require(AWS_SECRET_KEY, secretAccessKey),
          sessionToken
      );
    }

    if (accessKeyId != null || secretAccessKey != null) {
      return new AwsCredentials(
          require(AWS_ACCESS_KEY_ID, accessKeyId),
          require(AWS_SECRET_KEY, secretAccessKey)
      );
    }

    // No properties provided, so skip.
    return null;
  }

  private String require(String name, @Nullable String value) throws AwsMissingValueException {
    if (value == null) {
      throw new AwsMissingValueException("Missing required JVM property '" + name + "'");
    }
    return value;
  }
}
