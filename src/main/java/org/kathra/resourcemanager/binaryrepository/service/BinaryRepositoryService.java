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

package org.kathra.resourcemanager.binaryrepository.service;

import org.kathra.core.model.BinaryRepository;
import org.kathra.resourcemanager.resource.service.AbstractService;
import org.kathra.resourcemanager.security.SessionService;
import org.kathra.resourcemanager.binaryrepository.dao.BinaryRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Business service managing BinaryRepository
 *
 * Auto-generated by resource-db-generator@1.2.0 at 2019-12-26T23:57:25.183Z
 * @author Julien Boubechtoula
 */
@Service
public class BinaryRepositoryService extends AbstractService<BinaryRepository,String> {

    public BinaryRepositoryService(@Autowired BinaryRepositoryDao dao, @Autowired SessionService sessionService) {
        super(dao, sessionService);
    }
}
