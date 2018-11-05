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

import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.helper.PollingControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.PollingPO;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 20, 2017
 * @Time 3:28:29 PM com.fanniemae.mbsportal.api.controller
 *       PollingController.java
 * @Description: This class is designed to expose API for Polling for Lender and
 *               Trader Based on requested Date Time it will check whether there
 *               is event or not
 */
@RestController
public class PollingController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Polling Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingController.class);

    /**
     *
     * pollingControllerHelper PollingControllerHelper
     */
    @Autowired
    PollingControllerHelper pollingControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "PollingController";

    /**
     *
     * Lender events
     *
     * @param reqDateTimestamp
     *            the reqDateTimestamp
     * @param headers
     *            Map of headers and values
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbslendertransactions/events/{reqDateTimestamp}", "/mbslendertransactions/events",
            "/capital-markets/trading/securities/mbs/lendertransactions/events/{reqDateTimestamp}",
            "/capital-markets/trading/securities/mbs/lendertransactions/events" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.TSP_TRADE_EXECUTE, })
    @ResponseHeaderRequired
    public ResponseEntity<Object> lenderPollingRequest(
            @PathVariable(value = "reqDateTimestamp") Optional<String> reqDateTimestamp,
            @RequestHeader Map<String, String> headers) {

        LOGGER.debug("Entering lenderPollingRequest method in PollingController (reqDateTimestamp):" + reqDateTimestamp);
        PollingPO pollingPOResponse = null;
        try {
            pollingPOResponse = pollingControllerHelper.lenderPollingRequest(reqDateTimestamp, headers);

        } catch (MBSBaseException ex) {
            LOGGER.error("Failed to fetch polling request for Lender " + ex);
            LOGGER.error("Exiting lenderPollingRequest method in PollingController");
            return getResponseEntityException(CLASS_NAME, "lenderPollingRequest", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting lenderPollingRequest method in PollingController");
            return getResponseEntityInternalServerException(CLASS_NAME, "lenderPollingRequest", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting lenderPollingRequest method in PollingController (reqDateTimestamp):" + reqDateTimestamp);
        return ResponseEntity.ok(pollingPOResponse);
    }

    /**
     *
     * Trader events
     *
     * @param reqDateTimestamp
     *            the reqDateTimestamp
     * @param headers
     *            Map of headers and values
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbstradertransactions/events/{reqDateTimestamp}",
            "/mbstradertransactions/events" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.TRADER_TRADE_EXECUTE, EntitlementRole.ADMIN,
            EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE,
            // CMMBSSTA01-882 - Read Only User access - Start
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_LE, EntitlementRole.MBSP_FM_TRADER_READ_ONLY_HE
            // CMMBSSTA01-882 - Read Only User access - End
    })
    @ResponseHeaderRequired
    public ResponseEntity<Object> traderPollingRequest(
            @PathVariable(value = "reqDateTimestamp") Optional<String> reqDateTimestamp,
            @RequestHeader Map<String, String> headers) {

        LOGGER.debug("Entering traderPollingRequest method in PollingController (reqDateTimestamp):" + reqDateTimestamp);
        PollingPO pollingPOResponse = null;
        try {
            pollingPOResponse = pollingControllerHelper.traderPollingRequest(reqDateTimestamp, headers);

        } catch (MBSBaseException ex) {
            LOGGER.error("Failed to fetch polling request for Trader " + ex);
            LOGGER.error("Exiting traderPollingRequest method in PollingController");
            return getResponseEntityException(CLASS_NAME, "traderPollingRequest", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting traderPollingRequest method in PollingController");
            return getResponseEntityInternalServerException(CLASS_NAME, "traderPollingRequest", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting traderPollingRequest method in PollingController (reqDateTimestamp):" + reqDateTimestamp);
        return ResponseEntity.ok(pollingPOResponse);
    }

    /**
     *
     * Clear the events
     *
     * @throws MBSBaseException
     */
    @RequestMapping(value = "/mbsclearevents", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public void clearAllEvents() throws MBSBaseException {
        LOGGER.debug("Entering clearAllEvents method in PollingController");
        pollingControllerHelper.clearAllEvents();
        LOGGER.debug("Exiting clearAllEvents method in PollingController");
    }

}
