/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.cdx.template;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 8, 2018
 * @File: com.fanniemae.mbsportal.utils.template.GatewayProxyTemplate.java 
 * @Revision: 
 * @Description: GatewayProxyTemplate.java
 */
public class GatewayProxyTemplate extends RestTemplate {

    /**
     * 
     * Constructor to create a Rest Template with Proxy Details
     * 
     * @param simpleClientHttpRequestFactory
     *            Input for SimpleClient HTTP Request Factory
     */
    public GatewayProxyTemplate(SimpleClientHttpRequestFactory simpleClientHttpRequestFactory) {
        super.setRequestFactory(simpleClientHttpRequestFactory);
    }
}
