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

package com.fanniemae.mbsportal.controller.interceptor.security;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith( SpringJUnit4ClassRunner.class )
@WebAppConfiguration
@EnableWebMvcSecurity
//@SpringApplicationConfiguration( classes = {
//        MockServletContext.class,
//        //HttpSessionConfig.class,
//        //WebSecurityConfig.class
//} )
@Ignore
public class SecurityConfigurationTest  {

    private MockMvc mockMvc = null;
    
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup( context )
                //.addFilter( new CORSFilter(), "/*" )
                .build();
    }
    
    @Test
    protected void configureTest () throws Exception {
        
//        mockMvc.perform( MockMvc.get("/") );
//        .andExpect( status().is2xxSuccessful() )
//        .andExpect( header().string( "Access-Control-Allow-Origin", Assert.notNullValue() ) )
//        .andExpect( header().string( "Access-Control-Allow-",
//                equalToIgnoringCase( "POST, GET, PUT, OPTIONS, DELETE" ) ) )
//        .andExpect( header().string( "Access-Control-Allow-Headers",
//                equalToIgnoringCase( "content-type, x-auth-token, x-requested-with" ) ) )
//        .andExpect( header().string( "Access-Control-Expose-Headers", equalToIgnoringCase( "Location" ) ) )
//        .andExpect( header().string( "Access-Control-Max-Age", notNullValue() ) );
} 
}
