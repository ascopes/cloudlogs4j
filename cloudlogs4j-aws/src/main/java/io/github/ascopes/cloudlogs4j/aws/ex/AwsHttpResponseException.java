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

import io.avaje.http.client.HttpException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Objects;

/**
 * Exception that is raised if an HTTP request fails with an error response.
 *
 * @author Ashley Scopes
 * @since 0.0.1
 */
public final class AwsHttpResponseException extends AwsIoException {

  private final String method;
  private final URI uri;
  private final int responseStatus;
  private final String responseBody;

  /**
   * Initialise the exception.
   *
   * @param method         the method.
   * @param uri            the URI.
   * @param responseStatus the response status.
   * @param responseBody   the response body.
   */
  public AwsHttpResponseException(String method, URI uri, int responseStatus, String responseBody) {
    super(String.format(
        "HTTP request [%s %s] failed with response code [%d] and body [%s]",
        method, uri, responseStatus, responseBody
    ));
    this.method = method;
    this.uri = uri;
    this.responseStatus = responseStatus;
    this.responseBody = responseBody;
  }

  /**
   * Initialise the exception.
   *
   * @param exception the HTTP exception.
   */
  public AwsHttpResponseException(HttpException exception) {
    this(
        exception.httpResponse().request().method(),
        exception.httpResponse().uri(),
        exception.statusCode(),
        exception.bodyAsString()
    );
    initCause(exception);
  }

  /**
   * Get the method.
   *
   * @return the method.
   */
  public String getMethod() {
    return method;
  }

  /**
   * Get the URI.
   *
   * @return the URI.
   */
  public URI getUri() {
    return uri;
  }

  /**
   * Get the response status.
   *
   * @return the response status.
   */
  public int getResponseStatus() {
    return responseStatus;
  }

  /**
   * Get the response body.
   *
   * @return the response body.
   */
  public String getResponseBody() {
    return responseBody;
  }
}
