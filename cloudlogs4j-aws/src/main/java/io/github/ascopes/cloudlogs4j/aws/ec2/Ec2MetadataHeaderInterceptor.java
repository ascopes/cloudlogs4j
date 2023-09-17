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
package io.github.ascopes.cloudlogs4j.aws.ec2;

import io.avaje.http.client.HttpClientRequest;
import io.avaje.http.client.RequestIntercept;
import io.github.ascopes.cloudlogs4j.core.intern.UserAgent;

/**
 * A request interceptor that injects the required headers into the EC2 instance metadata
 * requests.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class Ec2MetadataHeaderInterceptor implements RequestIntercept {
  private final String userAgent;

  public Ec2MetadataHeaderInterceptor() {
    userAgent = UserAgent.getUserAgentFor(getClass().getPackage());
  }

  @Override
  public void beforeRequest(HttpClientRequest request) {
    request
        .header("Accept", "*/*")
        .header("Connection", "close")
        .header("User-Agent", userAgent);
  }
}
