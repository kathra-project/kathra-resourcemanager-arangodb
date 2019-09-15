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
package org.kathra.resourcemanager.resource.dao;

import com.arangodb.ArangoCursor;
import com.arangodb.model.CollectionCreateOptions;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.core.ArangoOperations;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kathra.core.model.Resource;
import org.kathra.resourcemanager.component.dao.ComponentDb;
import org.kathra.resourcemanager.resource.converter.ConverterResourceToResourceDb;
import org.apache.catalina.util.ResourceSet;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Abstract Resource Service managing CrudRepository and object mapping
 *
 * @param <X>
 * @param <V>
 */
public abstract class AbstractResourceDao<X extends Resource,V extends IResourceDb, ID> implements ConverterResourceToResourceDb<X,V> {


    protected ArangoOperations operations;
    protected CrudRepository<V, ID> repository;
    protected Class<V> clazzResourcedDb = null;

    public AbstractResourceDao(CrudRepository<V, ID> repository, ArangoOperations operations) {
        this.repository = repository;
        this.operations = operations;
        initResourcedDbClazz();
        initResourceReferenceCollectionIfNotExist();

    }

    private void initResourcedDbClazz() {
        Class clazz = getClass();
        do {
            try {
                clazzResourcedDb = (Class<V>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[1];
            } catch(Exception e) {
                clazz = getClass().getSuperclass();
            }
        } while(!clazz.equals(Object.class) && clazzResourcedDb == null);
        if (clazzResourcedDb == null)
            throw new RuntimeException("Unable to find clazz parameter");
    }

    public void initResourceReferenceCollectionIfNotExist(){
        try {
            operations.collection(ResourceReference.class.getAnnotationsByType(Document.class)[0].value());
        } catch (Exception e) {
            //if exception occurred, we suppose the collection doesn't exist
            operations.insert(new ResourceReference("init"));
            operations.delete("init", Resource.class);
            e.printStackTrace();
        }
    }

    public void create(X object, String author) throws Exception {
        V objectDb = this.convertResourceToResourceDb(object);
        objectDb.setStatus(Resource.StatusEnum.PENDING);
        updateCreateAt(objectDb);
        objectDb.setCreatedBy(author);
        objectDb.setId(generateIdentifier(objectDb));
        operations.insert(objectDb);
        object.setId(objectDb.getId().toString());
        object.setStatus(objectDb.getStatus());
        object.setCreatedAt(Long.valueOf(objectDb.getCreatedAt()).intValue());
        object.setCreatedBy(author);
    }

    private ID generateIdentifier(V object) {
        int maxAttempt = 5;
        int iAttempt = 0;
        ResourceReference resourceReference = getResourceReference(object);
        ArangoCursor<?> result;
        String uuid;
        do {
            if (iAttempt > maxAttempt) {
                throw new RuntimeException("Unable to generate an uuid identifier");
            }
            uuid = UUID.randomUUID().toString();
            result = operations.query("FOR doc IN " + ResourceReference.class.getAnnotationsByType(Document.class)[0].value() +" FILTER doc._key == \"" + uuid + "\" RETURN doc", ResourceReference.class);
            iAttempt++;
        } while(result.count() > 0);
        resourceReference.uuid = uuid;
        operations.insert(resourceReference);
        return (ID) resourceReference.uuid;
    }

    private ResourceReference getResourceReference(V object) {
        ResourceReference resourceReference = new ModelMapper().map(object, ResourceReference.class);
        resourceReference.name = object.getName();
        resourceReference.status = object.getStatus();
        resourceReference.type = object.getClass().getAnnotationsByType(Document.class)[0].value();
        resourceReference.createdAt = object.getCreatedAt();
        resourceReference.createdBy = object.getUpdatedBy();
        resourceReference.updatedAt = object.getUpdatedAt();
        resourceReference.updatedBy = object.getUpdatedBy();
        if (object.getId() != null) { resourceReference.uuid = object.getId().toString(); }
        return resourceReference;
    }

    private void updateResourceReference(ResourceReference object) {
        operations.update(object.uuid, object);
    }


    /**
     *
     * @param object
     */
    public void delete(X object, String author) throws Exception {
        V objectDb = repository.findById((ID)object.getId()).get();
        updateUpdatedAt(objectDb);
        objectDb.setStatus(Resource.StatusEnum.DELETED);
        objectDb.setUpdatedBy(author);
        updateResourceReference(getResourceReference(objectDb));
        repository.save(objectDb);
        object.setStatus(Resource.StatusEnum.DELETED);
        object.setUpdatedAt(Long.valueOf(objectDb.getUpdatedAt()).intValue());
        object.setUpdatedBy(author);
    }

    public void update(X object, String author) throws Exception {
        V objectDb = this.convertResourceToResourceDb(object);
        updateUpdatedAt(objectDb);
        objectDb.setUpdatedBy(author);
        updateResourceReference(getResourceReference(objectDb));
        repository.save(objectDb);
        object.setUpdatedAt(Long.valueOf(objectDb.getUpdatedAt()).intValue());
        object.setUpdatedBy(author);
    }

    public List<X> findAll() {
        ArangoCursor<V> results = operations.query("FOR doc IN " + getDocumentOfResourceDb().value() + " FILTER doc.status != \"" + Resource.StatusEnum.DELETED.toString() + "\" RETURN doc", getClassOfResourceDb());
        return convertResourceDbToResource(StreamSupport.stream(results.spliterator(), false)).collect(Collectors.toList());
    }

    public List<X> findAll(List<ID> ids) throws Exception {
        ArangoCursor<V> results = operations.query("FOR doc IN " + getDocumentOfResourceDb().value() + " FILTER doc._key in "+ new ObjectMapper().writeValueAsString(ids) + " RETURN doc", getClassOfResourceDb());
        return convertResourceDbToResource(StreamSupport.stream(results.spliterator(), false)).collect(Collectors.toList());
    }

    public List<ID> findAllIdentifiers() throws Exception {
        return (List<ID>) StreamSupport.stream(operations.query("FOR doc IN " + getDocumentOfResourceDb().value() + " FILTER doc.status != \"" + Resource.StatusEnum.DELETED.toString() + "\" RETURN doc._key", String.class).spliterator(), false).collect(Collectors.toList());
    }

    protected Class<V> getClassOfResourceDb() {
        return clazzResourcedDb;
    }

    protected Document getDocumentOfResourceDb(){
        return getClassOfResourceDb().getAnnotationsByType(Document.class)[0];
    }

    public Optional<X> findById(ID id) {
        Optional<V> result = repository.findById(id);
        return result.isPresent() && (result.get().getStatus() == null || !result.get().getStatus().equals(Resource.StatusEnum.DELETED)) ? Optional.of(this.convertWithLean(result.get())) : Optional.empty();
    }

    private X convertWithLean(V objectDb) {
        return this.convertResourceDbToResource(objectDb);
    }

    private void updateUpdatedAt(V object){
        object.setUpdatedAt(System.currentTimeMillis()/1000);
    }
    private void updateCreateAt(V object){
        object.setCreatedAt(System.currentTimeMillis()/1000);
    }
}
