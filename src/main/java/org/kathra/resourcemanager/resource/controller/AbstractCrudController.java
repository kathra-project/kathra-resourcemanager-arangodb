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
package org.kathra.resourcemanager.resource.controller;

import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.resource.service.AbstractService;
import org.kathra.utils.KathraException;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface AbstractCrudController<X extends Resource> {

    public AbstractService<X, String> getService();

    default X get(String resourceId) throws Exception {
        try {
            if (resourceId == null) {
                throw new IllegalArgumentException("identifier is null");
            }
            verifyStringIsUUID(resourceId);
            return getService().findById(resourceId).orElseThrow(() -> new NotFoundException("not found object with id :" + resourceId));
        } catch(Exception e) {
            manageException(e);
            return null;
        }
    }

    private void verifyStringIsUUID(String uuidAsString) {
        try{
            UUID.fromString(uuidAsString);
        } catch (IllegalArgumentException exception){
            throw new IllegalArgumentException("resourceId malformated : "+uuidAsString);
        }
    }

    default X add(X object) throws Exception {
        try {
            if (object == null) {
                throw new IllegalArgumentException("object is null");
            }
            getService().create(object);
            return get(object.getId());
        } catch(Exception e) {
            manageException(e);
            return null;
        }
    }

    default void delete(String resourceId) throws Exception {
        try {
            verifyStringIsUUID(resourceId);
            getService().delete(this.get(resourceId));
        } catch(Exception e) {
            manageException(e);
        }
    }

    default List<X> getAll() throws Exception {
        try {
            return getService().findAll();
        } catch(Exception e) {
            manageException(e);
            return null;
        }
    }

    default X update(String resourceId, X object) throws Exception {
        try {
            if (resourceId == null) {
                throw new IllegalArgumentException("identifier is null");
            }
            verifyStringIsUUID(resourceId);
            if (object == null) {
                throw new IllegalArgumentException("object is null");
            }
            object.setId(resourceId);
            getService().update(object);
            return this.get(resourceId);
        } catch(Exception e) {
            manageException(e);
            return null;
        }
    }

    default X patch(String resourceId, X object) throws Exception {
        try {
            if (resourceId == null) {
                throw new IllegalArgumentException("identifier is null");
            }
            verifyStringIsUUID(resourceId);
            if (object == null) {
                throw new IllegalArgumentException("object is null");
            }
            object.setId(resourceId);
            getService().patch(object);
            return this.get(resourceId);
        } catch(Exception e) {
            manageException(e);
            return null;
        }
    }

    default void manageException(Exception e) throws KathraException {
        if (e instanceof IllegalArgumentException) {
            throw new KathraException(e.getMessage(), e, KathraException.ErrorCode.BAD_REQUEST);
        } else if (e instanceof IllegalAccessException) {
            throw new KathraException(e.getMessage(), e, KathraException.ErrorCode.UNAUTHORIZED);
        } else if (e instanceof NotFoundException) {
            throw new KathraException(e.getMessage(), e, KathraException.ErrorCode.NOT_FOUND);
        } else if (e instanceof KathraException) {
            throw (KathraException) e;
        } else {
            throw new KathraException(e.getMessage(), e, KathraException.ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
