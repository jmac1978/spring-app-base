/*
 * ApiErrorBasicAuthenticationEntryPoint.java    Oct 9 2014, 01:31
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

package com.drunkendev.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.drunkendev.web.ApiError;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;


/**
 * Entry point for REST services that will write a JSON {@link ApiError} to the output stream.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class ApiErrorBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private final ObjectMapper om;

    /**
     * Creates a new {@code XBasicAuthenticationEntryPoint} instance.
     *
     * @param   om
     *          Object mapper used for writing the output.
     */
    public ApiErrorBasicAuthenticationEntryPoint(ObjectMapper om) {
        this.om = om;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + super.getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        om.writeValue(response.getOutputStream(),
                      new ApiError(request.getRequestURI(),
                                   HttpServletResponse.SC_UNAUTHORIZED,
                                   "You must sign in or provide basic authentication."));
    }

}
