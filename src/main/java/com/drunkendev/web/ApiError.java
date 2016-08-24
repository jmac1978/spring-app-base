/*
 * ApiError.java    Apr 1 2014, 15:51
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

package com.drunkendev.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;


/**
 *
 * @author  Brett Ryan
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiError {

    private final String errorType;
    private final String reason;
    private final String message;
    private final String url;
    private final String stackTrace;

    private final HttpStatus status;
    private final int statusCode;
    private final String series;
    private final int seriesCode;

    /**
     * Creates a new {@code ErrorInfo} instance.
     *
     * @param   url
     *          URL which resulted in the exception.
     * @param   ex
     *          Exception thrown.
     * @param   includeTrace
     *          True to include the trace information in the error object.
     */
    public ApiError(String url, Throwable ex, boolean includeTrace) {
        if (ex instanceof HttpStatusCodeException) {
            HttpStatusCodeException ex2 = (HttpStatusCodeException) ex;
            this.reason = ex2.getStatusCode().getReasonPhrase();
            this.status = ex2.getStatusCode();
        } else if (ex instanceof AccessDeniedException) {
            this.status = HttpStatus.FORBIDDEN;
            this.reason = null;
        } else {
            ResponseStatus rs = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
            if (rs == null) {
                this.status = HttpStatus.INTERNAL_SERVER_ERROR;
                this.reason = null;
            } else {
                this.status = rs.value();
                this.reason = rs.reason();
            }
        }
        this.errorType = includeTrace ? ex.getClass().getName() : null;
        this.stackTrace = includeTrace ? ExceptionUtils.getStackTrace(ex) : null;
        this.message = ex.getLocalizedMessage();
        this.url = url;

        this.statusCode = this.status.value();
        this.series = this.status.series().toString();
        this.seriesCode = this.status.series().value();
    }

    public ApiError(String url, Throwable ex, int status, boolean includeTrace) {
        this.errorType = includeTrace ? ex.getClass().getName() : null;
        this.stackTrace = includeTrace ? ExceptionUtils.getStackTrace(ex) : null;
        ResponseStatus rs = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (rs == null) {
            this.status = HttpStatus.valueOf(status);
            this.reason = null;
        } else {
            this.status = rs.value();
            this.reason = rs.reason();
        }
        this.message = ex.getLocalizedMessage();
        this.url = url;

        this.statusCode = this.status.value();
        this.series = this.status.series().toString();
        this.seriesCode = this.status.series().value();
    }

    public ApiError(String url, int status, String message) {
        this.errorType = null;
        this.stackTrace = null;
        this.status = HttpStatus.valueOf(status);
//        this.status = status;
        this.reason = null;
        this.message = message;
        this.url = url;

        this.statusCode = this.status.value();
        this.series = this.status.series().toString();
        this.seriesCode = this.status.series().value();
    }

    public String getErrorType() {
        return errorType;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getSeries() {
        return series;
    }

    public int getSeriesCode() {
        return seriesCode;
    }

}
