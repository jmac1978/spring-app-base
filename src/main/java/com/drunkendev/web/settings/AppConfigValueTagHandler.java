/*
 * AppConfigValueTagHandler.java    21 May 2013, 16:24
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

import javax.servlet.jsp.JspWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;


/**
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class AppConfigValueTagHandler extends RequestContextAwareTag {

    private String key;

    public AppConfigValueTagHandler() {
        super();
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getSession().getServletContext());
        AppConfig config = ctx.getBean(AppConfig.class);

        JspWriter out = pageContext.getOut();
        out.print(config.getString(key));

        return SKIP_BODY;
    }

}
