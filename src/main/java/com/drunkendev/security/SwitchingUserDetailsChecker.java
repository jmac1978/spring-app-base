/*
 * SwitchingUserDetailsChecker.java    May 18 2017, 04:41
 *
 * Copyright 2017 Drunken Dev.
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

package com.drunkendev.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.stream.Collectors.toSet;


/**
 * Provides a user checker that will fail if the user switching to exists in
 * provided authorities that the current authentication does not.
 *
 * @author  Brett Ryan
 */
public class SwitchingUserDetailsChecker extends AccountStatusUserDetailsChecker {

    private Collection<GrantedAuthority> auths;
    private String errorMessage =
            "You may not switch to accounts with administrator levels higher than you.";

    /**
     *
     * @param   userDetails
     */
    @Override
    public void check(UserDetails userDetails) {
        super.check(userDetails);

        if (auths == null) {
            return;
        }

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        if (auths.stream()
                .anyMatch(a -> userDetails.getAuthorities().contains(a) &&
                               !currentAuth.getAuthorities().contains(a))) {
            throw new InsufficientAuthenticationException(errorMessage);
        }
    }

    public void setAdminRoles(Collection<String> authorities) {
        if (auths != null) {
            throw new IllegalStateException("May not reinitialise authorities.");
        }
        auths = Collections.unmodifiableSet(authorities.stream().map(n -> new SimpleGrantedAuthority(n)).collect(toSet()));
    }

    public void setAdminAuthorities(Collection<GrantedAuthority> authorities) {
        if (auths != null) {
            throw new IllegalStateException("May not reinitialise authorities.");
        }
        auths = Collections.unmodifiableSet(authorities.stream().collect(toSet()));
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
