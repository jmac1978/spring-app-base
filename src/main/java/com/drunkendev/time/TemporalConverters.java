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
 * Methods for converting various temporal units.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class TemporalConverters {

    /**
     * Convert a {@link java.util.Date Date} to a {@link java.time.LocalDate LocalDate} with the system zone.
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     * @see     #toLocalDate(Date, ZoneId)
     */
    public static LocalDate toLocalDate(java.util.Date value) {
        return toLocalDate(value, ZoneId.systemDefault());
    }

    /**
     * Convert a {@link java.util.Date Date} to a {@link java.time.LocalDate LocalDate} with the given zone.
     *
     * @param   value
     *          Value to be converted.
     * @param   zoneId
     *          Zone to use to create the instant.
     * @return  Converted value or null if {@code value} was null.
     * @see     #toLocalDate(Date, ZoneId)
     * @since   1.1
     */
    public static LocalDate toLocalDate(java.util.Date value, ZoneId zoneId) {
        // NOTE: java.sql.Date does not support toInstant. To prevent an UnsupportedOperationException
        // do not use toInstant on dates.
        //return value == null ? null
        //       : LocalDateTime.ofInstant(value.toInstant(), zoneId).toLocalDate();
        return value == null ? null
               : LocalDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()),
                                         zoneId).toLocalDate();
    }

    /**
     * Convert a {@link java.util.Date Date} to a {@link java.time.LocalDateTime LocalDateTime} with the system zone.
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static LocalDateTime toLocalDateTime(java.util.Date value) {
        return toLocalDateTime(value, ZoneId.systemDefault());
    }

    /**
     * Convert a {@link java.util.Date Date} to a {@link java.time.LocalDateTime LocalDateTime} with the given zone.
     *
     * @param   value
     *          Value to be converted.
     * @param   zoneId
     *          Zone to use to create the instant.
     * @return  Converted value or null if {@code value} was null.
     * @since   1.1
     */
    public static LocalDateTime toLocalDateTime(java.util.Date value, ZoneId zoneId) {
        // NOTE: java.sql.Date does not support toInstant. To prevent an UnsupportedOperationException
        // do not use toInstant on dates.
        //return value == null ? null : LocalDateTime.ofInstant(value.toInstant(), zoneId);
        return value == null ? null
               : LocalDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()),
                                         zoneId);
    }

    /**
     * Convert a {@link java.sql.Timestamp Timestamp} to a {@link java.time.ZonedDateTime ZonedDateTime} with the system zone.
     *
     * <h3>Note</h3>
     *
     * If your databases timezone is the same as the current system timezone a call such as the following is sufficient.
     *
     * <pre>{@code
 ZonedDateTime toZonedDateTime(rs.getTimestamp("FIELD_NAME"));
}</pre>
     *
     * However, if your session is not running in the same time-zone as the database
     * you may need to specify the calendar the database is stored as.
     *
     * <pre>{@code
 Calendar dbCal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
 ZonedDateTime toZonedDateTime(rs.getTimestamp("FIELD_NAME", dbCal));
}</pre>
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     *
     * @since   1.1
     * @see     #toZonedDateTime(Timestamp, ZoneId)
     */
    public static ZonedDateTime toZonedDateTime(Timestamp value) {
        return toZonedDateTime(value, ZoneId.systemDefault());
    }

    /**
     * Convert a {@link java.sql.Timestamp Timestamp} to a {@link java.time.ZonedDateTime ZonedDateTime} with provided zone.
     *
     * <h3>Usage</h3>
     *
     * This method may be desired where the users session zone is desired.
     *
     * <pre>{@code
 public ModelAndView controllerMethod(ZoneId userZoneId) {
     Calendar dbCal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
     ZonedDateTime zdt = toZonedDateTime(rs.getTimestamp("FIELD_NAME", dbCal), userZoneId);
 }
}</pre>
     *
     * <h3>Note</h3>
     *
     * If your databases timezone is the same as the current system timezone a call such as the following is sufficient.
     *
     * <pre>{@code
 ZonedDateTime toZonedDateTime(rs.getTimestamp("FIELD_NAME"));
}</pre>
     *
     * However, if your session is not running in the same time-zone as the database
     * you may need to specify the calendar the database is stored as.
     *
     * <pre>{@code
 Calendar dbCal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
 ZonedDateTime toZonedDateTime(rs.getTimestamp("FIELD_NAME", dbCal));
}</pre>
     *
     * @param   value
     *          Value to be converted.
     * @param   zoneId
     *          Zone to use to create the instant.
     * @return  Converted value or null if {@code value} was null.
     *
     * @since   1.1
     * @see     #toZonedDateTime(Timestamp)
     */
    public static ZonedDateTime toZonedDateTime(Timestamp value, ZoneId zoneId) {
        return value == null ? null
               : ZonedDateTime.ofInstant(value.toInstant(), ZoneOffset.UTC)
                        .withZoneSameInstant(zoneId);
    }

    /**
     * Convert a {@link java.sql.Timestamp Timestamp} to a {@link java.time.LocalDateTime LocalDateTime} with the system zone.
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static LocalDateTime toLocalDateTime(Timestamp value) {
        return value == null ? null : value.toLocalDateTime();
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static java.sql.Date toSqlDate(LocalDateTime value) {
        return value == null ? null : new java.sql.Date(value.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static java.sql.Date toSqlDate(ZonedDateTime value) {
        return value == null ? null : new java.sql.Date(value.toInstant().toEpochMilli());
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static java.sql.Date toSqlDate(LocalDate value) {
        return value == null ? null : java.sql.Date.valueOf(value);
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static Timestamp toTimestamp(LocalDateTime value) {
        return value == null ? null : new Timestamp(value.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static Timestamp toTimestamp(ZonedDateTime value) {
        return value == null ? null : new Timestamp(value.toInstant().toEpochMilli());
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static GregorianCalendar toGregorianCalendar(ZonedDateTime value) {
        return value == null ? null : GregorianCalendar.from(value);
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static GregorianCalendar toGregorianCalendar(LocalDateTime value) {
        return toGregorianCalendar(value, ZoneId.systemDefault());
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @param   zoneId
     *          Zone to use to create the instant.
     * @return  Converted value or null if {@code value} was null.
     * @since   1.1
     */
    public static GregorianCalendar toGregorianCalendar(LocalDateTime value, ZoneId zoneId) {
        return value == null ? null : toGregorianCalendar(ZonedDateTime.of(value, zoneId));
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @return  Converted value or null if {@code value} was null.
     */
    public static GregorianCalendar toGregorianCalendar(LocalDate value) {
        return toGregorianCalendar(value, ZoneId.systemDefault());
    }

    /**
     *
     *
     * @param   value
     *          Value to be converted.
     * @param   zoneId
     *          Zone to use to create the instant.
     * @return  Converted value or null if {@code value} was null.
     * @since   1.1
     */
    public static GregorianCalendar toGregorianCalendar(LocalDate value, ZoneId zoneId) {
        return value == null ? null : toGregorianCalendar(ZonedDateTime.of(value.atStartOfDay(), zoneId));
    }

}
