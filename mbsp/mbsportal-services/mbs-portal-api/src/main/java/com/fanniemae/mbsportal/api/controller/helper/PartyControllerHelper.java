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

import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.service.PartyService;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: May 22, 2018
 * @File: com.fanniemae.mbsportal.api.controller.helper.PartyControllerHelper.
 *        java
 * @Revision:
 * @Description: PartyControllerHelper.java
 */
@Component
public class PartyControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartyControllerHelper.class);

    /**
     *
     * partyServiceV1 PartyService
     */
    @Autowired
    private PartyService partyServiceV1;

    /**
     *
     * select the version from mediatype
     *
     * @param mediaType
     *            the mediaType
     * @return BaseProcessor
     */
    @SuppressWarnings("rawtypes")
    private BaseProcessor selectDataServiceByMediaType(String mediaType) {
        LOGGER.debug("Entering selectDataServiceByMediaType method in PartyControllerHelper " + mediaType);
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE) || mediaType.equals(getVersion1json().toString())) {
            return partyServiceV1;
        } else {
            return null;
        }
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
    public PartyPO saveOrUpdateParty(PartyPO partyPO) throws MBSBaseException {
        LOGGER.debug("Entering saveOrUpdateParty method in PartyControllerHelper ");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;

        PartyPO partyPOResponse;

        // Select the required version of the service object
        if (!Objects.equals(partyPO, null)) {

            BaseProcessor service = selectDataServiceByMediaType(mediaType.trim());

            if (!Objects.equals(service, null)) {

                PartyService partyService = (PartyService) service;

                partyPOResponse = partyService.saveParty(partyPO);

            } else {
                LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
                throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                        MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        } else {
            LOGGER.error("Failed to save or update Party, partyPO(input) object is null");
            throw new MBSBusinessException("Failed to save or update Party, partyPO(input) object is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        LOGGER.debug("Exiting saveOrUpdateParty method in PartyControllerHelper");
        return partyPOResponse;

    }

    /**
     *
     * get party for the ssn passed
     *
     * @param sellerSerivcerNumber
     *            the sellerSerivcerNumber
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public PartyPO getParty(String sellerSerivcerNumber) throws MBSBaseException {
        LOGGER.debug("Entering getParty method in PartyControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;

        PartyPO partyPOResponse;

        // Select the required version of the service object
        if (!Objects.equals(sellerSerivcerNumber, null)) {

            BaseProcessor service = selectDataServiceByMediaType(mediaType.trim());

            if (!Objects.equals(service, null)) {

                PartyService partyService = (PartyService) service;

                partyPOResponse = partyService.getParty(sellerSerivcerNumber);

            } else {
                LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
                throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                        MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        } else {
            LOGGER.error("sellerSerivcerNumber is empty");
            throw new MBSBusinessException("sellerSerivcerNumber is empty", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        LOGGER.debug("Exiting getParty method in PartyControllerHelper");
        return partyPOResponse;
    }

    /**
     *
     * Clears the party
     *
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public void deleteParty(String sellerSerivcerNumber) throws MBSBaseException {
        LOGGER.debug("Entering deleteParty method in PartyControllerHelper");
        partyServiceV1.clear(sellerSerivcerNumber);
        LOGGER.debug("Exiting deleteParty method in PartyControllerHelper");
    }

    /**
     *
     * Clears all the Parties
     *
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public void deleteParties() throws MBSBaseException {
        LOGGER.debug("Entering deleteParties method in PartyControllerHelper");
        partyServiceV1.clearAll();
        LOGGER.debug("Exiting deleteParties method in PartyControllerHelper");
    }

}
