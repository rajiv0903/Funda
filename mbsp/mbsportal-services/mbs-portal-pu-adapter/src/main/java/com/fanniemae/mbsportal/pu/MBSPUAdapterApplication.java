/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.pu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

import com.fanniemae.mbsportal.pu.adapter.config.MbspProperties;
import com.fanniemae.mbsportal.pu.adapter.config.PUBootstrapConfig;

/**
 * 
 * @author g8upjv
 * @Version:
 * @Date Jun 1 2018
 * 
 *      com.fanniemae.mbsportal
 *      MBSPUAdapterApplication.java
 * @Description: For Blue/ Green - Added Profile
 */
@SpringBootApplication(exclude = {EmbeddedServletContainerAutoConfiguration.class, 
        WebMvcAutoConfiguration.class})
@ComponentScan({"com.fanniemae.mbsportal"})
@ImportResource(value = { "classpath*:*-client-gf-context.xml" })
public class MBSPUAdapterApplication {
    
    /**
     * 
     * appcode application code
     */
    @Value("${mbsp.appCd}")
    String appCode;
    
    /**
     * 
     * env identifies environment
     */
    @Value("${mbsp.envCd}")
    String env;
    
    /**
     * 
     * tsObjRefId identifies the trade service object reference id
     */
    @Value("${mbsp.epvRefId}")
    String tsObjRefId;
    
    /**
     * 
     * Returning the mbsp properties file
     * 
     * @return MbspProperties
     */
    @Bean
    MbspProperties mbspProp() {
        return new MbspProperties();
    }
    
    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MBSPUAdapterApplication.class);
    
    
    /**
     * 
     * Start method
     * 
     * @param args Array of arguments passed to the main method
     */
    public static void main(String[] args) {
        SpringApplication.run(MBSPUAdapterApplication.class, args);
        LOGGER.info("PU Adapter active");
    }

    /**
     * 
     * Returning the PUBootstrapConfig object
     * 
     * @return PUBootstrapConfig
     */
    @Bean
    @Profile({ "LOCAL", "DEV1", "DEV2", "ACPT1", "ACPT2","PERF", "TEST1", "PROD1", "PROD2" })
    public PUBootstrapConfig bootstrapConfig() {
        LOGGER.info("PU bootstrapConfig..");
       
        PUBootstrapConfig bootstrapConfig = new PUBootstrapConfig();
        return bootstrapConfig;
    }
    

}
