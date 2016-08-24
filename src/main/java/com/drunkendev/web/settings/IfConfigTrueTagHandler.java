/*
 * IfConfigTrueTagHandler.java    May 30 2013, 10:19
 *
 * Copyright 2013 Drunken Dev.
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

package com.drunkendev.web.settings;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import static javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE;
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;


/**
 *
 * @author  Brett Ryan
 */
public class IfConfigTrueTagHandler extends RequestContextAwareTag {

    private String key;

    /**
     * Creates a new {@code IfConfigTrueTagHandler} instance.
     */
    public IfConfigTrueTagHandler() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getSession().getServletContext());
        AppConfig config = ctx.getBean(AppConfig.class);
        return config.getBoolean(key) ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

}
