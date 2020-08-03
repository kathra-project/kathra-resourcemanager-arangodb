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
package org.kathra.resourcemanager.resource.service;

import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.kathra.resourcemanager.resource.utils.LeanResourceUtils;
import org.kathra.resourcemanager.security.SessionService;
import javassist.NotFoundException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractService<X extends Resource,ID> {

    protected AbstractResourceDao<X,?,ID> serviceDao;
    protected SessionService sessionService;

    private LeanResourceUtils<X> leanUtils = new LeanResourceUtils<X>();

    public AbstractService(AbstractResourceDao serviceDao, SessionService sessionService) {
        this.serviceDao = serviceDao;
        this.sessionService = sessionService;
    }

    protected String getAuthor() {
        if (sessionService.getCurrentSession() == null) {
            throw new IllegalStateException("Unable to find current session");
        } else if (sessionService.getCurrentSession().getCallerName() == null) {
            throw new IllegalStateException("Unable to find caller name from current session");
        }
        return sessionService.getCurrentSession().getCallerName();
    }

    public void create(X object) throws Exception {
        serviceDao.create(object, getAuthor());
    }

    public void delete(X object) throws Exception {
        serviceDao.delete(object, getAuthor());
    }

    public void update(X object) throws Exception {
        serviceDao.update(object, getAuthor());
    }

    public void patch(X object) throws Exception {
        Optional<X> objectToUpdate = serviceDao.findById((ID) object.getId());
        if (!objectToUpdate.isPresent()) {
            throw new NotFoundException("Unable to find "+object.getId());
        }
        try {
            for(PropertyDescriptor propertyDescriptor :
                    Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()){
                patchProperty(object, objectToUpdate.get(), propertyDescriptor);
            }
            serviceDao.update(objectToUpdate.get(), getAuthor());
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Patch property
     * @param objectPatched
     * @param objectToUpdate
     * @param propertyDescriptor
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void patchProperty(X objectPatched, X objectToUpdate, PropertyDescriptor propertyDescriptor) throws Exception {
        if (propertyDescriptor.getName().equals("metadata") && objectPatched.getMetadata() != null) {
            if (objectToUpdate.getMetadata() == null) {
                objectToUpdate.setMetadata(objectPatched.getMetadata());
            } else {
                objectToUpdate.getMetadata().putAll(objectPatched.getMetadata());
            }
        } else {
            if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null) {
                Object valueDefined = propertyDescriptor.getReadMethod().invoke(objectPatched);
                if (valueDefined != null) {
                    propertyDescriptor.getWriteMethod().invoke(objectToUpdate, valueDefined);
                }
            }
        }
    }

    public List<X> findAll() throws Exception {
        return serviceDao.findAll().parallelStream().map(leanUtils::leanObject).collect(Collectors.toList());
    }

    public Optional<X> findById(ID id) throws Exception {
        return serviceDao.findById(id).map(leanUtils::leanObject);
    }

}