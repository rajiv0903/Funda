/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *
 */

package com.fanniemae.mbsportal.pu.adapter.config;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.api.ESBClientCredentials;
import com.fanniemae.all.messaging.api.ESBClientException;
import com.fanniemae.all.messaging.api.ESBClientFactory;
import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate;

/**
 * @author g8uaxt Created on 11/21/2017.
 */

/**
 * 
 * @author g8upjv
 * @Version:
 * @Date June 5 2018
 * @Time 11:07:59 AM com.fanniemae.mbsportal.pu.adapter.config PUAdapterConfig.java
 * @Description: 
 */
@Configuration
@Profile({ "DEV1", "DEV2", "ACPT1", "ACPT2","PERF", "TEST1", "PROD1", "PROD2" })
public class PUAdapterConfig {
    
    /**
     * 
     * LOGGER Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PUAdapterConfig.class);
    
    /**
     * 
     * esbUserId String
     */
    @Value("${mbsp.systemUserId}")
    private String esbUserId;
    
    /**
     * 
     * appCode String
     */
    @Value("${mbsp.appCd}")
    private String appCode;
    
    /**
     * 
     * env String
     */
    @Value("${mbsp.envCd}")
    private String env;
    
    /**
     * 
     * objRef String
     */
    @Value("${mbsp.epvRefId}")
    private String objRef;
    
    /**
     * 
     * awsVref String
     */
    @Value("${mbsp.awsVref}")
    private String awsVref;
    
    /**
     * 
     * cDXApiClientConfig CDXApiClientConfig
     */
    @Autowired
    CDXApiClientConfig cDXApiClientConfig;
    
    /**
     * 
     * esbUserPwd String
     */
    private String esbUserPwd;
    
    /**
     * 
     * EFSSTrustStorePassword String
     */
    private String EFSSTrustStorePassword;
    
    /**
     * 
     * 
     * @return EpvConnector
     */
    @Bean("epvConnector")
    public EpvConnector epvConnector() {
        LOGGER.info("epvConnector bean initializing");
        LOGGER.debug("MBSP-PU:Setting System Properties: AWS_VREF {}", awsVref);
        System.setProperty("AWS_VREF", awsVref);
        if(StringUtils.isBlank(awsVref)){
            LOGGER.error("awsVref value is blank.");
            System.exit(0);
        }
        EpvConnector epvConnector = new EpvConnector();
        try {
            epvConnector.initialize(appCode, env, objRef);
        } catch (MBSBaseException e) {
            LOGGER.error("epvConnector bean initialization failed", e.getRootException());
        }
        LOGGER.info("MBSP-PU:epvConnector bean initialized");
        return epvConnector;
    }
    
    /**
     *
     * Returning the EpvConnector with initialization params for local environment
     *
     * @return EpvConnector
     */
    @Bean (name="epvConnector")
    @Profile({ "default","LOCAL", "test" })
    public EpvConnector epvConnectorLocal() {
        LOGGER.debug(String.format("epvConnector initializing with appCode=%s env=%s tsObjRefId=%s", appCode, env,
                objRef));
        EpvConnector epvConnector = new EpvConnector();
        LOGGER.info("epvConnector bean initialized");
        return epvConnector;
    }
    
    /**
     * 
     * 
     * @return ESBClient
     * @throws ESBClientException
     */
    @Bean
    public ESBClient esbClient() throws ESBClientException {
        ESBClient esbClient = ESBClientFactory.createESBClient(esbClientCredentials());
        LOGGER.info("esbClient bean initialized");
        return esbClient;
    }
    
    /**
     * 
     * 
     * @return ESBClientCredentials
     */
    @Bean
    @DependsOn("epvConnector")
    ESBClientCredentials esbClientCredentials() {
        esbUserPwd = epvConnector().getValutPwd();
        LOGGER.debug("esbClientCredentials with esbUserId: " + esbUserId);
        ESBClientCredentials esbClientCredentials = new MBSEsbClientCredentials(esbUserId, esbUserPwd,
                EFSSTrustStorePassword);
        return esbClientCredentials;
    }

    /**
     * 
     * Bean Definition for GatewayProxyTemplate
     * CMMBSSTA01-1033: (Tech) TradeService - Digital Channel - Network Connectivity Issue
     * 
     * @return GatewayProxyTemplate
     */
    @Bean
    public GatewayProxyTemplate gatewayProxyTemplate() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        InetSocketAddress address = new InetSocketAddress(cDXApiClientConfig.getProxyHost(),
                cDXApiClientConfig.getProxyPort());
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        factory.setProxy(proxy);

        GatewayProxyTemplate gatewayProxyTemplate = new GatewayProxyTemplate(factory);
        return gatewayProxyTemplate;
    }
    
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
    @DependsOn("httpComponentsClientHttpRequestFactory")
    public MBSRestInternalTemplate mbsRestInternalTemplate()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        
        MBSRestInternalTemplate mbsRestInternalTemplate = new MBSRestInternalTemplate(httpComponentsClientHttpRequestFactory());
        return mbsRestInternalTemplate;
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
    @DependsOn("requestConfig")
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
        connMgr.setMaxTotal(20);
        b.setConnectionManager(connMgr);
        CloseableHttpClient httpClient = b.setDefaultRequestConfig(requestConfig()).build();
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpClient);
        return httpComponentsClientHttpRequestFactory;
    }

    /**
     * 
     * requestConfig
     * 
     * @return RequestConfig
     */
    @Bean(name="requestConfig")
    public RequestConfig requestConfig() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(120000).setConnectTimeout(120000)
                .setSocketTimeout(120000).build();
        return requestConfig;
    }
    
}
