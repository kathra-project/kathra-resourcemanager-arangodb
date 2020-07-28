/*
 * Copyright (c) 2020. The Kathra Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *    IRT SystemX (https://www.kathra.org/)
 *
 */
package org.kathra.resourcemanager.configuration;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = { "org.kathra.resourcemanager" })
public class KathraArangoConfiguration implements ArangoConfiguration {

    @Value("${spring.data.arangodb.database}")
    String database;
    @Value("${spring.data.arangodb.host}")
    String host;
    @Value("${spring.data.arangodb.port}")
    int port;
    @Value("${spring.data.arangodb.user}")
    String user;
    @Value("${spring.data.arangodb.password}")
    String password;

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder()
                .host(host, port)
                .password(password)
                .user(user);
    }

    @Override
    public String database() {
        return database;
    }

}