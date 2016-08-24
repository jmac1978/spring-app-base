/*
 * SimpleUserlogService.java    Oct 9 2013, 17:14
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

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import static com.drunkendev.time.TemporalConverters.toLocalDate;
import static com.drunkendev.time.TemporalConverters.toLocalDateTime;


/**
 * {@link UserlogService} implementation that writes to a user_history table.
 *
 * @author  Brett Ryan
 */
public class SimpleUserlogService implements UserlogService {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleUserlogService.class);
//    private final MailQueue mq;
    private final JdbcTemplate jt;

    /**
     * Creates a new {@code UserlogService} instance.
     *
     * @param   jt
     *          {@link JdbcTemplate} containing user_history table.
     */
    public SimpleUserlogService(JdbcTemplate jt) {
//        this.mq = mq;
        this.jt = jt;
    }

    @Override
    public void add(HttpServletRequest request,
                    HttpServletResponse response,
                    Object handler,
                    Exception ex) {
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
            add(user,
                actingAs,
                method,
                path,
                query,
                contentType,
                agent,
                address,
                ex);
        }
    }

    @Override
    public void add(String user,
                    String actingAs,
                    String method,
                    String url,
                    String query,
                    String contentType,
                    String userAgent,
                    String remoteAddress,
                    Exception ex) {
        LOG.debug("Logging user request: user:{} actingAs:{} method:{} url:{} query:{} contentType:{} userAgent:{}",
                  user,
                  actingAs,
                  method,
                  url,
                  query,
                  contentType,
                  userAgent);
        Long exid = null;
        if (ex != null) {
            try {
                jt.execute("insert into exceptions (" +
                           "  message" +
                           " ,root_type" +
                           " ,trace" +
                           ") values (?, ?, ?)",
                           (PreparedStatement ps) -> {
                               ps.setString(1, ex.getMessage());
                               ps.setString(2, ex.getClass().getName());

                               StringWriter errors = new StringWriter();
                               ex.printStackTrace(new PrintWriter(errors));

                               StringReader sr = new StringReader(errors.toString());
                               ps.setCharacterStream(3, sr);
                               ps.executeUpdate();
                               return null;
                           });
                exid = jt.queryForObject("call identity()", Long.class);
            } catch (RuntimeException e) {
                LOG.error("Could not insert exception into userlog. " + e.getMessage());
            }
        }
        jt.update("insert into user_history (" +
                  "  request_date" +
                  " ,username" +
                  " ,sudo_username" +
                  " ,method" +
                  " ,url" +
                  " ,query" +
                  " ,content_type" +
                  " ,user_agent" +
                  " ,remote_address" +
                  " ,exception_id" +
                  ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                  new Date(),
                  StringUtils.left(user, 12),
                  StringUtils.left(actingAs, 12),
                  StringUtils.left(method, 8),
                  StringUtils.abbreviate(url, 100),
                  StringUtils.left(query, 250),
                  StringUtils.left(contentType, 100),
                  StringUtils.left(userAgent, 250),
                  StringUtils.abbreviate(remoteAddress, 45),
                  exid);
//        if (ex != null && containsIgnoreCase(ex.getMessage(), url)) {
//            try {
//                StringBuilder m = new StringBuilder();
//                m.append("An error has been raised by ").append(user);
//                if (isNotBlank(actingAs)) {
//                    m.append(" who is impersonating ").append(user);
//                }
//                m.append(".");
//                mq.sendSystemError("Exception in user request.",
//                                   m.toString(),
//                                   url,
//                                   ex);
//            } catch (MessagingException | IOException ex1) {
//                LOG.error("Couldn't send system error message: {}", ex1.getMessage(), ex1);
//            }
//        }
    }

    @Override
    public List<UserUsageSummary> getUserUsageSummary() {
        return jt.query("select username" +
                        "      ,min(request_date) first_request" +
                        "      ,max(request_date) last_request" +
                        "      ,count(*) c" +
                        "  from user_history" +
                        " where url not like '/api/%'" +
                        "   and url not like '/error/%'" +
                        " group by username" +
                        " order by username",
                        (rs, i) -> new UserUsageSummary(
                                rs.getString("username"),
                                toLocalDateTime(rs.getTimestamp("first_request")),
                                toLocalDateTime(rs.getTimestamp("last_request")),
                                rs.getInt("c")));
    }

    @Override
    public List<SudoSummary> getSudoSummary() {
        return jt.query("select username" +
                        "      ,sudo_username" +
                        "      ,convert(request_date, date) dt" +
                        "      ,count(*) c" +
                        "  from user_history" +
                        " where sudo_username is not null" +
                        " group by username" +
                        "         ,sudo_username" +
                        "         ,dt" +
                        " order by dt desc" +
                        "            ,username",
                        (rs, i) -> new SudoSummary(
                                rs.getString("username"),
                                rs.getString("sudo_username"),
                                toLocalDate(rs.getTimestamp("dt")),
                                rs.getInt("c")));
    }

    @Override
    public List<ReadableUserAgent> getUserAgentUsage() {
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        return jt.query("select distinct user_agent, count(*) ct from USER_HISTORY group by user_agent",
                        (rs, i) -> parser.parse(rs.getString("user_agent")));
    }

}
