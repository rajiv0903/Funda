/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import com.fanniemae.mbsportal.api.utils.MBSThreadPoolTaskExecutor;
import com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate;

/**
 * @author g8uaxt Created on 10/19/2017.
 */

/**
 *
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 28, 2017
 * @Time 11:07:59 AM com.fanniemae.mbsportal.api.config BootstrapConfig.java
 * @Description: For Blue/ Green - Added Profile
 */

@Configuration
@Profile({ "default", "LOCAL", "DEV1", "DEV2", "ACPT1", "ACPT2", "PERF", "TEST1", "PROD1", "PROD2" })
public class LowerEnvConfig {
    
   
    /**
     * 
     * MBS Rest template without proxy for internal use
     * 
     * @return MBSRestTemplate
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */

    @Bean
    public MBSRestInternalTemplate mbsRestInternalTemplate()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        
        MBSRestInternalTemplate mbsRestInternalTemplate = new MBSRestInternalTemplate(httpComponentsClientHttpRequestFactory());
        return mbsRestInternalTemplate;
    }
    
    /**
     * default bean to use
     * @return
     */
    //@Bean
    public MBSThreadPoolTaskExecutor mbsThreadPoolTaskExecutor() {
        MBSThreadPoolTaskExecutor mbsThreadPoolTaskExecutor = new MBSThreadPoolTaskExecutor();
        mbsThreadPoolTaskExecutor.initialize();
        return mbsThreadPoolTaskExecutor;
    }
    
    /**
     * 
     * DMZ HTTP Client Factory with SSL 
     * 
     * @return HttpComponentsClientHttpRequestFactory
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    @SuppressWarnings({ "deprecation"})
    @Bean(name="httpComponentsClientHttpRequestFactory")
    public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        //To ignore SSL for DMZ
        HttpClientBuilder b = HttpClientBuilder.create();
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();
        b.setSslcontext(sslContext);
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory)
                .build();
        //Allows Multi-Thread use
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(100);
        b.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = b.setDefaultRequestConfig(requestConfig()).build();
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpClient);
        return httpComponentsClientHttpRequestFactory;
    }

    /**
     * requestConfig
     * 
     * @return RequestConfig
     */
    @Bean
    public RequestConfig requestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(120000).setConnectTimeout(120000)
                .setSocketTimeout(120000).build();
        return requestConfig;
    }
    

}