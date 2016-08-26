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
 * Menu item to render within web application.
 *
 * It is recommended to use {@link MenuBuilder#builder()} to create a menu structure within code
 * or {@link MenuBuilder#load(java.io.InputStream) MenuBuilder.load(InputStream)}
 * to load from a resource.
 *
 * @author  Brett Ryan
 * @since   1.0
 * @see     MenuBuilder
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

    /**
     * Create a new menu node.
     *
     * @param   text
     *          Display text.
     * @param   html
     *          HTML text to display on node.
     * @param   href
     *          HTML to render for node.
     * @param   className
     *          Class names for nodes containing element.
     * @param   secured
     *          Spring security EL used to include/exclude this menu item.
     * @param   securedPath
     *          If true the path will be checked for spring security to include/exclude this menu item.
     * @param   iconClass
     *          Additional icon classes to use rendered alongside this element within a span element.
     * @param   iconOnly
     *          True to not render the text for this element.
     * @param   items
     *          Child items for this node.
     */
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

    /**
     * Display text
     *
     * @return  text.
     */
    public String getText() {
        return text;
    }

    /**
     * Display html
     *
     * @return  html.
     */
    public String getHtml() {
        return html;
    }

    /**
     * Menu URL/URI
     *
     * @return  href.
     */
    public String getHref() {
        return href;
    }

    /**
     * Elements class name(s)
     *
     * @return  class name(s).
     */
    public String getClassName() {
        return className;
    }

    /**
     * Spring security EL string.
     *
     * @return  spring security EL.
     */
    public String getSecured() {
        return secured;
    }

    /**
     * True if this node is to be secured based on spring securities path.
     *
     * @return  true if path is secured.
     */
    public boolean isSecuredPath() {
        return securedPath;
    }

    /**
     * Icon classes to display alongside this element to the left in a {@code <span>} element.
     *
     * For more control use the {@link #getHtml()} can be used to render any html
     * content.
     *
     * @return  icon classes.
     */
    public String getIconClass() {
        return iconClass;
    }

    /**
     * True to only display the icon span element.
     *
     * @return  true if icon only.
     */
    public boolean isIconOnly() {
        return iconOnly;
    }

    /**
     * Child nodes.
     *
     * @return  child items.
     */
    public List<MenuItem> getChildren() {
        return children;
    }

}
