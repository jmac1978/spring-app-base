/*
 * package-info.java    Sep 6 2016, 23:41
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

/**
 * Provides support for <a href="https://hc.apache.org">Apache HttpComponents</a>
 * within Spring applications.
 *
 * <h3>Example</h3>
 *
 * Following is an example XML configuration that allows configuring a list of
 * basic authentications for a credentials provider which will then be used within
 * a {@link org.apache.http.client.HttpClient HttpClient} bean.
 *
 * {@code
  <bean id="credentialsProvider"
        class="com.drunkendev.http.client.config.BasicCredentialsProviderFactoryBean">
    <property name="authScopes">
      <list>
        <bean class="com.drunkendev.http.client.config.UsernamePasswordAuthScopeCredentials"
              p:host="service.example.com"
              p:username="username"
              p:password="password" />
      </list>
    </property>
  </bean>

  <bean id="httpClient" class="com.drunkendev.http.client.config.HttpClientFactoryBean">
    <property name="defaultCredentialsProvider" ref="credentialsProvider"/>
    <property name="defaultAuthSchemeRegistry">
      <bean class="com.drunkendev.http.client.config.LookupFactoryBean">
        <property name="lookupEntries">
          <map>
            <entry key="Basic">
              <bean class="org.apache.http.impl.auth.BasicSchemeFactory"/>
            </entry>
          </map>
        </property>
      </bean>
    </property>
  </bean>

  <bean id="clientHttpRequestFactory"
        class="org.springframework.http.client.HttpComponentsClientHttpRequestFactory"
        p:httpClient-ref="httpClient"/>

  <bean id="messageConverterList"
        class="com.drunkendev.http.converter.HttpMessageConverterListFactoryBean"/>

  <bean class="org.springframework.web.client.RestTemplate">
    <constructor-arg ref="clientHttpRequestFactory"/>
    <property name="messageConverters" ref="messageConverterList"/>
  </bean>}
 *
 * @since   1.1
 */
package com.drunkendev.http.client.config;
