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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.helper.PartyControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: May 22, 2018
 * @File: com.fanniemae.mbsportal.api.controller.PartyController.java
 * @Revision:
 * @Description: PartyController.java
 */
@RestController
public class PartyController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Party Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartyController.class);

    /**
     *
     * partyControllerHelper PartyControllerHelper
     */
    @Autowired
    PartyControllerHelper partyControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "PartyController";

    /**
     *
     * create Party
     *
     * @param partyPO
     *            the PartyPO
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/mbsparty", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
            "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> saveOrUpdateParty(@RequestBody PartyPO partyPO) {

        LOGGER.debug("Entering saveOrUpdateParty method in PartyControllerHelper");

        PartyPO partyPOResponse = null;

        try {
            partyPOResponse = partyControllerHelper.saveOrUpdateParty(partyPO);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting saveOrUpdateParty in PartyControllerHelper");
            return getResponseEntityException(CLASS_NAME, "saveOrUpdateParty", ex);

        } catch (Exception ex) {
            LOGGER.error("Failed to save or Update,  exception in the service call" + ex);
            LOGGER.error("Exiting saveOrUpdateParty in PartyControllerHelper");
            return getResponseEntityInternalServerException(CLASS_NAME, "saveOrUpdateParty", ex);
        }
        LOGGER.debug("Exiting saveOrUpdateParty in PartyControllerHelper");
        return ResponseEntity.ok(partyPOResponse);

    }

    /**
     *
     * get the Party based on username
     *
     * @param username
     *            the username
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbsparty/{sellerSerivcerNumber}" }, method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getParty(@PathVariable("sellerSerivcerNumber") String sellerSerivcerNumber) {

        LOGGER.debug("Entering getParty method in PartyControllerHelper");
        PartyPO partyPOResponse = null;

        try {
            partyPOResponse = partyControllerHelper.getParty(sellerSerivcerNumber);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getParty in PartyControllerHelper");
            return getResponseEntityException(CLASS_NAME, "getParty", ex);

        } catch (Exception ex) {
            LOGGER.error("Failed to getParty,  exception in the service call" + ex);
            LOGGER.error("Exiting getParty in PartyControllerHelper");
            return getResponseEntityInternalServerException(CLASS_NAME, "getParty", ex);
        }
        LOGGER.debug("Exiting getParty in PartyControllerHelper");
        return ResponseEntity.ok(partyPOResponse);
    }

    /**
     *
     * clear all profiles
     *
     * @throws MBSBaseException
     */
    @RequestMapping(value = "/mbsparty/{sellerSerivcerNumber}", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    public void deleteParty(@PathVariable("sellerSerivcerNumber") String sellerSerivcerNumber) throws MBSBaseException {

        LOGGER.debug("Entering deleteParty method in PartyControllerHelper");
        partyControllerHelper.deleteParty(sellerSerivcerNumber);
        LOGGER.debug("Exiting deleteParty in PartyControllerHelper");
    }

    /**
     *
     * clear all profiles
     *
     * @throws MBSBaseException
     */
    @RequestMapping(value = "/mbsparty", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE,
            "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    public void deleteParties() throws MBSBaseException {

        LOGGER.debug("Entering deleteParties method in PartyControllerHelper");
        partyControllerHelper.deleteParties();
        LOGGER.debug("Exiting deleteParties in PartyControllerHelper");
    }

}
