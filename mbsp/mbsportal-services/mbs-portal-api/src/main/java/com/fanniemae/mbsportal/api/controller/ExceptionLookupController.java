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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.helper.ExceptionLookupControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class is the controller for the Exception lookup related transactions
 *
 * @author g8upjv
 *
 */
@RestController
public class ExceptionLookupController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Exception Lookup Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLookupController.class);

    /**
     *
     * ExceptionLookupControllerHelper passes the call from controller to
     * service
     */
    @Autowired
    ExceptionLookupControllerHelper exceptionLookupControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "ExceptionLookupController";

    /**
     *
     * Purpose: This method calls the ExceptionLookupService create method to
     * create Exception Info for version1
     *
     * @param exceptionLookupPOLst
     *            List<ExceptionLookupPO> The body parameter with values from
     *            the input is taken from the presentation object
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/mbsexceptions", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> createExceptionLookupData(@RequestHeader Map<String, String> headers,
            @RequestBody List<ExceptionLookupPO> exceptionLookupPOLst) {

        LOGGER.debug("Entering createExceptionLookupData method in ExceptionLookupController");
        List<ExceptionLookupPO> exceptionLookupPOLstResponse = null;
        try {
            exceptionLookupPOLstResponse = exceptionLookupControllerHelper
                    .createExceptionLookupData(exceptionLookupPOLst);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting createExceptionLookupData method in ExceptionLookupController");
            return getResponseEntityException(CLASS_NAME, "getExceptionLookupData", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting createExceptionLookupData method in ExceptionLookupController");
            return getResponseEntityInternalServerException(CLASS_NAME, "createExceptionLookupData", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting createExceptionLookupData method in ExceptionLookupController");
        return ResponseEntity.ok(exceptionLookupPOLstResponse);
    }

    /**
     *
     * Purpose: This method clears all the records in Exceptions
     *
     * @throws MBSBaseException
     */
    @RequestMapping(value = { "/mbsexceptions",
            "/mbsexceptions/{errorCode}" }, method = RequestMethod.DELETE, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public void clearExceptionLookupData(@RequestHeader Map<String, String> headers,
            @PathVariable Optional<String> errorCode) throws MBSBaseException {
        LOGGER.debug("Entering clearAllExceptionLookupData method in ExceptionLookupController");
        exceptionLookupControllerHelper.clearAllExceptionLookupData(errorCode);
        LOGGER.debug("Exiting clearAllExceptionLookupData method in ExceptionLookupController");
    }

    /**
     *
     * Purpose: This method retrieve the exception info object by calling the
     * service
     *
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbsexceptions", "/mbsexceptions/{errorCode}" }, method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.TRADER_TRADE_EXECUTE,
            EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getExceptionLookupData(@RequestHeader Map<String, String> headers,
            @PathVariable Optional<String> errorCode) {
        LOGGER.debug("Entering getExceptionLookupData method in ExceptionLookupController");
        List<ExceptionLookupPO> lstExceptionLookupPO;
        try {
            lstExceptionLookupPO = exceptionLookupControllerHelper.getExceptionLookupData(errorCode);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getExceptionLookupData method in ExceptionLookupController");
            return getResponseEntityException(CLASS_NAME, "getExceptionLookupData", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting getExceptionLookupData method in ExceptionLookupController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getExceptionLookupData", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting getExceptionLookupData method in ExceptionLookupController");
        return ResponseEntity.ok(lstExceptionLookupPO);
    }
}
