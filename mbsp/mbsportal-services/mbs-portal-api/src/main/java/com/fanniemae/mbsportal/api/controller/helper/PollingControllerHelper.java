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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.po.PollingPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.service.PollingService;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 20, 2017
 * @Time 3:29:09 PM com.fanniemae.mbsportal.api.controller.helper
 *       PollingControllerHelper.java
 * @Description:
 */
@Component
public class PollingControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingControllerHelper.class);

    /**
     *
     * pollingServiceV1 PollingService
     */
    @Autowired
    private PollingService pollingServiceV1;

    /**
     *
     * cdxClientApi CDXClientApi
     */
    @Autowired
    private CDXClientApi cdxClientApi;

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
        LOGGER.debug("Entering selectDataServiceByMediaType method in PollingControllerHelper " + mediaType);
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE) || mediaType.equals(getVersion1json().toString())) {
            return pollingServiceV1;
        } else {
            return null;
        }
    }

    /**
     *
     * lender polling request
     *
     * @param reqDateTimestamp
     *            the request timestamp
     * @param headers
     *            Map having headers and values
     * @return PollingPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public PollingPO lenderPollingRequest(Optional<String> reqDateTimestamp, Map<String, String> headers)
            throws MBSBaseException {

        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        String lenderName;
        PollingPO pollingPOResponse;

        LOGGER.debug("Entering lenderPollingRequest method in PollingControllerHelper (reqDateTimestamp):"
                + reqDateTimestamp);
        /*
         * CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
         * Pass null as role so that role can be fetched from token
         */
        ProfileEntitlementPO profileEntitlementPO = cdxClientApi.getProfileEntitlementPO(headers, null);
        if (!Objects.equals(profileEntitlementPO, null)) {
            lenderName = profileEntitlementPO.getUserName();

        } else {
            LOGGER.error("Failed to retrieve lender profile");
            throw new MBSBusinessException("Failed to retrieve lender profile",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        if (!Objects.equals(reqDateTimestamp, null)) {
            BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());

            pollingPOResponse = ((PollingService) transReqService).getPoollingReq(lenderName, reqDateTimestamp,
                    MBSRoleType.LENDER);
        } else {
            LOGGER.error("Failed to fetch polling request for, timestamp is null");
            throw new MBSBusinessException("Failed to fetch polling request for, timestamp is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting lenderPollingRequest method in PollingControllerHelper (reqDateTimestamp):"
                + reqDateTimestamp);
        return pollingPOResponse;
    }

    /**
     *
     * trader polling request
     *
     * @param reqDateTimestamp
     *            the request timestamp
     * @param headers
     *            Map having headers and values
     * @return PollingPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public PollingPO traderPollingRequest(Optional<String> reqDateTimestamp, Map<String, String> headers)
            throws MBSBaseException {

        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        String traderName = TradeConstants.TRADE_EVENT_USRE_NAME;
        PollingPO pollingPOResponse;

        LOGGER.debug("Entering traderPollingRequest method in PollingControllerHelper (reqDateTimestamp):"
                + reqDateTimestamp);

        if (!Objects.equals(reqDateTimestamp, null)) {
            BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());

            pollingPOResponse = ((PollingService) transReqService).getPoollingReq(traderName, reqDateTimestamp,
                    MBSRoleType.TRADER);
        } else {
            LOGGER.error("Failed to fetch polling request for, timestamp is null");
            throw new MBSBusinessException("Failed to fetch polling request for, timestamp is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting traderPollingRequest method in PollingControllerHelper (reqDateTimestamp):"
                + reqDateTimestamp);
        return pollingPOResponse;
    }

    /**
     *
     * clear all events
     *
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public void clearAllEvents() throws MBSBaseException {
        LOGGER.debug("Entering clearAllEvents method in PollingControllerHelper");
        pollingServiceV1.clearAll();
        LOGGER.debug("Exiting clearAllEvents method in PollingControllerHelper");
    }

}
