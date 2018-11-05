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

package com.fanniemae.mbsportal.dao;

import com.fanniemae.mbsportal.config.test.GemFireConfig;
import com.fanniemae.mbsportal.config.test.IntegrationDaoConfig;
import com.fanniemae.mbsportal.config.test.MBSPortalApplicationDAOIntConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author g8uaxt Created on 12/15/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { MBSPortalApplicationDAOIntConfig.class, GemFireConfig.class,IntegrationDaoConfig.class})
@ActiveProfiles("default")
@DirtiesContext
//@ImportResource(value = { "classpath*:*-mbs-portal-client-gf-context.xml" })
@ComponentScan(basePackages = "com.fanniemae.mbsportal.dao")
public class IntegrationDaoTest {
        
        @Test
        public void init(){
        
        }
}
