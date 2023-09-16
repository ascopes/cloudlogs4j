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
package io.github.ascopes.cloudlogs4j.api.ex;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link ForwarderTimeoutException} tests.
 *
 * @author Ashley Scopes
 */
@DisplayName("ForwarderTimeoutException tests")
class ForwarderTimeoutExceptionTest {

  @DisplayName("String constructor initialises the message")
  @Test
  void stringConstructorInitialisesTheMessage() {
    // Given
    var message = "Something broke blah blah " + UUID.randomUUID();

    // When
    var ex = new ForwarderTimeoutException(message);

    // Then
    assertThat(ex).hasMessage(message);
  }

  @DisplayName("String constructor sets the cause to null")
  @Test
  void stringConstructorSetsCauseToNull() {
    // Given
    var message = "Something broke blah blah " + UUID.randomUUID();

    // When
    var ex = new ForwarderTimeoutException(message);

    // Then
    assertThat(ex).hasNoCause();
  }
}
