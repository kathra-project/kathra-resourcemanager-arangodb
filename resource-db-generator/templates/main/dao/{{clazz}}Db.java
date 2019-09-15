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

import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import {{coreModelPackage}}.{{clazz}};
import {{coreModelPackage}}.{{clazz}}.*;
import org.kathra.resourcemanager.resource.dao.AbstractResourceDb;
import java.util.List;

{{imports}}

/**
 * Entity class {{clazz}}Db implementing db resource for class {{clazz}}
 *
 * Auto-generated by {{generatorSignature}}
 * @author {{author}}
 */
{{clazzAnnotations}}
public class {{clazz}}Db{{clazzTypesParameters}} extends {{parentClazz}}{{parentClazzTypesParameters}} {

{{fields}}

        public {{clazz}}Db() {
        }
        public {{clazz}}Db(String id) {
            super(id);
        }

{{methods}}

}
