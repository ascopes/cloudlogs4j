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

import static java.util.Objects.requireNonNull;

import io.github.ascopes.cloudlogs4j.aws.ex.AwsException;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsIoException;
import io.github.ascopes.cloudlogs4j.aws.ex.AwsMissingValueException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Properties;
import org.jspecify.annotations.Nullable;

/**
 * Credentials provider that reads from the provided properties file.
 *
 * <p>This expects the properties file to contain two attributes: {@code accessKeyId}
 * and {@code secretAccessKey}.
 *
 * <p>If the file does not exist, this provider will skip attempting to load the file
 * after the first attempt.
 *
 * <p>The results of loading the file are cached after being read.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class PropertiesFileCredentialsProvider extends LazyLoadedCredentialsProvider {
  private static final String ACCESS_KEY_ID = "accessKeyId";
  private static final String SECRET_ACCESS_KEY = "secretAccessKey";

  private final Path path;

  /**
   * Create a properties file credentials provider from the given path.
   *
   * @param path the string path to the file.
   */
  public PropertiesFileCredentialsProvider(String path) {
    this(Path.of(path));
  }

  /**
   * Create a properties file credentials provider from the given path.
   *
   * @param path the NIO path to the file.
   */
  public PropertiesFileCredentialsProvider(Path path) {
    this.path = path;
  }

  @Nullable
  @Override
  protected AwsCredentials fetchCredentials() throws AwsException {
    try (var inputStream = Files.newInputStream(path)) {
      var properties = new Properties(2);
      properties.load(inputStream);
      return new AwsCredentials(
          require(properties, ACCESS_KEY_ID),
          require(properties, SECRET_ACCESS_KEY)
      );
    } catch (NoSuchFileException ex) {
      // No file provided, so skip.
      return null;
    } catch (IOException ex) {
      throw new AwsIoException("Failed to read file '" + path + "'", ex);
    }
  }

  private String require(Properties properties, String key) throws AwsException {
    var value = properties.getProperty(key);

    if (value == null) {
      throw new AwsMissingValueException(
          "File '" + path + "' has missing required property '" + key + "'"
      );
    }

    return value;
  }
}
