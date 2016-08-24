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
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.ParameterizableViewController;


/**
 * Interceptor for logging user requests.
 *
 * @author  Brett Ryan
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
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (request.getRemoteUser() != null &&
            (handler instanceof HandlerMethod || handler instanceof ParameterizableViewController)) {
            threadPool.submit(() -> ul.add(request, response, handler, ex));
        }
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void destroy() throws Exception {
        LOG.info("Shutting down request logging interceptor.");
        threadPool.shutdown();
    }

}
