/*
 * H2DBBackup.java    Sep 5 2014, 11:11
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

package com.drunkendev.jdbc.h2;

import com.drunkendev.web.settings.AppConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.drunkendev.io.FileUtils.delete;
import static com.drunkendev.io.FileUtils.getLastModifiedTime;


/**
 * Helper for backing up a H2 database to the applications home directory.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class H2DBBackup {

    private static final Logger LOG = LoggerFactory.getLogger(H2DBBackup.class);
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd_hhmm");

    private final JdbcTemplate jt;
    private final AppConfig config;

    /**
     * Creates a new {@code BackupTask} instance.
     *
     * @param   jt
     *          {@link JdbcTemplate} instance.
     * @param   config
     *          Application configuration.
     */
    public H2DBBackup(JdbcTemplate jt, AppConfig config) {
        this.jt = jt;
        this.config = config;
    }

    /**
     * Backs up a h2 database to a target directory.
     *
     * Backup folder will first attempt to be set to a config attribute, if
     * the attribute does not exist the location will default to home dir/backup.
     * File name will be {@code ${app.name}-db-backup-yyyy-MM-dd_hhmm.zip}
     *
     * @throws  IOException
     *          If the backup file could not be created.
     */
    public void backupDatabase() throws IOException {
        Path path = config.getPathOrDefault("backup.folder", config.getHomePath().resolve("backup"))
                .normalize();
        Path p = path
                .resolve(config.getAppName() + "-db-backup-" + df.format(LocalDateTime.now()) + ".zip")
                .normalize();
        LOG.info("Backing up database to {}", p);
        Files.createDirectories(path);

        long cut = LocalDateTime.now().minusDays(2).toEpochSecond(ZoneOffset.UTC);
        Files.list(path)
                .filter(n -> getLastModifiedTime(n)
                .to(TimeUnit.SECONDS) < cut)
                .forEach(n -> delete(n, null));
        jt.execute("backup to '" + p.toString() + "'");
        LOG.debug("Backup complete {}", p);
    }

}
