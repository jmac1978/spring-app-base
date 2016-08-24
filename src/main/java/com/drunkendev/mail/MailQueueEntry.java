/*
 * MailQueueEntry.java    Aug 12 2016, 11:46
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

package com.drunkendev.mail;

import java.time.LocalDateTime;


/**
 * Represents a queued mail item.
 *
 * @author  Brett Ryan
 */
public class MailQueueEntry {

    private final long id;
    private final String subject;
    private final LocalDateTime created;
    private final LocalDateTime sent;
    private final String error;
    private final long exceptionId;
    private final int tries;

    public MailQueueEntry(long id,
                          String subject,
                          LocalDateTime created,
                          LocalDateTime sent,
                          String error,
                          long exceptionId,
                          int tries) {
        this.id = id;
        this.subject = subject;
        this.created = created;
        this.sent = sent;
        this.error = error;
        this.exceptionId = exceptionId;
        this.tries = tries;
    }

    public long getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public String getError() {
        return error;
    }

    public long getExceptionId() {
        return exceptionId;
    }

    public int getTries() {
        return tries;
    }

}
