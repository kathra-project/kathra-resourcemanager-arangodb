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
package org.kathra.resourcemanager.resource.dao;

import com.arangodb.springframework.annotation.Document;
import org.kathra.core.model.Resource.StatusEnum;
import org.kathra.core.model.Resource;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractResourceDb<X extends Resource> implements IResourceDb<String,X> {


    @Id
    private String id;
    private StatusEnum status;
    private String name = null;
    private Map<String, Object> metadata = null;

    private String createdBy;
    private long createdAt;
    private String updatedBy;
    private long updatedAt;


    public AbstractResourceDb(){

    }
    public AbstractResourceDb(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String toArangoIdentifier() {
        Optional<String> annotationDocumentValue = Arrays.stream(this.getClass().getDeclaredAnnotations()).filter(annotation -> (annotation instanceof Document)).map(annotation -> ((Document) annotation).value()).findFirst();
        if (!annotationDocumentValue.isPresent()) {
            throw new IllegalStateException("Unable to find annotation "+Document.class.getName()+" into class "+this.getClass());
        }
        return annotationDocumentValue.get()+"/"+this.getId();
    }

    public boolean equals(Object obj) {
        return (obj instanceof IResourceDb) ? this.id.equals(((IResourceDb) obj).getId()) : super.equals(obj);
    }
}
