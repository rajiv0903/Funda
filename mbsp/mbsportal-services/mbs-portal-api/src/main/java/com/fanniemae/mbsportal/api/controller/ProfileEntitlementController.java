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

import com.fanniemae.mbsportal.api.controller.helper.ProfileEntitlementControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author gaur5c
 * @Descripiption - Controller to exposed API Endpoint related to User Profile
 *                and Login Service
 */
@RestController
public class ProfileEntitlementController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Profile Entitlement Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileEntitlementController.class);

    /**
     *
     * profileEntitlementControllerHelper ProfileEntitlementControllerHelper
     */
    @Autowired
    ProfileEntitlementControllerHelper profileEntitlementControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "ProfileEntitlementController";

    /**
     *
     * get profile entitlements
     *
     * @param headers
     *            Map having headers and values
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbsprofileentitlements",
            "/capital-markets/trading/securities/mbs/profileentitlements" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @ResponseHeaderRequired
    public ResponseEntity<Object> saveOrUpdateProfile(@RequestHeader Map<String, String> headers) {

        LOGGER.debug("Entering saveOrUpdateProfile in ProfileEntitlementController");
        ProfileEntitlementPO profileEntitlementPOResponse = null;
        try {
            profileEntitlementPOResponse = profileEntitlementControllerHelper.saveOrUpdateProfile(headers);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting saveOrUpdateProfile in ProfileEntitlementController");
            return getResponseEntityException(CLASS_NAME, "getEntitledProfile", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting saveOrUpdateProfile in ProfileEntitlementController");
            return getResponseEntityInternalServerException(CLASS_NAME, "saveOrUpdateProfile", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting saveOrUpdateProfile in ProfileEntitlementController");
        return ResponseEntity.ok(profileEntitlementPOResponse);
    }

    /**
     *
     * create profile entitlements
     *
     * @param profileEntitlementPO
     *            the profileEntitlementPO
     * @param headers
     *            Map having headers and values
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/mbsprofileentitlements", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> createProfile(@RequestBody ProfileEntitlementPO profileEntitlementPO,
            @RequestHeader Map<String, String> headers) {

        LOGGER.debug("Entering createProfile method in ProfileEntitlementController");

        ProfileEntitlementPO profileEntitlementPOResponse = null;

        try {
            profileEntitlementPOResponse = profileEntitlementControllerHelper.createProfile(profileEntitlementPO,
                    headers);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting createProfile in ProfileEntitlementController");
            return getResponseEntityException(CLASS_NAME, "createEntitledProfile", ex);

        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting createProfile in ProfileEntitlementController");
            return getResponseEntityInternalServerException(CLASS_NAME, "createProfile", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End

        }
        LOGGER.debug("Exiting createProfile in ProfileEntitlementController");
        return ResponseEntity.ok(profileEntitlementPOResponse);

    }

    /**
     *
     * clear all profiles
     *
     * @throws MBSBaseException
     */
    @RequestMapping(value = "/mbsprofileentitlements", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    public void clearAllProfiles() throws MBSBaseException {

        LOGGER.debug("Entering clearAllProfiles method in ProfileEntitlementController");
        profileEntitlementControllerHelper.clearAllProfiles();
        LOGGER.debug("Exiting clearAllProfiles in ProfileEntitlementController");
    }

    /**
     *
     * get the profile based on username
     *
     * @param username
     *            the username
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbsprofileentitlements/{username}" }, method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getProfile(@PathVariable("username") String username) {

        LOGGER.debug("Entering getprofile method in ProfileEntitlementController");
        ProfileEntitlementPO profileEntitlementPOResponse = null;
        try {
            profileEntitlementPOResponse = profileEntitlementControllerHelper.getProfile(username);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getprofile in ProfileEntitlementController");
            return getResponseEntityException(CLASS_NAME, "getProfile", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting getprofile in ProfileEntitlementController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getProfile", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting getprofile in ProfileEntitlementController");
        return ResponseEntity.ok(profileEntitlementPOResponse);
    }
}
