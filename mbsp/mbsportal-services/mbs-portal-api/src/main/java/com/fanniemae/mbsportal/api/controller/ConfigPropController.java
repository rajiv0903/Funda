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

import com.fanniemae.mbsportal.api.controller.helper.ConfigPropControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.ConfigPropPO;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 *
 * @author: Rajiv Chaudhuri
 * @Date: Jul 6, 2018
 * @File: com.fanniemae.mbsportal.api.controller.ConfigController.java
 * @Revision:
 * @Description: ConfigController.java
 */
@RestController
public class ConfigPropController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Configuration Properties Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPropController.class);
    /**
     *
     * configControllerHelper ConfigControllerHelper
     */
    @Autowired
    ConfigPropControllerHelper configPropControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "ConfigPropController";

    /**
     *
     * @param headers
     *            Map of headers and values
     * @param key
     *            the key
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbsconfig/{key}" }, method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getConfigProp(@RequestHeader Map<String, String> headers,
            @PathVariable(value = "key") String key) {

        ConfigPropPO configPO = null;

        try {
            LOGGER.debug("Entering getKey getConfigProp in ConfigPropController" + key);
            configPO = configPropControllerHelper.getConfigProp(key);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getKey getConfigProp in ConfigPropController");
            return getResponseEntityException(CLASS_NAME, "getConfigProp", ex);

        } catch (Exception ex) {
            LOGGER.error("Exiting getKey getConfigProp in ConfigPropController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getConfigProp", ex);

        }
        LOGGER.debug("Exiting getKey getConfigProp in ConfigPropController");
        return ResponseEntity.ok(configPO);
    }

    /**
     *
     * create Party
     *
     * @param partyPO
     *            the PartyPO
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/mbsconfig", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
            "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> saveOrUpdateConfigProp(@RequestBody ConfigPropPO configPropPO) {

        ConfigPropPO configPropPOResponse = null;

        try {
            LOGGER.debug("Entering saveOrUpdateConfigProp method in ConfigPropController");
            configPropPOResponse = configPropControllerHelper.saveOrUpdateConfigProp(configPropPO);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting saveOrUpdateConfigProp in ConfigPropController");
            return getResponseEntityException(CLASS_NAME, "saveOrUpdateParty", ex);

        } catch (Exception ex) {
            LOGGER.error("Exiting saveOrUpdateConfigProp in ConfigPropController");
            LOGGER.error("Failed to save or Update,  exception in the service call" + ex);
            return getResponseEntityInternalServerException(CLASS_NAME, "saveOrUpdateParty", ex);
        }
        LOGGER.debug("Exiting saveOrUpdateConfigProp in ConfigPropController");
        return ResponseEntity.ok(configPropPOResponse);

    }

}
