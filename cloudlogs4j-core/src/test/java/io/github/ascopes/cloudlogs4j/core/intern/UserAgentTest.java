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
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link UserAgent} tests.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
@DisplayName("UserAgent tests")
class UserAgentTest {
  @DisplayName("The User-Agent is determined from the JAR MANIFEST")
  @Test
  void theUserAgentIsDeterminedFromTheJarManifest() {
    // Use a JAR for something other than the current project, as IDEs may not generate the
    // MANIFEST.mf prior to JAR packaging, so we will see invalid values returned.

    // Given
    var somePackage = Test.class.getPackage();
    var corePackage = UserAgent.class.getPackage();

    // When
    var userAgent = UserAgent.getUserAgentFor(somePackage);

    // Then
    assertThat(userAgent)
        .isEqualTo(
            "%s/%s %s/%s Java/%s",
            requireNonNullElse(corePackage.getImplementationTitle(), "cloudlogs4j-core"),
            requireNonNullElse(corePackage.getImplementationVersion(), "UNKNOWN"),
            somePackage.getImplementationTitle(),
            somePackage.getImplementationVersion(),
            Runtime.version()
        );
  }
}
