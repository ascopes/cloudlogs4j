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
package io.github.ascopes.cloudlogs4j.core.intern;

import static java.util.Objects.requireNonNullElse;

import java.util.concurrent.ConcurrentHashMap;

/**
 * User-Agent provider for cloudlogs4j libraries.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class UserAgent {

  private static final String MODULE_NAME = "cloudlogs4j-core";
  private static final String UNKNOWN = "UNKNOWN";
  private static final ConcurrentHashMap<Package, String> userAgents = new ConcurrentHashMap<>();
  private static final Package thisPackage = UserAgent.class.getPackage();

  private UserAgent() {
    throw new UnsupportedOperationException("static-only class");
  }

  /**
   * Create a user-agent string for the given package.
   *
   * <p>This result is cached after the first call for the given package.
   *
   * @param pkg the package containing the class to make the user-agent for.
   * @return the user-agent string.
   */
  public static String getUserAgentFor(Package pkg) {
    return userAgents.computeIfAbsent(pkg, UserAgent::createUserAgentFor);
  }

  private static String createUserAgentFor(Package pkg) {
    return MODULE_NAME
        + "/"
        + requireNonNullElse(thisPackage.getImplementationVersion(), UNKNOWN)
        + " "
        + requireNonNullElse(pkg.getImplementationTitle(), UNKNOWN)
        + "/"
        + requireNonNullElse(pkg.getImplementationVersion(), UNKNOWN)
        + " Java/"
        + Runtime.version();
  }
}
