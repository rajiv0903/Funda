/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */


package com.fanniemae.mbsportal.cdx.client;

import java.util.Map;
import java.util.Objects;

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

import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.cdx.exception.CDXBaseException;
import com.fanniemae.mbsportal.cdx.exception.CDXExceptionConstants;
import com.fanniemae.mbsportal.cdx.exception.CDXRetryableException;
import com.fanniemae.mbsportal.cdx.model.CDXUserProfile;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 31, 2018
 * @File: com.fanniemae.mbsportal.cdx.client.CDXClientCallApi.java 
 * @Revision: 
 * @Description: CDXClientCallApi.java
 */
@Component
public class CDXClientCallApi {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CDXClientCallApi.class);
    
    /**
     * 
     * cDXApiClientConfig API client config obj
     */
    @Autowired
    CDXApiClientConfig cDXApiClientConfig;  
    
    /**
     * 
     * gatewayProxyTemplate GatewayProxyTemplate
     */
    @Autowired
    @Qualifier("gatewayProxyTemplate")
    private GatewayProxyTemplate gatewayProxyTemplate;

    /**
     * 
     * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API
     *               with valid Session ID
     * @param cdxHeaderMap
     *            - Header Map from Request
     * @return CDXUserProfile from CDX
     * @throws MBSBaseException
     */
    public CDXUserProfile getProfileFromSession(Map<String, String> cdxHeaderMap) throws CDXRetryableException, CDXBaseException {

        LOGGER.debug("Entering getProfileFromSession method in CDXClientCallApi");
        CDXUserProfile cDXUserProfile = null;
        ResponseEntity<String> response = null;

        try {

            String url = cDXApiClientConfig.getBaseurl() + cDXApiClientConfig.getProfileapi();
            HttpHeaders headers = new HttpHeaders();
            headers.add(CDXHeaderMap.CHANNEL.getValue(), cdxHeaderMap.get(CDXHeaderMap.CHANNEL.getValue()));
            headers.add(CDXHeaderMap.SUB_CHANNEL.getValue(), cdxHeaderMap.get(CDXHeaderMap.SUB_CHANNEL.getValue()));
            headers.add(CDXHeaderMap.SESSION_ID.getValue(), cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue()));

            LOGGER.debug("Url: {}, Headers: {}",  url, headers);

            
            HttpEntity<String> request = new HttpEntity<>(headers);
            response = gatewayProxyTemplate.exchange(url, HttpMethod.POST, request, String.class);

            LOGGER.debug("Response: {}, Status Code: {}" , response, response.getStatusCode().value());

            if (HttpStatus.CREATED.value() == response.getStatusCode().value()
                    || HttpStatus.OK.value() == response.getStatusCode().value()) {
                
                ObjectMapper mapper = new ObjectMapper();
                cDXUserProfile = mapper.readValue(response.getBody(), CDXUserProfile.class);
                
            } else {
                LOGGER.error("Status Code For the getProfileFromSession Call, Session ID:" + cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue()
                                + ", might not be valid, Status code:" + response.getStatusCodeValue()));
                throw new CDXBaseException(response.getBody() ,response.getStatusCode().value());
            }
            
            /*
             * Need to check whether it is required or not
             */
            if (Objects.equals(cDXUserProfile, null)) {
                
                LOGGER.error("Failed to fetch profile for session ID:{"
                        + cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue()) + "}");
                throw new CDXBaseException("Failed to fetch profile for session ID:{"
                        + cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue()) + "}",
                        CDXExceptionConstants.SYSTEM_EXCEPTION);
            }
            LOGGER.debug("Exiting getProfileFromSession method in CDXClientCallApi");
            return cDXUserProfile;
            

        } 
        catch (HttpClientErrorException exe) {
            
            LOGGER.error("Failed to fetch profile for session ID:{" + cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue())
            + "} Status code is {"+exe.getStatusCode().value()+"}");
            
            if(HttpStatus.TOO_MANY_REQUESTS.value() == exe.getStatusCode().value()){ 
                throw new CDXRetryableException("Too many request raised hence going for retry");
                
            }else{
                throw new CDXBaseException("Failed to fetch profile for session ID:{"
                        + cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue()) + "}",
                        CDXExceptionConstants.SYSTEM_EXCEPTION);
            }
        }
        catch (Exception exe) {
            
            LOGGER.error("Failed to fetch profile for session ID:{" + cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue())
                    + "} Exception: ", exe);
            throw new CDXBaseException("Failed to fetch profile for session ID:{"
                    + cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue()) + "}",
                    CDXExceptionConstants.SYSTEM_EXCEPTION);
                
        }
    }
}
