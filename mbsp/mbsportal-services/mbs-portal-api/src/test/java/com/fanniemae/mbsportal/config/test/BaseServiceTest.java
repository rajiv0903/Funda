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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import org.slf4j.Logger;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fanniemae.mbsportal.config.test.DaoTestConfig;
import com.fanniemae.mbsportal.config.test.GemFireConfig;

/**
 * @author g8uaxt Created on 9/27/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/application-test.properties")
@ContextConfiguration(classes = { MBSPortalApplicationApiTestConfig.class, ServiceTestConfigNew.class,
        GemFireConfig.class, DaoTestConfig.class
               /* ,TestODSAdapterConfig.class,*/ })
@ActiveProfiles("test")
@DirtiesContext
public class BaseServiceTest {
        @Mock
        protected Logger LOGGER;
        
        @Before
        public void setup() {
                doNothing().when(LOGGER).debug(any());
                doNothing().when(LOGGER).info(any());
                doNothing().when(LOGGER).warn(any());
                doNothing().when(LOGGER).error(any());
        }
        
        @Test
        public void init() {
        
        }
}
