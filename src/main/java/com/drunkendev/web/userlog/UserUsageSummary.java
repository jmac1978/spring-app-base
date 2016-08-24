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
 *
 * @author  Brett Ryan
 */
public class UserUsageSummary {

    private final String username;
    private final LocalDateTime firstRequest;
    private final LocalDateTime lastRequest;
    private final int count;

    public UserUsageSummary(String username, LocalDateTime firstRequest, LocalDateTime lastRequest, int count) {
        this.username = username;
        this.firstRequest = firstRequest;
        this.lastRequest = lastRequest;
        this.count = count;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getFirstRequest() {
        return firstRequest;
    }

    public LocalDateTime getLastRequest() {
        return lastRequest;
    }

    public int getCount() {
        return count;
    }

}
