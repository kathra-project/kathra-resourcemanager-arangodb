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
package org.kathra.resourcemanager.catalogentry.dao;

import org.kathra.core.model.CatalogEntry;
import org.kathra.resourcemanager.resource.dao.ResourceDbMapper;
import fr.xebia.extras.selma.Field;
import fr.xebia.extras.selma.Mapper;
import fr.xebia.extras.selma.IgnoreMissing;

@Mapper(
        withCyclicMapping = true,
        withIgnoreMissing = IgnoreMissing.ALL,
        withCustomFields = {
                @Field( value = "createdAt", withCustom = ResourceDbMapper.CustomCreatedAt.class),
                @Field( value = "updatedAt", withCustom = ResourceDbMapper.CustomUpdatedAt.class),
                @Field( value = "metadata", withCustom = ResourceDbMapper.CustomMetadata.class)
        })
public interface CatalogEntryMapper extends ResourceDbMapper {

    CatalogEntryDb asCatalogEntryDb(CatalogEntry in);
    CatalogEntry asCatalogEntry(CatalogEntryDb in);
}
