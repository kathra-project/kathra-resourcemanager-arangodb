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

package org.kathra.resourcemanager.implementation.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.implementation.dao.ImplementationDb;
import org.kathra.resourcemanager.component.dao.ComponentDb;


/**
 * Edge linking Implementation and Component
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:15.474Z
 * @author jboubechtoula
 */
@Edge
public class ImplementationComponentEdge {

    @Id
    public String id;

    @From(lazy = true)
    public ImplementationDb implementation;

    @To(lazy = true)
    public ComponentDb component;

    public ImplementationComponentEdge(){

    }

    public ImplementationComponentEdge(final ImplementationDb implementation, final ComponentDb component) {
        this.component = component;
        this.implementation = implementation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImplementationDb getImplementation() {
        return implementation;
    }

    public void setImplementation(ImplementationDb implementation) {
        this.implementation = implementation;
    }

    public ComponentDb getComponent() {
        return component;
    }

    public void setComponent(ComponentDb component) {
        this.component = component;
    }
}
