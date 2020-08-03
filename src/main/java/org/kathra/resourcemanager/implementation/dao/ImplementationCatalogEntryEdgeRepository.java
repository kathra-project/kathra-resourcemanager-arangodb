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

import com.arangodb.springframework.repository.ArangoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ArangoDb Edge Repository linking Implementation and CatalogEntry
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-02-06T20:56:15.475Z
 * @author jboubechtoula
 */
@Repository
public interface ImplementationCatalogEntryEdgeRepository extends ArangoRepository<ImplementationCatalogEntryEdge, String> {

    public List<ImplementationCatalogEntryEdge> findAllByImplementation(String from);
    public List<ImplementationCatalogEntryEdge> findAllByCatalogEntry(String to);
    public void deleteByImplementation(String from);
    public void deleteByCatalogEntry(String to);
}
