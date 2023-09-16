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
package io.github.ascopes.cloudlogs4j.api;

import io.github.ascopes.cloudlogs4j.api.ex.ForwarderQueueOverflowException;
import io.github.ascopes.cloudlogs4j.api.ex.ForwarderTimeoutException;
import java.time.Duration;
import org.jspecify.annotations.Nullable;

/**
 * Base interface for a log forwarder.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public interface LogForwarder {

  /**
   * Determine if the forwarder is running or not.
   *
   * @return {@code true} if the forwarder is running, or {@code false} if the forwarder has
   *     stopped.
   */
  boolean isRunning();

  /**
   * Start the forwarder if it is not already running.
   *
   * <p>If already running, then the call will not do anything.
   */
  void start();

  /**
   * Stop the forwarder if it is running.
   *
   * @param timeout the timeout to wait for before force closing any resources. If {@code null},
   *                then wait indefinitely.
   * @throws ForwarderTimeoutException if the operation times out.
   * @throws InterruptedException if the waiting operation is interrupted by another thread.
   */
  void stop(@Nullable Duration timeout) throws ForwarderTimeoutException, InterruptedException;

  /**
   * Submit a log entry to be forwarded to the Cloud provider.
   *
   * @param timestamp the UNIX timestamp of the log entry, in milliseconds.
   * @param contents  the byte-encoded contents of the log entry.
   */
  void submitLogEntry(long timestamp, byte[] contents) throws ForwarderQueueOverflowException;
}
