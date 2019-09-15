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
package org.kathra.resourcemanager.resource.utils;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge
public class ChildParentLnk {

    @From
    public EdgeUtilsTest.ExampleParentDb parent;

    @To
    public EdgeUtilsTest.ExampleChildDb child;

    public ChildParentLnk(EdgeUtilsTest.ExampleParentDb exampleParentDb, EdgeUtilsTest.ExampleChildDb exampleChildDb) {
        this.parent = exampleParentDb;
        this.child = exampleChildDb;
    }

    public EdgeUtilsTest.ExampleParentDb getParent() {
        return parent;
    }

    public void setParent(EdgeUtilsTest.ExampleParentDb parent) {
        this.parent = parent;
    }

    public EdgeUtilsTest.ExampleChildDb getChild() {
        return child;
    }

    public void setChild(EdgeUtilsTest.ExampleChildDb child) {
        this.child = child;
    }
}