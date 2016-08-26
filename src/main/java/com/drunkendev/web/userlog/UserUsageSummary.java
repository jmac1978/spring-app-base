/*
 * UserUsageSummary.java    Jul 31 2016, 00:20
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

import java.time.LocalDateTime;


/**
 * Structure representing first/last access within application and total count.
 *
 * This may be restricted to within a date-range for an initial query.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class UserUsageSummary {

    private final String username;
    private final LocalDateTime firstRequest;
    private final LocalDateTime lastRequest;
    private final int count;

    /**
     * Constructs a new {@code UserUsageSummary} instance.
     *
     * @param   username
     *          Username.
     * @param   firstRequest
     *          First request.
     * @param   lastRequest
     *          Last request.
     * @param   count
     *          Request count.
     */
    public UserUsageSummary(String username,
                            LocalDateTime firstRequest,
                            LocalDateTime lastRequest,
                            int count) {
        this.username = username;
        this.firstRequest = firstRequest;
        this.lastRequest = lastRequest;
        this.count = count;
    }

    /**
     * Username requests are made by.
     *
     * @return  Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Timestamp of oldest request.
     *
     * @return  Timestamp of first request.
     */
    public LocalDateTime getFirstRequest() {
        return firstRequest;
    }

    /**
     * Timestamp of most recent request.
     *
     * @return  Timestamp of last request.
     */
    public LocalDateTime getLastRequest() {
        return lastRequest;
    }

    /**
     * Total requests made and logged.
     *
     * @return  Count of requests made by this user.
     */
    public int getCount() {
        return count;
    }

}
