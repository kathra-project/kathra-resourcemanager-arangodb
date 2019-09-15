/* 
 * Copyright 2019 The Kathra Authors.
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
 *
 *    IRT SystemX (https://www.kathra.org/)    
 *
 */
package org.kathra.resourcemanager;

import com.arangodb.springframework.core.ArangoOperations;
import org.kathra.core.model.ApiVersion;
import org.kathra.core.model.Component;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionDao;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionDb;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionRepository;
import org.kathra.resourcemanager.component.service.ComponentService;
import org.kathra.resourcemanager.security.SessionService;
import org.kathra.utils.Session;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.StreamSupport;

@SpringBootApplication
@ComponentScan(basePackages="org.kathra.resourcemanager")
public class Application{

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        int  cpus = runtime.availableProcessors();
        long mmax = runtime.maxMemory() / 1024 / 1024;
        System.out.println("Cores : " + cpus);
        System.out.println("Memory: " + mmax);
        SpringApplication.run(Application.class, args);
    }

}
