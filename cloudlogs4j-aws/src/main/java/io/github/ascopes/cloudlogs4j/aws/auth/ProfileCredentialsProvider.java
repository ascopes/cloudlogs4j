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
 * Credentials provider that reads the credentials from the given IAM profile first.
 *
 * <p>This inspects the {@code AWS_PROFILE} environment variable.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class ProfileCredentialsProvider extends LazyLoadedCredentialsProvider {

  @Nullable
  @Override
  protected AwsCredentials fetchCredentials() {
    // TODO: implement
    return null;
  }
}
