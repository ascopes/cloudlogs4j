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

import io.avaje.http.api.Client;
import io.avaje.http.api.Get;
import io.avaje.http.client.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse;
import java.time.Duration;

@Client
public interface Ec2InstanceMetadataClient {

  @Get("/latest/meta-data/iam/security-credentials/")
  HttpResponse<String> getSecurityCredentials();

  @Get("/latest/meta-data/iam/security-credentials/{securityCredential}")
  HttpResponse<Ec2SecurityCredential> getSecurityCredential(String securityCredential);

  static Ec2InstanceMetadataClient createClient(String ec2MetadataEndpoint) {
    return HttpClient.builder()
        .baseUrl(ec2MetadataEndpoint)
        .connectionTimeout(Duration.ofSeconds(5))
        .requestIntercept(new Ec2MetadataHeaderInterceptor())
        .requestLogging(false)
        .requestTimeout(Duration.ofSeconds(5))
        .version(Version.HTTP_1_1)
        .build()
        .create(Ec2InstanceMetadataClient.class);
  }
}
