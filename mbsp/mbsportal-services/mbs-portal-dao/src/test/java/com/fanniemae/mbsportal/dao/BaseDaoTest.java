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

import com.fanniemae.mbsportal.config.test.DaoTestConfig;
import com.fanniemae.mbsportal.config.test.GemFireConfig;
import com.fanniemae.mbsportal.config.test.MBSPortalApplicationDAOTestConfig;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by g8uaxt on 7/31/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { MBSPortalApplicationDAOTestConfig.class, GemFireConfig.class,DaoTestConfig.class})
@ActiveProfiles("test")
@DirtiesContext
@ComponentScan(basePackages = "com.fanniemae.mbsportal.dao")
public class BaseDaoTest {
        @Mock
        protected Logger LOGGER;
     
        @SuppressWarnings("rawtypes")
        @Autowired
        protected BaseDaoWrapper baseDaoWrapper;
        @Autowired
        protected IDServiceDao idServiceDao;
        @Mock
        protected Region<Object, Object> mockRegion;
        @Mock
        protected RegionService mockRegionService;
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Before
        public void setup() {
                //TODO: temp fix. But do we need logger for test?
                doNothing().when(LOGGER).debug(any());
                doNothing().when(LOGGER).info(any());
                doNothing().when(LOGGER).warn(any());
                doNothing().when(LOGGER).error(any());
                doNothing().when((BaseDaoWrapper)baseDaoWrapper).saveOrUpdate(any());
                when(mockRegion.getRegionService()).thenReturn(mockRegionService);
               
        
        }
        @Test
        public void init(){
        
        }
}
