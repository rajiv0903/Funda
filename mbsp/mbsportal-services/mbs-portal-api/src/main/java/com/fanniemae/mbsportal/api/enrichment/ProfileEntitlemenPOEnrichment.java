/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *
 */

package com.fanniemae.mbsportal.api.enrichment;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.utils.ServiceUrlUtil;
import com.fanniemae.mbsportal.enrichment.BaseEnricher;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 24, 2018
 * @File: com.fanniemae.mbsportal.api.enrichment.ProfileEntitlemenPOEnrichment.java
 * @Revision:
 * @Description: ProfileEntitlemenPOEnrichment.java
 */
@Component
public class ProfileEntitlemenPOEnrichment<T extends TransformationObject> extends BaseEnricher<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ProfileEntitlemenPOEnrichment.class);

    /**
     * 
     * ServiceUrlUtil serviceUrlUtil variable
     */
    @Autowired
    ServiceUrlUtil serviceUrlUtil;

    /**
     * 
     * 
     * @param transformationObject
     *            the TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void enrich(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering enrich method in ProfileEntitlemenPOEnrichment");
        try {
            ProfileEntitlementPO profileEntitlementPO = enrichSourceObject(transformationObject);
            transformationObject.setSourcePojo(profileEntitlementPO);
        } catch (MBSBaseException e) {
            LOGGER.error("Exception when enriching data", e);
            throw e;
        }
        LOGGER.debug("Exiting enrich method in ProfileEntitlemenPOEnrichment");
    }

    /**
     * 
     * enrichSourceObject
     * 
     * @param transformationObject
     *            the TransformationObject
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     */
    ProfileEntitlementPO enrichSourceObject(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering enrichSourceObject method in ProfileEntitlemenPOEnrichment");
        ProfileEntitlementPO profileEntitlementPO = null;

        try {
            profileEntitlementPO = (ProfileEntitlementPO) transformationObject.getSourcePojo();
            PartyPO partyPO = (PartyPO) transformationObject.getTransformationDataMap()
                    .get(MBSPServiceConstants.PARTY_INFO);

            profileEntitlementPO.setPartyShortName(partyPO.getShortName());

            if (CollectionUtils.isNotEmpty(partyPO.getTspPartyLenders())) {

                profileEntitlementPO.setTspLenders(partyPO.getTspPartyLenders());
            }
            profileEntitlementPO.setUserConfig(serviceUrlUtil.getUserConfigPO(profileEntitlementPO.getUserName(), profileEntitlementPO.isFannieMaeUser()));

        } catch (Exception ex) {
            LOGGER.error("Exception when enriching party info, enrichSourceObject", ex);
            throw new MBSSystemException("Exception when enriching profilr info, enrichToPartyObject",
                    MBSExceptionConstants.SYSTEM_EXCEPTION, ex);
        }
        return profileEntitlementPO;
    }

}
