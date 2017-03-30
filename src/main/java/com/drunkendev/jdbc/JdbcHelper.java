/*
 * JdbcHelper.java    Jul 28 2016, 15:39
 *
 * Copyright 2016 Drunken Dev.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import static java.lang.Character.isWhitespace;


/**
 * Utilities for working with JDBC.
 *
 * Methods within may be for working with {@link JdbcTemplate} or SQL files.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class JdbcHelper {

    private static final int INIT = 0;
    private static final int ISA_MINUS = 1;
    private static final int ISI_LINE_COMMENT = 2;
    private static final int ISA_SLASH = 3;
    private static final int ISI_BLOCK_COMMENT = 4;
    private static final int ISA_BCOMMENT_STAR = 5;
    private static final int ISI_DOUBLE_QUOTE = 6;
    private static final int ISI_SINGLE_QUOTE = 7;
    private static final int ISI_SQUARE_QUOTE = 8;

    /**
     * Given an input file path will return the textual contents with comments removed.
     *
     * SQL files may either contain single line comments {@code --} or block comments <code>&#47;* *&#47;</code>
     *
     * @param   file
     *          Input stream containing SQL content.
     * @return  Textual content with both line and block comments removed.
     * @since   1.0
     */
    public static String getSql(String file) {
        return getSql(Paths.get(file));
    }

    /**
     * Given an input file path will return the textual contents with comments removed.
     *
     * SQL files may either contain single line comments {@code --} or block comments <code>&#47;* *&#47;</code>
     *
     * @param   file
     *          Input stream containing SQL content.
     * @return  Textual content with both line and block comments removed.
     * @since   1.1
     */
    public static String getSql(Path file) {
        try (InputStream stream = Files.newInputStream(file)) {
            return getSql(stream);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex.getMessage(), ex);
        }
    }

    /**
     * Given an input stream will return the textual contents with comments removed.
     *
     * SQL files may either contain single line comments {@code --} or block comments <code>&#47;* *&#47;</code>
     *
     * @param   is
     *          Input stream containing SQL content.
     * @return  Textual content with both line and block comments removed.
     */
    public static String getSql(InputStream is) {
        StringBuilder res = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is))) {
            int cc;
            char c;
            int state = INIT;
            boolean prevNewLine = false;

            while ((cc = r.read()) != -1) {
                c = (char) cc;
                switch (state) {
                    case INIT:
                        switch (c) {
                            case '-':
                                state = ISA_MINUS;
                                break;
                            case '/':
                                state = ISA_SLASH;
                                break;
                            case '\'':
                                state = ISI_SINGLE_QUOTE;
                                res.append(c);
                                break;
                            case '"':
                                state = ISI_DOUBLE_QUOTE;
                                res.append(c);
                                break;
                            case '[':
                                state = ISI_SQUARE_QUOTE;
                                res.append(c);
                                break;
                            default:
                                if (c == '\n' || c == '\r') {
                                    if (!prevNewLine && res.length() > 0) {
                                        res.append('\n');
                                        prevNewLine = true;
                                    }
                                } else if (isWhitespace(c)) {
                                    if (res.length() > 0) {
                                        res.append(c);
                                        prevNewLine = false;
                                    }
                                } else {
                                    res.append(c);
                                    prevNewLine = false;
                                }
                                break;
                        }
                        break;
                    case ISA_MINUS:
                        if (c == '-') {
                            state = ISI_LINE_COMMENT;
                        } else {
                            state = INIT;
                            res.append('-');
                            res.append(c);
                        }
                        break;
                    case ISI_LINE_COMMENT:
                        if (c == '\n' || c == '\r') {
                            if (res.length() > 0) {
                                if (!prevNewLine) {
                                    res.append('\n');
                                }
                                prevNewLine = true;
                            }
                            state = INIT;
                        }
                        break;
                    case ISA_SLASH:
                        if (c == '*') {
                            state = ISI_BLOCK_COMMENT;
                        } else {
                            state = INIT;
                            res.append('/');
                            res.append(c);
                            prevNewLine = false;
                        }
                        break;
                    case ISI_BLOCK_COMMENT:
                        if (c == '*') {
                            state = ISA_BCOMMENT_STAR;
                        }
                        break;
                    case ISA_BCOMMENT_STAR:
                        state = c == '/' ? INIT : ISI_BLOCK_COMMENT;
                        break;
                    case ISI_SINGLE_QUOTE:
                        res.append(c);
                        if (c == '\'') {
                            state = INIT;
                        }
                        break;
                    case ISI_DOUBLE_QUOTE:
                        res.append(c);
                        if (c == '"') {
                            state = INIT;
                        }
                        break;
                    case ISI_SQUARE_QUOTE:
                        res.append(c);
                        if (c == ']') {
                            state = INIT;
                        }
                        break;
                }
            }

            switch (state) {
                case ISA_MINUS:
                    res.append('-');
                    break;
                case ISA_SLASH:
                    res.append('/');
                    break;
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex.getMessage(), ex);
        }
        return res.toString();
    }

    public static RowMapper<Boolean> booleanMapper(int col) {
        return (rs, i) -> rs.getBoolean(col);
    }

    public static RowMapper<Byte> byteMapper(int col) {
        return (rs, i) -> rs.getByte(col);
    }

    public static RowMapper<java.sql.Date> dateMapper(int col) {
        return (rs, i) -> rs.getDate(col);
    }

    public static RowMapper<Float> floatMapper(int col) {
        return (rs, i) -> rs.getFloat(col);
    }

    public static RowMapper<Double> doubleMapper(int col) {
        return (rs, i) -> rs.getDouble(col);
    }

    public static RowMapper<Integer> intMapper(int col) {
        return (rs, i) -> rs.getInt(col);
    }

    public static RowMapper<Long> longMapper(int col) {
        return (rs, i) -> rs.getLong(col);
    }

    public static RowMapper<String> nstringMapper(int col) {
        return (rs, i) -> rs.getNString(col);
    }

    public static <T> RowMapper<T> objectMapper(int col) {
        return (rs, i) -> (T) rs.getObject(col);
    }

    public static RowMapper<Short> shortMapper(int col) {
        return (rs, i) -> rs.getShort(col);
    }

    public static RowMapper<String> stringMapper(int col) {
        return (rs, i) -> rs.getString(col);
    }

    public static RowMapper<Time> timeMapper(int col) {
        return (rs, i) -> rs.getTime(col);
    }

    public static RowMapper<Timestamp> timestampMapper(int col) {
        return (rs, i) -> rs.getTimestamp(col);
    }

    public static RowMapper<URL> urlMapper(int col) {
        return (rs, i) -> rs.getURL(col);
    }

    public static RowMapper<Boolean> booleanMapper(String col) {
        return (rs, i) -> rs.getBoolean(col);
    }

    public static RowMapper<Byte> byteMapper(String col) {
        return (rs, i) -> rs.getByte(col);
    }

    public static RowMapper<java.sql.Date> dateMapper(String col) {
        return (rs, i) -> rs.getDate(col);
    }

    public static RowMapper<Float> floatMapper(String col) {
        return (rs, i) -> rs.getFloat(col);
    }

    public static RowMapper<Double> doubleMapper(String col) {
        return (rs, i) -> rs.getDouble(col);
    }

    public static RowMapper<Integer> intMapper(String col) {
        return (rs, i) -> rs.getInt(col);
    }

    public static RowMapper<Long> longMapper(String col) {
        return (rs, i) -> rs.getLong(col);
    }

    public static RowMapper<String> nstringMapper(String col) {
        return (rs, i) -> rs.getNString(col);
    }

    public static <T> RowMapper<T> objectMapper(String col) {
        return (rs, i) -> (T) rs.getObject(col);
    }

    public static RowMapper<Short> shortMapper(String col) {
        return (rs, i) -> rs.getShort(col);
    }

    public static RowMapper<String> stringMapper(String col) {
        return (rs, i) -> rs.getString(col);
    }

    public static RowMapper<Time> timeMapper(String col) {
        return (rs, i) -> rs.getTime(col);
    }

    public static RowMapper<Timestamp> timestampMapper(String col) {
        return (rs, i) -> rs.getTimestamp(col);
    }

    public static RowMapper<URL> urlMapper(String col) {
        return (rs, i) -> rs.getURL(col);
    }

    /**
     * Given a {@link RowMapper} will return a {@link ResultSetExtractor} that returns a single result or null.
     *
     * This implementation checks if there is another record by calling {@link java.sql.ResultSet#next() ResultSet.next()},
     * if there is it will return a result or {@code null}.
     *
     * @param   <T>
     *          Input type of mapper which extractor will be a type of.
     * @param   mapper
     *          Mapper used to map records for this extractor.
     * @return  {@link ResultSetExtractor} for {@code mapper}
     */
    public static <T> ResultSetExtractor<T> singletonExtractor(RowMapper<? extends T> mapper) {
        return rs -> rs.next() ? mapper.mapRow(rs, 1) : null;
    }

    /**
     * Given a {@link RowMapper} will return a {@link ResultSetExtractor} that returns an {@code Optional<T>}.
     *
     * This implementation checks if there is another record by calling {@link java.sql.ResultSet#next() ResultSet.next()},
     *
     * @param   <T>
     *          Input type of mapper which extractor will be a type of.
     * @param   mapper
     *          Mapper used to map records for this extractor.
     * @return  {@link ResultSetExtractor} for {@code mapper}
     */
    public static <T> ResultSetExtractor<Optional<T>> singletonOptionalExtractor(RowMapper<? extends T> mapper) {
        return rs -> rs.next() ? Optional.of(mapper.mapRow(rs, 1)) : Optional.empty();
    }

    /**
     * Return an {@link java.lang.Boolean} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Boolean getBoolean(ResultSet rs, String columnLabel) throws SQLException {
        Boolean res = rs.getBoolean(columnLabel);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Boolean} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnIndex
     *          the first column is 1, the second is 2, ...
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Boolean getBoolean(ResultSet rs, int columnIndex) throws SQLException {
        Boolean res = rs.getBoolean(columnIndex);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Byte} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Byte getByte(ResultSet rs, String columnLabel) throws SQLException {
        Byte res = rs.getByte(columnLabel);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Byte} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnIndex
     *          the first column is 1, the second is 2, ...
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Byte getByte(ResultSet rs, int columnIndex) throws SQLException {
        Byte res = rs.getByte(columnIndex);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Byte} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true
     * or the value is zero.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Byte getByteZeroNull(ResultSet rs, String columnLabel) throws SQLException {
        Byte res = rs.getByte(columnLabel);
        return rs.wasNull() || res == 0 ? null : res;
    }

    /**
     * Return an {@link java.lang.Double} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Double getDouble(ResultSet rs, String columnLabel) throws SQLException {
        Double res = rs.getDouble(columnLabel);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Double} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnIndex
     *          the first column is 1, the second is 2, ...
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Double getDouble(ResultSet rs, int columnIndex) throws SQLException {
        Double res = rs.getDouble(columnIndex);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Double} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true
     * or the value is zero.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Double getDoubleZeroNull(ResultSet rs, String columnLabel) throws SQLException {
        Double res = rs.getDouble(columnLabel);
        return rs.wasNull() || res == 0.0d ? null : res;
    }

    /**
     * Return an {@link java.lang.Float} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Float getFloat(ResultSet rs, String columnLabel) throws SQLException {
        Float res = rs.getFloat(columnLabel);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Float} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnIndex
     *          the first column is 1, the second is 2, ...
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Float getFloat(ResultSet rs, int columnIndex) throws SQLException {
        Float res = rs.getFloat(columnIndex);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Float} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true
     * or the value is zero.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Float getFloatZeroNull(ResultSet rs, String columnLabel) throws SQLException {
        Float res = rs.getFloat(columnLabel);
        return rs.wasNull() || res == 0.0f ? null : res;
    }

    /**
     * Return an {@link java.lang.Integer} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Integer getInteger(ResultSet rs, String columnLabel) throws SQLException {
        Integer res = rs.getInt(columnLabel);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Integer} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnIndex
     *          the first column is 1, the second is 2, ...
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Integer getInteger(ResultSet rs, int columnIndex) throws SQLException {
        Integer res = rs.getInt(columnIndex);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Integer} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true
     * or the value is zero.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Integer getIntegerZeroNull(ResultSet rs, String columnLabel) throws SQLException {
        Integer res = rs.getInt(columnLabel);
        return rs.wasNull() || res == 0 ? null : res;
    }

    /**
     * Return an {@link java.lang.Long} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Long getLong(ResultSet rs, String columnLabel) throws SQLException {
        Long res = rs.getLong(columnLabel);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Long} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnIndex
     *          the first column is 1, the second is 2, ...
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Long getLong(ResultSet rs, int columnIndex) throws SQLException {
        Long res = rs.getLong(columnIndex);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Long} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true
     * or the value is zero.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Long getLongZeroNull(ResultSet rs, String columnLabel) throws SQLException {
        Long res = rs.getLong(columnLabel);
        return rs.wasNull() || res == 0 ? null : res;
    }

    /**
     * Return an {@link java.lang.Short} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Short getShort(ResultSet rs, String columnLabel) throws SQLException {
        Short res = rs.getShort(columnLabel);
        return rs.wasNull() ? null : res;
    }

    /**
     * Return an {@link java.lang.Short} preserving null values from the {@code ResultSet}.
     *
     * This method will return null if the call to {@link ResultSet#wasNull()} is true
     * or the value is zero.
     *
     * @param   rs
     *          ResultSet to retrieve the value from.
     * @param   columnLabel
     *          the label for the column specified with the SQL AS clause. If
     *          the SQL AS clause was not specified, then the label is the name
     *          of the column.
     * @return  the column value; if the value is SQL {@code NULL}, the value
     *          returned is {@code null}.
     *
     * @throws  SQLException
     *          if the columnLabel is not valid; if a database access error
     *          occurs or this method is called on a closed result set.
     *
     * @since   1.3
     */
    public static Short getShortZeroNull(ResultSet rs, String columnLabel) throws SQLException {
        Short res = rs.getShort(columnLabel);
        return rs.wasNull() || res == 0 ? null : res;
    }

}
