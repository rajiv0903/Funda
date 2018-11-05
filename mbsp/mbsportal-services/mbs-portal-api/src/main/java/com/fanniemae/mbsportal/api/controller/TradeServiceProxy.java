/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller;
/**

 */

import static com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap.JWS_TOKEN;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.service.TradeServicePoller;
import com.fanniemae.mbsportal.utils.ApiError;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 22, 2018
 * @File: com.fanniemae.mbsportal.api.controller.TradeServiceProxy.java
 * @Revision : 
 * @Description: TradeServiceProxy.java
 */
@RestController
public class TradeServiceProxy extends BaseController{
    
	@PostConstruct
	public void init() {
		LOGGER.info("Trade Service Proxy Controller Started");
	}

    /**
     * 
     * tradeServicePoller TradeServicePoller
     */
    @Autowired
    TradeServicePoller tradeServicePoller;
    
    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "TradeServiceProxy";


    /**
     * 
     * Set the token for TS
     *
     * This is being routed from API GW with JWT token which Poller posted 
     * Check the Token is having ADMIN role or not 
     * Retrieve the Trade ID Lists and Fetch the Trades and post to 
     * Trade Service Load Balancer URL - https://alv-trade-a001.fanniemae.com:8443/mbspts/tradeservice
     * @param body the body
     * @param headers Map having headers and values
     * @param method the method
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/tradeservice" }, method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { 
            EntitlementRole.ADMIN,
            EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE})
    @ResponseHeaderRequired
    public ResponseEntity<Object> proxyPostToTradeService(@RequestBody(required = false) String body,
            @RequestHeader MultiValueMap<String, String> headers, HttpMethod method) {
        
        HttpStatus responseStatus = null;
        String responseMsg = null;
        try {
            List<String> tokens = headers.get(JWS_TOKEN.getValue());
            if (tokens == null || tokens.isEmpty() || tokens.get(0) == null || tokens.get(0).isEmpty()) {
                LOGGER.error("Auth token missing from DC");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiError("Auth token missing from DC"));
            }
            String authToken = tokens.get(0);
            // call dispatcher from here
            if (body == null || body.isEmpty()) {
                throw new Exception("Missing trade Ids. could be ping request from DC");
            }
            //TODO: retrieve key:transReqNumber and settlement date
            List<String> tradeIds = retrieveTradeIds(body);
            
            LOGGER.debug("tradeIds formatted ", tradeIds);
            if(tradeIds.isEmpty()){
                LOGGER.warn("tradeIds is null after retrieved ", tradeIds);
                responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                responseMsg = new ApiError("tradeIds is null after retrieved" + tradeIds).getMessage();
            }else{
                Boolean status = tradeServicePoller.disptachAndUpdateStatus(authToken, tradeIds);
                if (status) {
                    LOGGER.debug("Posted trades {} to Trade Service Successfuly ", tradeIds);
                    responseStatus = HttpStatus.OK;
                    responseMsg = "{\"Message\":\"Trades Posted\"}";
                } else {
                    LOGGER.debug("Posted to Trade Service failed at Proxy for one or few trades.", tradeIds);
                    responseStatus = HttpStatus.MULTI_STATUS;
                    responseMsg = "{\"Message\":\"Trade NOT posted for one or few trades and would be retried later.\"}";
                }
            }
        } catch (Exception ex) {
         // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            return getResponseEntityInternalServerException(CLASS_NAME, "proxyPostToTradeService", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        return ResponseEntity.status(responseStatus).body(responseMsg);
    }

    /**
     * filter the unwanted char
     * 
     * @param body the body
     * @return List<String>
     */
    List<String> retrieveTradeIds(String body) {
        LOGGER.debug("body: " + body);
        body = body.replace("\"", ""); // "["18A08843","18A08845"]
        body = body.replace("[", "");
        body = body.replace("]", "");
        return Arrays.asList(body.split(","));
    }

    /**
     *
     * @param body the body
     * @param headers Map having headers and values
     * @param method the method
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/traderesponse" }, method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    public ResponseEntity<Object> traderesponse(@RequestBody(required = false) String body,
            @RequestHeader MultiValueMap<String, String> headers, HttpMethod method) {
  
        try {
            LOGGER.debug("body of request" + body);
            List<String> tokens = headers.get(JWS_TOKEN.getValue());
            if (tokens == null || tokens.isEmpty() || tokens.get(0) == null) {
                LOGGER.warn("Auth token missing from DC");
            }
            String authToken = tokens.get(0);
            LOGGER.debug("authToken" + authToken);
        } catch (Exception ex) {
         // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            return getResponseEntityInternalServerException(CLASS_NAME, "proxyPostToTradeService", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"Message\":\"received\"}");
    }

}
