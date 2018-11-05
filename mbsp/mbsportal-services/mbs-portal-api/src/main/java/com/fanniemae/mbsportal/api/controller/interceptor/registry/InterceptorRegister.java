/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller.interceptor.registry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementInterceptor;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderInterceptor;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderInterceptor;

/**
 * 
 * @author g8upjv
 *
 */
@Configuration
@EnableWebSecurity
public class InterceptorRegister extends WebMvcConfigurerAdapter{
    
    /**
     * 
     * requestHeaderInterceptor RequestHeaderInterceptor
     */
    @Autowired
    RequestHeaderInterceptor requestHeaderInterceptor;
    
    /**
     * 
     * entitlementInterceptor EntitlementInterceptor
     */
    @Autowired
    EntitlementInterceptor entitlementInterceptor;
    
    /**
     * 
     * responseHeaderInterceptor ResponseHeaderInterceptor
     */
    @Autowired
    ResponseHeaderInterceptor responseHeaderInterceptor;
    
    /**
     * 
     * 
     * @param registry the InterceptorRegistry
     * 
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) 
    {
        registry.addInterceptor(requestHeaderInterceptor);
        registry.addInterceptor(entitlementInterceptor);
        registry.addInterceptor(responseHeaderInterceptor);
    }
}
