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

package com.drunkendev.menu;

import java.io.OutputStream;
import java.nio.file.Files;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 *
 * @author Brett Ryan
 */
public class MenuBuilderTest {

    /**
     * Test of builder method, of class MenuBuilder.
     */
    @Test
    public void testBuilder() {
        System.out.println("builder");
        MenuItem menu = MenuBuilder.builder()
                .child("Product")
                .child("Knowledge Base", "/kb")
                .withChild("Support")
                .child("Help Me", "/")
                .child("Tickets")
                .end()
                .child("Blog", "/blog")
                .build();

        assertEquals("Root menu length.", 4, menu.getChildren().size());
        assertEquals("Menu item", "Product", menu.getChildren().get(0).getText());
        assertEquals("Menu item", "Knowledge Base", menu.getChildren().get(1).getText());
        assertEquals("Menu item", "Support", menu.getChildren().get(2).getText());
        assertEquals("Menu item", "Help Me", menu.getChildren().get(2).getChildren().get(0).getText());
    }

    /**
     * Test of write method, of class MenuBuilder.
     */
    @Test
    public void testWrite() throws Exception {
        MenuItem build = MenuBuilder.builder()
                .child("Product")
                .child("Knowledge Base", "/kb")
                .withChild("Support")
                .child("Help Me", "/")
                .child("Tickets")
                .end()
                .child("Blog", "/blog")
                .build();

        try (OutputStream os = Files.newOutputStream(Files.createTempFile("menu-test", ".xml"))) {
            MenuBuilder.write(build, os);
        }
    }

    /**
     * Test of load method, of class MenuBuilder.
     */
    @Test
    public void testLoad() throws Exception {
        MenuItem menu = MenuBuilder.load(MenuBuilderTest.class.getResourceAsStream("menu1.xml"));

        assertEquals("Root menu length.", 4, menu.getChildren().size());
        assertEquals("Menu item", "Product", menu.getChildren().get(0).getText());
        assertEquals("Menu item", "Knowledge Base", menu.getChildren().get(1).getText());
        assertEquals("Menu item", "Support", menu.getChildren().get(2).getText());
        assertEquals("Menu item", "Help Me", menu.getChildren().get(2).getChildren().get(0).getText());
    }

}
