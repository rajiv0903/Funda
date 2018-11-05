/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 8, 2018
 * @File: com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate.java 
 * @Revision: 
 * @Description: MBSRestInternalTemplate.java
 */
public class MBSRestInternalTemplate extends RestTemplate {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MBSRestInternalTemplate.class);

    /**
     *
     * @param requestFactory
     */
    public MBSRestInternalTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    public <T> ResponseEntity<T> exchange(List<String> urls, int retryCount, HttpMethod method, HttpEntity<?> requestEntity,
            Class<T> responseType, Object... uriVariables) throws RestClientException {

        LOGGER.debug("Entering exchange method in MBSRestInternalTemplate");
        ResponseEntity<T> responseEntity = null;
        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
        int retryCounter = 0;

        retryCount = retryCount * urls.size();

        while (retryCounter < (retryCount + 1)) {
            for (String url : urls) {
                try {
                    LOGGER.debug("MBSRestInternalTemplate: exchange: Trying with URL: {}", url);
                    responseEntity = execute(url, method, requestCallback, responseExtractor, uriVariables);
                    LOGGER.debug("Exiting exchange method in MessagePublishRestTemplate");
                    return responseEntity;
                } catch (RestClientException clientExe) {
                    LOGGER.error(
                            "MBSRestInternalTemplate: RestClientException: Exception occurs with URL: {}, will retry ",
                            url);
                    retryCounter++;
                    if (retryCounter == retryCount) {
                        LOGGER.error("MBSRestInternalTemplate: RestClientException: {}", clientExe.getMessage());
                        throw clientExe;
                    }
                }
            }
        }

        return responseEntity;
    }

}
