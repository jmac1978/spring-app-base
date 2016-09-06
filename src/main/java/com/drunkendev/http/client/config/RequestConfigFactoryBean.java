/*
 * RequestConfigFactoryBean.java    Feb 12 2014, 01:37
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

import java.net.InetAddress;
import java.util.Collection;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * Provides a {@link RequestConfig} bean instance.
 *
 * This implementation uses the {@link RequestConfig#custom()} builder and sets
 * all properties specified here against that builder instance.
 *
 * @author  Brett Ryan
 * @since   1.1
 */
public class RequestConfigFactoryBean implements InitializingBean,
                                                 FactoryBean<RequestConfig> {


    private RequestConfig requestConfig;

    private boolean authenticationEnabled;
    private boolean circularRedirectsAllowed;
    private int connectTimeout;
    private int connectionRequestTimeout;
    private boolean contentCompressionEnabled;
    private String cookieSpec;
    private boolean expectContinueEnabled;
    private InetAddress localAddress;
    private int maxRedirects;
    private HttpHost proxy;
    private Collection<String> proxyPreferredAuthSchemes;
    private boolean redirectsEnabled;
    private boolean relativeRedirectsAllowed;
    private int socketTimeout;
    private Collection<String> targetPreferredAuthSchemes;

    /**
     * Creates a new {@code RequestConfigFactoryBean} instance.
     *
     * Default values have been pulled directly from the {@link org.apache.http.client.config.RequestConfig.Builder RequestConfig.Builder}
     * main private constructor.
     */
    public RequestConfigFactoryBean() {
        this.redirectsEnabled = true;
        this.maxRedirects = 50;
        this.relativeRedirectsAllowed = true;
        this.authenticationEnabled = true;
        this.connectionRequestTimeout = -1;
        this.connectTimeout = -1;
        this.socketTimeout = -1;
        this.contentCompressionEnabled = true;
    }

    @Override
    public Class<?> getObjectType() {
        return RequestConfig.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public boolean isAuthenticationEnabled() {
        return authenticationEnabled;
    }

    public void setAuthenticationEnabled(boolean authenticationEnabled) {
        this.authenticationEnabled = authenticationEnabled;
    }

    public boolean isCircularRedirectsAllowed() {
        return circularRedirectsAllowed;
    }

    public void setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
        this.circularRedirectsAllowed = circularRedirectsAllowed;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public boolean isContentCompressionEnabled() {
        return contentCompressionEnabled;
    }

    public void setContentCompressionEnabled(boolean contentCompressionEnabled) {
        this.contentCompressionEnabled = contentCompressionEnabled;
    }

    public String getCookieSpec() {
        return cookieSpec;
    }

    public void setCookieSpec(String cookieSpec) {
        this.cookieSpec = cookieSpec;
    }

    public boolean isExpectContinueEnabled() {
        return expectContinueEnabled;
    }

    public void setExpectContinueEnabled(boolean expectContinueEnabled) {
        this.expectContinueEnabled = expectContinueEnabled;
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public HttpHost getProxy() {
        return proxy;
    }

    public void setProxy(HttpHost proxy) {
        this.proxy = proxy;
    }

    public Collection<String> getProxyPreferredAuthSchemes() {
        return proxyPreferredAuthSchemes;
    }

    public void setProxyPreferredAuthSchemes(Collection<String> proxyPreferredAuthSchemes) {
        this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
    }

    public boolean isRedirectsEnabled() {
        return redirectsEnabled;
    }

    public void setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
    }

    public boolean isRelativeRedirectsAllowed() {
        return relativeRedirectsAllowed;
    }

    public void setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
        this.relativeRedirectsAllowed = relativeRedirectsAllowed;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Collection<String> getTargetPreferredAuthSchemes() {
        return targetPreferredAuthSchemes;
    }

    public void setTargetPreferredAuthSchemes(Collection<String> targetPreferredAuthSchemes) {
        this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.requestConfig = RequestConfig.custom()
                .setAuthenticationEnabled(authenticationEnabled)
                .setCircularRedirectsAllowed(circularRedirectsAllowed)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setContentCompressionEnabled(contentCompressionEnabled)
                .setCookieSpec(cookieSpec)
                .setExpectContinueEnabled(expectContinueEnabled)
                .setLocalAddress(localAddress)
                .setMaxRedirects(maxRedirects)
                .setProxy(proxy)
                .setProxyPreferredAuthSchemes(proxyPreferredAuthSchemes)
                .setRedirectsEnabled(redirectsEnabled)
                .setRelativeRedirectsAllowed(relativeRedirectsAllowed)
                .setSocketTimeout(socketTimeout)
                .setTargetPreferredAuthSchemes(targetPreferredAuthSchemes)
                .build();
    }

    @Override
    public RequestConfig getObject() throws Exception {
        return requestConfig;
    }

}
