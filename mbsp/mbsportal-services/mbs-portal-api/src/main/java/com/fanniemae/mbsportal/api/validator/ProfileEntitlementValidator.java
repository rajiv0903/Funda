/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.validator;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.BaseValidator;

@Component
public class ProfileEntitlementValidator<T extends TransformationObject> extends BaseValidator<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ProfileEntitlementValidator.class);

    /**
     * 
     * Purpose: This method validates the ProfileEntitlement PO object
     * 
     * @param transformationObject
     *            TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void validate(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering validate method in ProfileEntitlementPOValidator");

        ProfileEntitlementPO profileEntitlementPO = (ProfileEntitlementPO) transformationObject.getSourcePojo();

        // username
        if (Objects.isNull(profileEntitlementPO.getUserName())) {
            LOGGER.error("Bad Request:Profile Entitlement Mandatory username Missing/empty.");
            throw new MBSBusinessException("Bad Request:Profile Entitlement username Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // first name
        if (Objects.isNull(profileEntitlementPO.getFirstName())) {
            LOGGER.error("Bad Request:Profile Entitlement Mandatory firstname Missing/empty.");
            throw new MBSBusinessException("Bad Request:Profile Entitlement firstname Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // last name
        if (Objects.isNull(profileEntitlementPO.getLastName())) {
            LOGGER.error("Bad Request:Profile Entitlement Mandatory lastname Missing/empty.");
            throw new MBSBusinessException("Bad Request:Profile Entitlement lastname Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // roles
        if (profileEntitlementPO.getRoles() == null || profileEntitlementPO.getRoles().isEmpty()) {
            LOGGER.error("Bad Request:Profile Entitlement Mandatory roles Missing/empty.");
            throw new MBSBusinessException("Bad Request:Profile Entitlement roles Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }

  
    }
}
