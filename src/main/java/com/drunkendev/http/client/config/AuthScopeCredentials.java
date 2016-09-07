/*
 * AuthScopeCredentials.java    Feb 11 2014, 15:39
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

package com.drunkendev.http.client.config;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;


/**
 * Interface allowing an {@link AuthScope} and {@link Credentials} pair to be provided.
 *
 * INtended to be used with an {@link BasicCredentialsProviderFactoryBean} with
 * spring XML configuration.
 *
 * @author  Brett Ryan
 * @since   1.1
 * @see     BasicCredentialsProviderFactoryBean
 */
public interface AuthScopeCredentials {

    AuthScope getAuthscope();

    Credentials getCredentials();

}
