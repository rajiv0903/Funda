/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;

import static com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap.CHANNEL;
import static com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap.SESSION_ID;
import static com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap.SUB_CHANNEL;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 19, 2018
 * @File: com.fanniemae.mbsportal.api.service.TradeServiceProxyClient.java
 * @Revision:
 * @Description: TradeServiceProxyClient.java
 */
@Component
public class TradeServiceProxyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeServiceProxyClient.class);

    /**
     * 
     * tradeServiceProperties TradeServiceProperties
     */
    @Autowired
    private TradeServiceProperties tradeServiceProperties;

    /**
     * 
     * gatewayProxyTemplate GatewayProxyTemplate
     */
    @Autowired
    @Qualifier("gatewayProxyTemplate")
    private GatewayProxyTemplate gatewayProxyTemplate;

    /**
     * 
     * cDXApiClientConfig CDXApiClientConfig
     */
    @Autowired
    private CDXApiClientConfig cDXApiClientConfig;

    /**
     * 
     * callTradeServiceProxy - calling mbsp endpoint to get auth token
     * 
     * @param sessionId
     *            the sessionId
     * @param traderIds
     *            the traderIds
     * @return boolean
     * @throws Exception
     * 
     */
    public boolean callTradeServiceProxy(String sessionId, List<String> traderIds) throws Exception {
        try {

            LOGGER.debug("Entering callTradeServiceProxy method in TradeServiceProxyClient");

            HttpHeaders headers = new HttpHeaders();
            headers.set(CHANNEL.getValue(), cDXApiClientConfig.getHeaderChannel());
            headers.set(SUB_CHANNEL.getValue(), cDXApiClientConfig.getHeaderSubChannel());
            headers.set(SESSION_ID.getValue(), sessionId);
            // payload is transReqIds
            HttpEntity<Object> request = new HttpEntity<Object>(traderIds, headers);

            String endPoint = cDXApiClientConfig.getHostUrl() + tradeServiceProperties.getRefreshUrl();

            LOGGER.debug("Proxy Endpoint: {}, Headers: {}", endPoint, headers);

            ResponseEntity<Object> response = gatewayProxyTemplate.exchange(endPoint, HttpMethod.POST, request,
                    Object.class);

            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                LOGGER.warn("Failed to call Proxy StatusCode:" + response.getStatusCode() + "Details Message: "
                        + response.getBody());
                throw new MBSSystemException(
                        "StatusCode:" + response.getStatusCode().toString() + " " + "Message:" + response.getBody());
            } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                LOGGER.error("Failed to call Proxy StatusCode:" + response.getStatusCode() + " Details Message: "
                        + response.getBody());
                throw new MBSSystemException(
                        "StatusCode:" + response.getStatusCode().toString() + " " + "Message:" + response.getBody());
            } else if (response.getStatusCode() == HttpStatus.MULTI_STATUS) {
                LOGGER.warn("One or few records failed to call Proxy StatusCode:" + response.getStatusCode()
                        + " Details Message: " + response.getBody());
            } else if (response.getStatusCode() != HttpStatus.OK) {
                throw new MBSSystemException(
                        "StatusCode:" + response.getStatusCode().toString() + " " + "Message:" + response.getBody());
            }

            LOGGER.debug("Exiting callTradeServiceProxy method in TradeServiceProxyClient");

            return true;
        } catch (HttpServerErrorException | HttpClientErrorException hsex) {
            if (hsex.getStatusCode() == HttpStatus.BAD_GATEWAY) {
                LOGGER.warn("error in callTradeServiceProxy : Bad Gateway", hsex);
            }
            if (hsex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                LOGGER.warn("error in callTradeServiceProxy : UNAUTHORIZED");
            }
            throw hsex;

        } catch (Exception e) {
            LOGGER.error("error in callTradeServiceProxy", e);
            throw e;
        }
    }
}
