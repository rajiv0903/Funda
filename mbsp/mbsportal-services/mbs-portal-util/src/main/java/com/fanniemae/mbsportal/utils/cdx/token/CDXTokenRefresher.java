/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils.cdx.token;

import static com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap.CHANNEL;
import static com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap.SESSION_ID;
import static com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap.SUB_CHANNEL;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
import org.springframework.util.Base64Utils;

import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 19, 2018
 * @File: com.fanniemae.mbsportal.utils.cdx.token.CDXTokenRefresher.java
 * @Revision:
 * @Description: CDXTokenRefresher.java
 */
@Component
public class CDXTokenRefresher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CDXTokenRefresher.class);
    /**
     * 
     * cDXApiClientConfig CDXApiClientConfig
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
     * epvConnector EpvConnector
     */
    @Autowired
    private EpvConnector epvConnector;

    /**
     * 
     * authKey String
     */
    private String authKey;

    /**
     * 
     * sessionId String
     */
    private String sessionId;

    /**
     * 
     * method to call DC and valid session in case of invalid
     * 
     * 
     * @return String
     * @throws MBSBaseException
     */
    public String refreshLogin() throws MBSBaseException {
        
        String sessionId = null;
        try {
            
            LOGGER.debug("Entering refreshLogin method in CDXTokenRefresher");

            HttpHeaders headers = new HttpHeaders();
            headers.set(CHANNEL.getValue(), cDXApiClientConfig.getHeaderChannel());
            headers.set(SUB_CHANNEL.getValue(), cDXApiClientConfig.getHeaderSubChannel());

            String pwd = epvConnector.getValutPwd();
            if (pwd == null || pwd.isEmpty()) {
                LOGGER.error("pwd is empty for user: {}", cDXApiClientConfig.getUserId());
                throw new MBSSystemException("Pwd for CDX admin is null/empty");
            }

            authKey = Base64Utils.encodeToString((cDXApiClientConfig.getUserId() + ":" + pwd).getBytes());
            headers.set(AUTHORIZATION, String.format("Basic %s", authKey));

            LOGGER.debug("Header Info for Session Refresh {}:{}, {}:{}, {}:{} ", CHANNEL.getValue(),
                    cDXApiClientConfig.getHeaderChannel(), SUB_CHANNEL.getValue(),
                    cDXApiClientConfig.getHeaderSubChannel(), 
                    AUTHORIZATION, authKey);

            HttpEntity<Object> requestDC = new HttpEntity<Object>("", headers);
            String dcEndpoint = cDXApiClientConfig.getBaseurl() + cDXApiClientConfig.getLoginapi();
            LOGGER.debug("DC Endpoint : {}", dcEndpoint);
            // TODO: check whether it can be limited here
            ResponseEntity<Object> responseDC = gatewayProxyTemplate.exchange(dcEndpoint, HttpMethod.POST, requestDC,
                    Object.class);
            if (responseDC.getStatusCode() != HttpStatus.OK) {
                LOGGER.warn("Error while calling login url StatusCode:" + responseDC.getStatusCode());
                throw new MBSSystemException("Error while calling login url StatusCode::" + responseDC.getStatusCode()
                        + "Details Message: " + responseDC.getBody());
            }
            HttpHeaders headersResp = responseDC.getHeaders();
            List<String> tokens = headersResp.get(SESSION_ID.getValue());

            sessionId = tokens.get(0);
            setSessionId(tokens.get(0));

            LOGGER.debug("Exiting refreshLogin method in CDXTokenRefresher");

        } catch (Exception e) {
            LOGGER.error("Exception: ", e);
            throw new MBSSystemException("Error in getSession- refreshLogin", e);

        }
        return sessionId;
    }

    /**
     * 
     * getSessionId
     * 
     * @return String
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 
     * setSessionId
     * 
     * @param sessionId
     *            the sessionId
     */
    private void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 
     * encrypt the token using cert/key file - 256 bit encryption?
     * 
     * @param token
     *            the token
     * @return String
     * 
     */
    public String encrypt(String token) {
        //TODO : encrypt sometime later
        return token;
    }

    /**
     * 
     * decrypt the token using same cert/key file
     * 
     * @param token
     *            the token
     * @return String
     */
    public String decrypt(String token) {
        //TODO : decrypt sometime later
        return token;
    }
}
