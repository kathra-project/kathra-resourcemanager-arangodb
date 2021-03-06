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

package {{package}};

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ArangoDb Edge Repository linking {{clazzA}} and {{clazzB}}
 *
 * Auto-generated by {{generatorSignature}}
 * @author {{author}}
 */
@Repository
public interface {{clazz}}Repository extends ArangoRepository<{{clazz}}, String> {

    public List<{{clazz}}> findAllBy{{propertyA}}(String from);
    public List<{{clazz}}> findAllBy{{propertyB}}(String to);
    public void deleteBy{{propertyA}}(String from);
    public void deleteBy{{propertyB}}(String to);
}
