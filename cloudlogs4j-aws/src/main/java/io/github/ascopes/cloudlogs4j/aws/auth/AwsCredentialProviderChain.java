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
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Chain of credential providers. The first provider to return a non-null value internally will be
 * the one used to supply credentials.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class AwsCredentialProviderChain implements AwsCredentialsProvider {

  private final List<AwsCredentialsProvider> providers;

  public AwsCredentialProviderChain() {
    providers = new ArrayList<>();
  }

  /**
   * Add a provider to the back of the chain.
   *
   * @param provider the provider to add.
   */
  public void addProvider(AwsCredentialsProvider provider) {
    providers.add(provider);
  }

  @Nullable
  @Override
  public AwsCredentials getCredentials() throws AwsException {
    for (var provider : providers) {
      var credential = provider.getCredentials();
      if (credential != null) {
        return credential;
      }
    }

    return null;
  }
}
