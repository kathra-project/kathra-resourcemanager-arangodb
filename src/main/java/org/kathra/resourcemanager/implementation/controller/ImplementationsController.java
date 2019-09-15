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

package org.kathra.resourcemanager.implementation.controller;

import org.kathra.core.model.Implementation;
import org.kathra.resourcemanager.resource.controller.AbstractCrudController;
import org.kathra.utils.annotations.Eager;
import org.apache.camel.cdi.ContextName;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.inject.Named;

/**
 * Controller implementing ImplementationsService
 *
 * Auto-generated by resource-db-generator@1.0.0 at 2019-01-03T13:23:17.416Z
 * @author julien.boubechtoula
 */
@Component
@Named("ImplementationsController")
@ContextName("ResourceManager")
@Eager
@ComponentScan(basePackages = {"org.kathra.resourcemanager"})
public class ImplementationsController extends AbstractImplementationsController {

    public ImplementationsController() {

    }

}
