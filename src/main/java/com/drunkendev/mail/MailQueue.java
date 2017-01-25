/*
 * MailQueue.java    Aug 12 2016, 11:48
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

import java.io.IOException;
import java.util.List;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * Provides a mail queue service to send mail asynchronously from within an application.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public interface MailQueue {

    /**
     * Sends any unsent mail.
     */
    void sendUnsent();

    /**
     * Enqueues a message for sending.
     *
     * @param   msg
     *          Message to be sent.
     *
     * @throws  IOException
     *          If an IO Error occurs.
     * @throws  MessagingException
     *          If a messaging error occurs.
     */
    void enqueue(MimeMessage msg)
            throws IOException, MessagingException;

    /**
     * Enqueues a message for sending.
     *
     * @param   subject
     *          Subject for the message.
     * @param   body
     *          Plain text body for the message.
     * @param   recipients
     *          Recipients of the message.
     *
     * @throws  IOException
     *          If an IO Error occurs.
     * @throws  MessagingException
     *          If a messaging error occurs.
     */
    void enqueuePlain(String subject,
                      String body,
                      Address[] recipients)
            throws IOException, MessagingException;

    /**
     * Enqueues a message for sending.
     *
     * @param   subject
     *          Subject for the message.
     * @param   body
     *          Pre-formatted HTML content for the message.
     * @param   recipients
     *          Recipients of the message.
     *
     * @throws  IOException
     *          If an IO Error occurs.
     * @throws  MessagingException
     *          If a messaging error occurs.
     */
    void enqueueHtml(String subject,
                     String body,
                     Address[] recipients)
            throws IOException, MessagingException;

    /**
     * Determines if a system error can be sent using {@link #sendSystemError(String, String, String, Exception)}
     *
     * @return  true if calls to {@link #sendSystemError(String, String, String, Exception)}
     *          will send an email.
     */
    default boolean isSystemErrorSupported() {
        return false;
    }

    /**
     * Enqueues a system error message that may be sent to administrators for review.
     *
     * @param   subject
     *          Subject for the message.
     * @param   message
     *          Message relating to the error.
     * @param   url
     *          URL that the error was raised on.
     * @param   ex
     *          Exception that caused the error.
     *
     * @throws  IOException
     *          If an IO Error occurs.
     * @throws  MessagingException
     *          If a messaging error occurs.
     */
    void sendSystemError(String subject,
                         String message,
                         String url,
                         Exception ex)
            throws MessagingException, IOException;

    /**
     * Retrieves any unsent mail.
     *
     * @return  List of all unsent mail items.
     */
    List<MailQueueEntry> getUnsent();

}
