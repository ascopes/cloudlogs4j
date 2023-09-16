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
package io.github.ascopes.cloudlogs4j.aws;

/**
 * Headers used by AWS HTTP calls.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class AwsHeaders {
  public static final String ACCEPT = "Accept";
  public static final String CONNECTION = "Connection";
  public static final String USER_AGENT = "User-Agent";

  private AwsHeaders() {
    throw new UnsupportedOperationException("Static-only class");
  }

}
