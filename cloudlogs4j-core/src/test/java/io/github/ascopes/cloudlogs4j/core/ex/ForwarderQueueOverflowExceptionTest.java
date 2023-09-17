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

import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link ForwarderQueueOverflowException} tests.
 *
 * @author Ashley Scopes
 */
@DisplayName("ForwarderQueueOverflowException tests")
class ForwarderQueueOverflowExceptionTest {

  static final Random RANDOM = new Random();

  @DisplayName("Initialising with a queue capacity sets the queue capacity")
  @Test
  void initialisingWithQueueCapacitySetsQueueCapacity() {
    // Given
    var queueCapacity = RANDOM.nextInt(1, 1_000);

    // When
    var ex = new ForwarderQueueOverflowException(queueCapacity);

    // Then
    assertThat(ex.getQueueCapacity()).isEqualTo(queueCapacity);
  }

  @DisplayName("Initialising with a queue capacity sets the exception message")
  @Test
  void initialisingWithQueueCapacitySetsExceptionMessage() {
    // Given
    var queueCapacity = RANDOM.nextInt(1, 1_000);

    // When
    var ex = new ForwarderQueueOverflowException(queueCapacity);

    // Then
    assertThat(ex)
        .hasNoCause()
        .hasMessage(
            "Internal log queue capacity has been reached; log events may "
                + "be dropped (capacity is %d)",
            queueCapacity
        );
  }
}
