/*
 * AppConfig.java    May 21 2013, 14:15
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;


/**
 * Application configuration which uses values from {@code ${appname}.config.properties}
 * and {@code ${appname}.user.config.properties}.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class AppConfig implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

    private final String appNameLc;
    private final String appNameUc;
    private final String configSystemProp;
    private final String configSystemEnv;
    private final String homeSystemProp;
    private final String homeSystemEnv;

    private Properties props;
    private final ClassLoader loader;

    /**
     * Creates a new {@code AppConfig} instance.
     *
     * Properties will be loaded from a file within the given class loader under
     * {@code /lower(${appName}).properties} and {@code /lower(${appName}).user.properties}
     *
     * If you have either {@code app.config} system property or {@code APP_CONFIG}
     * environment variable defined and set to a properties file for loading that
     * file will be used to load additional properties.
     *
     * @param   loader
     *          Loader used for locating built-in configuration resources.
     * @param   appName
     *          Base name of application for environment and property names.
     *          This name should contain no spaces and will be used to determine
     */
    public AppConfig(ClassLoader loader, String appName) {
        appNameLc = defaultString(trimToNull(appName), "app").toLowerCase();
        appNameUc = defaultString(trimToNull(appName), "APP").toUpperCase();
        configSystemProp = appNameLc + ".config";
        configSystemEnv = appNameUc + "_CONFIG";
        homeSystemProp = appNameLc + ".home";
        homeSystemEnv = appNameUc + "_HOME";
        this.loader = loader == null ? AppConfig.class.getClassLoader() : loader;
        LOG.debug("APP NAME: " + appName);
    }

    /**
     * Creates a new {@code AppConfig} instance.
     *
     * @param   loaderClass
     *          Loader class used for locating built-in configuration resources.
     * @param   appName
     *          Base name of application for environment and property names.
     *          This name should contain no spaces and will be used to determine
     */
    public AppConfig(Class loaderClass, String appName) {
        this(loaderClass.getClassLoader(), appName);
    }

    /**
     * Creates a new {@code AppConfig} instance.
     *
     * This class is used as the loader which will not find any properties files.
     * For this to work you must have either {@code app.config} system property
     * or {@code APP_CONFIG} environment variable defined and set to a properties
     * file for loading.
     */
    public AppConfig() {
        this(AppConfig.class, null);
    }

    /**
     * Configuration file from either {@code app.config} system property or
     * {@code APP_CONFIG}.
     *
     * If not set then {@code null} is returned.
     *
     * @return  Application configuration file or {@code null} if not defined.
     */
    public String getConfigFile() {
        String configFile = System.getProperty(configSystemProp);
        if (isBlank(configFile)) {
            configFile = System.getenv(configSystemEnv);
        }
        return defaultIfBlank(configFile, null);
    }

    String homeDir;

    /**
     * Home directory for application.
     *
     * This will check int he following order for
     *
     * <ul>
     *  <li>{@code app.home} system property</li>
     *  <li>{@code APP_HOME} environment variable</li>
     *  <li>{@code user.dir} system property</li>
     *  <li>{@code HOME} environment variable</li>
     * </ul>
     *
     * If none of the above succeed the executing directory of the application is returned.
     *
     * @return  Home directory for application.
     */
    public String getHomeDir() {
        if (homeDir != null) {
            return homeDir;
        }

        String path = System.getProperty(homeSystemProp);
        LOG.debug("PATH: (prop:{}) {}", homeSystemProp, path);
        if (isBlank(path)) {
            path = System.getenv(homeSystemEnv);
            LOG.debug("PATH: (env:{}) {}", homeSystemEnv, path);
            if (isBlank(path)) {
                path = System.getenv("user.dir");
                LOG.debug("PATH: user.dir {}", path);
                if (isBlank(path)) {
                    path = System.getenv("HOME");
                    LOG.debug("PATH: HOME {}", path);
                    if (isBlank(path)) {
                        path = Paths.get(".").normalize().toString();
                        LOG.debug("PATH: {}", path);
                    }
                }
            }
        }
        homeDir = path;
        LOG.debug("RETURNING PATH: {}", homeDir);
        return homeDir;
    }

    public Path getHomePath() {
        String v = getHomeDir();
        return v == null ? null : Paths.get(v).normalize();
    }

    public void load() throws IOException {
        load(loader.getResourceAsStream("/" + appNameLc + ".config.properties"),
             loader.getResourceAsStream("/" + appNameLc + ".user.config.properties"));
    }

    public void load(InputStream def,
                     InputStream over) throws IOException {
        Properties defaultProps = new Properties();

        try (InputStream in = def) {
            if (in != null) {
                defaultProps.load(in);
            }
        }
        this.props = new Properties(defaultProps);
        try (InputStream in = over) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception ex) {
        }

        String configFile = getConfigFile();
        if (configFile != null) {
            try (InputStream in = new FileInputStream(configFile)) {
                LOG.debug("Loading properties {} := {}", configFile, in);
                props.load(in);
            }
        }
    }

    public void save() throws FileNotFoundException, IOException {
        String configFile = getConfigFile();
        if (configFile != null) {
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                props.store(fos, "App configuration file.");
            }
        }
    }

    public String getString(String key) {
        return props.getProperty(key);
    }

    public String getString(String... key) {
        for (String k : key) {
            String v = props.getProperty(k);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    public Object getObject(String key) {
        return props.get(key);
    }

    public Object getObject(String... key) {
        for (String k : key) {
            Object v = props.get(k);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    public Path getPath(String key) {
        String val = getString(key);
        return val == null ? null : Paths.get(val).normalize();
    }

    public Path getPathOrDefault(String key, Path def) {
        String val = getString(key);
        return val == null ? def : Paths.get(val).normalize();
    }

    public Integer getInt(String key) {
        String v = getString(key);
        return v == null ? 0 : Integer.valueOf(v);
    }

    public Integer getInt(String key, int def) {
        String v = getString(key);
        return v == null ? def : Integer.valueOf(v);
    }

    public int getInt(String... key) {
        for (String k : key) {
            String v = props.getProperty(k);
            if (v != null) {
                return Integer.valueOf(v);
            }
        }
        return 0;
    }

    public Long getLong(String key) {
        String v = getString(key);
        return v == null ? 0 : Long.valueOf(v);
    }

    public long getLong(String... key) {
        for (String k : key) {
            String v = props.getProperty(k);
            if (v != null) {
                return Long.valueOf(v);
            }
        }
        return 0;
    }

    public boolean getBoolean(String key) {
        return isTrue(trimToEmpty(getString(key)));
    }

    public boolean getBoolean(String... key) {
        for (String k : key) {
            String v = props.getProperty(k);
            if (v != null) {
                return isTrue(v);
            }
        }
        return false;
    }

    private static boolean isTrue(String v) {
        return v != null && ("1".equals(v) || toBoolean(v) || "on".equalsIgnoreCase(v));
    }

    public Set<String> getPropertyNames() {
        return props.stringPropertyNames();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (props == null) {
            load();
        }
    }

}
