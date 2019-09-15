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

import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class ResourceSecurityService {

    Logger logger = LoggerFactory.getLogger(ResourceSecurityService.class);

    @Autowired
    KeycloakClient keycloakClient;

    public List<String> getIdsAuthorized(SecurityContext context, Class<? extends Resource> clazz, Scope scope) throws Exception {

        try {
            return keycloakClient.getResourcesByType(context.getSession(), getTypeFromClazz(clazz), getScopeIdentifier(clazz,scope));
        } catch (Exception e) {
            logger.error("unable to get identifiers authorized for resources "+getTypeFromClazz(clazz)+" with scope "+scope.getScopeName());
            throw e;
        }
    }

    public boolean isAuthorized(SecurityContext context, Resource resource, Scope scope) throws Exception {
        try {
            String result = keycloakClient.getResourceById(context.getSession(), getIdentifier(resource), getScopeIdentifier(resource.getClass(),scope));
            return result != null && result.equals(getIdentifier(resource));
        } catch (Exception e) {
            logger.error("unable to get identifiers authorized for resources "+getIdentifier(resource)+" with scope "+scope.getScopeName());
            throw e;
        }
    }

    private String getTypeFromClazz(Class clazz) {
        return clazz.getSimpleName().toLowerCase();
    }


    public String getScopeIdentifier(Class clazz, Scope scope) {
        return "kathra:scope:"+getTypeFromClazz(clazz)+":"+scope.getScopeName();
    }

    public void grantAccess(SecurityContext context, Resource resource, Scope[] scopes) throws Exception {
        List<String> scopesString = Arrays.stream(scopes).map(scope -> getScopeIdentifier(resource.getClass(),scope)).collect(Collectors.toList());
        try {
            keycloakClient.createResource(context.getSession(), getTypeFromClazz(resource.getClass()), resource.getId(), scopesString, getMetadata(resource));
        } catch (Exception e) {
            logger.error("unable to grant access to resource "+getIdentifier(resource)+" with scopes : "+String.join(",",scopesString));
            throw e;
        }
    }

    private Map<String,String> getMetadata(Resource resource){
        Map<String,String> metadata = new HashMap<>();
        if (resource.getMetadata() != null) {
            for (Map.Entry<String, Object> entry : resource.getMetadata().entrySet()) {
                metadata.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return metadata;
    }

    public void revokeAccess(SecurityContext context, Resource resource) throws Exception {
        AtomicReference<Exception> exception = new AtomicReference();
        try {
            keycloakClient.deleteResource(context.getSession(), getIdentifier(resource), getScopeIdentifier(resource.getClass(), Scope.DELETE));
        } catch (Exception e) {
            logger.error("unable to revoke access to resource "+getIdentifier(resource)+" with scope "+Scope.DELETE);
            exception.set(e);
        }
    }

    public void deleteResourceScope(SecurityContext context, Resource resource, Scope[] scopes) throws Exception {
        AtomicReference<Exception> exception = new AtomicReference();
        Arrays.stream(scopes).forEach(scope -> {
            try {
                keycloakClient.deleteResourceScope(context.getSession(), getIdentifier(resource), getScopeIdentifier(resource.getClass(),scope));
            } catch (Exception e) {
                logger.error("unable to delete scope to resource "+getIdentifier(resource)+" with scope "+scope.getScopeName());
                exception.set(e);
            }
        });

        if (exception.get() != null) {
            throw exception.get();
        }
    }


    private String getIdentifier(Resource resource){
        return resource.getId();
    }
}
