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

import com.fanniemae.mbsportal.dao.BaseDaoWrapper;
import com.fanniemae.mbsportal.dao.IDServiceDao;
import com.fanniemae.mbsportal.util.DAOUtils;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Created by g8uaxt on 7/31/2017.
 */
@Configuration
@Profile("test")
@ComponentScan({"com.fanniemae.mbsportal.test","com.fanniemae.mbsportal.dao","com.fanniemae.mbsportal.util","com.fanniemae.mbsportal.utils.logging"})
public class DaoTestConfig {
        
        @Bean
        @Primary
        public IDServiceDao idServiceDao() {
                return Mockito.mock(IDServiceDao.class);
        }
        
        @SuppressWarnings("rawtypes")
        @Bean
        @Primary
        public BaseDaoWrapper baseDaoWrapper() {
                return Mockito.mock(BaseDaoWrapper.class);
        }
        
      
  
}
