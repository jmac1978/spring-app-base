/*
 * IfConfigPresentTagHandler.java    May 21 2013, 18:16
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;


/**
 *
 * @author  Brett Ryan
 */
public class IfConfigPresentTagHandler extends RequestContextAwareTag {

    private static final Logger log = LoggerFactory.getLogger(IfConfigPresentTagHandler.class);
    private String key;

    /**
     * Creates a new {@code IfConfigPresentTagHandler} instance.
     */
    public IfConfigPresentTagHandler() {
        super();
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        if (pageContext.getSession() == null || pageContext.getSession().getServletContext() == null) {
            log.warn("IfConfigPresentTagHandler:doStartTagInternal - Session or servlet context is null");
            return SKIP_BODY;
        }
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getSession().getServletContext());
        if (ctx == null) {
            log.warn("IfConfigPresentTagHandler:doStartTagInternal - Application context is null");
            return SKIP_BODY;
        }
        AppConfig config = ctx.getBean(AppConfig.class);

        return StringUtils.isBlank(config.getString(key)) ? SKIP_BODY : EVAL_BODY_INCLUDE;
    }

}
