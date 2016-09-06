/*
 * BasicCredentialsProviderFactoryBean.java    Feb 11 2014, 15:39
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

import java.util.Collection;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.factory.FactoryBean;


/**
 * Factory provider for a {@link BasicCredentialsProvider} instance allowing XML
 * configuration of {@link BasicCredentialsProvider#setCredentials(org.apache.http.auth.AuthScope, org.apache.http.auth.Credentials) BasicCredentialsProvider#setCredentials}.
 *
 * <pre>{@code <bean id="credentialsProvider"
       class="com.drunkendev.http.client.config.BasicCredentialsProviderFactoryBean">
   <property name="authScopes">
     <list>
       <bean class="com.drunkendev.http.client.config.UsernamePasswordAuthScopeCredentials"
             p:host="service.example.com"
             p:username="username"
             p:password="password" />
     </list>
   </property>
 </bean>}</pre>
 *
 * @author  Brett Ryan
 * @since   1.1
 */
public class BasicCredentialsProviderFactoryBean implements FactoryBean<BasicCredentialsProvider> {

    private final BasicCredentialsProvider instance;

    /**
     * Creates a new {@code BasicCredentialsProviderFactory} instance.
     */
    public BasicCredentialsProviderFactoryBean() {
        instance = new BasicCredentialsProvider();
    }

    public void setAuthScopes(Collection<AuthScopeCredentials> entries) {
        entries.forEach(ent -> instance.setCredentials(ent.getAuthscope(),
                                                       ent.getCredentials()));
    }

    @Override
    public BasicCredentialsProvider getObject() throws Exception {
        return instance;
    }

    @Override
    public Class<?> getObjectType() {
        return BasicCredentialsProvider.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
