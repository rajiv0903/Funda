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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * @author g8uaxt Created on 9/18/2017.
 */
@Configuration
//@ContextConfiguration()
/*@SpringBootApplication(scanBasePackages = {"com.fanniemae.mbsportal.test","com.fanniemae.mbsportal.service","com.fanniemae.mbsportal.config","com"
        + ".fanniemae.mbsportal.validator",
        "com.fanniemae.mbsportal.transformation","com.fanniemae.mbsportal.discovery","com.fanniemae.mbsportal.enrichment",
        "com.fanniemae.mbsportal.persister","com.fanniemae.mbsportal.dao"})*/
@TestPropertySource("classpath:application.properties")
@ActiveProfiles("test")
@ComponentScan({"com.fanniemae.mbsportal.test","com.fanniemae.mbsportal.dao","com.fanniemae.mbsportal.util", "com.fanniemae.mbsportal.utils.logging"})
public class MBSPortalApplicationDAOTestConfig {

}
