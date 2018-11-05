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

import com.fanniemae.mbsportal.dao.MBSProductDao;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author g8uaxt Created on 12/14/2017.
 */
@Configuration
@ActiveProfiles("test")
public class DaoTestConfig {
        
        
        @Bean
        @Primary
        public MBSProductDao mbsProductDao() {
                return Mockito.mock(MBSProductDao.class);
        }
        @Bean
        @Primary
        public MBSProfileDao mbsProfileDao() {
                return Mockito.mock(MBSProfileDao.class);
        }
        @Bean
        @Primary
        public MBSTransactionRequestDao mbsTransactionRequestDao() {
                return Mockito.mock(MBSTransactionRequestDao.class);
        }
        
        
}
