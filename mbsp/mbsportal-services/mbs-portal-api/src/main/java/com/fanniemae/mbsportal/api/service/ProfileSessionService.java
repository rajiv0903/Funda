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

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.dao.MBSProfileSessionDao;
import com.fanniemae.mbsportal.model.MBSProfileSession;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 13, 2018
 * @File: com.fanniemae.mbsportal.api.service.ProfileSessionService.java
 * @Revision :
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@SuppressWarnings("rawtypes")
@Service
public class ProfileSessionService extends BaseProcessor {

    /**
     * 
     * LOGGER Logger
     */
    @InjectLog
    private Logger LOGGER;

    /**
     * 
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
    MBSObjectCreator mbsObjectCreator;

    /**
     * 
     * 
     * @param profileSessionValidator
     *            the profileSessionValidator
     * @param profileSessionTransformer
     *            the profileSessionTransformer
     * @param profileSessionPersister
     *            the profileSessionPersister
     * @param profileSessionPOTransformer
     *            the profileSessionPOTransformer
     */
    @SuppressWarnings("unchecked")
    @Autowired
    public ProfileSessionService(Validator profileSessionValidator, Transformer profileSessionTransformer,
            Persister profileSessionPersister, Transformer profileSessionPOTransformer) {

        super.validator = profileSessionValidator;
        super.transformer = profileSessionTransformer;
        super.persister = profileSessionPersister;
        super.poTransformer = profileSessionPOTransformer;
    }

    /**
     * 
     * @param profileSessionPO
     *            the profileSessionPO
     * @return ProfileSessionPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public ProfileSessionPO saveProfileSession(ProfileSessionPO profileSessionPO) throws MBSBaseException {

        LOGGER.debug("Entering saveProfileSession method in ProfileSessionService");
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        transformationObject.setSourcePojo(profileSessionPO);
        MBSProfileSession mBSProfileSession = null;
        List<MBSProfileSession> mbsProfileLst;
        // Get the existing profile for merging
        try {
            mBSProfileSession = this.getProfileSessionFromRepo(profileSessionPO.getSessionId());
        } catch (Exception ex) {
            LOGGER.warn("saveProfileSession falied for session : {} ", profileSessionPO.getUserName());

        }
        if (mBSProfileSession == null) {
            mBSProfileSession = new MBSProfileSession();
        }
        //remove the old entries
        String userName = profileSessionPO.getUserName();
        mbsProfileLst = getProfileSessionByUserName(userName);
        for(MBSProfileSession mbsProfileSession : mbsProfileLst){
            removeProfileSession(mbsProfileSession.getSessionId());
        }

        transformationObject.setTargetPojo(mBSProfileSession);
        // end of merging
        super.processRequest(transformationObject);
        LOGGER.debug("Exiting saveProfileSession method in ProfileSessionService");
        return (ProfileSessionPO) transformationObject.getSourcePojo();
    }

    /**
     * 
     * @param sessionId
     *            the sessionId
     * @return ProfileSessionPO If session Profile Does exist then return
     *         ProfileSessionPO with user name associate with the session ID and
     *         if does not exists then return null
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public ProfileSessionPO getProfileSession(String sessionId) throws MBSBaseException {

        LOGGER.debug("Entering getProfileSession method in ProfileSessionService");
        LOGGER.debug("Entering getProfileSession method in ProfileSessionService: sessionId: {}", MBSPortalUtils.getLeftPaddedString(sessionId));
        MBSProfileSession mBSProfileSession = this.getProfileSessionFromRepo(sessionId);
        if (mBSProfileSession == null) {
            return null;
        }
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        transformationObject.setTargetPojo(mBSProfileSession);
        transformationObject = poTransformer.transform(transformationObject);
        LOGGER.debug("Exiting getProfileSession method in ProfileEntitlementService");
        return (ProfileSessionPO) transformationObject.getSourcePojo();
    }

    /**
     * 
     * @author gaur5c
     * @param sessionId
     *            the sessionId
     * @return MBSProfileSession
     * @throws MBSBaseException
     * @Description: To mock and bypass the Unit Testing Dao
     */
    @SuppressWarnings("deprecation")
    protected MBSProfileSession getProfileSessionFromRepo(String sessionId) throws MBSBaseException {
        return (MBSProfileSession) persister.getDao().getById(sessionId);
    }
    
    /**
     * 
     * 
     * @param userName
     * @return
     * @throws MBSBaseException
     */
    @SuppressWarnings("deprecation")
    protected List<MBSProfileSession> getProfileSessionByUserName(String userName) throws MBSBaseException {
        return ((MBSProfileSessionDao)persister.getDao()).getSessionByUserName(userName);
        
    }
    
    /**
     * 
     * 
     * @param sessionId
     * @throws MBSBaseException
     */
    @SuppressWarnings("deprecation")
    protected void removeProfileSession(String sessionId) throws MBSBaseException {
        ((MBSProfileSessionDao)persister.getDao()).removeById(sessionId);
        
    }
    
    
}
