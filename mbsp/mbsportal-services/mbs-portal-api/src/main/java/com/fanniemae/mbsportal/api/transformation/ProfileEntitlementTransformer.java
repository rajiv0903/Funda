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

import com.fanniemae.mbsportal.api.po.UserConfigPO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSProfileRole;
import com.fanniemae.mbsportal.model.UserConfig;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 28, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.ProfileEntitlementTransformer.java
 * @Revision :
 * @Description: This class handles the transformations required for Model
 */
@Component
public class ProfileEntitlementTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileEntitlementTransformer.class);

    /**
     * 
     * @param transformationObject
     *            TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering transform method in ProfileEntitlementTransformer");
        ProfileEntitlementPO profileEntitlementPO = (ProfileEntitlementPO) transformationObject.getSourcePojo();
        MBSProfile mBSProfile = (MBSProfile) transformationObject.getTargetPojo();
        boolean mergedProfile = transformationObject.isMergedProfile();

        // Transform the Presentation object to domain object
        mBSProfile = convertToModel(profileEntitlementPO, mBSProfile, mergedProfile);
        transformationObject.setTargetPojo(mBSProfile);
        LOGGER.debug("Exiting transform method in ProfileEntitlementTransformer");
        return transformationObject;
    }

    /**
     * 
     * Convert to Model object from ProfileEntitlementPO
     * 
     * @param profileEntitlementPO
     *            ProfileEntitlementPO
     * @return mBSProfile MBSProfile
     * @throws MBSBaseException
     */
    private MBSProfile convertToModel(ProfileEntitlementPO profileEntitlementPO, MBSProfile mBSProfile,
            boolean mergedProfile) throws MBSBaseException {
        
        mBSProfile.setFirstName(profileEntitlementPO.getFirstName());
        mBSProfile.setLastName(profileEntitlementPO.getLastName());
        mBSProfile.setEmailAddress(profileEntitlementPO.getEmailAddress());
        mBSProfile.setMobileNumber(profileEntitlementPO.getMobileNumber());
        mBSProfile.setWorkNumber(profileEntitlementPO.getWorkNumber());
        mBSProfile.setCustomerName(profileEntitlementPO.getCustomerName());
        mBSProfile.setUserName(profileEntitlementPO.getUserName());
        mBSProfile.setSellerServicerNumber(profileEntitlementPO.getSellerServicerNumber());
        mBSProfile.setDealerOrgName(profileEntitlementPO.getDealerOrgName());
        mBSProfile.setDealerOrgId(profileEntitlementPO.getDealerOrgId());
        // Role From PO
        List<MBSProfileRole> rolesFromPO = null;
        if (profileEntitlementPO.getRoles() != null && !profileEntitlementPO.getRoles().isEmpty()) {
            rolesFromPO = new ArrayList<>();
        }
        for (ProfileEntitlementRolePO role : profileEntitlementPO.getRoles()) {
            MBSProfileRole mbsRole = new MBSProfileRole();
            mbsRole.setName(role.getName());
            mbsRole.setAppCode(role.getAppCode());
            mbsRole.setPermissions(role.getPermissions());
            rolesFromPO.add(mbsRole);
        }
        // End of Roles from PO

        // Merge Role
        List<MBSProfileRole> existingRoles = mBSProfile.getRoles();

        if (existingRoles == null) {
            existingRoles = new ArrayList<>();
        }

        if (rolesFromPO == null) {
            rolesFromPO = new ArrayList<>();
        }

        Set<MBSProfileRole> roleSet = new HashSet<MBSProfileRole>();
        /*
         * CMMBSSTA01-1382: Automated way to clean up stale Role
         */
        if (mergedProfile) {
            roleSet.addAll(existingRoles);
        }
        roleSet.addAll(rolesFromPO);

        List<MBSProfileRole> mergedRoles = new ArrayList<>(roleSet);

        mBSProfile.setRoles(mergedRoles);
        // End of Merging

        // Merge Seller Details
        List<String> existingSellers = mBSProfile.getSellerServicerDetails();
        List<String> sellersFromPO = profileEntitlementPO.getSellerServicerDetails();

        if (existingSellers == null) {
            existingSellers = new ArrayList<>();
        }

        if (sellersFromPO == null) {
            sellersFromPO = new ArrayList<>();
        }

        Set<String> sellerServiceDetailsSet = new HashSet<String>();
        /*
         * CMMBSSTA01-1382: Automated way to clean up stale Role
         */
        if (mergedProfile) {
            sellerServiceDetailsSet.addAll(existingSellers);
        }
        sellerServiceDetailsSet.addAll(sellersFromPO);

        List<String> mergedSellerServiceDetails = new ArrayList<>(sellerServiceDetailsSet);

        mBSProfile.setSellerServicerDetails(mergedSellerServiceDetails);
        // End of Seller Merging

        mBSProfile.setInstitutionId(profileEntitlementPO.getInstitutionId());
        mBSProfile.setDefaultSellerServicerNumber(profileEntitlementPO.getDefaultSellerServicerNumber());
        mBSProfile.setLenderDetails(profileEntitlementPO.getLenderDetails());
        mBSProfile.setFannieMaeUser(profileEntitlementPO.isFannieMaeUser());

        if (StringUtils.isNotBlank(profileEntitlementPO.getBrsUserName())) {
            mBSProfile.setBrsUserName(profileEntitlementPO.getBrsUserName());
        }
        // end of merging

        // CMMBSSTA01-1371: (Lender page) TSP chooses / switches a lender
        List<String> userRoles = profileEntitlementPO.getRoles().stream().map(ProfileEntitlementRolePO::getName).map(String::toLowerCase).collect(Collectors.toList());
        List<String> tspRole = new ArrayList<String>(Arrays.asList(EntitlementRole.TSP_TRADE_EXECUTE.toLowerCase()));
        
        if (CollectionUtils.containsAny(userRoles, tspRole)) {
            profileEntitlementPO.setTspUser(true);
        }
        
        mBSProfile.setTspUser(profileEntitlementPO.isTspUser());
        // End
        //Populate the User Given URL for user specific- if Given else keep as it is
        UserConfig userConfig  = prepareUserConfig(mergedProfile,profileEntitlementPO.getUserConfig(),mBSProfile.getUserConfig());
        mBSProfile.setUserConfig(userConfig);
        //end
        return mBSProfile;
    }
    
    /**
     * prepare user config
     * @param isProfileMerged
     * @param userConfigPO
     * @param userConfig
     * @return
     */
    private UserConfig prepareUserConfig(boolean isProfileMerged, UserConfigPO userConfigPO,UserConfig userConfig) {
        if (isProfileMerged || Objects.isNull(userConfig)) {
            userConfig = new UserConfig();
        }
        if(Objects.nonNull(userConfigPO)){
            //User has given direct url
            if(StringUtils.isNotBlank(userConfigPO.getDirectApiUrl())){
                LOGGER.info("setting DirectApiUrl {} in prepareUserConfig",userConfigPO.getDirectApiUrl());
                userConfig.setDirectApiUrl(userConfigPO.getDirectApiUrl());
            }
            //WS Url to be set from SoapUI
            if(StringUtils.isNotBlank(userConfigPO.getWebSocketUrl())){
                LOGGER.info("setting WebSocketUrl {} in prepareUserConfig",userConfigPO.getWebSocketUrl());
                userConfig.setWebSocketUrl(userConfigPO.getWebSocketUrl());
            }
        }
        return userConfig;
    }
}
