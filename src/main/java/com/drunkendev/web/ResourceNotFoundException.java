/*
 * ResourceNotFoundException.java    Jul 30 2016, 00:07
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
 * Generic exception used for resource not found (404).
 *
 * @author  Brett Ryan
 * @since   1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND,
                reason = "Requested resource could not be found.")
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new instance of <code>ResourceNotFoundException</code> without detail message.
     */
    public ResourceNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ResourceNotFoundException</code> with the specified detail message.
     *
     * @param   msg
     *          the detail message.
     */
    public ResourceNotFoundException(String msg) {
        super(msg);
    }

}
