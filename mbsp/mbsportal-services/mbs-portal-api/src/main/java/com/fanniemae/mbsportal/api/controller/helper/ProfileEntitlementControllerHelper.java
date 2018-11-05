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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.service.ProfileEntitlementService;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 *
 * Helper class for profile entitlement
 *
 * @author g8upjv
 *
 */
@Component
public class ProfileEntitlementControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileEntitlementControllerHelper.class);

    /**
     *
     * profileEntitlementServiceV1 ProfileEntitlementService
     */
    @Autowired
    private ProfileEntitlementService profileEntitlementServiceV1;

    /**
     *
     * cDXClientApi CDXClientApi
     */
    @Autowired
    private CDXClientApi cDXClientApi;

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
        LOGGER.debug("Entering selectDataServiceByMediaType method in ProfileEntitlementControllerHelper " + mediaType);
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE) || mediaType.equals(getVersion1json().toString())) {
            return profileEntitlementServiceV1;
        } else {
            return null;
        }
    }

    /**
     *
     * Get the profile
     *
     * @param headers
     *            Map having header and values
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public ProfileEntitlementPO saveOrUpdateProfile(Map<String, String> headers) throws MBSBaseException {
        LOGGER.debug("Entering saveOrUpdateProfile method in ProfileEntitlementControllerHelper");
        ProfileEntitlementPO profileEntitlementPOResponse = null;
        String mediaType = MediaType.APPLICATION_JSON_VALUE;

        ProfileEntitlementService profileEntitlementService = (ProfileEntitlementService) selectDataServiceByMediaType(
                mediaType.trim());

        if (Objects.equals(profileEntitlementService, null)) {
            LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
            throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        ProfileEntitlementPO profileEntitlementPO = cDXClientApi.getProfileFromToken(headers);
        profileEntitlementPOResponse = profileEntitlementService.saveOrUpdateProfile(profileEntitlementPO, false);
        profileEntitlementPOResponse = profileEntitlementService.getProfile(profileEntitlementPO.getUserName());
        LOGGER.debug("Exiting saveOrUpdateProfile method in ProfileEntitlementControllerHelper");
        return profileEntitlementPOResponse;
    }

    /**
     *
     * create profile
     *
     * @param profileEntitlementPO
     *            the profileEntitlementPO
     * @param headers
     *            Map having header and values
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     *
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public ProfileEntitlementPO createProfile(ProfileEntitlementPO profileEntitlementPO, Map<String, String> headers)
            throws MBSBaseException {
        LOGGER.info("creating profile for user: {}",profileEntitlementPO.getUserName());
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        ProfileEntitlementPO profileEntitlementPOResponse;
        // Select the required version of the service object
        if (!Objects.equals(profileEntitlementPO, null)) {
            BaseProcessor service = selectDataServiceByMediaType(mediaType.trim());
            if (!Objects.equals(service, null)) {
                ProfileEntitlementService profileEntitlementService = (ProfileEntitlementService) service;
                profileEntitlementPOResponse = profileEntitlementService.saveOrUpdateProfile(profileEntitlementPO,
                        true); //calling from UI case
            } else {
                LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
                throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                        MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        } else {
            LOGGER.error("Failed to create Profile, profileEntitlementPO(input) object is null");
            throw new MBSBusinessException("Failed to create Profile, profileEntitlementPO(input) object is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        LOGGER.info("created profile for user: {}",profileEntitlementPOResponse.getUserName());
        return profileEntitlementPOResponse;
    }

    /**
     *
     * Clears all the profile
     *
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public void clearAllProfiles() throws MBSBaseException {
        LOGGER.warn("Entering clearAllProfiles method in ProfileEntitlementControllerHelper");
        profileEntitlementServiceV1.clearAll();
        LOGGER.warn("Exiting clearAllProfiles method in ProfileEntitlementControllerHelper");
    }

    /**
     *
     * get profile for the user name passed
     *
     * @param userName
     *            the userName
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public ProfileEntitlementPO getProfile(String userName) throws MBSBaseException {
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        ProfileEntitlementPO profileEntitlementPOResponse;
        // Select the required version of the service object
        if (!Objects.equals(userName, null)) {
            BaseProcessor service = selectDataServiceByMediaType(mediaType.trim());
           if (!Objects.equals(service, null)) {
                ProfileEntitlementService profileEntitlementService = (ProfileEntitlementService) service;
               profileEntitlementPOResponse = profileEntitlementService.getProfile(userName);
           } else {
                LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
                throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                        MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        } else {
            LOGGER.error("username is empty");
            throw new MBSBusinessException("username is empty", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        LOGGER.info("returning profile for user: {}",profileEntitlementPOResponse.getUserName());
        return profileEntitlementPOResponse;
    }
}
