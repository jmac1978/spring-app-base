/*
 * TemporalConverters.java    Jul 28 2016, 13:52
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

package com.drunkendev.time;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;


/**
 *
 * @author  Brett Ryan
 */
public class TemporalConverters {

    public static LocalDate toLocalDate(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
                                       ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
                                       ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(Timestamp date) {
        if (date == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
                                       ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(Timestamp date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDateTime();
    }

    public static java.sql.Date toSqlDate(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        java.util.Date d = java.util.Date.from(ldt.toInstant(ZoneOffset.UTC));
        return new java.sql.Date(d.getTime());
    }

    public static java.sql.Date toSqlDate(ZonedDateTime zdt) {
        if (zdt == null) {
            return null;
        }
        java.util.Date d = java.util.Date.from(zdt.toInstant());
        return new java.sql.Date(d.getTime());
    }

    public static java.sql.Date toSqlDate(LocalDate ld) {
        return ld == null ? null : java.sql.Date.valueOf(ld);
    }

    public static Timestamp toTimestamp(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        java.util.Date d = java.util.Date.from(ldt.toInstant(ZoneOffset.UTC));
        return new Timestamp(d.getTime());
    }

    public static Timestamp toTimestamp(ZonedDateTime zdt) {
        if (zdt == null) {
            return null;
        }
        java.util.Date d = java.util.Date.from(zdt.toInstant());
        return new Timestamp(d.getTime());
    }

    public static GregorianCalendar toGregorianCalendar(ZonedDateTime zdt) {
        if (zdt == null) {
            return null;
        }
        return GregorianCalendar.from(zdt);
    }

    public static GregorianCalendar toGregorianCalendar(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return toGregorianCalendar(ZonedDateTime.of(ldt, ZoneId.systemDefault()));
    }

    public static GregorianCalendar toGregorianCalendar(LocalDate val) {
        if (val == null) {
            return null;
        }
        return toGregorianCalendar(ZonedDateTime.of(val.atStartOfDay(), ZoneId.systemDefault()));
    }

}
