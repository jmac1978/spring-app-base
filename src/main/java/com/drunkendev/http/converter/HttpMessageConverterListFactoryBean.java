/*
 * HttpMessageConverterListFactoryBean.java    Feb 11 2014, 21:48
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

package com.drunkendev.http.converter;

import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;


/**
 * Provides bean access to the list of {@link HttpMessageConverter}s that have been registered
 * within the spring {@link ApplicationContext}.
 *
 * @author  Brett Ryan
 * @since   1.1
 */
public class HttpMessageConverterListFactoryBean
        implements InitializingBean,
                   ApplicationContextAware,
                   FactoryBean<List<HttpMessageConverter<?>>> {

    private RequestMappingHandlerAdapter rmha;
    private List<HttpMessageConverter<?>> converters;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.rmha = ac.getBean(RequestMappingHandlerAdapter.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.converters = rmha.getMessageConverters();
    }

    @Override
    public List<HttpMessageConverter<?>> getObject() throws Exception {
        return converters;
    }

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
