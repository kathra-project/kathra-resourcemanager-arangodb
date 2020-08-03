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
import org.kathra.core.model.ApiVersion;
import org.kathra.core.model.Component;
import org.kathra.resourcemanager.resource.service.security.*;
import org.kathra.resourcemanager.security.SecurityContext;
import org.kathra.resourcemanager.security.SessionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ResourceSecurityProcessorTest {

    ResourceSecurityProcessor underTest;

    @Mock
    SessionService sessionSecurity;

    @Mock
    ResourceSecurityService resourceSecurityService;

    @Mock
    DefaultParameterNameDiscoverer defaultParameterNameDiscoverer;

    @Mock
    SecurityContext securityContext;

    TestService testService;

    @BeforeEach
    public void foreach(){
        Mockito.when(sessionSecurity.get()).thenReturn(securityContext);
        underTest = new ResourceSecurityProcessor(resourceSecurityService, sessionSecurity, defaultParameterNameDiscoverer);
    }

    private class TestService {


        @ResourceSecured(action= ResourceSecuredAction.REGISTER, clazz=Component.class, target="input")
        public void register(Component input){}
        @ResourceSecured(action= ResourceSecuredAction.UNREGISTER, clazz=Component.class, target="input")
        public void unregister(Component input){}
        @ResourceSecured(action= ResourceSecuredAction.REGISTER, clazz=Component.class, target="output")
        public Component registerOut(){return null;}
        @ResourceSecured(action= ResourceSecuredAction.UNREGISTER, clazz=Component.class, target="output")
        public Component unregisterOut(){return null;}
        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="input")
        public void testAll(Component input){}
        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="output", scopes = Scope.READ)
        public Component get(Component input){
            return input;
        }
        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="output", scopes = Scope.READ)
        public List<Component> getAll(){
            return null;
        }

        @ResourceSecured(action= ResourceSecuredAction.FILTER, clazz=Component.class, target="output", scopes = Scope.READ)
        public List<Component> getAllAccessible(){
            return null;
        }
        @ResourceSecured(action= ResourceSecuredAction.FILTER, clazz=Component.class, target="list", scopes = Scope.DELETE)
        public void deleteAllAccessible(List<Component> list){return; }

        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="list", scopes = Scope.UPDATE)
        public void updateAll(List<Component> list){
            return;
        }
        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="input", scopes = Scope.DELETE)
        public void delete(Component input){}
        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="id", scopes = Scope.DELETE)
        public void deleteById(String id){}

        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="input/component")
        public void testXPathInput(ApiVersion input){}

        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="output")
        public Optional<Component> getOptional(){return null;}

        @ResourceSecured(action= ResourceSecuredAction.VALID, clazz=Component.class, target="output/component")
        public Optional<ApiVersion> testXPathOutput(){return null;}

        @ResourceSecured(action= ResourceSecuredAction.FILTER, clazz=Component.class, target="output/component", scopes = Scope.READ)
        public List<Component> getAllAccessibleByXPath(){
            return null;
        }
        @ResourceSecured(action= ResourceSecuredAction.FILTER, clazz=Component.class, target="list/component", scopes = Scope.DELETE)
        public void deleteAllAccessibleByXPath(List<Component> list){ }
    }

    @Test
    public void given_joinPoint_to_validate_all_scopes_w_input_obj_when_before_then_works() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("testAll",paramsName, paramsArgs, "obj");

        mockAutorizations(resource, true, true, true);

        underTest.before(joinPoint);
        verifyCallAutorizations(resource, true, true, true);
    }

    private void verifyCallAutorizations(Component resource, boolean read, boolean write, boolean delete) throws Exception {
        Mockito.verify(resourceSecurityService, Mockito.times(read ? 1 : 0)).isAuthorized(Mockito.eq(securityContext), Mockito.argThat(arg -> arg.getId().equals(resource.getId())), Mockito.eq(Scope.READ));
        Mockito.verify(resourceSecurityService, Mockito.times(write ? 1 : 0)).isAuthorized(Mockito.eq(securityContext), Mockito.argThat(arg -> arg.getId().equals(resource.getId())), Mockito.eq(Scope.UPDATE));
        Mockito.verify(resourceSecurityService, Mockito.times(delete ? 1 : 0)).isAuthorized(Mockito.eq(securityContext), Mockito.argThat(arg -> arg.getId().equals(resource.getId())), Mockito.eq(Scope.DELETE));
    }

    private void mockAutorizations(Component resource, boolean read, boolean write, boolean delete) throws Exception {
        Mockito.when(resourceSecurityService.isAuthorized(Mockito.eq(securityContext), Mockito.argThat(arg -> arg.getId().equals(resource.getId())), Mockito.eq(Scope.READ))).thenReturn(read);
        Mockito.when(resourceSecurityService.isAuthorized(Mockito.eq(securityContext), Mockito.argThat(arg -> arg.getId().equals(resource.getId())), Mockito.eq(Scope.UPDATE))).thenReturn(write);
        Mockito.when(resourceSecurityService.isAuthorized(Mockito.eq(securityContext), Mockito.argThat(arg -> arg.getId().equals(resource.getId())), Mockito.eq(Scope.DELETE))).thenReturn(delete);
    }

    private void mockAutorizations(List<Component> resources, Scope scope) throws Exception {
        List<String> ids = resources.stream().map(Component::getId).collect(Collectors.toList());
        Mockito.when(resourceSecurityService.getIdsAuthorized(Mockito.eq(securityContext), Mockito.eq(Component.class), Mockito.eq(scope))).thenReturn(ids);
    }

    @Test
    public void given_jointPoint_to_validate_optional_when_getOptional_then_works() throws Throwable {
        Component component = getComponent();
        String[] paramsName = {};
        Object[] paramsArgs = {};
        JoinPoint joinPoint = mockJoinPoint("getOptional",paramsName, paramsArgs, "output");
        mockAutorizations(component, true, true, true);
        underTest.after(joinPoint, Optional.of(component));
        verifyCallAutorizations(component, true, true, true);
    }
    @Test
    public void given_jointPoint_to_validate_empty_optional_when_getOptional_then_works() throws Throwable {
        Component component = getComponent();
        String[] paramsName = {};
        Object[] paramsArgs = {};
        JoinPoint joinPoint = mockJoinPoint("getOptional",paramsName, paramsArgs, "output");
        mockAutorizations(component, true, true, true);
        underTest.after(joinPoint, Optional.empty());
        verifyCallAutorizations(component, false, false, false);
    }

    @Test
    public void given_jointPoint_to_validate_propertyObject_when_testXPathInput_then_works() throws Throwable {
        Component component = getComponent();
        ApiVersion apiVersion = new ApiVersion().component(component);

        String[] paramsName = {"input"};
        Object[] paramsArgs = {apiVersion};
        JoinPoint joinPoint = mockJoinPoint("testXPathInput",paramsName, paramsArgs, "input");

        mockAutorizations(component, true, true, true);

        underTest.before(joinPoint);
        verifyCallAutorizations(component, true, true, true);
    }

    @Test
    public void given_jointPoint_to_validate_propertyObject_when_testXPathOutput_then_works() throws Throwable {
        Component component = getComponent();
        ApiVersion apiVersion = new ApiVersion().component(component);

        String[] paramsName = {};
        Object[] paramsArgs = {};
        JoinPoint joinPoint = mockJoinPoint("testXPathOutput",paramsName, paramsArgs, "output");

        mockAutorizations(component, true, true, true);

        underTest.after(joinPoint, Optional.of(apiVersion));
        verifyCallAutorizations(component, true, true, true);
    }

    @Test
    public void given_jointPoint_to_validate_null_propertyObject_when_testXPathOutput_then_throw_exception() throws Throwable {
        Component component = getComponent();
        ApiVersion apiVersion = new ApiVersion().component(null);

        String[] paramsName = {};
        Object[] paramsArgs = {};
        JoinPoint joinPoint = mockJoinPoint("testXPathOutput",paramsName, paramsArgs, "output");

        mockAutorizations(component, true, true, true);

        Exception error = null;
        try {
         underTest.after(joinPoint, Optional.of(apiVersion));
        } catch(Exception e){
            error = e;
        }
        Assertions.assertNotNull(error);
        Assertions.assertEquals(error.getMessage(), "Property from JXPath component of class org.kathra.core.model.ApiVersion's instance is null");
    }


    @Test
    public void given_joinPoint_to_validate_all_scopes_w_input_obj_w_error_when_before_then_throw_exception() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("testAll",paramsName, paramsArgs, "input");

        mockAutorizations(resource, true, true, false);
        Exception error = null;
        try {
            underTest.before(joinPoint);
        } catch(Exception e){
            error = e;
        }

        Assertions.assertNotNull(error);
        Assertions.assertEquals(error.getMessage(), "Access denied (scope:DELETE) for resource " +  resource.getId());
    }

    @Test
    public void given_joinPoint_to_validate_delete_scope_w_input_obj_when_before_then_works() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("delete",paramsName, paramsArgs, "input");

        mockAutorizations(resource, false, false, true);
        underTest.before(joinPoint);
        verifyCallAutorizations(resource, false, false, true);
    }

    @Test
    public void given_joinPoint_to_validate_delete_scope_w_input_id_when_before_then_works() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"id"};
        Object[] paramsArgs = {resource.getId()};
        JoinPoint joinPoint = mockJoinPoint("deleteById",paramsName, paramsArgs, "input");

        mockAutorizations(resource, false, false, true);
        underTest.before(joinPoint);
        verifyCallAutorizations(resource, false, false, true);
    }

    @Test
    public void given_joinPoint_to_validate_delete_scope_w_input_obj_w_error_when_before_then_throw_exception() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("testAll", paramsName, paramsArgs, "input");

        mockAutorizations(resource, true, true, false);
        Exception error = null;
        try {
            underTest.before(joinPoint);
        } catch(Exception e){
            error = e;
        }

        Assertions.assertNotNull(error);
        Assertions.assertEquals(error.getMessage(), "Access denied (scope:DELETE) for resource " +  resource.getId());
    }

    @Test
    public void given_joinPoint_to_validate_output_obj_when_after_then_throw_exception() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("get", paramsName, paramsArgs, "output");

        mockAutorizations(resource, false, false, false);
        underTest.before(joinPoint);
        Exception error = null;
        try {

            underTest.after(joinPoint, resource);
        } catch(Exception e){
            error = e;
        }

        Assertions.assertNotNull(error);
        Assertions.assertEquals(error.getMessage(), "Access denied (scope:READ) for resource " +  resource.getId());
    }

    @Test
    public void given_joinPoint_to_validate_output_list_obj_w_error_access_when_after_then_throws_exception() throws Throwable {

        Component resource = getComponent();
        Component resource2 = getComponent();
        Component resource3 = getComponent();

        String[] paramsName = {};
        Object[] paramsArgs = {};
        JoinPoint joinPoint = mockJoinPoint("getAll", paramsName, paramsArgs, "output");

        mockAutorizations(ImmutableList.of(resource, resource2), Scope.READ);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.UPDATE);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.DELETE);

        underTest.before(joinPoint);
        List list = new ArrayList();
        list.add(resource);
        list.add(resource2);
        list.add(resource3);
        Exception error = null;
        try {
            underTest.after(joinPoint, list);
        } catch(Exception e){
            error = e;
        }

        Assertions.assertNotNull(error);
        Assertions.assertEquals(error.getMessage(), "Access denied for resource " +  resource3.getId());
    }

    @Test
    public void given_joinPoint_to_filter_output_list_obj_when_after_then_works() throws Throwable {

        Component resource = getComponent();
        Component resource2 = getComponent();
        Component resource3 = getComponent();

        String[] paramsName = {};
        Object[] paramsArgs = {};
        JoinPoint joinPoint = mockJoinPoint("getAllAccessible", paramsName, paramsArgs, "output");

        mockAutorizations(ImmutableList.of(resource, resource2, resource3), Scope.READ);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.UPDATE);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.DELETE);

        underTest.before(joinPoint);
        List list = new ArrayList();
        list.add(resource);
        list.add(resource2);
        list.add(resource3);
        underTest.after(joinPoint, list);

        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.contains(resource));
        Assertions.assertTrue(list.contains(resource2));
        Assertions.assertTrue(list.contains(resource3));
    }

    @Test
    public void given_joinPoint_to_filter_output_list_w_JXPath_obj_when_after_then_works() throws Throwable {

        Component resource = getComponent();
        Component resource2 = getComponent();
        Component resource3 = getComponent();

        String[] paramsName = {};
        Object[] paramsArgs = {};
        JoinPoint joinPoint = mockJoinPoint("getAllAccessibleByXPath", paramsName, paramsArgs, "output/component");

        mockAutorizations(ImmutableList.of(resource, resource3), Scope.READ);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.UPDATE);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.DELETE);

        underTest.before(joinPoint);
        ApiVersion apiVersion = new ApiVersion().component(resource);
        ApiVersion apiVersion2 = new ApiVersion().component(resource2);
        ApiVersion apiVersion3 = new ApiVersion().component(resource3);
        List list = new ArrayList();
        list.add(apiVersion);
        list.add(apiVersion2);
        list.add(apiVersion3);

        underTest.after(joinPoint, list);

        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.contains(apiVersion));
        Assertions.assertFalse(list.contains(apiVersion2));
        Assertions.assertTrue(list.contains(apiVersion3));
    }


    @Test
    public void given_joinPoint_to_filter_input_list_obj_when_after_then_works() throws Throwable {

        Component resource = getComponent();
        Component resource2 = getComponent();
        Component resource3 = getComponent();
        List list = new ArrayList();
        list.add(resource);
        list.add(resource2);
        list.add(resource3);

        String[] paramsName = {"list"};
        Object[] paramsArgs = {list};
        JoinPoint joinPoint = mockJoinPoint("deleteAllAccessible", paramsName, paramsArgs, "list");

        mockAutorizations(ImmutableList.of(resource, resource2, resource3), Scope.READ);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.UPDATE);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.DELETE);

        underTest.before(joinPoint);
        underTest.after(joinPoint, list);

        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.contains(resource));
        Assertions.assertTrue(list.contains(resource2));
        Assertions.assertFalse(list.contains(resource3));
    }

    @Test
    public void given_joinPoint_to_filter_input_w_xPath_list_obj_when_after_then_works() throws Throwable {

        Component resource = getComponent();
        Component resource2 = getComponent();
        Component resource3 = getComponent();
        ApiVersion apiVersion = new ApiVersion().component(resource);
        ApiVersion apiVersion2 = new ApiVersion().component(resource2);
        ApiVersion apiVersion3 = new ApiVersion().component(resource3);

        List list = new ArrayList();
        list.add(apiVersion);
        list.add(apiVersion2);
        list.add(apiVersion3);

        String[] paramsName = {"list"};
        Object[] paramsArgs = {list};
        JoinPoint joinPoint = mockJoinPoint("deleteAllAccessibleByXPath", paramsName, paramsArgs, "list/component");

        mockAutorizations(ImmutableList.of(resource, resource2, resource3), Scope.READ);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.UPDATE);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.DELETE);

        underTest.before(joinPoint);
        underTest.after(joinPoint, list);

        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.contains(apiVersion));
        Assertions.assertTrue(list.contains(apiVersion2));
        Assertions.assertFalse(list.contains(apiVersion3));
    }


    @Test
    public void given_joinPoint_to_validate_input_list_obj_w_error_access_when_before_then_throw_exception() throws Throwable {

        Component resource = getComponent();
        Component resource2 = getComponent();
        Component resource3 = getComponent();

        List list = new ArrayList();
        list.add(resource);
        list.add(resource2);
        list.add(resource3);

        String[] paramsName = {"list"};
        Object[] paramsArgs = {list};
        JoinPoint joinPoint = mockJoinPoint("updateAll", paramsName, paramsArgs, "list");

        mockAutorizations(ImmutableList.of(resource, resource2, resource3), Scope.READ);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.UPDATE);
        mockAutorizations(ImmutableList.of(resource, resource2), Scope.DELETE);

        Exception error = null;
        try {
            underTest.before(joinPoint);
        } catch(Exception e){
            error = e;
        }

        Assertions.assertNotNull(error);
        Assertions.assertEquals(error.getMessage(), "Access denied for resource " +  resource3.getId());
    }


    @Test
    public void given_joinPoint_to_register_input_obj_when_after_then_throw_works() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("register", paramsName, paramsArgs, "input");

        mockAutorizations(resource, true, true, true);
        underTest.before(joinPoint);
        underTest.after(joinPoint, resource);
        Mockito .verify(resourceSecurityService)
                .grantAccess(   Mockito.eq(securityContext),
                                Mockito.argThat(arg -> arg.getId().equals(resource.getId())),
                                Mockito.eq(Scope.values()));

    }

    @Test
    public void given_joinPoint_to_unregister_input_obj_when_after_then_throw_works() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("unregister", paramsName, paramsArgs, "input");

        mockAutorizations(resource, true, true, true);
        underTest.before(joinPoint);
        underTest.after(joinPoint, resource);
        Mockito .verify(resourceSecurityService)
                .revokeAccess(   Mockito.eq(securityContext),
                        Mockito.argThat(arg -> arg.getId().equals(resource.getId())));

    }

    @Test
    public void given_joinPoint_to_register_output_obj_when_after_then_throw_works() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("register", paramsName, paramsArgs, "output");

        mockAutorizations(resource, true, true, true);
        underTest.before(joinPoint);
        underTest.after(joinPoint, resource);
        Mockito .verify(resourceSecurityService)
                .grantAccess(   Mockito.eq(securityContext),
                        Mockito.argThat(arg -> arg.getId().equals(resource.getId())),
                        Mockito.eq(Scope.values()));

    }

    @Test
    public void given_joinPoint_to_unregister_output_obj_when_after_then_throw_works() throws Throwable {

        Component resource = getComponent();

        String[] paramsName = {"input"};
        Object[] paramsArgs = {resource};
        JoinPoint joinPoint = mockJoinPoint("unregister", paramsName, paramsArgs, "output");

        mockAutorizations(resource, true, true, true);
        underTest.before(joinPoint);
        underTest.after(joinPoint, resource);
        Mockito .verify(resourceSecurityService)
                .revokeAccess(   Mockito.eq(securityContext),
                        Mockito.argThat(arg -> arg.getId().equals(resource.getId())));

    }

    private Component getComponent() {
        Component resource = new Component();
        resource.setId(Math.random()*1000+"");
        return resource;
    }


    private JoinPoint mockJoinPoint(String methodName, String[] paramsName, Object[] paramsArgs, String targetName) {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        MethodSignature methodSignature = Mockito.mock(MethodSignature.class);

        Method method = Arrays.stream(TestService.class.getMethods()).filter(m -> m.getName().equals(methodName)).findFirst().get();
        Mockito.when(joinPoint.getArgs()).thenReturn(paramsArgs);
        Mockito.when(defaultParameterNameDiscoverer.getParameterNames(method)).thenReturn(paramsName);

        Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
        Mockito.when(methodSignature.getMethod()).thenReturn(method);

        return joinPoint;
    }
}
