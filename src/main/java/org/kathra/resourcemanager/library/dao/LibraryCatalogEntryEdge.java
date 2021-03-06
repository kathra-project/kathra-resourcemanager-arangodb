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

package org.kathra.resourcemanager.library.dao;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import org.springframework.data.annotation.Id;

import org.kathra.resourcemanager.library.dao.LibraryDb;
import org.kathra.resourcemanager.catalogentry.dao.CatalogEntryDb;


/**
 * Edge linking Library and CatalogEntry
 *
 * Auto-generated by resource-db-generator@1.3.0 at 2020-01-12T20:00:41.555Z
 * @author jboubechtoula
 */
@Edge
public class LibraryCatalogEntryEdge {

    @Id
    public String id;

    @From(lazy = true)
    public LibraryDb library;

    @To(lazy = true)
    public CatalogEntryDb catalogEntry;

    public LibraryCatalogEntryEdge(){

    }

    public LibraryCatalogEntryEdge(final LibraryDb library, final CatalogEntryDb catalogEntry) {
        this.catalogEntry = catalogEntry;
        this.library = library;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LibraryDb getLibrary() {
        return library;
    }

    public void setLibrary(LibraryDb library) {
        this.library = library;
    }

    public CatalogEntryDb getCatalogEntry() {
        return catalogEntry;
    }

    public void setCatalogEntry(CatalogEntryDb catalogEntry) {
        this.catalogEntry = catalogEntry;
    }
}
