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
package io.github.ascopes.cloudlogs4j.core.ex;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link CloudLogs4jException} tests.
 *
 * @author Ashley Scopes
 */
@DisplayName("CloudLogs4jException tests")
class CloudLogs4jExceptionTest {
  @DisplayName("String constructor initialises the message")
  @Test
  void stringConstructorInitialisesTheMessage() {
    // Given
    var message = "Something broke blah blah " + UUID.randomUUID();

    // When
    var ex = new ExceptionImpl(message);

    // Then
    assertThat(ex).hasMessage(message);
  }

  @DisplayName("String constructor sets the cause to null")
  @Test
  void stringConstructorSetsCauseToNull() {
    // Given
    var message = "Something broke blah blah " + UUID.randomUUID();

    // When
    var ex = new ExceptionImpl(message);

    // Then
    assertThat(ex).hasNoCause();
  }

  @DisplayName("String-Throwable constructor initialises the message")
  @Test
  void stringThrowableConstructorInitialisesTheMessage() {
    // Given
    var message = "Something broke blah blah " + UUID.randomUUID();
    var cause = new Throwable("Some exception blah blah " + UUID.randomUUID());

    // When
    var ex = new ExceptionImpl(message, cause);

    // Then
    assertThat(ex).hasMessage(message);
  }

  @DisplayName("String-Throwable constructor can set the cause to null")
  @Test
  void stringThrowableConstructorCanSetCauseToNull() {
    // Given
    var message = "Something broke blah blah " + UUID.randomUUID();

    // When
    var ex = new ExceptionImpl(message, null);

    // Then
    assertThat(ex).hasNoCause();
  }

  @DisplayName("String-Throwable constructor can set the cause to another exception")
  @Test
  void stringThrowableConstructorCanSetCauseToAnotherException() {
    // Given
    var message = "Something broke blah blah " + UUID.randomUUID();
    var cause = new Throwable("Some exception blah blah " + UUID.randomUUID());

    // When
    var ex = new ExceptionImpl(message, cause);

    // Then
    assertThat(ex).hasCause(cause);
  }

  static class ExceptionImpl extends CloudLogs4jException {
    ExceptionImpl(String message) {
      super(message);
    }

    ExceptionImpl(String message, @Nullable Throwable cause) {
      super(message, cause);
    }
  }
}
