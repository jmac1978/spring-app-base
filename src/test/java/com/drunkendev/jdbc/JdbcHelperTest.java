/*
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
import org.junit.Test;

import static com.drunkendev.jdbc.JdbcHelper.getSql;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;


/**
 *
 * @author Brett Ryan
 */
public class JdbcHelperTest {

    private static String readAllLines(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            return br.lines().collect(joining("\n"));
        }
    }

    /**
     * Test of getSql method, of class JdbcHelper.
     */
    @Test
    public void testGetSql_InputStream_test1() throws IOException {
        System.out.println("getSql test1");
        String expected = readAllLines(JdbcHelperTest.class.getResourceAsStream("test1.txt")).trim();
        String found = getSql(JdbcHelperTest.class.getResourceAsStream("test1.sql")).trim();
        System.out.println("\n\nExpected: [" + expected + "]");
        System.out.println("\n\nFound: [" + found + "]");
        assertEquals(expected, found);
    }

}
