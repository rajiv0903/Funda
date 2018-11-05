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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.helper.UILoggingControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.UILoggingMessagePO;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 *
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 3, 2018
 * @Time 3:38:14 PM com.fanniemae.mbsportal.api.controller
 *       UILoggingController.java
 * @Description: CMMBSSTA01-941 : (Tech) Logging API to capture UI logs
 */
@RestController
public class UILoggingController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("UI Logging Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UILoggingController.class);
    /**
     *
     * uiLoggingControllerHelper UILoggingControllerHelper
     */
    @Autowired
    UILoggingControllerHelper uiLoggingControllerHelper;

    /**
     *
     * @param uiLoggingMessagePOs
     *            the List of uiLoggingMessagePO
     * @return ResponseEntity<Void>
     */
    @RequestMapping(value = "/mbsprofileentitlements/ui/logging", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.TRADER_TRADE_EXECUTE, EntitlementRole.ADMIN,
            EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE,
            EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.TSP_TRADE_EXECUTE,
            // CMMBSSTA01-882 - Read Only User access - Start
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_LE, EntitlementRole.MBSP_FM_TRADER_READ_ONLY_HE
            // CMMBSSTA01-882 - Read Only User access - End
    })
    @ResponseHeaderRequired
    public ResponseEntity<Void> logUIMsg(@RequestBody List<UILoggingMessagePO> uiLoggingMessagePOs) {

        LOGGER.debug("Entering logUIMsg method in UILoggingController");
        try {
            uiLoggingControllerHelper.logUIMsg(uiLoggingMessagePOs);

        } catch (MBSBaseException ex) {
            // TODO-change it to reurn ResponseEntity<Object> instead of void?
            LOGGER.error("Exiting logUIMsg in UILoggingController");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception ex) {
            LOGGER.error("Exiting logUIMsg in UILoggingController");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        LOGGER.debug("Exiting logUIMsg in UILoggingController");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
