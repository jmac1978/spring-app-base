/*
 * ConfigFunctions.java    Oct 9 2013, 03:55
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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 *
 * @author  Brett Ryan
 */
@Component
public class ConfigFunctions implements ApplicationContextAware {

    private static AppConfig config;

    /**
     * Creates a new {@code ConfigFunctions} instance.
     */
    public ConfigFunctions() {
    }

    public static String getString(String k) {
        return config == null ? null : config.getString(k);
    }

    public static boolean getBoolean(String k) {
        return config == null ? false : config.getBoolean(k);
    }

    public static int getInt(String k) {
        return config == null ? 0 : config.getInt(k);
    }

    public static long getLong(String k) {
        return config == null ? 0L : config.getLong(k);
    }

    public static Object getObject(String k) {
        return config == null ? null : config.getObject(k);
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.config = ac.getBean(AppConfig.class);
    }

}
