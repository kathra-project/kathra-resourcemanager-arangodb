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

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ArangoDb Edge Repository linking LibraryApiVersion and Library
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-28T17:06:40.653Z
 * @author Julien Boubechtoula
 */
@Repository
public interface LibraryApiVersionLibraryEdgeRepository extends ArangoRepository<LibraryApiVersionLibraryEdge, String> {

    public List<LibraryApiVersionLibraryEdge> findAllByLibraryApiVersion(String from);
    public List<LibraryApiVersionLibraryEdge> findAllByLibrary(String to);
    public void deleteByLibraryApiVersion(String from);
    public void deleteByLibrary(String to);
}
