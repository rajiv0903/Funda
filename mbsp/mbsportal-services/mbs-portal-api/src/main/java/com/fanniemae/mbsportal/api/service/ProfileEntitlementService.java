/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.enrichment.Enricher;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * @author gaur5c
 */
@SuppressWarnings("rawtypes")
@Service
public class ProfileEntitlementService extends BaseProcessor {

    @Autowired
    @Lazy
    PartyService partyService;
    /**
     * 
     * LOGGER Logger
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * mbspProperties MbspProperties
     */
    @Autowired
    MbspProperties mbspProperties;

    /**
     * 
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
    MBSObjectCreator mbsObjectCreator;

    /**
     * 
     * 
     * @param profileEntitlementValidator
     *            the profileEntitlementValidator
     * @param profileEntitlementTransformer
     *            the profileEntitlementTransformer
     * @param profileEntitlemenPersister
     *            the profileEntitlemenPersister
     * @param profileEntitlemenPOTransformer
     *            the profileEntitlemenPOTransformer
     * @param profileEntitlemenPOEnrichment
     *            the profileEntitlemenPOEnrichment
     */
    @SuppressWarnings("unchecked")
    @Autowired
    public ProfileEntitlementService(Validator profileEntitlementValidator, Transformer profileEntitlementTransformer, 
            Persister profileEntitlemenPersister, Transformer profileEntitlemenPOTransformer,
            Enricher profileEntitlemenPOEnrichment) {

        super.validator = profileEntitlementValidator;
        super.transformer = profileEntitlementTransformer;
        super.persister = profileEntitlemenPersister;
        super.poTransformer = profileEntitlemenPOTransformer;
        super.poEnricher = profileEntitlemenPOEnrichment;
    }

    /**
     * 
     * @param profileEntitlementPO
     *            the profileEntitlementPO
     * @param mergedProfile
     *            whether to merged with existing profile or not
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public ProfileEntitlementPO saveOrUpdateProfile(ProfileEntitlementPO profileEntitlementPO, boolean mergedProfile)
            throws MBSBaseException {
        LOGGER.debug("Entering saveOrUpdateProfile method in ProfileEntitlementService");
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        transformationObject.setSourcePojo(profileEntitlementPO);
        /*
         * CMMBSSTA01-1382: Automated way to clean up stale Role
         */
        transformationObject.setMergedProfile(mergedProfile);

        MBSProfile mBSProfile = null;
        // Get the existing profile for merging
        try {
            mBSProfile = this.getMBSProfileFromRepo(profileEntitlementPO.getUserName());

        } catch (Exception ex) {
            LOGGER.warn("saveProfile falied for user : {} ", profileEntitlementPO.getUserName());

        }
        if (mBSProfile == null) {
            mBSProfile = new MBSProfile();
        }
        transformationObject.setTargetPojo(mBSProfile);
        // end of merging
        
        //If profile merge i.e SOAP UI and FannieMae User then populate dummy SSN
        if(mergedProfile && profileEntitlementPO.isFannieMaeUser()){
            profileEntitlementPO.setSellerServicerNumber(""+mbspProperties.getFnmSellerSerivcerNo());
        }
        //end
        
        if(!NumberUtils.isDigits(profileEntitlementPO.getSellerServicerNumber())){
            throw new MBSBusinessException("Seller Service Number is not numeric for:" + profileEntitlementPO.getUserName(),
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        
        //Enrich with Party Short Name and Lender Details
        PartyPO partyPO = partyService.getParty(profileEntitlementPO.getSellerServicerNumber());
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PARTY_INFO, partyPO);
        //End
        
        super.processRequest(transformationObject);
        LOGGER.debug("Exiting saveOrUpdateProfile method in ProfileEntitlementService");
        return (ProfileEntitlementPO) transformationObject.getSourcePojo();
    }

    /**
     * 
     * @param userName
     *            the userName
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public ProfileEntitlementPO getProfile(String userName) throws MBSBaseException {
        LOGGER.debug("Entering getProfile method in ProfileEntitlementService");
        LOGGER.debug("Entering getProfile method in ProfileEntitlementService: userName:" + userName);
        MBSProfile mBSProfile = this.getMBSProfileFromRepo(userName);
        if (mBSProfile == null) {
            throw new MBSBusinessException("No Profile information for the username:" + userName,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        transformationObject.setTargetPojo(mBSProfile);
        transformationObject = poTransformer.transform(transformationObject);
        
        // Enrich with Party Short Name and Lender Details
        enrichPartyInfo(userName, transformationObject);
        // end
        
        LOGGER.debug("Exiting getProfile method in ProfileEntitlementService");
        return (ProfileEntitlementPO) transformationObject.getSourcePojo();
    }

    /**
     * 
     * Get the list of profiles for the entity
     * 
     * @param dealerOrgId
     *            the dealerOrgId
     * @return List<ProfileEntitlementPO>
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public List<ProfileEntitlementPO> getUserNames(String dealerOrgId) throws MBSBaseException {

        LOGGER.debug("Entering getUserNames method in ProfileEntitlementService: dealerOrgId:" + dealerOrgId);
        if (dealerOrgId == null || dealerOrgId.isEmpty()) {
            LOGGER.warn("dealerOrgId is missing/empty");
            throw new MBSBusinessException("dealerOrgId is missing/empty");
        }
        List<MBSProfile> mBSProfileLst = ((MBSProfileDao) persister.getBaseDao()).getMBSProfile(dealerOrgId);
        List<ProfileEntitlementPO> profileLst = new ArrayList<ProfileEntitlementPO>();
        if (CollectionUtils.isEmpty(mBSProfileLst)) {
            LOGGER.warn("There are no users for this organisation/entity");
        }
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        for (MBSProfile mbsProfile : mBSProfileLst) {
            
            transformationObject = mbsObjectCreator.getTransformationObject();
            transformationObject.setTargetPojo(mbsProfile);
            transformationObject = super.poTransformer.transform(transformationObject);
            
            // Enrich with Party Short Name and Lender Details
            enrichPartyInfo(mbsProfile.getUserName(), transformationObject);
            // end
            
            profileLst.add((ProfileEntitlementPO) transformationObject.getSourcePojo());
        }

        LOGGER.debug("Exiting getUserNames method in ProfileEntitlementService");
        return profileLst;
    }
    @Deprecated
    //TODO: seems like not used. remove it
    public List<ProfileEntitlementPO> getProfilesForUsers(List<String> userNames) throws MBSBaseException {
    	List<MBSProfile> mBSProfileLst = ((MBSProfileDao) super.persister.getBaseDao()).getProfileByUserNames(userNames);
        List<ProfileEntitlementPO> profileLst = new ArrayList<ProfileEntitlementPO>();
        if (CollectionUtils.isEmpty(mBSProfileLst)) {
            LOGGER.warn("There are no users for this organisation/entity");
        }
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        for (MBSProfile mbsProfile : mBSProfileLst) {
            
            transformationObject = mbsObjectCreator.getTransformationObject();
            transformationObject.setTargetPojo(mbsProfile);
            transformationObject = poTransformer.transform(transformationObject);
            
            // Enrich with Party Short Name and Lender Details
            enrichPartyInfo(mbsProfile.getUserName(), transformationObject);
            // end
            
            profileLst.add((ProfileEntitlementPO) transformationObject.getSourcePojo());
        }

        LOGGER.debug("Exiting getUserNames method in ProfileEntitlementService");
    	return profileLst;
    }

    /**
     * 
     * @author gaur5c
     * @param userName
     *            the userName
     * @return MBSProfile
     * @throws MBSBaseException
     * @Description: To mock and bypass the Unit Testing Dao
     */
    protected MBSProfile getMBSProfileFromRepo(String userName) throws MBSBaseException {
        return (MBSProfile) super.persister.getBaseDao().getById(userName);
    }

    /**
     * 
     * This method clears the Product records
     * 
     * @throws MBSBaseException
     */
    public void clearAll() throws MBSBaseException {
        persister.clearAll();
    }
    
    /**
     * 
     * This is to enrich Profile PO with Party Info - Param Transaformation Object should have 
     * Presentation Object and it will throw error for any user not having SSN unless if it is not 
     * Trader 
     * 
     * @param userName
     * @param transformationObject
     * @throws MBSBusinessException
     * @throws MBSBaseException
     */
    private void enrichPartyInfo(String userName, TransformationObject transformationObject)
            throws MBSBusinessException, MBSBaseException {
        
        LOGGER.debug("Entering enrichPartyInfo method in ProfileEntitlementService: userName:" + userName);
        
        String sellerSerivcerNo = ((ProfileEntitlementPO) transformationObject.getSourcePojo()).getSellerServicerNumber();
        if(!NumberUtils.isDigits(sellerSerivcerNo)){
            throw new MBSBusinessException("Seller Service Number is not numeric for:" + userName,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        PartyPO partyPO = partyService.getParty(sellerSerivcerNo);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PARTY_INFO, partyPO);
        super.poEnricher.enrich(transformationObject);
        
        LOGGER.debug("Exiting enrichPartyInfo method in ProfileEntitlementService");
    }

}
