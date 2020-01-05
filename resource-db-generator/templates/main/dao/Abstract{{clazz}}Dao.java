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

package {{package}}.dao;

import com.arangodb.springframework.core.ArangoOperations;
import {{coreModelPackage}}.{{clazz}};
import org.kathra.resourcemanager.resource.dao.AbstractResourceDao;
import org.kathra.resourcemanager.resource.utils.LeanResourceDbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.kathra.resourcemanager.resource.utils.EdgeUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import fr.xebia.extras.selma.Selma;
import java.util.stream.Stream;

{{imports}}

/**
 * Abtrasct dao service to manage {{clazz}} using {{clazz}}Db with ArangoRepository
 *
 * Auto-generated by {{generatorSignature}}
 * @author {{author}}
 */
public abstract class Abstract{{clazz}}Dao extends AbstractResourceDao<{{clazz}}, {{clazz}}Db, String> {

{{fields}}

    public Abstract{{clazz}}Dao(@Autowired {{clazz}}Repository repository, @Autowired ArangoOperations operations){
        super(repository, operations);
    }

    @PostConstruct
    public void initCollectionIfNotExist(){
        if(repository.count() == 0) {
            try {
                operations.insert(new {{clazz}}Db("init"));
                repository.deleteById("init");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
{{initCollectionIfNotExistOverride}}
    }

    @Override
    public void create({{clazz}} object, String author) throws Exception {
        super.create(object, author);
        updateReferences(object);
    }

    @Override
    public void update({{clazz}} object, String author) throws Exception {
        super.update(object, author);
        updateReferences(object);
    }
    @Override
    public void delete({{clazz}} object, String author) throws Exception {
        super.delete(object, author);
        updateReferences(object);
    }
    private void updateReferences({{clazz}} object) throws Exception {
        {{clazz}}Db resourceDb = this.convertResourceToResourceDb(object);
{{updateOverride}}
    }


    {{clazz}}Mapper mapper = Selma.mapper({{clazz}}Mapper.class);

    public {{clazz}}Db convertResourceToResourceDb({{clazz}} object) {
        return mapper.as{{clazz}}Db(object);
    }

    public {{clazz}} convertResourceDbToResource({{clazz}}Db object) {
        LeanResourceDbUtils leanResourceDbUtils = new LeanResourceDbUtils();
        return mapper.as{{clazz}}(({{clazz}}Db) leanResourceDbUtils.leanResourceDb(object));
    }

    public Stream<{{clazz}}> convertResourceDbToResource(Stream<{{clazz}}Db> objectsStream){
        LeanResourceDbUtils leanUtils = new LeanResourceDbUtils();
        return objectsStream.map(i -> ({{clazz}}Db) leanUtils.leanResourceDb(i))
        .collect(Collectors.toList())
        .parallelStream()
        .map(i -> mapper.as{{clazz}}(i));
    }
}
