/*
 * LookupFactoryBean.java    Feb 11 2014, 22:30
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

import java.util.Map;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * Provides a bean access to creating a {@link Lookup} bean.
 *
 *<pre>{@code <bean class="com.drunkendev.http.client.config.LookupFactoryBean">
   <property name="lookupEntries">
     <map>
       <entry key="Basic">
         <bean class="org.apache.http.impl.auth.BasicSchemeFactory"/>
       </entry>
     </map>
   </property>
 </bean>}</pre>
 *
 * @author  Brett Ryan
 * @since   1.1
 */
public class LookupFactoryBean<T> implements FactoryBean<Lookup<T>>, InitializingBean {

    private Map<String, T> entries;
    private Registry<T> value;

    /**
     * Creates a new {@code AuthSchemeProviderLookupFactoryBean} instance.
     */
    public LookupFactoryBean() {
    }

    public void setLookupEntries(Map<String, T> entries) {
        this.entries = entries;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RegistryBuilder<T> builder = RegistryBuilder.<T>create();
        entries.forEach((k, v) -> builder.register(k, v));
        this.value = builder.build();
    }

    @Override
    public Lookup<T> getObject() throws Exception {
        return value;
    }

    @Override
    public Class<?> getObjectType() {
        return Lookup.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
