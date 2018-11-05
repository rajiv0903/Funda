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

package com.fanniemae.mbsportal.config.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author g8uaxt Created on 9/18/2017.
 */

@Configuration
@SpringBootApplication(scanBasePackages = {"com.fanniemae.mbsportal.test","com.fanniemae.mbsportal.api.service","com.fanniemae.mbsportal.api.config",
        "com.fanniemae.mbsportal.api.validator",
        "com.fanniemae.mbsportal.api.transformation","com.fanniemae.mbsportal.discovery","com.fanniemae.mbsportal.api.enrichment",
        "com.fanniemae.mbsportal.api.persister","com.fanniemae.mbsportal.dao","com.fanniemae.mbsportal.utils.logging",
        "com.fanniemae.mbsportal.transformer","com.fanniemae.mbsportal.api.utils","com.fanniemae.mbsportal.calendar.client",
        "com.fanniemae.mbsportal.api.publisher", "com.fanniemae.mbsportal.util",
        "com.fanniemae.mbsportal.streaming", "com.fanniemae.mbsportal.utils.cdx.token", "com.fanniemae.mbsportal.utils.config",
        "com.fanniemae.mbsportal.service", "com.fanniemae.mbsportal.persist", "com.fanniemae.mbsportal.transformation",
        "com.fanniemae.mbsportal.validator",
        "com.fanniemae.mbsportal.streaming.prop",
        "com.fanniemae.mbsportal.streaming.socket.broker",
        "com.fanniemae.mbsportal.cdx.client",
        "com.fanniemae.mbsportal.cdx.config"
        })
@ActiveProfiles("test")
public class MBSPortalApplicationApiTestConfig {

}
