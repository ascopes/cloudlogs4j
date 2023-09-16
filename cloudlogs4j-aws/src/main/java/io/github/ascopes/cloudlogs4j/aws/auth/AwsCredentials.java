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

import org.jspecify.annotations.Nullable;

/**
 * Holder for AWS credentials.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class AwsCredentials {
  private final String accessKeyId;
  private final String secretAccessKey;
  private final @Nullable String sessionToken;

  /**
   * Create a set of AWS credentials.
   *
   * @param accessKeyId the access key ID.
   * @param secretAccessKey the secret access key.
   */
  public AwsCredentials(String accessKeyId, String secretAccessKey) {
    this.accessKeyId = accessKeyId;
    this.secretAccessKey = secretAccessKey;
    sessionToken = null;
  }

  /**
   * Create a set of AWS credentials.
   *
   * @param accessKeyId the access key ID.
   * @param secretAccessKey the secret access key.
   * @param sessionToken the session token.
   */
  public AwsCredentials(String accessKeyId, String secretAccessKey, String sessionToken) {
    this.accessKeyId = accessKeyId;
    this.secretAccessKey = secretAccessKey;
    this.sessionToken = sessionToken;
  }

  /**
   * Get the access key ID.
   *
   * @return the access key ID.
   */
  public String getAccessKeyId() {
    return accessKeyId;
  }

  /**
   * Get the secret access key.
   *
   * @return the secret access key.
   */
  public String getSecretAccessKey() {
    return secretAccessKey;
  }

  /**
   * Get the session token, if it is provided.
   *
   * @return the session token, or {@code null} if no token is provided.
   */
  @Nullable
  public String getSessionToken() {
    return sessionToken;
  }

  /**
   * Get a string representation of this object.
   *
   * @return the string representation.
   */
  @Override
  public String toString() {
    return getClass().getSimpleName() + "{accessKeyId='" + accessKeyId + "'}";
  }
}

