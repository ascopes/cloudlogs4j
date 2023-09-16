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

import static java.util.Objects.requireNonNullElse;

/**
 * User-Agent to use for HTTP clients.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class UserAgent {

  /**
   * The user-agent value.
   */
  public static final String USER_AGENT;

  static {
    var pkg = UserAgent.class.getPackage();
    var libraryName = requireNonNullElse(pkg.getImplementationTitle(), "cloudlogs4j-aws");
    var libraryVersion = requireNonNullElse(pkg.getImplementationVersion(), "UNKNOWN");
    var javaVersion = Runtime.version();

    USER_AGENT = String.format(
        "%s/%s Java/%d.%d.%d.%d",
        libraryName,
        libraryVersion,
        javaVersion.feature(),
        javaVersion.interim(),
        javaVersion.update(),
        javaVersion.patch()
    );
  }

  private UserAgent() {
    throw new UnsupportedOperationException("Static-only class");
  }
}
