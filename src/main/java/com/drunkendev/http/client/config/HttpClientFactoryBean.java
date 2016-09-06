/*
 * HttpClientFactoryBean.java    Feb 11 2014, 17:14
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

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * Extends a {@link HttpClientBuilder} which provides bean access to the built {@link HttpClient}.
 *
 * @author  Brett Ryan
 * @since   1.1
 */
public class HttpClientFactoryBean
        extends HttpClientBuilder
        implements InitializingBean,
                   FactoryBean<HttpClient> {

    private HttpClient value;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.value = build();
    }

    @Override
    public HttpClient getObject() throws Exception {
        return value;
    }

    @Override
    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
