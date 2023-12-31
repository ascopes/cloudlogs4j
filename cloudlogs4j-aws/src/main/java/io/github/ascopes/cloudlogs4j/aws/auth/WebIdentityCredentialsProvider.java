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
 * Credentials provider that fetches the web identity token provided to the given run environment.
 *
 * <p>This is used by mechanisms like IRSA in EKS to delegate credential management to IAM for
 * pods.</p>
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class WebIdentityCredentialsProvider extends LazyLoadedCredentialsProvider {

  @Override
  protected @Nullable AwsCredentials fetchCredentials() throws AwsException {
    // TODO: implement
    return null;
  }
}
