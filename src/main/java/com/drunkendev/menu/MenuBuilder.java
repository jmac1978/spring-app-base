/*
 * MenuBuilder.java    Aug 18 2016, 10:11
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

package com.drunkendev.menu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;


/**
 * Provides capabilities for retrieving a menu structure from XML or building statically with java code.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class MenuBuilder {

    private static MenuItemType newMenuItem(String text) {
        MenuItemType item = new MenuItemType();
        item.setText(text);
        item.setHref("#");
        return item;
    }

    private static MenuItemType newMenuItem(String text, String href) {
        MenuItemType item = new MenuItemType();
        item.setText(text);
        item.setHref(href);
        return item;
    }

    private static MenuItem convert(MenuItemType menuItemType) {
        return menuItemType == null ? null : new MenuItem(menuItemType.getText(),
                                                          menuItemType.getHtml(),
                                                          menuItemType.getHref(),
                                                          menuItemType.getClassName(),
                                                          menuItemType.getSecured(),
                                                          Objects.equals(Boolean.TRUE, menuItemType.isSecuredPath()),
                                                          menuItemType.getIconClass(),
                                                          Objects.equals(Boolean.TRUE, menuItemType.isIconOnly()),
                                                          convert(menuItemType.getMenu()));
    }

    private static List<MenuItem> convert(List<MenuItemType> list) {
        return list == null ? emptyList() : list.stream().map(n -> convert(n)).collect(toList());
    }


    private static MenuItemType convert(ObjectFactory of, MenuItem menu) {
        MenuItemType res = of.createMenuItemType();
        res.setClassName(menu.getClassName());
        res.setHref(menu.getHref());
        res.setIconClass(menu.getIconClass());
        res.setIconOnly(menu.isIconOnly());
        res.setSecured(menu.getSecured());
        res.setSecuredPath(menu.isSecuredPath());
        res.setText(menu.getText());
        menu.getChildren().stream().forEach(m -> res.getMenu().add(convert(of, m)));
        return res;
    }

    /**
     * Creates a new {@code MenuBuilder} instance.
     *
     * @return  New {@code MenuBuilder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Writes the given menu structure as an XML formatted file.
     *
     * Any node of a menu structure may be used to output a menu and does not need
     * to be the root node. This will become the root of the outputted file.
     *
     * @param   menu
     *          Menu structure to write.
     * @param   stream
     *          Output stream to write to.
     * @throws  IOException
     *          If an error occurs with the underlying stream.
     */
    public static void write(MenuItem menu, OutputStream stream) throws IOException {
        try {
            ObjectFactory of = new ObjectFactory();
            MenuItemType doc = convert(of, menu);

            JAXBElement<MenuItemType> element = of.createMenu(doc);
            JAXBContext context = JAXBContext.newInstance(MenuItemType.class);
            Marshaller m = context.createMarshaller();
            m.marshal(element, stream);
        } catch (JAXBException ex) {
            throw new IOException("Error writing menu structure.", ex);
        } finally {
            stream.flush();
            stream.close();
        }
    }

    /**
     * Load a menu structure from an XML file.
     *
     * @param   stream
     *          Input stream to load from.
     * @return  Root menu item.
     * @throws  JAXBException
     *          If the XML file is badly formed.
     */
    public static MenuItem load(InputStream stream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        return convert(((JAXBElement<MenuItemType>) jaxbContext.createUnmarshaller().unmarshal(stream)).getValue());
    }


    /**
     * Builder class to construct {@link MenuItem} trees from.
     */
    public static final class Builder {

        private Builder parent;
        private MenuItemType item;

        private Builder() {
            this.item = new MenuItemType();
        }

        private Builder(Builder parent, MenuItemType item) {
            this.parent = parent;
            this.item = item;
        }

        /**
         * Creates a new separator menu item.
         *
         * @return  This nodes builder instance.
         */
        public Builder separator() {
            this.item.getMenu().add(newMenuItem(null));
            return this;
        }

        /**
         * Creates a new child menu item with text only.
         *
         * @param   text
         *          Text for new menu item.
         * @return  This nodes builder instance.
         */
        public Builder child(String text) {
            this.item.getMenu().add(newMenuItem(text));
            return this;
        }

        /**
         * Creates a new child menu item with text and link.
         *
         * @param   text
         *          Text for new menu item.
         * @param   href
         *          Link for menu item.
         * @return  This nodes builder instance.
         */
        public Builder child(String text, String href) {
            this.item.getMenu().add(newMenuItem(text, href));
            return this;
        }

        /**
         * Creates a new menu item and returns that menu item.
         *
         * Call {@link #end()} to return to the previous node.
         *
         * @param   text
         *          Text for new menu item.
         * @return  New {@code MenuItem} builder node.
         */
        public Builder withChild(String text) {
            MenuItemType newItem = newMenuItem(text);
            Builder child = new Builder(this, newItem);
            this.item.getMenu().add(newItem);
            return child;
        }

        /**
         * Creates a new menu item and returns that menu item.
         *
         * @param   text
         *          Display text for new menu item.
         * @param   href
         *          Link for menu item.
         * @return  New {@code MenuItem} builder node.
         */
        public Builder withChild(String text, String href) {
            MenuItemType newItem = newMenuItem(text, href);
            Builder child = new Builder(this, newItem);
            this.item.getMenu().add(newItem);
            return child;
        }

        /**
         * Sets the class name(s) for this menu item.
         *
         * Multiple classes are separated with whitespace.
         *
         * <strong>NOTE</strong>: Repeated calls will overwrite any prior values.
         *
         * @param   className
         *          Class name to set.
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder className(String className) {
            this.item.setClassName(className);
            return this;
        }

        /**
         * Sets the {@code href} attribute for the menu items node.
         *
         * @param   href
         *          Link for menu item.
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder href(String href) {
            this.item.setHref(href);
            return this;
        }

        /**
         * HTML content to set for the node.
         *
         * Beware that this content is not validated and opens a potential security risk.
         * Do not load this content from untrusted sources.
         *
         * @param   html
         *          Display HTML content to set for the menu item.
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder html(String html) {
            this.item.setHtml(html);
            this.item.setText(null);
            return this;
        }

        /**
         * Sets the spring security EL expression used to secure this node.
         *
         * When the EL evaluates to not allow this menu item will not be rendered.
         *
         * @param   security
         *          Spring security EL to use for this node.
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder secured(String security) {
            this.item.setSecured(security);
            return this;
        }

        /**
         * Sets the spring security path secure element.
         *
         * If set to true the value of {@link #secured(String)} will be ignored.
         *
         * When the EL evaluates to not allow this menu item will not be rendered.
         *
         * @param   securedPath
         *          True to secure the path.
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder securedPath(boolean securedPath) {
            this.item.setSecuredPath(securedPath);
            return this;
        }

        /**
         * Sets the spring security path secure element.
         *
         * When set the value of {@link #secured(String)} will be ignored.
         *
         * When the EL evaluates to not allow this menu item will not be rendered.
         *
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder securedPath() {
            this.item.setSecuredPath(true);
            return this;
        }

        /**
         * Sets the display text for the current node.
         *
         * @param   text
         *          Display text for new menu item.
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder text(String text) {
            this.item.setText(text);
            this.item.setHtml(null);
            return this;
        }

        /**
         * Classes to display in a span to the left of the text/html element.
         *
         * @param   iconClass
         *          Icon class to be set.
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder iconClass(String iconClass) {
            this.item.setIconClass(iconClass);
            return this;
        }

        /**
         * Classes to display in a span to the left of the text/html element.
         *
         * This will disable any text from appearing.
         *
         * @param   iconClass
         *          Icon class to be set.
         * @return  This nodes builder instance.
         * @since   1.0
         */
        public Builder iconOnly(String iconClass) {
            this.item.setIconClass(iconClass);
            this.item.setIconOnly(true);
            return this;
        }

        /**
         * Returns the parent builder if available or this builder if already at the root.
         *
         * @return  Parent or current builder.
         * @since   1.0
         */
        public Builder end() {
            return parent == null ? this : parent;
        }

        /**
         * Returns to the root nodes builder.
         *
         * If this builder is the root it will be returned.
         *
         * @return  Root nodes builder instance.
         * @since   1.1
         */
        public Builder root() {
            return getRootBuilder();
        }

        /**
         * Builds menu structure and returns the root {@link MenuItem} node.
         *
         * @return  Menu item built from the root.
         * @since   1.0
         */
        public MenuItem build() {
            Builder b = getRootBuilder();
            MenuItemType res = b.item;
            b.item = null;
            return convert(res);
        }

        /**
         * Builds menu structure and returns the root nodes children.
         *
         * @return  Children of all nodes from the root node.
         * @since   1.0
         */
        public List<MenuItem> buildChildren() {
            Builder b = getRootBuilder();
            MenuItemType res = b.item;
            b.item = null;
            return convert(res.getMenu());
        }

        private Builder getRootBuilder() {
            Builder next = this;
            while (next.parent != null) {
                next = next.parent;
            }
            return next;
        }

    }

}
