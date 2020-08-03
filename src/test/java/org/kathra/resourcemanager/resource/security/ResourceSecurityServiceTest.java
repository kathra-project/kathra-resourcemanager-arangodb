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
package org.kathra.resourcemanager.resource.security;


import com.google.common.collect.ImmutableList;
import org.kathra.core.model.Component;
import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.resource.service.security.KeycloakClient;
import org.kathra.resourcemanager.resource.service.security.ResourceSecurityService;
import org.kathra.resourcemanager.resource.service.security.Scope;
import org.kathra.resourcemanager.security.SecurityContext;
import org.kathra.utils.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ResourceSecurityServiceTest {

    @Mock
    KeycloakClient keycloakClient;

    @InjectMocks
    ResourceSecurityService underTest;


    @Test
    public void given_context_clazz_and_scope_when_getIdsAuthorized_then_return_ids_as_string() throws Exception {

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(context.getSession()).thenReturn(session);

        Scope scope = Scope.READ;

        Mockito.when(keycloakClient.getResourcesByType(session, "component", "kathra:scope:component:read")).thenReturn(ImmutableList.of("789456","123654","987321"));

        List<String> ids = underTest.getIdsAuthorized(context, Component.class, scope);
        Assertions.assertNotNull(ids);
        Assertions.assertEquals(ids.size(), 3);
        Assertions.assertTrue(ids.contains("789456"));
        Assertions.assertTrue(ids.contains("123654"));
        Assertions.assertTrue(ids.contains("987321"));
    }

    @Test
    public void given_resource_scope_when_grantAccess_then_return_works() throws Exception {

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(context.getSession()).thenReturn(session);

        Scope[] scopes = Scope.values();

        List<String> scopesKC = ImmutableList.of("kathra:scope:component:read", "kathra:scope:component:update", "kathra:scope:component:delete");

        Resource resource = new Component();
        resource.setId("123456");
        resource.putMetadataItem("x","y");

        underTest.grantAccess(context, resource, scopes);

        Mockito.verify(keycloakClient).createResource(session, "component", "123456", scopesKC, Map.of("x","y"));

    }

    @Test
    public void given_resource_scope_when_deleteResource_then_return_works() throws Exception {

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(context.getSession()).thenReturn(session);

        Scope[] scopes = Scope.values();


        Resource resource = new Component();
        resource.setId("123456");
        resource.putMetadataItem("x","y");

        underTest.revokeAccess(context, resource);

        Mockito.verify(keycloakClient).deleteResource(session, "123456", "kathra:scope:component:delete");

    }

    @Test
    public void given_resource_scope_authorized_when_isAuthorized_then_return_true() throws Exception {

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(context.getSession()).thenReturn(session);

        Scope scope = Scope.READ;


        Resource resource = new Component();
        resource.setId("123456");
        resource.putMetadataItem("x","y");

        Mockito.when(keycloakClient.getResourceById(context.getSession(), "123456", "kathra:scope:component:read")).thenReturn("123456");

        boolean result = underTest.isAuthorized(context, resource, scope);
        Assertions.assertTrue(result);
    }

    @Test
    public void given_resource_scope_unauthorized_when_isAuthorized_then_return_false() throws Exception {

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(context.getSession()).thenReturn(session);

        Scope scope = Scope.READ;


        Resource resource = new Component();
        resource.setId("123456");
        resource.putMetadataItem("x","y");

        Mockito.when(keycloakClient.getResourceById(context.getSession(), "123456", "kathra:scope:component:read")).thenReturn(null);

        boolean result = underTest.isAuthorized(context, resource, scope);
        Assertions.assertFalse(result);
    }

}
