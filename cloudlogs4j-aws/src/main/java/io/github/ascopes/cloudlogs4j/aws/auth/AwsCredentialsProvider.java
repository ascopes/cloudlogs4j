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

import io.github.ascopes.cloudlogs4j.aws.ex.AwsException;
import org.jspecify.annotations.Nullable;

/**
 * API for fetching AWS credentials from certain places.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public interface AwsCredentialsProvider {

  /**
   * Get the AWS credentials.
   *
   * @return the credentials, or {@code null} if no credentials exist.
   * @throws AwsException if something fails.
   */
  @Nullable
  AwsCredentials getCredentials() throws AwsException;
}
