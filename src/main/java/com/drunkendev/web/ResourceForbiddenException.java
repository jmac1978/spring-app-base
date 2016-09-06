/*
 * ResourceForbiddenException.java    May 30 2013, 19:46
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

package com.drunkendev.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Exception to be thrown where a user does not have access to a given resource.
 *
 * @author  Brett Ryan
 * @since   1.1
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN,
                reason = "Insufficient privileges.")
public class ResourceForbiddenException extends RuntimeException {

    /**
     * Creates a new {@code ResourceForbiddenException} instance.
     */
    public ResourceForbiddenException() {
    }

    /**
     * Constructs a new {@code ResourceForbiddenException} with the specified detail message.
     *
     * @param   message
     *          the detail message.
     */
    public ResourceForbiddenException(String message) {
        super(message);
    }

}
