/*
 * MailService.java    29 Oct 2014, 18:33
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

package com.drunkendev.mail;

import com.drunkendev.web.settings.AppConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import static com.drunkendev.time.TemporalConverters.toLocalDateTime;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


/**
 * Mail queue implementation using JDBC backed meta storage and file based mail storage.
 *
 * Note that this service itself does not setup any async queue for sending mail.
 * It is intended that this be left to a quartz or spring task schedule allowing
 * larger flexibility of the implementation.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class AppMailQueue implements MailQueue {

    private static final Logger LOG = LoggerFactory.getLogger(AppMailQueue.class);

    private final AppConfig conf;
    private final JdbcTemplate jt;
    private final JavaMailSender jms;

    private final Path queuePath;
    private final String fromAddress;
    private final String errorMailto;
    private final int maxTries;
    private final String mailerAgent;

    /**
     * Creates a new {@code MailQueue} instance.
     *
     * Configuration of the mail queue will be read from the application configuration,
     * changes to this configuration will not be reflected in this instance.
     *
     * Parameters read are as follows:
     *
     * <ul>
     *   <li><strong>mail.queue.path</strong>: Location to store mail queue files (default: ${home}/mail-queue).</li>
     *   <li><strong>mail.queue.tries</strong>: Maximum tries to send mail (default: 3).</li>
     *   <li><strong>mail.from</strong>: Default from address (required for sending error mail).</li>
     *   <li><strong>mail.errorto</strong>: Address to send error reports (required for sending error mail).</li>
     * </ul>
     *
     * @param   conf
     *          App configuration.
     * @param   jt
     *          Jdbc Template instance.
     * @param   jms
     *          Java Mail Sender instance.
     */
    public AppMailQueue(AppConfig conf, JdbcTemplate jt, JavaMailSender jms) {
        this.conf = conf;
        this.jt = jt;
        this.jms = jms;

        this.queuePath = conf.getPathOrDefault("mail.queue.path",
                                               conf.getHomePath().resolve("mail-queue").normalize());
        this.fromAddress = conf.getString("mail.from");
        this.errorMailto = conf.getString("error.mailto");
        int n = conf.getInt("mail.queue.tries", 3);
        maxTries = n > 0 ? n : 3;

        this.mailerAgent = defaultString(conf.getString("mail.agent"), "Drunken-Dev-App-Mailer");
    }

    @Override
    public void enqueuePlain(String subject,
                             String body,
                             Address[] recipients)
            throws MessagingException, IOException {
        MimeMessage msg = jms.createMimeMessage();
        msg.setSubject(subject);
        msg.setFrom(conf.getString("mail.from"));
        msg.addRecipients(Message.RecipientType.TO, recipients);

        Multipart mp = new MimeMultipart();

        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(body, "text/plain");
        mp.addBodyPart(mbp);

        msg.setContent(mp);
        enqueue(msg);
    }

    @Override
    public void enqueueHtml(String subject,
                            String body,
                            Address[] recipients)
            throws MessagingException, IOException {
        MimeMessage msg = jms.createMimeMessage();
        msg.setSubject(subject);
        msg.setFrom(conf.getString("mail.from"));
        msg.addRecipients(Message.RecipientType.TO, recipients);

        Multipart mp = new MimeMultipart();

        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setContent(body, "text/html");
        mp.addBodyPart(mbp);

        msg.setContent(mp);
        enqueue(msg);
    }

    /**
     * Enqueues an error to be sent to the configured error recipient.
     *
     * @param   subject (optional)
     *          Subject for the error
     * @param   message (optional)
     *          Additional message content.
     * @param   url (optional)
     *          URL that the error was raised on.
     * @param   ex
     *          Exception that cause the error.
     *
     * @throws  IOException
     *          If an IO Error occurs.
     * @throws  MessagingException
     *          If a messaging error occurs.
     */
    @Override
    public void sendSystemError(String subject,
                                String message,
                                String url,
                                Exception ex) throws MessagingException, IOException {
        MimeMessage msg = jms.createMimeMessage();
        msg.setFrom(fromAddress);
        msg.addRecipients(Message.RecipientType.TO, errorMailto);
        msg.setSubject("App Exception" + (isBlank(subject) ? "" : (": " + subject)));

        StringBuilder body = new StringBuilder();
        if (isBlank(message)) {
            body.append("<p>An exception has been recorded.</p>");
        } else {
            body.append("<p>").append(message).append("</p>");
        }
        if (isNotBlank(url)) {
            body.append("<p><strong>URL</strong>: ").append(url).append("</p>");
        }
        if (ex != null) {
            try (StringWriter stringWriter = new StringWriter();
                 PrintWriter printWriter = new PrintWriter(stringWriter)) {
                ex.printStackTrace(printWriter);

                body.append("<p><strong>Message</strong>: ").append(ex.getMessage()).append("</p>");
                body.append("<h4>Stack Trace</h4>");
                body.append("<pre><code>");
                body.append(stringWriter.toString());
                body.append("</code></pre>");
            }
        }

        Multipart mp = new MimeMultipart();

        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(body.toString(), "text/html")));
        mp.addBodyPart(mbp);

        msg.setContent(mp);

        enqueue(msg);
    }

    @Override
    public void enqueue(MimeMessage msg) throws IOException,
                                                MessagingException {
        msg.setHeader("X-Mailer", mailerAgent);
        msg.setSentDate(new Date());
        if (msg.getFrom() == null) {
            try {
                msg.setFrom(conf.getString("mail.from"));
            } catch (Exception ex) {
            }
        }

        Files.createDirectories(queuePath);
        long mid;
        try {
            jt.update("insert into mail_queue (subject, created) values (?, current_timestamp())", msg.getSubject());
            mid = jt.queryForObject("call identity()", Long.class);
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof MessagingException) {
                throw (MessagingException) ex.getCause();
            }
            LOG.error(ex.getMessage(), ex);
            throw new IOException(ex);
        }

        Path p = queuePath.resolve(mid + ".eml.gz");
        if (Files.exists(p)) {
            LOG.warn("Overwriting existing message with id=" + mid);
        }

        try (OutputStream fos = Files.newOutputStream(p);
             GZIPOutputStream zos = new GZIPOutputStream(fos)) {
            LOG.info("Writing mail message: " + p.toString());
            msg.writeTo(zos);
        }
    }

    /**
     * Sends any unsent mail.
     *
     * This is the main task execution intended to be called by scheduled tasks.
     */
    @Override
    public void sendUnsent() {
        if (!conf.getBoolean("mail.queue.enabled")) {
            return;
        }
        LOG.debug("Processing mail queue...");
        try {
            jt.query("select * from mail_queue where sent is null and tries < ?", (rs, i)
                     -> new MailQueueEntry(rs.getLong("id"),
                                           rs.getString("subject"),
                                           toLocalDateTime(rs.getTimestamp("created")),
                                           toLocalDateTime(rs.getTimestamp("sent")),
                                           rs.getString("error"),
                                           rs.getLong("exception_id"),
                                           rs.getInt("tries")),
                     maxTries
            ).stream().forEach(n -> {
                LOG.info("Sending mail for : " + n.getId());

                Path path = queuePath.resolve(n.getId() + ".eml.gz").normalize();
                if (Files.exists(path)) {
                    MimeMessage msg = null;
                    try (InputStream s = Files.newInputStream(path);
                         GZIPInputStream zs = new GZIPInputStream(s)) {
                        msg = jms.createMimeMessage(zs);
                    } catch (Exception ex) {
                        LOG.error("Couldn't send message for : " + n.toString() + " -> " + ex.getMessage());
                        jt.update("update mail_queue set error = ?, tries = tries + 1 where id = ?", ex.getMessage(), n.getId());
                    }
                    if (msg != null) {
                        try {
                            jms.send(msg);
                            try {
                                Files.deleteIfExists(path);
                            } catch (Exception ioe) {
                            }
                            jt.update("update mail_queue set sent = current_timestamp(), tries = tries + 1 where id = ?", n.getId());
                        } catch (MailException ex) {
                            LOG.error("Couldn't send message for : " + n.toString() + " -> " + ex.getMessage());
                            jt.update("update mail_queue set error = ?, tries = tries + 1 where id = ?", ex.getMessage(), n.getId());
                        } catch (DataAccessException ex) {
                            LOG.error("Couldn't update sent status for message : " + n.toString() + " -> " + ex.getMessage());
                        }
                    }
                } else {
                    LOG.error("Couldn't email queue entry as file does not exist: " + path.toString());
                    jt.update("update mail_queue set error = ?, tries = tries + 1 where id = ?",
                              "File not found.",
                              n.getId());
                }
            });

            LOG.debug("Mail queue processing complete!");
        } catch (Exception ex) {
            LOG.error("Mail queue processing failed.", ex);
        }
    }

    @Override
    public List<MailQueueEntry> getUnsent() {
        return jt.query("select * from mail_queue where sent is null", (rs, i)
                        -> new MailQueueEntry(rs.getLong("id"),
                                              rs.getString("subject"),
                                              toLocalDateTime(rs.getTimestamp("created")),
                                              toLocalDateTime(rs.getTimestamp("sent")),
                                              rs.getString("error"),
                                              rs.getLong("exception_id"),
                                              rs.getInt("tries")));
    }

}
