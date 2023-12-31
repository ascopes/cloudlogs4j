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
package io.github.ascopes.cloudlogs4j.aws.ec2;

import io.avaje.jsonb.Json;
import java.time.OffsetDateTime;

/**
 * JSON model for instance metadata credentials fetched from the EC2 instance metadata endpoint.
 *
 * @param accessKey          the access key.
 * @param secretKey          the secret key.
 * @param token              the token.
 * @param expirationDateTime the credentials expiration date/time.
 * @author Ashley Scopes
 * @since 0.0.1
 */
@Json
public record Ec2SecurityCredential(
    @Json.Property("AccessKeyId")
    String accessKey,

    @Json.Property("SecretAccessKey")
    String secretKey,

    @Json.Property("Token")
    String token,

    @Json.Property("Expiration")
    OffsetDateTime expirationDateTime
) {
}
