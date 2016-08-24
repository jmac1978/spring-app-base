/*
 * UserlogService.java    Aug 11 2016, 15:29
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

package com.drunkendev.web.userlog;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.uadetector.ReadableUserAgent;


/**
 * Interface for a user logging system.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public interface UserlogService {

    /**
     * Log a new request for the given HTTP request.
     *
     * Implementations should NOT alter the request or alter state in any way.
     *
     * @param   request
     *          Request object
     * @param   response
     *          Response object
     * @param   handler
     *          Spring handler if present.
     * @param   ex
     *          Exception if one occurred.
     */
    public void add(HttpServletRequest request,
                    HttpServletResponse response,
                    Object handler,
                    Exception ex);

    /**
     * Log a new request.
     *
     * @param   user
     *          User request was made with.
     * @param   actingAs
     *          If sudo is present the user the user is acting as.
     * @param   method
     *          HTTP method.
     * @param   url
     *          URL of request.
     * @param   query
     *          Request query string.
     * @param   contentType
     *          Content type of response.
     * @param   userAgent
     *          User agent string.
     * @param   remoteAddress
     *          Remote address of user.
     * @param   ex
     *          Exception if one occurred.
    */
    public void add(String user,
                    String actingAs,
                    String method,
                    String url,
                    String query,
                    String contentType,
                    String userAgent,
                    String remoteAddress,
                    Exception ex);

    /**
     * Get user usage summary for all time.
     *
     * @return  List of user usage summary instances.
     */
    public List<UserUsageSummary> getUserUsageSummary();

    /**
     * Get sudo usage summary for all time.
     *
     * @return  List of sudo usage for all time.
     */
    public List<SudoSummary> getSudoSummary();

    /**
     * Get a list of user agent usage.
     *
     * @return  List of user agent usage.
     */
    public List<ReadableUserAgent> getUserAgentUsage();

}
