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
package org.kathra.resourcemanager.resource.service.security;

import org.kathra.utils.Session;
import org.kathra.utils.security.KeycloakUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakClient {


    @PostConstruct
    public void init(){
        try {
            KeycloakUtils.init();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public List<String> getResourcesByType(Session session, String type, String scopeIdentifier) throws Exception {
        return KeycloakUtils.getResourcesByType(session, type, scopeIdentifier);
    }

    public void createResource(Session session, String type, String id, List<String> scopes, Map<String, String> metadata)  throws Exception {
        KeycloakUtils.createResource(session, type, id, scopes, metadata);
    }

    public void deleteResource(Session session, String identifier, String scopeIdentifier) throws Exception {
        KeycloakUtils.deleteResource(session, identifier, scopeIdentifier);
    }

    public void deleteResourceScope(Session session, String identifier, String scopeIdentifier) throws Exception {
        KeycloakUtils.deleteResourceScope(session, identifier, scopeIdentifier);
    }


    public String getResourceById(Session session, String identifier, String scopeIdentifier) throws Exception {
        return KeycloakUtils.getResourceById(session, identifier, scopeIdentifier);
    }
}
