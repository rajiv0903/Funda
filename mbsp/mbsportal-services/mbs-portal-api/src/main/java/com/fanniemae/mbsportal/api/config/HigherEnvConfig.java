/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;


/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Feb 7, 2018
 * @Time 11:04:23 AM com.fanniemae.mbsportal.api.config HigherEnvConfig.java
 * @Description: Added GatewayProxyTemplate
 */
@Configuration
@Import({ CDXApiClientConfig.class })
public class HigherEnvConfig {

  

}
