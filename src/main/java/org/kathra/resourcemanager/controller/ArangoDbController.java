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

package org.kathra.resourcemanager.controller;

import com.arangodb.*;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionType;
import com.arangodb.model.CollectionCreateOptions;
import com.arangodb.model.DocumentUpdateOptions;
import com.arangodb.model.HashIndexOptions;
import com.arangodb.velocypack.module.jdk8.VPackJdk8Module;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kathra.core.model.Resource;

import java.io.IOException;
import java.util.*;

/**
 * @author Jérémy Guillemot <Jeremy.Guillemot@kathra.org>
 */
public class ArangoDbController {
    ArangoDB arangoDb;
    ArangoDatabase database;
    private final ObjectMapper oMapper;

    public ArangoDbController(String host, int port, String user, String password, String dbname) {
        arangoDb = new ArangoDB.Builder()
                .host(host, port)
                .user(user)
                .password(password)
                .registerModule(new VPackJdk8Module())
                .build();

        database = arangoDb.db(dbname);

        oMapper = new ObjectMapper();
        oMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        oMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        if (!database.exists()) arangoDb.createDatabase(dbname);
    }

    public void initializeEdgesCollection(String name, String... attributes) throws ArangoDBException {
        if (attributes != null && attributes.length > 0) {
            initializeEdgesCollection(name, new HashSet(Arrays.asList(attributes)));
        }
    }

    public void initializeEdgesCollection(String name, Set<String> attributes) throws ArangoDBException {
        ArangoCollection collection = database.collection(name);
        if (!collection.exists()) {
            HashIndexOptions hio = new HashIndexOptions();
            hio.unique(true);
            database.createCollection(name, new CollectionCreateOptions().type(CollectionType.EDGES));
            collection.ensureHashIndex(attributes, hio);
        }
    }

    public void initializeCollection(String name) throws ArangoDBException {
        Set<String> attributes = new HashSet();
        attributes.add("id");
        initializeCollection(name, attributes);
    }

    public void initializeCollection(String name, Set<String> attributes) throws ArangoDBException {
        ArangoCollection collection = database.collection(name);
        //attributes.add("id");
        if (!collection.exists()) {
            database.createCollection(name, null);
            HashIndexOptions hio = new HashIndexOptions();
            hio.unique(true);
            collection.ensureHashIndex(attributes, hio);
            collection.ensureFulltextIndex(Collections.singletonList("name"), null);
        }
    }

    public void initializeCollection(String name, String... attributes) throws ArangoDBException {
        if (attributes.length > 0) {
            initializeCollection(name, new HashSet(Arrays.asList(attributes)));
        }
    }

    public ArangoDatabase getDatabase() {
        return database;
    }

    /**
     * Create resource
     *
     * @param body Created resource object (required)
     */
    public BaseDocument createResource(Resource body) throws ArangoDBException {
        return createResource(body, body.getClass().getSimpleName().toLowerCase() + "s");
    }

    /**
     * Create resource
     *
     * @param body Created resource object (required)
     */
    public BaseDocument createResource(Resource body, String collection) throws ArangoDBException {
        if (body.getId() == null || body.getId().isEmpty()) {
            body.id(generateGUID());
        }
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(body, Map.class);
        BaseDocument myObject = new BaseDocument(map);
        myObject.setKey(body.getId());
        myObject.setId(body.getId());
        database.collection(collection).insertDocument(myObject);
        return myObject;
    }

    /**
     * Delete scenario
     *
     * @param id The id of the resource that needs to be deleted (required)
     * @param c  The class of the resource that needs to be deleted (required)
     */
    public boolean deleteResource(String id, Class c) {
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("id", id);
        bindVars.put("@collection", c.getSimpleName().toLowerCase());
        String aqlQuery = "FOR resource IN @@collection FILTER resource.id == @id REMOVE resource IN @@collection";
        database.query(aqlQuery, bindVars, null, c);
        return true;
    }

    /**
     * Get resource by id
     *
     * @param id The id of the resource that needs to be fetched. (required)
     * @return Resource
     */
    public Object getResource(String id, String collection) throws Exception {
        return getResource(id, Object.class, collection);
    }

    /**
     * Get resource by id
     *
     * @param id The id of the resource that needs to be fetched. (required)
     * @return Resource
     */
    public Object getResource(String id, Class c) throws Exception {
        return getResource(id, c, c.getSimpleName().toLowerCase());
    }

    /**
     * Get resource by id
     *
     * @param id The id of the resource that needs to be fetched. (required)
     * @return Resource
     */
    public Object getResource(String id, Class c, String collection) throws Exception {
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("id", id);
        bindVars.put("@collection", collection);
        String aqlQuery = "FOR resource IN @@collection FILTER resource.id == @id RETURN resource";

        ArangoCursor<Object> cursor = database.query(aqlQuery, bindVars, null, c);

        Object o = null;
        while (cursor.hasNext()) {
            o = cursor.next();
        }
        cursor.close();
        return o;
    }

    public <O, C extends Class> List<O> getResourcesById(List<String> ids, C c, String collection) throws Exception {
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("ids", ids);
        bindVars.put("@collection", collection);
        String aqlQuery = "FOR resource IN @@collection FILTER resource.id IN @ids SORT resource.id ASC RETURN resource";

        ArangoCursor<Object> cursor = database.query(aqlQuery, bindVars, null, Object.class);

        List o = new ArrayList();
        while (cursor.hasNext()) {
            o.add(oMapper.convertValue(cursor.next(),c));
        }
        cursor.close();
        return o;
    }

    public <O, C extends Class> List<O> getResourcesById(List<String> ids, C c) throws Exception {
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("ids", ids);
        bindVars.put("@collection", c.getSimpleName().toLowerCase());
        String aqlQuery = "FOR resource IN @@collection FILTER resource.id IN @ids SORT resource.id ASC RETURN resource";

        ArangoCursor<Object> cursor = database.query(aqlQuery, bindVars, null, c);

        List o = new ArrayList();
        while (cursor.hasNext()) {
            o.add(c.cast(cursor.next()));
        }
        cursor.close();
        return o;
    }

    /**
     * Finds all resources in collection
     *
     * @param query The partial name of the resources to retrieve (optional)
     * @return List<Object>
     */
    public <O extends Object, C extends Class> List<O> getAllResources(String query, C c) throws Exception {
        String aqlQuery;
        ArangoCursor<Object> cursor;
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("@collection", c.getSimpleName().toLowerCase() + "s");
        if (query != null && !query.isEmpty()) {
            bindVars.put("name", query);
            aqlQuery = "FOR resources IN FULLTEXT(\"@@collection\", \"name\", @name) RETURN resources";
        } else {
            aqlQuery = "FOR resources IN @@collection LIMIT 1000 RETURN resources";
        }
        cursor = database.query(aqlQuery, bindVars, null, c);

        List l = new ArrayList();
        while (cursor.hasNext()) {
            l.add(c.cast(cursor.next()));
        }
        cursor.close();
        return l;
    }

    /**
     * Finds all resources in collection and returns the specified fields
     *
     * @param query The partial name of the resources to retrieve (optional)
     * @return List<Object>
     */
    public <O extends Object, C extends Class> List<O> getAllResources(C c, List<String> fields) throws Exception {
        if (fields == null || fields.size() == 0) {
            return getAllResources(null, c);
        }

        final Map<String, Object> bindVars = new HashMap();
        bindVars.put("@collection", c.getSimpleName().toLowerCase());
        String aqlQuery = "FOR resource IN @@collection RETURN { ";
        final int fieldsNumber = fields.size();
        int fieldCounter = 0;
        for (String field : fields) {
            aqlQuery += field + ": resource." + field;
            if (++fieldCounter < fieldsNumber)
                aqlQuery += ", ";
        }
        aqlQuery += " }";

        System.out.println("=> " + aqlQuery);

        final ArangoCursor<Object> cursor = database.query(aqlQuery, bindVars, null, c);
        final List result = new ArrayList();
        while (cursor.hasNext()) {
            result.add(c.cast(cursor.next()));
        }
        cursor.close();
        return result;
    }

    /**
     * Update resource
     *
     * @param id   Id of the resource that need to be updated (required)
     * @param body Updated resource object (required)
     */
    public boolean updateResource(String id, Object body) {
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("body", body);
        bindVars.put("id", id);
        bindVars.put("@collection", body.getClass().getSimpleName().toLowerCase());
        String aqlQuery = "FOR resource IN @@collection FILTER resource.id == @id REPLACE resource WITH @body IN @@collection";
        database.query(aqlQuery, bindVars, null, body.getClass());
        return true;
    }

    /**
     * Update resource
     *
     * @param id   Id of the resource that need to be updated (required)
     * @param body Updated resource object (required)
     */
    public <O extends Object> Object updateResourceAttributes(String id, O o, String collection) {
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("body", o);
        bindVars.put("id", collection + "/" + id);
        DocumentUpdateOptions options = new DocumentUpdateOptions();
        options.returnNew(true);
        HashMap aNew = database.collection(collection).updateDocument(id, oMapper.convertValue(o, HashMap.class), options).getNew();
        return oMapper.convertValue(aNew, o.getClass());
    }

    private String getNewIdForResource(String collection) throws IOException {
        Map<String, Object> bindVars = new HashMap();
        bindVars.put("@collection", collection);
        String aqlQuery = "FOR resource IN @@collection COLLECT AGGREGATE maxId = MAX(resource.id) RETURN maxId";
        ArangoCursor<String> cursor = database.query(aqlQuery, bindVars, null, String.class);
        String l = null;
        while (cursor.hasNext()) {
            l = cursor.next();
            break;
        }
        cursor.close();
        return l == null ? Integer.toUnsignedString(1) : l + 1;
    }

    private String generateGUID() {
        return UUID.randomUUID().toString();
    }
}
