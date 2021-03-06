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

package org.kathra.resourcemanager.libraryapiversion.controller;

import org.kathra.core.model.LibraryApiVersion;
import org.kathra.resourcemanager.libraryapiversion.service.LibraryApiVersionService;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.resourcemanager.service.LibraryApiVersionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.kathra.resourcemanager.resource.service.AbstractService;
import java.util.List;

/**
 * Abstract Controller implementing LibraryApiVersionsService
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:21.078Z
 * @author jboubechtoula
 */
public abstract class AbstractLibraryApiVersionsController implements AbstractCrudController<LibraryApiVersion>, LibraryApiVersionsService {

    @Autowired
    private LibraryApiVersionService service;

    @Override
    public LibraryApiVersionService getService() {
        return service;
    }

    public LibraryApiVersion addLibraryApiVersion(LibraryApiVersion object) throws Exception {
        return add(object);
    }

    public String deleteLibraryApiVersion(String resourceId) throws Exception {
        delete(resourceId);
        return resourceId;
    }

    public LibraryApiVersion getLibraryApiVersion(String resourceId) throws Exception {
        return get(resourceId);
    }

    public List<LibraryApiVersion> getLibraryApiVersions() throws Exception {
        return getAll();
    }

    public LibraryApiVersion updateLibraryApiVersion(String resourceId, LibraryApiVersion object) throws Exception {
        return update(resourceId, object);
    }

    public LibraryApiVersion updateLibraryApiVersionAttributes(String resourceId, LibraryApiVersion object) throws Exception {
        return patch(resourceId, object);
    }
}
