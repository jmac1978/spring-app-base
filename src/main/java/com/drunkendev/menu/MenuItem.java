/*
 * MenuItem.java    Aug 19 2016, 12:31
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableList;


/**
 *
 * @author  Brett Ryan
 */
public class MenuItem {

    protected final String text;
    protected final String html;
    protected final String href;
    protected final String className;
    protected final String secured;
    protected final boolean securedPath;
    protected final String iconClass;
    protected final boolean iconOnly;
    protected final List<MenuItem> children;

    public MenuItem(String text,
                    String html,
                    String href,
                    String className,
                    String secured,
                    boolean securedPath,
                    String iconClass,
                    boolean iconOnly,
                    Collection<MenuItem> items) {
        this.text = text;
        this.html = html;
        this.href = href;
        this.className = className;
        this.secured = secured;
        this.securedPath = securedPath;
        this.iconClass = iconClass;
        this.iconOnly = iconOnly;
        this.children = unmodifiableList(new ArrayList<>(items));
    }

    public String getText() {
        return text;
    }

    public String getHtml() {
        return html;
    }

    public String getHref() {
        return href;
    }

    public String getClassName() {
        return className;
    }

    public String getSecured() {
        return secured;
    }

    public boolean isSecuredPath() {
        return securedPath;
    }

    public String getIconClass() {
        return iconClass;
    }

    public boolean isIconOnly() {
        return iconOnly;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

}
