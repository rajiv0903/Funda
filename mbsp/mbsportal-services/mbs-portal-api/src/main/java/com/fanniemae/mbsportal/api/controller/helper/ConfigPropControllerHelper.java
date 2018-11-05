/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller.helper;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ConfigPropPO;
import com.fanniemae.mbsportal.api.service.ConfigPropService;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 *
 * @author: Rajiv Chaudhuri
 * @Date: Jul 6, 2018
 * @File: com.fanniemae.mbsportal.api.controller.helper.
 *        ConfigPropControllerHelper.java
 * @Revision:
 * @Description: ConfigPropControllerHelper.java
 */
@Component
public class ConfigPropControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPropControllerHelper.class);

    /**
     *
     * configPropServiceV1 ConfigPropService
     */
    @Autowired
    private ConfigPropService configPropServiceV1;

    /**
     *
     * select the media type for versioning
     *
     * @param mediaType
     *            the mediaType
     * @return BaseProcessor
     */
    @SuppressWarnings("rawtypes")
    private BaseProcessor selectDataServiceByMediaType(String mediaType) {
        LOGGER.debug("Entering selectDataServiceByMediaType method in ConfigPropControllerHelper " + mediaType);
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE) || mediaType.equals(getVersion1json().toString())) {
            return configPropServiceV1;
        } else {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public ConfigPropPO getConfigProp(String key) throws MBSBaseException {
        LOGGER.debug("Entering getConfigProp method in ConfigPropControllerHelper ");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        ConfigPropPO configPOResponse;

        if (!Objects.equals(key, null)) {
            BaseProcessor service = selectDataServiceByMediaType(mediaType.trim());

            if (!Objects.equals(service, null)) {
                ConfigPropService configPropService = (ConfigPropService) service;
                configPOResponse = configPropService.getConfigProp(key);

            } else {
                LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
                throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                        MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        } else {
            LOGGER.error("key is empty");
            throw new MBSBusinessException("key is empty", MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting getConfigProp method in ConfigPropControllerHelper");
        return configPOResponse;
    }

    /**
     *
     * create party
     *
     * @param partyPO
     *            the PartyPO
     * @return partyPO
     * @throws MBSBaseException
     *
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public ConfigPropPO saveOrUpdateConfigProp(ConfigPropPO configPropPO) throws MBSBaseException {
        LOGGER.debug("Entering saveOrUpdateConfigProp method in ConfigPropControllerHelper ");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;

        ConfigPropPO configPropPOResponse;

        // Select the required version of the service object
        if (!Objects.equals(configPropPO, null)) {

            BaseProcessor service = selectDataServiceByMediaType(mediaType.trim());
            if (!Objects.equals(service, null)) {
                ConfigPropService configPropService = (ConfigPropService) service;
                configPropPOResponse = configPropService.saveOrUpdateConfigProp(configPropPO);

            } else {
                LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
                throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                        MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        } else {
            LOGGER.error("Failed to save or update ConfigProp, configPropPO(input) object is null");
            throw new MBSBusinessException("Failed to save or update ConfigProp, configPropPO(input) object is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        LOGGER.debug("Exiting saveOrUpdateConfigProp method in ConfigPropControllerHelper ");
        return configPropPOResponse;

    }

}
