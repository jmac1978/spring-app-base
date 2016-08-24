/*
 * JSR310TagHandler.java    May 16 2014, 12:13
 *
 * Copyright 2014 Drunken Dev.
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

package com.drunkendev.time;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * Tag handler for conversion of jsr-310 data types.
 *
 * @author  Brett Ryan
 */
public class JSR310TagHandler extends TagSupport {

    private Temporal value;
    private DateTimeFormatter formatter;

    /**
     * Creates a new {@code JSR310TagHandler} instance.
     */
    public JSR310TagHandler() {
        formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
    }

    /**
     * Value to be converted.
     *
     * @param   value
     *          Value to convert.
     */
    public void setValue(Temporal value) {
        this.value = value;
    }

    /**
     * Pattern to be converted to.
     *
     * @param   pattern
     *          Pattern to convert to.
     */
    public void setPattern(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * Writes formatted temporal to output.
     *
     * @return  {@link javax.servlet.jsp.tagext.Tag#SKIP_BODY Tag.SKIP_BODY}
     *
     * @throws  JspException
     *          If parse or write error occurs.
     */
    @Override
    public int doStartTag() throws JspException {
        if (value != null) {
            try {
                pageContext.getOut().write(formatter.format(value));
            } catch (DateTimeException | IOException ex) {
                throw new JspException(ex);
            }
        }
        return SKIP_BODY;
    }

}
