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
import org.kathra.resourcemanager.security.SessionService;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
public class ResourceSecurityProcessor {

    @Autowired
    private ResourceSecurityService resourceSecurityService;

    @Autowired
    private SessionService sessionSecurity;

    private DefaultParameterNameDiscoverer defaultParameterNameDiscoverer;

    private final static String TARGET_OUTPUT_PREFIX = "output";
    private final static String TARGET_SEPARATOR_JXPATH = "/";

    public ResourceSecurityProcessor(){
        defaultParameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public ResourceSecurityProcessor(ResourceSecurityService resourceSecurityService, SessionService sessionSecurity) {
        this.resourceSecurityService = resourceSecurityService;
        this.sessionSecurity = sessionSecurity;
        defaultParameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public ResourceSecurityProcessor(ResourceSecurityService resourceSecurityService, SessionService sessionSecurity, DefaultParameterNameDiscoverer defaultParameterNameDiscoverer) {
        this.resourceSecurityService = resourceSecurityService;
        this.sessionSecurity = sessionSecurity;
        this.defaultParameterNameDiscoverer = defaultParameterNameDiscoverer;
    }

    /**
     * Execute before method annotated
     * @param joinPoint
     * @throws Throwable
     */
    @Before("@annotation(ResourceSecured)")
    public void before(JoinPoint joinPoint) throws Throwable {

        Executable method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ResourceSecured resourceSecuredAnnotation = method.getAnnotation(ResourceSecured.class);
        switch(resourceSecuredAnnotation.action()) {
            case VALID:
                // Check input
                Optional<Object> targetToValidate = getTargetFromJoinPoint(joinPoint, resourceSecuredAnnotation);
                String XPath = getJXPathFromTarget(resourceSecuredAnnotation);
                if (targetToValidate.isPresent()){
                    validate(resourceSecuredAnnotation, sessionSecurity.get(), XPath == null ? targetToValidate.get() : getObjectFromXPath(targetToValidate.get(), XPath));
                }
                break;
            case FILTER:
                Optional<Object> targetToFilter = getTargetFromJoinPoint(joinPoint, resourceSecuredAnnotation);
                if (targetToFilter.isPresent()){
                    if (targetToFilter.get() instanceof List){
                        filter(resourceSecuredAnnotation, sessionSecurity.get(), (List) targetToFilter.get());
                    } else {
                        throw new IllegalStateException("Input param object to filter should be a list");
                    }
                }
                break;
        }
    }

    /**
     * Find object from joinJoint and parameter name
     * @param joinPoint
     * @param resourceSecuredAnnotation
     * @return
     */
    private Optional<Object> getTargetFromJoinPoint(JoinPoint joinPoint,  ResourceSecured resourceSecuredAnnotation) {
        String[] params = defaultParameterNameDiscoverer.getParameterNames(((MethodSignature) joinPoint.getSignature()).getMethod());
        Optional<Object> object = IntStream.range(0, params.length)
                .filter(i -> params[i].equals(getRootFromTarget(resourceSecuredAnnotation)))
                .mapToObj(i -> joinPoint.getArgs()[i])
                .findFirst();
        return object;
    }

    private Object getTargetFromOutput(Object outputObject, ResourceSecured resourceSecuredAnnotation) {
        if (!resourceSecuredAnnotation.target().startsWith(TARGET_OUTPUT_PREFIX)) {
            throw new IllegalStateException("target should start with '"+TARGET_OUTPUT_PREFIX+"'");
        }
        Object objectToGet = outputObject instanceof Optional ? ((Optional) outputObject).orElse(null) : outputObject;
        if (objectToGet == null) {
            return null;
        }
        String XPath = getJXPathFromTarget(resourceSecuredAnnotation);
        return (XPath != null) ? getObjectFromXPath(objectToGet, XPath) : objectToGet;
    }

    private Object getObjectFromXPath(Object object, String JXPath) {
        Object propertyObject = JXPathContext.newContext(object).getValue(JXPath);
        if (propertyObject == null) {
            throw new IllegalArgumentException("Property from JXPath "+JXPath+" of "+object.getClass().toString()+"'s instance is null");
        }
        return propertyObject;
    }

    /**
     * Execute after method annotated
     * @param joinPoint
     * @param objectReturned
     * @throws Throwable
     */
    @AfterReturning(pointcut = "@annotation(ResourceSecured)", returning = "objectReturned")
    public void after(JoinPoint joinPoint, Object objectReturned) throws Throwable {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ResourceSecured resourceSecuredAnnotation = method.getAnnotation(ResourceSecured.class);
        // Check input
        SecurityContext context = sessionSecurity.get();

        switch(resourceSecuredAnnotation.action()) {
            case VALID:
                // Check output
                if (resourceSecuredAnnotation.target().startsWith(TARGET_OUTPUT_PREFIX) && !getTargetFromJoinPoint(joinPoint, resourceSecuredAnnotation).isPresent()){
                    validate(resourceSecuredAnnotation, context, getTargetFromOutput(objectReturned, resourceSecuredAnnotation));
                }
                break;
            case FILTER:
                if (resourceSecuredAnnotation.target().startsWith(TARGET_OUTPUT_PREFIX) && !getTargetFromJoinPoint(joinPoint, resourceSecuredAnnotation).isPresent()) {
                    if (objectReturned instanceof List){
                        filter(resourceSecuredAnnotation, sessionSecurity.get(), (List) objectReturned);
                    } else {
                        throw new IllegalStateException("Input param object to filter should be a list");
                    }
                }
                break;
            case REGISTER:
                Optional<Object> targetToRegister = getTargetFromJoinPoint(joinPoint, resourceSecuredAnnotation);
                if (!targetToRegister.isPresent() && resourceSecuredAnnotation.target().startsWith(TARGET_OUTPUT_PREFIX)){
                    targetToRegister = Optional.of(objectReturned);
                }
                if (targetToRegister.isPresent() && targetToRegister.get().getClass().isAssignableFrom(resourceSecuredAnnotation.clazz())){
                    resourceSecurityService.grantAccess(context, (Resource) targetToRegister.get(), resourceSecuredAnnotation.scopes());
                }
                break;
            case UNREGISTER:
                Optional<Object> targetToUnregister = getTargetFromJoinPoint(joinPoint, resourceSecuredAnnotation);
                if (!targetToUnregister.isPresent() && resourceSecuredAnnotation.target().startsWith(TARGET_OUTPUT_PREFIX)){
                    targetToUnregister = Optional.of(objectReturned);
                }
                if (targetToUnregister.isPresent() && targetToUnregister.get().getClass().isAssignableFrom(resourceSecuredAnnotation.clazz())){
                    resourceSecurityService.revokeAccess(context, (Resource) targetToUnregister.get());
                }
                break;
        }
    }

    /**
     * Validate object or filter item list
     * @param resourceSecuredAnnotation
     * @param context
     * @param argument
     * @throws Exception
     */
    private void validate(ResourceSecured resourceSecuredAnnotation, SecurityContext context, Object argument) throws Exception {
        if (argument instanceof Resource && argument.getClass().isAssignableFrom(resourceSecuredAnnotation.clazz())) {
            checkResource(resourceSecuredAnnotation, context, (Resource) argument);
        } else if (argument instanceof List) {
            List list = (List) argument;
            List<String> ids = getIdentifiersAuthorized(context, resourceSecuredAnnotation.scopes(), resourceSecuredAnnotation.clazz());
            for (Object item : list) {
                if (item instanceof Resource && item.getClass().isAssignableFrom(resourceSecuredAnnotation.clazz())) {
                    if (!ids.contains(((Resource) item).getId())){
                        throw new IllegalAccessException("Access denied for resource " +  ((Resource) item).getId());
                    }
                }
            }
        } else if (argument instanceof String) {
            // string is considered like identifier
            Resource resource = resourceSecuredAnnotation.clazz().getConstructor().newInstance();
            resource.setId((String) argument);
            checkResource(resourceSecuredAnnotation, context, resource);
        } else if (argument != null){
            throw new NotImplementedException("Not implemented ! ");
        }
    }

    private String getRootFromTarget(ResourceSecured resourceSecuredAnnotation){
        return resourceSecuredAnnotation.target().split(TARGET_SEPARATOR_JXPATH)[0];
    }

    private String getJXPathFromTarget(ResourceSecured resourceSecuredAnnotation){
        String target = resourceSecuredAnnotation.target();
        String[] objectPath = target.split(TARGET_SEPARATOR_JXPATH);
        if (objectPath.length > 1) {
            return String.join(".", ArrayUtils.remove(objectPath, 0));
        } else {
            return null;
        }
    }

    /**
     *
     * @param resourceSecuredAnnotation
     * @param context
     * @param list
     * @throws Exception
     */
    private void filter(ResourceSecured resourceSecuredAnnotation, SecurityContext context, List list) throws Exception {
        List itemsToRemove = new ArrayList();
        String XPath = getJXPathFromTarget(resourceSecuredAnnotation);

        HashMap<Class<? extends Resource>, List<String>> idsAuthorized = new HashMap<>();

        for (Object item : list) {
            Object objectToValidate = (XPath == null) ? item : getObjectFromXPath(item, XPath);
            Resource resourceToValidate;
            if (objectToValidate instanceof String) {
                // string is considered like identifier
                resourceToValidate = resourceSecuredAnnotation.clazz().getConstructor().newInstance();
                resourceToValidate.setId((String) objectToValidate);
            } else if (objectToValidate instanceof Resource) {
                resourceToValidate = (Resource) objectToValidate;
            } else {
                continue;
            }

            Class<? extends Resource> clazz = resourceSecuredAnnotation.clazz();

            // retrieve resource authorized for this clazz
            if (!idsAuthorized.containsKey(clazz)) {
                idsAuthorized.put(clazz, getIdentifiersAuthorized(context, resourceSecuredAnnotation.scopes(), clazz));
            }
            if (!idsAuthorized.get(clazz).contains(resourceToValidate.getId())) {
                itemsToRemove.add(item);
                continue;
            }
        }
        /*
        List<String> ids = getIdentifiersAuthorized(context, resourceSecuredAnnotation.scopes(), resourceSecuredAnnotation.clazz());
        for (Object item : list) {
            if (item instanceof Resource && item.getClass().isAssignableFrom(resourceSecuredAnnotation.clazz())) {
                if (!ids.contains(((Resource) item).getId())){
                    itemsToRemove.add(item);
                }
            }
        }
        */
        list.removeAll(itemsToRemove);
    }

    private List<String> getIdentifiersAuthorized(SecurityContext securityContext, Scope[] scopes, Class clazz) throws Exception {
        List<String> identifiers = new ArrayList<>();
        for(Scope scope:scopes){
            List<String> idAuthorizedForScope = resourceSecurityService.getIdsAuthorized(securityContext, clazz, scope);
            if (identifiers.isEmpty()) {
                identifiers.addAll(idAuthorizedForScope);
            } else {
                List<String> idsNotAuthorized = identifiers.parallelStream().filter(id -> idAuthorizedForScope.stream().noneMatch(idScope -> idScope.equals(id))).collect(Collectors.toList());
                identifiers.removeAll(idsNotAuthorized);
            }
        }
        return identifiers;
    }

    /**
     * Check resource with scopes associated
     * @param resourceSecuredAnnotation
     * @param context
     * @param argument
     * @throws Exception
     */
    private void checkResource(ResourceSecured resourceSecuredAnnotation, SecurityContext context, Resource argument) throws Exception {
        for (Scope scope : resourceSecuredAnnotation.scopes()) {
            if (!resourceSecurityService.isAuthorized(context, argument, scope)) {
                throw new IllegalAccessException("Access denied (scope:"+scope+") for resource " +  argument.getId());
            }
        }
    }

}
