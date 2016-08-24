/*
 * SudoSummary.java    Jul 31 2016, 00:20
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
public class SudoSummary {

    private final String username;
    private final String sudoUsername;
    private final LocalDateTime date;
    private final int count;

    public SudoSummary(String username, String sudoUsername, LocalDateTime date, int count) {
        this.username = username;
        this.sudoUsername = sudoUsername;
        this.date = date;
        this.count = count;
    }

    public String getUsername() {
        return username;
    }

    public String getSudoUsername() {
        return sudoUsername;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

}
