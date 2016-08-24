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
 *
 * @author  Brett Ryan
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

    public static Builder builder() {
        return new Builder();
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

    public static void write(MenuItem menu, OutputStream s) throws JAXBException, IOException {
        try {
            ObjectFactory of = new ObjectFactory();
            MenuItemType doc = convert(of, menu);

            JAXBElement<MenuItemType> element = of.createMenu(doc);
            JAXBContext context = JAXBContext.newInstance(MenuItemType.class);
            Marshaller m = context.createMarshaller();
            m.marshal(element, s);
        } finally {
            s.flush();
            s.close();
        }
    }

    public static MenuItem load(InputStream is) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        return convert(((JAXBElement<MenuItemType>) jaxbContext.createUnmarshaller().unmarshal(is)).getValue());
    }


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

        public Builder separator() {
            this.item.getMenu().add(newMenuItem(null));
            return this;
        }

        public Builder child(String text) {
            this.item.getMenu().add(newMenuItem(text));
            return this;
        }

        public Builder child(String text, String href) {
            this.item.getMenu().add(newMenuItem(text, href));
            return this;
        }

        public Builder withChild(String text) {
            MenuItemType newItem = newMenuItem(text);
            Builder child = new Builder(this, newItem);
            this.item.getMenu().add(newItem);
            return child;
        }

        public Builder withChild(String text, String href) {
            MenuItemType newItem = newMenuItem(text, href);
            Builder child = new Builder(this, newItem);
            this.item.getMenu().add(newItem);
            return child;
        }

        public Builder className(String className) {
            this.item.setClassName(className);
            return this;
        }

        public Builder href(String href) {
            this.item.setHref(href);
            return this;
        }

        public Builder html(String html) {
            this.item.setHtml(html);
            this.item.setText(null);
            return this;
        }

        public Builder secured(String security) {
            this.item.setSecured(security);
            return this;
        }

        public Builder securedPath(boolean securedPath) {
            this.item.setSecuredPath(securedPath);
            return this;
        }

        public Builder securedPath() {
            this.item.setSecuredPath(true);
            return this;
        }

        public Builder text(String text) {
            this.item.setText(text);
            this.item.setHtml(null);
            return this;
        }

        public Builder iconClass(String iconClass) {
            this.item.setIconClass(iconClass);
            return this;
        }

        public Builder iconOnly(String iconClass) {
            this.item.setIconClass(iconClass);
            this.item.setIconOnly(true);
            return this;
        }

        public Builder end() {
            return parent == null ? this : parent;
        }

        private Builder getRootBuilder() {
            Builder next = this;
            while (next.parent != null) {
                next = next.parent;
            }
            return next;
        }

        public MenuItem build() {
            Builder b = getRootBuilder();
            MenuItemType res = b.item;
            b.item = null;
            return convert(res);
        }

        public List<MenuItem> buildChildren() {
            Builder b = getRootBuilder();
            MenuItemType res = b.item;
            b.item = null;
            return convert(res.getMenu());
        }

    }

}
