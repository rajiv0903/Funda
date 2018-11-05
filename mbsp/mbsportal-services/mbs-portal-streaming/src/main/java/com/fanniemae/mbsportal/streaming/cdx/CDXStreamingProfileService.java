/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.cdx;

import java.util.Map;

import org.apache.geode.cache.CommitConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.utils.client.GemfireUtil;
import com.fanniemae.mbsportal.cdx.client.CDXClientCallApi;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.cdx.exception.CDXBaseException;
import com.fanniemae.mbsportal.cdx.exception.CDXRetryableException;
import com.fanniemae.mbsportal.cdx.model.CDXUserProfile;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.dao.MBSProfileSessionDao;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSProfileSession;
import com.fanniemae.mbsportal.service.BaseProcessor;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 31, 2018
 * @File: com.fanniemae.mbsportal.streaming.cdx.CDXStreamingProfileService.java
 * @Revision:
 * @Description: CDXStreamingProfileService.java
 */
@Component
@EnableRetry
public class CDXStreamingProfileService {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CDXStreamingProfileService.class);

    /**
     *
     * mBSProfileSessionDao MBSProfileSessionDao
     */
    @Autowired
    MBSProfileSessionDao mBSProfileSessionDao;
    
    /**
    *
    * mBSProfileSessionDao MBSProfileSessionDao
    */
   @Autowired
   MBSProfileDao mBSProfileDao;
    /**
     *
     * cDXClientCallApi CDXClientCallApi
     */
    @Autowired
    CDXClientCallApi cDXClientCallApi;

    /**
     * 
     * This method will try to fetch the user name from Profile Session Local Cache - If available then fetch the profile 
     * If the profile session does not presents then call CDX for Authorization and then save the same
     * @param cdxHeaderMap
     * @return
     * @throws Exception
     */
    @Retryable(value = {
            CDXRetryableException.class }, 
            maxAttemptsExpression = "#{${cdx.client.retryMaxAttempts}}", 
            backoff = @Backoff(delayExpression = "#{${cdx.client.retryBackoff}}"))
    
    public MBSProfile authrizeCDXSessionRetry(Map<String, String> cdxHeaderMap) throws Exception {

        LOGGER.debug("Entering authrizeCDXSessionRetry method in CDXStreamingProfileService");

        MBSProfile mBSProfile = null;
        CDXUserProfile cDXUserProfile = null;
        String userName = null;
        String sessionId = cdxHeaderMap.get(CDXHeaderMap.SESSION_ID.getValue());

        try {
            /*
             * Get Profile from Local Cache
             */
            MBSProfileSession mBSProfileSession = mBSProfileSessionDao.getProfileSession(sessionId);
            /*
             * If session Profile does not exists
             */
            if (mBSProfileSession == null) {

                LOGGER.debug("Session info does not exist, going to call Gateway to validate; session ID: {}",
                        sessionId);

                cDXUserProfile = cDXClientCallApi.getProfileFromSession(cdxHeaderMap);
                userName = cDXUserProfile.getUserName();

                mBSProfileSession = new MBSProfileSession();
                mBSProfileSession.setSessionId(sessionId);
                mBSProfileSession.setUserName(userName);

                try {
                    BaseProcessor.beginTransaction(GemfireUtil.getTransactionManager());
                    mBSProfileSessionDao.saveOrUpdate(mBSProfileSession);
                    BaseProcessor.commitTransaction(GemfireUtil.getTransactionManager());

                } catch (CommitConflictException cce) {
                    LOGGER.error("CommitConflictException: with Id#" + mBSProfileSession.getId(), cce);
                    BaseProcessor.rollbackTransaction(GemfireUtil.getTransactionManager());
                    LOGGER.debug("Exiting authrizeCDXSessionRetry in CDXStreamingProfileSessionService");
                    return null;

                } catch (Exception e) {
                    LOGGER.error("Exception: with Id#" + mBSProfileSession.getId(), e);
                    BaseProcessor.handleTransactionException(GemfireUtil.getTransactionManager(), e);
                    LOGGER.debug("Exiting authrizeCDXSessionRetry in CDXStreamingProfileSessionService");
                    return null;

                }
                LOGGER.debug("Session info completed; username: {}", userName);
                
                // Fetch Profile from
                mBSProfile = mBSProfileDao.getProfile(userName);

            } else {

                LOGGER.debug("Session info exist, taking the user name from existing local cache; session ID: {}",
                        sessionId);
                userName = mBSProfileSession.getUserName();

                LOGGER.debug("Session info completed; username: {}", userName);
                
                // Fetch Profile from
                mBSProfile = mBSProfileDao.getProfile(userName);
            }

        } catch (CDXRetryableException exe) {
            LOGGER.error("CDXRetryableException in  method authrizeCDXSessionRetry :", exe);
            throw exe;

        } catch (CDXBaseException exe) {
            LOGGER.error("CDXBaseException in  method authrizeCDXSessionRetry :", exe);
            throw exe;
        }
        LOGGER.debug("Exiting authrizeCDXSessionRetry method in CDXStreamingProfileService");
        return mBSProfile;
    }
    
    /**
     * Fetch Profile from CDX session
     * @param cdxHeaderMap
     * @return
     * @throws Exception
     */
    public MBSProfile getProfileFromCDXSession(Map<String, String> cdxHeaderMap) throws Exception {
        
        LOGGER.debug("Entering getProfileFromCDXSession method in CDXStreamingProfileService");
        MBSProfile mBSProfile = null;
        mBSProfile =  authrizeCDXSessionRetry(cdxHeaderMap);
        LOGGER.debug("Exiting getProfileFromCDXSession method in CDXStreamingProfileService");
        return mBSProfile;
    }
}
