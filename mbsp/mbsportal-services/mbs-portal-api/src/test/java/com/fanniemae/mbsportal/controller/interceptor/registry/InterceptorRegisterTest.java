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

package com.fanniemae.mbsportal.controller.interceptor.registry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.fanniemae.mbsportal.api.controller.interceptor.registry.InterceptorRegister;
/**
 * @Author Rajiv Chaudhuri
 * @Date Nov 3, 2017
 * @Time: 4:45:35 PM
 * @Description:
 * 	com.fanniemae.mbsportal.api.controller.interceptor.registry
 * 	InterceptorRegisterTest.java
 * @Purpose: JUnit Test Cases for InterceptorRegister
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class InterceptorRegisterTest  {
    
    @InjectMocks
    InterceptorRegister interceptorRegister;
    
    @Mock
    InterceptorRegistry registry;
    
    @Mock
    InterceptorRegistration interceptorRegistration;

    @Test
    public void addInterceptorsTest() throws Exception {
        
        Mockito.doReturn(interceptorRegistration).when(registry).addInterceptor(Mockito.any());
        interceptorRegister.addInterceptors(registry);
    }
}
