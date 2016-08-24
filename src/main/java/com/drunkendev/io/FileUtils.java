/*
 * FileUtils.java    Aug 11 2016, 15:26
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

package com.drunkendev.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.function.BiConsumer;


/**
 * Provides unchecked versions {@link Files} methods.
 *
 * @author  Brett Ryan
 */
public class FileUtils {

    /**
     * Unchecked version of {@link Files#getLastModifiedTime(java.nio.file.Path, java.nio.file.LinkOption...) Files#getLastModifiedTime(Path, LinkOption...)}.
     *
     * Refer to {@link Files#getLastModifiedTime(java.nio.file.Path, java.nio.file.LinkOption...) Files#getLastModifiedTime(Path, LinkOption...)}
     * for details.
     *
     * @param   path
     *          the path to the file
     * @param   options
     *          options indicating how symbolic links are handled
     *
     * @return  a {@code FileTime} representing the time the file was last
     *          modified, or an implementation specific default when a time
     *          stamp to indicate the time of last modification is not supported
     *          by the file system
     *
     * @throws  UncheckedIOException
     *          if an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default provider, and a security manager is
     *          installed, its {@link SecurityManager#checkRead(String) checkRead}
     *          method denies read access to the file.
     *
     * @see     Files#getLastModifiedTime(java.nio.file.Path, java.nio.file.LinkOption...) Files#getLastModifiedTime(Path, LinkOption...)
     */
    public static FileTime getLastModifiedTime(Path path, LinkOption... options)
            throws UncheckedIOException {
        try {
            return Files.getLastModifiedTime(path, options);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     * An unchecked version of Files#delete(java.nio.file.Path) Files#delete(Path).
     *
     * Refer to Files#delete(java.nio.file.Path) Files#delete(Path) for details.
     *
     * @param   path
     *          the path to the file to delete
     * @param   errorMethod
     *          Optional consumer called if an {@link IOException} occurs.
     *
     * @throws  UncheckedIOException
     *          if an I/O error occurs. Other wrapped exceptions may be
     *          {@link java.nio.file.NoSuchFileException} or {@link java.nio.file.DirectoryNotEmptyException}
     * @throws  SecurityException
     *          In the case of the default provider, and a security manager is
     *          installed, the {@link SecurityManager#checkDelete(String)} method
     *          is invoked to check delete access to the file
     *
     * @see     Files#delete(java.nio.file.Path) Files#delete(Path)
     */
    public static void delete(Path path, BiConsumer<Path, Exception> errorMethod) {
        try {
            Files.delete(path);
        } catch (IOException ex) {
            if (errorMethod != null) {
                errorMethod.accept(path, ex);
            }
        }
    }

}
