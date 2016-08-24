/*
 * JdbcHolder.java    Aug 24 2012, 22:10
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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Pairs a {@link JdbcTemplate} and {@link TransactionTemplate} for a given JDBC
 * {@link DataSource}.
 *
 * @author  Brett Ryan
 * @see     JdbcManager
 */
public class JdbcHolder {

    private final JdbcTemplate jdbcTemplate;
    private final DataSourceTransactionManager transactionManager;
    private TransactionTemplate defaultTransactionTemplate;
    private TransactionTemplate readOnlyTransactionTemplate;

    /**
     * Creates a new {@code JdbcHolder} instance for a {@link DataSource}.
     *
     * @param   dataSource
     *          Data source to create a holder for.
     */
    public JdbcHolder(DataSource dataSource) {
        this(new JdbcTemplate(dataSource));
    }

    /**
     * Creates a new {@code JdbcHolder} instance for a {@link JdbcTemplate}.
     *
     * @param   jdbcTemplate
     *          JDBC Template to create a holder for.
     */
    public JdbcHolder(JdbcTemplate jdbcTemplate) {
        DefaultTransactionDefinition readOnlyDef;

        readOnlyDef = new DefaultTransactionDefinition();
        readOnlyDef.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        readOnlyDef.setReadOnly(true);

        this.transactionManager = new DataSourceTransactionManager(jdbcTemplate.getDataSource());
        this.jdbcTemplate = jdbcTemplate;
        this.defaultTransactionTemplate = new TransactionTemplate(transactionManager);
        this.readOnlyTransactionTemplate = new TransactionTemplate(transactionManager, readOnlyDef);
    }

    /**
     * Set the default transaction definition for default executions.
     *
     * This will replace the existing default template with a new one.
     *
     * @param   def
     *          New definition.
     */
    public void setTransactionDefinition(TransactionDefinition def) {
        this.defaultTransactionTemplate = new TransactionTemplate(transactionManager, def);
    }

    /**
     * Set the default transaction definition for read-only executions.
     *
     * This will replace the existing read-only template with a new one.
     *
     * @param   def
     *          New definition.
     */
    public void setReadOnlyTransactionDefinition(TransactionDefinition def) {
        this.readOnlyTransactionTemplate = new TransactionTemplate(transactionManager, def);
    }

    /**
     * Gets the JDBC Template definition for this connection.
     *
     * @return  JDBC Template for this holder.
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * Retrieve the {@link DataSourceTransactionManager} for this holder.
     *
     * @return  Transaction manager for this holder.
     */
    public DataSourceTransactionManager getDataSourceTransactionManager() {
        return transactionManager;
    }

    /**
     * Execute a callback to return a result.
     *
     * The callback function will be passed a {@link JdbcTemplate} instance for
     * JDBC execution and a {@link TransactionStatus} for transactional support.
     *
     * Note that this method will create a new {@link TransactionDefinition}
     * instance to execute the query with.
     *
     * <h2>Example</h2>
     *
     * <pre>
     * {@code
     *  DefaultTransactionDefinition def = new DefaultTransactionDefinition();
     *  def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
     *  List<Customer> custs = holder.execute(def, (template, status)
     *        -> template.query("select * from customer", MAPPER_CUSTOMER));
     * }
     * </pre>
     *
     * @param   <T>
     *          Return type
     * @param   def
     *          Definition to
     * @param   callback
     *          Function used to perform the data access operation.
     * @return  Result of the {@code callback} function.
     * @throws  DataAccessException
     *          if an underlying data access error occurs.
     * @see     #execute(BiFunction)
     * @see     #executeReadOnly(BiFunction)
     * @see     #executeReadOnlyVoid(BiConsumer)
     * @see     #executeVoid(BiConsumer)
     */
    public <T> T execute(TransactionDefinition def,
                         BiFunction<JdbcTemplate, TransactionStatus, T> callback)
            throws DataAccessException {
        return new TransactionTemplate(transactionManager, def)
                .execute(ts -> callback.apply(jdbcTemplate, ts));
    }

    /**
     * Execute a callback to return a result.
     *
     * The callback function will be passed a {@link JdbcTemplate} instance for
     * JDBC execution and a {@link TransactionStatus} for transactional support.
     *
     * Note that this method will create a new {@link TransactionDefinition}
     * instance to execute the query with.
     *
     * <h2>Example</h2>
     *
     * <pre>
     * {@code
     *  List<Customer> custs = holder.execute((template, status)
     *        -> template.query("select * from customer", MAPPER_CUSTOMER));
     * }
     * </pre>
     *
     * @param   <T>
     *          Return type
     * @param   callback
     *          Function used to perform the data access operation.
     * @return  Result of the {@code callback} function.
     * @throws  DataAccessException
     *          if an underlying data access error occurs.
     * @see     #execute(TransactionDefinition, BiFunction)
     * @see     #executeReadOnly(BiFunction)
     * @see     #executeReadOnlyVoid(BiConsumer)
     * @see     #executeVoid(BiConsumer)
     */
    public <T> T execute(BiFunction<JdbcTemplate, TransactionStatus, T> callback)
            throws DataAccessException {
        return defaultTransactionTemplate.execute(ts -> callback.apply(jdbcTemplate, ts));
    }

    /**
     * Execute a callback that does not return a result.
     *
     * The callback function will be passed a {@link JdbcTemplate} instance for
     * JDBC execution and a {@link TransactionStatus} for transactional support.
     *
     * <h2>Example</h2>
     *
     * <pre>
     * {@code
     *  holder.executeVoid((template, status)
     *        -> template.upate("insert into customer (id, name) values (?, ?)",
     *                          id, name));
     * }
     * </pre>
     *
     * @param   callback
     *          Function used to perform the data access operation.
     * @throws  DataAccessException
     *          if an underlying data access error occurs.
     * @see     #execute(BiFunction)
     * @see     #execute(TransactionDefinition, BiFunction)
     * @see     #executeReadOnly(BiFunction)
     * @see     #executeReadOnlyVoid(BiConsumer)
     */
    public void executeVoid(BiConsumer<JdbcTemplate, TransactionStatus> callback)
            throws DataAccessException {
        defaultTransactionTemplate.execute(ts -> {
            callback.accept(jdbcTemplate, ts);
            return null;
        });
    }

    /**
     * Execute a callback to return a result with read only transactions.
     *
     * The callback function will be passed a {@link JdbcTemplate} instance for
     * JDBC execution and a {@link TransactionStatus} for transactional support.
     *
     * NOTE: The read-only flag is a hint to the transaction subsystem and does not
     * ensure an underlying read-only connection.
     *
     * <h2>Example</h2>
     *
     * <pre>
     * {@code
     *  List<Customer> custs = holder.executeReadOnly((template, status)
     *        -> template.query("select * from customer", MAPPER_CUSTOMER));
     * }
     * </pre>
     *
     * @param   <T>
     *          Return type
     * @param   callback
     *          Function used to perform the data access operation.
     * @return  Result of the {@code callback} function.
     * @throws  DataAccessException
     *          if an underlying data access error occurs.
     * @see     #execute(BiFunction)
     * @see     #execute(TransactionDefinition, BiFunction)
     * @see     #executeReadOnlyVoid(BiConsumer)
     * @see     #executeVoid(BiConsumer)
     */
    public <T> T executeReadOnly(BiFunction<JdbcTemplate, TransactionStatus, T> callback)
            throws DataAccessException {
        return readOnlyTransactionTemplate.execute(ts -> callback.apply(jdbcTemplate, ts));
    }

    /**
     * Execute a callback that does not return a result with read only transactions.
     *
     * The callback function will be passed a {@link JdbcTemplate} instance for
     * JDBC execution and a {@link TransactionStatus} for transactional support.
     *
     * NOTE: The read-only flag is a hint to the transaction subsystem and does not
     * ensure an underlying read-only connection.
     *
     * @param   callback
     *          Function used to perform the data access operation.
     * @throws  DataAccessException
     *          if an underlying data access error occurs.
     * @see     #execute(BiFunction)
     * @see     #execute(TransactionDefinition, BiFunction)
     * @see     #executeReadOnly(BiFunction)
     * @see     #executeVoid(BiConsumer)
     */
    public void executeReadOnlyVoid(BiConsumer<JdbcTemplate, TransactionStatus> callback) throws DataAccessException {
        readOnlyTransactionTemplate.execute(ts -> {
            callback.accept(jdbcTemplate, ts);
            return null;
        });
    }

}
