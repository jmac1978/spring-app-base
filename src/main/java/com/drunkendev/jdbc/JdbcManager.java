/*
 * JdbcManager.java    Aug 24 2012, 22:19
 *
 * Copyright 2012 Drunken Dev.
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
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.upperCase;


/**
 * Provides a mechanism for pairing {@link org.springframework.jdbc.core.JdbcTemplate} and
 * {@link org.springframework.transaction.support.TransactionTemplate TransactionTemplate}
 * where an application may have multiple JDBC connections.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public final class JdbcManager {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcManager.class);
    private final Map<String, JdbcHolder> holders;

    /**
     * Creates a new {@code JdbcManager} instance.
     */
    public JdbcManager() {
        holders = new HashMap<>();
    }

    /**
     * Set all holders to those passed in.
     *
     * @param   holders
     *          Holder entries to set this instance to.
     */
    public void setHolders(Map<String, JdbcHolder> holders) {
        this.holders.clear();
        if (holders != null) {
            holders.forEach((k, v) -> this.holders.put(trimToEmpty(k).toUpperCase(), v));
        }
    }

    /**
     * Adds a holder to the holders map.
     *
     * If a holder already exists it will be replaced.
     *
     * <strong>NOTE</strong>: Keys are treated as case-insensitive strings with
     * leading and trailing white-space removed.
     *
     * @param   key
     *          Key to store holder against.
     * @param   dataSource
     *          Data source to add holder for.
     */
    public void add(String key, DataSource dataSource) {
        this.holders.put(trimToEmpty(key).toUpperCase(), new JdbcHolder(dataSource));
    }

    /**
     * Retrieves the holder for the given key.
     *
     * @param   key
     *          Key to retrieve holder for.
     * @return  Holder for given key or null if not found.
     */
    public JdbcHolder get(String key) {
        return holders.get(upperCase(key));
    }

}
