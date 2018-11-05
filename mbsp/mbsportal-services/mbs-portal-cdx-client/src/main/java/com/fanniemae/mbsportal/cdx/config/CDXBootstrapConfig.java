/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.cdx.config;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;

@Configuration
@Import({ CDXApiClientConfig.class })
@Profile({ "default", "LOCAL", "DEV1", "DEV2", "ACPT1", "ACPT2", "PERF", "TEST1", "PROD1", "PROD2" })
public class CDXBootstrapConfig {

    /**
     * 
     * cDXApiClientConfig CDXApiClientConfig
     */
    @Autowired
    CDXApiClientConfig cDXApiClientConfig;

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

}
