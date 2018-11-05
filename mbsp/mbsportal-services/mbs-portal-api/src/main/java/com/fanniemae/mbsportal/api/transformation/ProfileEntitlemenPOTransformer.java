/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.transformation;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSProfileRole;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * This is the transformer class to transform Profile Entitlement domain object
 * to PO
 * 
 * @author g8upjv
 *
 * @param <T>
 */
@Component
public class ProfileEntitlemenPOTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileEntitlemenPOTransformer.class);

    
    /**
     * 
     * mbspProperties  config obj
     */
    @Autowired
    MbspProperties mbspProperties;
    /**
     * 
     * This method does the transformation of object from domain to PO
     * 
     * @param transformationObject
     *            TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering transform method in ProfileEntitlemenPOTransformer");
        MBSProfile mBSProfile = (MBSProfile) transformationObject.getTargetPojo();
        ProfileEntitlementPO profileEntitlementPO = convertToProfileEntitlementPO(mBSProfile);
        transformationObject.setSourcePojo(profileEntitlementPO);
        LOGGER.debug("Exiting transform method in ProfileEntitlemenPOTransformer");
        return transformationObject;
    }

    /**
     * 
     * Purpose: This does the conversion from MBSProfile to ProfileEntitlementPO
     * object
     *
     * @param mBSProfile
     *            The TransactionRequest object
     * @return ProfileEntitlementPO The presentation object of MBSProfile object
     * @throws MBSBaseException
     */
    private ProfileEntitlementPO convertToProfileEntitlementPO(MBSProfile mBSProfile) throws MBSBaseException {
        ProfileEntitlementPO profileEntitlementPO = new ProfileEntitlementPO();
        try {
            profileEntitlementPO.setFirstName(mBSProfile.getFirstName());
            profileEntitlementPO.setLastName(mBSProfile.getLastName());
            profileEntitlementPO.setEmailAddress(mBSProfile.getEmailAddress());
            profileEntitlementPO.setMobileNumber(mBSProfile.getMobileNumber());
            profileEntitlementPO.setWorkNumber(mBSProfile.getWorkNumber());
            profileEntitlementPO.setCustomerName(mBSProfile.getCustomerName());
            profileEntitlementPO.setUserName(mBSProfile.getUserName());
            profileEntitlementPO.setSellerServicerNumber(mBSProfile.getSellerServicerNumber());
            profileEntitlementPO.setDealerOrgName(mBSProfile.getDealerOrgName());
            profileEntitlementPO.setDealerOrgId(mBSProfile.getDealerOrgName());
            for (MBSProfileRole role : mBSProfile.getRoles()) {
                profileEntitlementPO.addRole(createRole(role));
            }
            profileEntitlementPO.setSellerServicerDetails(mBSProfile.getSellerServicerDetails());
            profileEntitlementPO.setInstitutionId(mBSProfile.getInstitutionId());
            profileEntitlementPO.setDefaultSellerServicerNumber(mBSProfile.getDefaultSellerServicerNumber());
            profileEntitlementPO.setLenderDetails(mBSProfile.getLenderDetails());
            profileEntitlementPO.setFannieMaeUser(mBSProfile.isFannieMaeUser());
            profileEntitlementPO.setBrsUserName(mBSProfile.getBrsUserName());
            profileEntitlementPO.setTspUser(mBSProfile.isTspUser());
            
            //if Trader - Assign default SSN
            if(profileEntitlementPO.isFannieMaeUser() && StringUtils.isBlank(profileEntitlementPO.getSellerServicerNumber())){
                profileEntitlementPO.setSellerServicerNumber(""+mbspProperties.getFnmSellerSerivcerNo());
            }
            //Done

            LOGGER.debug(profileEntitlementPO.toString());
        } catch (Exception ex) {
            LOGGER.error("Error when transforming Profile object from data store ", ex);
            throw new MBSSystemException("Error when transforming Profile object from data store ",
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        return profileEntitlementPO;
    }

	private ProfileEntitlementRolePO createRole(MBSProfileRole role) {
		ProfileEntitlementRolePO rolePO = new ProfileEntitlementRolePO();
        rolePO.setName(role.getName());
        rolePO.setAppCode(role.getAppCode());
        rolePO.setPermissions(role.getPermissions());
		return rolePO;
	}
}
