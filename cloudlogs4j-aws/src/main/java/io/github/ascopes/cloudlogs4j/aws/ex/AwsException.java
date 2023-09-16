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
package io.github.ascopes.cloudlogs4j.aws.ex;

import io.github.ascopes.cloudlogs4j.api.ex.CloudLogs4jException;
import org.jspecify.annotations.Nullable;

/**
 * Base for any exceptions raised by AWS operations.
 */
public abstract class AwsException extends CloudLogs4jException {

  /**
   * Initialise the AWS exception.
   *
   * @param message the exception message.
   */
  protected AwsException(String message) {
    super(message);
  }

  /**
   * Initialise the AWS exception.
   *
   * @param message the exception message.
   * @param cause   the cause of the exception, may be {@code null}.
   */
  protected AwsException(String message, @Nullable Throwable cause) {
    super(message, cause);
  }
}
