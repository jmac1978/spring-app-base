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
 * Provides functions for taglib functions {@code http://drunkendev.com/taglib/app-config}.
 *
 * This bean has been marked as a {@link ApplicationContextAware} {@link Component}
 * which allows one to use component scanning for package {@link com.drunkendev.web.settings}.
 *
 * Component scanning of this class is not strictly required, one may use the static
 * setter {@link #setAppConfig(AppConfig)} to set the instance.
 *
 * <strong>NOTE</strong>: if no {@code config} is set at runtime any calls to {@code get}
 * values will throw a {@link IllegalStateException}.
 *
 * This class does not need to be configured as a bean but must have {@link #setAppConfig(AppConfig)}
 * called to register the {@link AppConfig} instance.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
@Component
public class ConfigFunctions implements ApplicationContextAware {

    private static AppConfig appConfig;

    /**
     * Creates a new {@code ConfigFunctions} instance.
     */
    public ConfigFunctions() {
    }

    private static void ensureConfig() {
        if (appConfig == null) {
            throw new IllegalStateException("appConfig must be set.");
        }
    }

    /**
     * Get configured value as a string.
     *
     * @param   key
     *          Key to get value for.
     * @return  Configured value as a string.
     * @throws  IllegalStateException
     *          If {@code appConfig} property has not been set.
     * @since   1.0
     */
    public static String getString(String key) {
        ensureConfig();
        return appConfig.getString(key);
    }

    /**
     * Get configured value as a {@code boolean}.
     *
     * @param   key
     *          Key to get value for.
     * @return  Configured value as a boolean.
     * @throws  IllegalStateException
     *          If {@code appConfig} property has not been set.
     * @since   1.0
     */
    public static boolean getBoolean(String key) {
        ensureConfig();
        return appConfig.getBoolean(key);
    }

    /**
     * Get configured value as an {@code int}.
     *
     * @param   key
     *          Key to get value for.
     * @return  Configured value as an integer.
     * @throws  IllegalStateException
     *          If {@code appConfig} property has not been set.
     * @since   1.0
     */
    public static int getInt(String key) {
        ensureConfig();
        return appConfig.getInt(key);
    }

    /**
     * Get configured value as a {@code boolean}.
     *
     * @param   key
     *          Key to get value for.
     * @return  Configured value as a long.
     * @throws  IllegalStateException
     *          If {@code appConfig} property has not been set.
     * @since   1.0
     */
    public static long getLong(String key) {
        ensureConfig();
        return appConfig.getLong(key);
    }

    /**
     * Get configured value as a {@code boolean}.
     *
     * @param   key
     *          Key to get value for.
     * @return  Configured value as an object.
     * @throws  IllegalStateException
     *          If {@code appConfig} property has not been set.
     * @since   1.0
     */
    public static Object getObject(String key) {
        ensureConfig();
        return appConfig.getObject(key);
    }

    /**
     * Set application context on bean instance.
     *
     * @param   applicationContext
     *          Application context.
     * @throws  BeansException
     *          if thrown by application context methods.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConfigFunctions.appConfig = applicationContext.getBean(AppConfig.class);
    }

    /**
     * Set context {@code AppConfig} instance.
     *
     * @param   value
     *          Value for the application configuration.
     * @since   1.1
     */
    public static void setAppConfig(AppConfig value) {
        appConfig = value;
    }

}
