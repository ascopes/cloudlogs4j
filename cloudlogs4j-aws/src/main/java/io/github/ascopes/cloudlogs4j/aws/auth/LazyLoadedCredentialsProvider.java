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
package io.github.ascopes.cloudlogs4j.aws.auth;

import io.github.ascopes.cloudlogs4j.aws.ex.AwsException;
import java.time.Clock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.jspecify.annotations.Nullable;

/**
 * Base for a credentials provider that caches their fetched value after the first call.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public abstract class LazyLoadedCredentialsProvider implements AwsCredentialsProvider {

  private final Clock clock;
  private final Lock lock;
  private volatile boolean set;
  private volatile @Nullable AwsCredentials credentials;
  private volatile boolean shouldCheckForReset;
  private volatile long resetAt;

  protected LazyLoadedCredentialsProvider() {
    this(Clock.systemUTC());
  }

  /**
   * Initialise this credential provider.
   *
   * @param clock the clock to use.
   */
  protected LazyLoadedCredentialsProvider(Clock clock) {
    this.clock = clock;
    lock = new ReentrantLock(true);
    set = false;
    credentials = null;
    shouldCheckForReset = false;
    resetAt = 0L;
  }

  /**
   * Get the credentials, caching them first if they have not yet been read.
   *
   * @return the credentials, or {@code null} if they do not exist.
   * @throws AwsException if the fetch-credentials operation fails.
   */
  @Nullable
  @Override
  public AwsCredentials getCredentials() throws AwsException {
    lock.lock();

    try {
      if (shouldCheckForReset && clock.millis() >= resetAt) {
        // Credentials have expired. Force reset.
        set = false;
      }

      if (!set) {
        credentials = fetchCredentials();
        set = true;
      }

      return credentials;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Request that credentials are reset after the given epoch in milliseconds.
   *
   * @param epochMillis the epoch to reset credentials after.
   */
  protected final void resetCredentialsAfter(long epochMillis) {
    resetAt = epochMillis;
    shouldCheckForReset = true;
  }

  /**
   * Fetch the credentials from their source.
   *
   * <p>The result of this call will be cached internally automatically, you do not need to
   * override this behaviour.
   *
   * @return the credentials value.
   * @throws AwsException if the operation fails.
   */
  @Nullable
  protected abstract AwsCredentials fetchCredentials() throws AwsException;
}
