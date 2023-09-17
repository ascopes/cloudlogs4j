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

import org.jspecify.annotations.Nullable;

/**
 * Base exception for any CloudLogs4J exception that gets raised from any internal libraries.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public abstract class CloudLogs4jException extends Exception {

  /**
   * Initialise this exception.
   *
   * @param message the exception message.
   */
  protected CloudLogs4jException(String message) {
    super(message);
  }

  /**
   * Initialise this exception.
   *
   * @param message the exception message.
   * @param cause   the cause of the exception, may be {@code null}.
   */
  protected CloudLogs4jException(String message, @Nullable Throwable cause) {
    super(message, cause);
  }
}
