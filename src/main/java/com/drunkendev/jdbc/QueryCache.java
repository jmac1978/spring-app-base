/*
 * QueryCache.java    Nov 11 2015, 23:19
 *
 * Copyright 2015 Drunken Dev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drunkendev.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.drunkendev.jdbc.JdbcHelper.getSql;


/**
 * Provides a backing holder for SQL resources.
 *
 * This implementation is used for loading queries from the class path that are
 * stored as resources alongside a class.
 *
 * Loaded queries are cached on load for further calls to be read and shared
 * across other classes.
 *
 * @author  Brett Ryan
 */
public class QueryCache {

    private final Map<Key, String> queries;

    /**
     * Creates a new {@code QueryCache} instance.
     */
    public QueryCache() {
        this.queries = new HashMap<>();
    }

    /**
     * Loads and returns the resource {@code name} stored with a class.
     *
     * If the resource has previously been loaded it will be returned from the cache.
     *
     * @param   clazz
     *          Class for where the resource will be loaded from.
     * @param   resourceName
     *          Resource name.
     * @return  SQL resource processed by {@link com.drunkendev.jdbc.JdbcHelper#getSql(java.io.InputStream) getSql(InputStream)}
     */
    public String get(Class clazz, String resourceName) {
        Key k = new Key(clazz, resourceName);
        if (!queries.containsKey(k)) {
            queries.put(k, getSql(clazz.getResourceAsStream(resourceName)));
        }
        return queries.get(k);
    }

    /**
     * Loads and returns the resource {@code name} stored with a class.
     *
     * This is the same as {@link #get(Class, String)} bypassing the cache.
     *
     * @param   clazz
     *          Class for where the resource will be loaded from.
     * @param   resourceName
     *          Resource name.
     * @return  SQL resource processed by {@link com.drunkendev.jdbc.JdbcHelper#getSql(java.io.InputStream) getSql(InputStream)}
     */
    public String getNoCache(Class clazz, String resourceName) {
        return getSql(clazz.getResourceAsStream(resourceName));
    }


    private static final class Key {

        private final String type;
        private final String name;

        Key(Class type, String name) {
            this.type = type.getPackage().getName();
            this.name = name;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.type);
            hash = 97 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() != obj.getClass()) {
                final Key other = (Key) obj;
                return Objects.equals(this.name, other.name) &&
                       Objects.equals(this.type, other.type);
            }
            return true;
        }

    }

}
