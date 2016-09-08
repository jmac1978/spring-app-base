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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

}
