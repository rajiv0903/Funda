/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal;

import com.fanniemae.mbsportal.api.config.AppVault;
import com.fanniemae.mbsportal.api.config.CorrelationFilter;
import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.scheduler.BootstrapConfig;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.stomp.MBSStompUtil;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSStompClient;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSWebSocketJsClient;
import com.fanniemae.mbsportal.streaming.stomp.session.MBSStompSessionHandler;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 28, 2017
 * @Time 11:11:49 AM com.fanniemae.mbsportal MBSPortalTradeApplication.java
 * @Description: For Blue/ Green - Added Profile
 */
@SpringBootApplication
@EnableScheduling
@EnableSwagger2
@ImportResource(value = { "classpath*:*-client-gf-context.xml" })
@Profile({ "!test" })
public class MBSPortalTradeApplication {

    /**
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MBSPortalTradeApplication.class);

    /**
     * appcode application code
     */
    @Value("${mbsp.appCd}")
    String appCode;

    /**
     * env identifies environment
     */
    @Value("${mbsp.envCd}")
    String env;

    /**
     * tsObjRefId identifies the trade service object reference id
     */
    @Value("${mbsp.epvRefId}")
    String tsObjRefId;

    /**
     * Start method
     *
     * @param args
     *            Array of arguments passed to the main method
     */
    public static void main(String[] args) {
        SpringApplication.run(MBSPortalTradeApplication.class, args);
        LOGGER.warn("endpoints active");
    }

    /**
     * Returning the BootstrapConfig object
     *
     * @return BootstrapConfig
     */
    @Bean
    @Profile({ "LOCAL", "DEV1", "DEV2", "ACPT1", "ACPT2", "TEST1", "PERF", "PROD1", "PROD2" })
    public BootstrapConfig bootstrapConfig() {
        LOGGER.info("bootstrapConfig..");
        BootstrapConfig bootstrapConfig = new BootstrapConfig();
        return bootstrapConfig;
    }

    /**
     * Returning the mbsp properties file
     *
     * @return MbspProperties
     */
    @Bean
    MbspProperties mbspProp() {
        return new MbspProperties();
    }

    @Bean
    StreamingClientProperties streamingClientProperties() {
        return new StreamingClientProperties();
    }

    /**
     * Returning the WebMvcConfigurer with allowed methods mapping
     *
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                LOGGER.debug("adding Cors Mapping.." + mbspProp().getCorsHostUrl());
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*");
            }
        };
    }

    /**
     * Purpose: This method is used to set the correlation filter to add
     * correlation id for every request.
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean setCorrelationFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorrelationFilter());
        registration.addUrlPatterns("/*");
        LOGGER.debug("MBSP:Setting System Properties: AWS_VREF {}", mbspProp().getAwsVref());
        System.setProperty("AWS_VREF", mbspProp().getAwsVref());
        System.setProperty("ENV_CD", mbspProp().getEnvCd());
        System.setProperty("EPV_REF_ID", mbspProp().getEpvRefId());
        LOGGER.debug("MBSP:Setting System Properties: APP_CD {}", System.getProperty("APP_CD"));
        LOGGER.debug("MBSP:Setting System Properties: ENV_CD {}", System.getProperty("ENV_CD"));
        LOGGER.debug("MBSP:Setting System Properties: EPV_REF_ID {}", System.getProperty("EPV_REF_ID"));
        return registration;
    }

    /**
     * hold application wide variables to share
     *
     * @return AppVault
     */
    @Bean
    public AppVault appVault() {
        AppVault appVault = new AppVault();
        LOGGER.debug("Initializing appVault" + appVault.getTsAdminToken());
        return appVault;
    }

    /**
     * Returning the EpvConnector with initialization params
     *
     * @return EpvConnector
     */
    @Bean
    @Profile({ "DEV1", "DEV2", "ACPT1", "ACPT2", "PERF", "TEST1", "PROD1", "PROD2" })
    @Primary
    public EpvConnector epvConnector() {
        LOGGER.debug(String.format("epvConnector initializing with appCode=%s env=%s tsObjRefId=%s", appCode, env,
                tsObjRefId));
        EpvConnector epvConnector = new EpvConnector();
        try {
            epvConnector.initialize(appCode, env, tsObjRefId);
        } catch (MBSBaseException e) {
            LOGGER.error("epvConnector bean initialization failed", e.getRootException());
        }

        LOGGER.info("epvConnector bean initialized");
        return epvConnector;
    }

    /**
     * Returning the EpvConnector with initialization params for local
     * environment
     *
     * @return EpvConnector
     */
    @Bean(name = "epvConnector")
    @Profile({ "default", "LOCAL", "test" })
    public EpvConnector epvConnectorLocal() {
        LOGGER.debug(String.format("epvConnector initializing with appCode=%s env=%s tsObjRefId=%s", appCode, env,
                tsObjRefId));
        EpvConnector epvConnector = new EpvConnector();
        LOGGER.info("epvConnector bean initialized");
        return epvConnector;
    }

    @Bean
    // @Profile({ "DEV1", "DEV2", "ACPT1", "ACPT2", "PERF", "TEST1", "PROD1", "PROD2" })
    public MBSStompClient mbsStompClient() {
        LOGGER.info("mbsStompClient initializing...");
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        MBSStompClient mbsStompClient = new MBSStompClient(new MBSWebSocketJsClient(transports),
                new SimpleMessageConverter(), new MBSStompSessionHandler(), streamingClientProperties().getWebSocketUrl());
        //save it for non spring beans
        MBSStompUtil.addClient("default",mbsStompClient);
        LOGGER.info("mbsStompClient initialized");
        return mbsStompClient;
    }
}