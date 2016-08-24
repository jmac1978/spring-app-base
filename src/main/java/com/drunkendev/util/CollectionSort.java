/*
 * CollectionSort.java    Oct 8 2013, 18:24
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

package com.drunkendev.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;


/**
 * Collection sorting methods for use in JSTL.
 *
 * @author  Brett Ryan
 */
public final class CollectionSort {

    /**
     * Returns a new collection in the default sort order.
     *
     * @param   <T>
     *          Type of element within collection.
     * @param   collection
     *          Collection to sort.
     * @return  New collection sorted in natural order.
     */
    public static <T extends Comparable<? super T>> Collection<T> sort(Collection<T> collection) {
        return collection.stream().sorted().collect(toList());
    }

    public static Collection<GrantedAuthority> sortAuthorities(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .sorted(comparing(GrantedAuthority::getAuthority)).collect(toList());
    }

    /**
     * Returns a new collection in the default reversed sort order.
     *
     * @param   <T>
     *          Type of element within collection.
     * @param   collection
     *          Collection to sort.
     * @return  New collection sorted in natural order.
     */
    public static <T extends Comparable<? super T>> Collection<T> sortDescending(Collection<T> collection) {
        return collection.stream()
                .sorted(Comparator.reverseOrder()).collect(toList());
    }

    /**
     * Returns a new collection of the original in reversed order.
     *
     * @param   collection
     *          Collection to reverse.
     * @return  New list in reversed order of the original collection.
     */
    @SuppressWarnings("unchecked")
    public static Collection reverse(Collection collection) {
        List n = new ArrayList(collection);
        Collections.reverse(n);
        return n;
    }

}
