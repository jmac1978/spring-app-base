/*
 * RequestLoggingInterceptor.java    Oct 8 2013, 22:39
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

package com.drunkendev.web.userlog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.ParameterizableViewController;


/**
 * Interceptor for logging user requests.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class RequestLoggingInterceptor extends HandlerInterceptorAdapter implements DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    private final UserlogService ul;
    private final ExecutorService threadPool;

    /**
     * Creates a new {@code RequestLoggingInterceptor} instance.
     *
     * @param   userlog
     *          Userlog service implementation.
     */
    public RequestLoggingInterceptor(UserlogService userlog) {
        this.ul = userlog;
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex)
            throws Exception {
        String _user = request.getRemoteUser();
        LOG.debug("Logging user request for {}", _user);
        if (_user != null && (handler instanceof HandlerMethod ||
                              handler instanceof ParameterizableViewController)) {
            final String method = request.getMethod();
            final String path = request.getServletPath();
            final String query = request.getQueryString();
            final String contentType = response.getContentType();
            final String agent = request.getHeader("User-Agent");
            final String address = request.getRemoteAddr();
            String _actingAs = null;
            if (request.isUserInRole(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR)) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                for (GrantedAuthority ga : auth.getAuthorities()) {
                    if (ga instanceof SwitchUserGrantedAuthority) {
                        SwitchUserGrantedAuthority sa = (SwitchUserGrantedAuthority) ga;
                        _actingAs = _user;
                        _user = sa.getSource().getName();
                        break;
                    }
                }
            }
            final String user = _user;
            final String actingAs = _actingAs;
            threadPool.submit(() -> ul.add(user,
                                           actingAs,
                                           method,
                                           path,
                                           query,
                                           contentType,
                                           agent,
                                           address,
                                           ex));
        }
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("Shutting down request logging interceptor.");
        threadPool.shutdown();
    }

}
