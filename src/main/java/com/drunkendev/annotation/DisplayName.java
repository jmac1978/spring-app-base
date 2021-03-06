/*
 * DisplayName.java    Dec 15 2015, 09:46
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

package com.drunkendev.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Provides a name for an identifier.
 *
 * This class serves a similar purpose to that of the C# <a href="https://msdn.microsoft.com/en-us/library/system.componentmodel.displaynameattribute.aspx">System.ComponentModel.DisplayNameAttribute</a>
 *
 * @author  Brett Ryan
 */
@Documented
@Inherited
@Target({ElementType.ANNOTATION_TYPE,
         ElementType.CONSTRUCTOR,
         ElementType.FIELD,
         ElementType.LOCAL_VARIABLE,
         ElementType.METHOD,
         ElementType.PACKAGE,
         ElementType.PARAMETER,
         ElementType.TYPE,
         ElementType.TYPE_PARAMETER,
         ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayName {

    /**
     * Provides a human readable name to an identifier.
     *
     * @return  Human readable name.
     */
    String value();

}
