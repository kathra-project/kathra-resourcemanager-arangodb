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

package org.kathra.resourcemanager.implementationversion.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.implementationversion.dao.ImplementationVersionDb;
import org.kathra.resourcemanager.apiversion.dao.ApiVersionDb;


/**
 * Edge linking ImplementationVersion and ApiVersion
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T12:41:55.426Z
 * @author jboubechtoula
 */
@Edge
public class ImplementationVersionApiVersionEdge {

    @Id
    public String id;

    @From(lazy = true)
    public ImplementationVersionDb implementationVersion;

    @To(lazy = true)
    public ApiVersionDb apiVersion;

    public ImplementationVersionApiVersionEdge(){

    }

    public ImplementationVersionApiVersionEdge(final ImplementationVersionDb implementationVersion, final ApiVersionDb apiVersion) {
        this.apiVersion = apiVersion;
        this.implementationVersion = implementationVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImplementationVersionDb getImplementationVersion() {
        return implementationVersion;
    }

    public void setImplementationVersion(ImplementationVersionDb implementationVersion) {
        this.implementationVersion = implementationVersion;
    }

    public ApiVersionDb getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(ApiVersionDb apiVersion) {
        this.apiVersion = apiVersion;
    }
}
