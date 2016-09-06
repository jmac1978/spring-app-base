/*
 * UsernamePasswordAuthScopeCredentials.java    Aug 29 2016, 20:58
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
import org.apache.http.auth.UsernamePasswordCredentials;


/**
 * {@link AuthScopeCredentials} implementation for {@link UsernamePasswordCredentials}
 *
 * @author  Brett Ryan
 * @since   1.1
 */
public class UsernamePasswordAuthScopeCredentials implements AuthScopeCredentials {

    private String host = null;
    private int port = -1;
    private String realm = null;
    private String scheme = null;
    private String username;
    private String password;

    public UsernamePasswordAuthScopeCredentials() {
    }

    @Override
    public AuthScope getAuthscope() {
        return new AuthScope(host, port, realm, scheme);
    }

    @Override
    public Credentials getCredentials() {
        return new UsernamePasswordCredentials(username, password);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
