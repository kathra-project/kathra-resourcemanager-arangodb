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
package org.kathra.resourcemanager.configuration;

import com.google.common.collect.ImmutableList;
import org.kathra.resourcemanager.ResourceManagerApi;
import org.apache.camel.model.MarshalDefinition;
import org.apache.camel.model.UnmarshalDefinition;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CamelRouteForSpring extends ResourceManagerApi {

    @Override
    public void configure() throws Exception {
        super.configure();

        super.restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .contextPath("")
                .port(null);

        super.restConfiguration().setComponentProperties(ImmutableList.of());
        super.restConfiguration().setEndpointProperties(ImmutableList.of());
        super.restConfiguration().setConsumerProperties(ImmutableList.of());

        super.getRestCollection().getRests().stream().forEach(rest -> {
            rest.getVerbs().stream().forEach(verb -> {
               verb.getRoute().setOutputs(verb.getRoute().getOutputs().stream().filter(output ->    !(output instanceof MarshalDefinition) &&
                                                                                                    !(output instanceof UnmarshalDefinition)).collect(Collectors.toList()));
            });
        });
    }

}