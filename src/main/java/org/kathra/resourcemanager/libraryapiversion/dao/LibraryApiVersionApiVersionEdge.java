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

package org.kathra.resourcemanager.libraryapiversion.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.libraryapiversion.dao.LibraryApiVersionDb;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionDb;


/**
 * Edge linking LibraryApiVersion and ApiVersion
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:21.077Z
 * @author jboubechtoula
 */
@Edge
public class LibraryApiVersionApiVersionEdge {

    @Id
    public String id;

    @From(lazy = true)
    public LibraryApiVersionDb libraryApiVersion;

    @To(lazy = true)
    public ApiVersionDb apiVersion;

    public LibraryApiVersionApiVersionEdge(){

    }

    public LibraryApiVersionApiVersionEdge(final LibraryApiVersionDb libraryApiVersion, final ApiVersionDb apiVersion) {
        this.apiVersion = apiVersion;
        this.libraryApiVersion = libraryApiVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LibraryApiVersionDb getLibraryApiVersion() {
        return libraryApiVersion;
    }

    public void setLibraryApiVersion(LibraryApiVersionDb libraryApiVersion) {
        this.libraryApiVersion = libraryApiVersion;
    }

    public ApiVersionDb getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(ApiVersionDb apiVersion) {
        this.apiVersion = apiVersion;
    }
}
