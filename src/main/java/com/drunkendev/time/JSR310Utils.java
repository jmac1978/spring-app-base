/*
 * JSR310Utils.java    Jul 24 2014, 13:10
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

package com.drunkendev.time;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;


/**
 * JSR-310 support for JSTL usage.
 *
 * @author  Brett Ryan
 * @since   1.0
 */
public class JSR310Utils {

    /**
     * Returns the default string value of a temporal by calling its {@code toString()} method.
     *
     * @param   value
     *          Value to format.
     * @return  String representation of temporal value.
     */
    public static String format(Temporal value) {
        return value == null ? null : value.toString();
    }

    /**
     * Returns the formatted temporal in the pattern formed by {@code pattern}.
     *
     * Result of this method is by calling {@code DateTimeFormatter.ofPattern(pattern).format(value)}
     *
     * @param   value
     *          Value to format.
     * @param   pattern
     *          Pattern to return temporal in.
     *
     * @return  Formatted temporal value.
     *
     * @see     DateTimeFormatter#ofPattern(String)
     */
    public static String format(Temporal value, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(value);
    }

    /**
     * Returns true if the value of {@code lhs} is after {@code rhs}.
     *
     * @param   lhs
     *          Value to check.
     * @param   rhs
     *          Value to check.
     * @return  True if {@code lhs} is after {@code rhs}.
     */
    public static boolean after(LocalDate lhs, LocalDate rhs) {
        if (lhs == rhs) {
            return false;
        }
        if (lhs == null) {
            return false;
        }
        if (rhs == null) {
            return true;
        }
        return lhs.isAfter(rhs);
    }

    /**
     * Returns true if the value of {@code lhs} is after or equal to {@code rhs}.
     *
     * @param   lhs
     *          Value to check.
     * @param   rhs
     *          Value to check.
     * @return  True if {@code lhs} is after or equal to {@code rhs}.
     */
    public static boolean afterOrEqual(LocalDate lhs, LocalDate rhs) {
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null) {
            return false;
        }
        if (rhs == null) {
            return true;
        }
        return lhs.isAfter(rhs) || lhs.equals(rhs);
    }

    /**
     * Returns true if the value of {@code lhs} is before {@code rhs}.
     *
     * @param   lhs
     *          Value to check.
     * @param   rhs
     *          Value to check.
     * @return  True if {@code lhs} is before {@code rhs}.
     */
    public static boolean before(LocalDate lhs, LocalDate rhs) {
        if (lhs == rhs) {
            return false;
        }
        if (lhs == null) {
            return false;
        }
        if (rhs == null) {
            return true;
        }
        return lhs.isBefore(rhs);
    }

    /**
     * Returns true if the value of {@code lhs} is before or equal to {@code rhs}.
     *
     * @param   lhs
     *          Value to check.
     * @param   rhs
     *          Value to check.
     * @return  True if {@code lhs} is before or equal to {@code rhs}.
     */
    public static boolean beforeOrEqual(LocalDate lhs, LocalDate rhs) {
        if (lhs == rhs) {
            return true;
        }
        if (lhs == null) {
            return true;
        }
        if (rhs == null) {
            return false;
        }
        return lhs.isBefore(rhs) || lhs.equals(rhs);
    }

}
