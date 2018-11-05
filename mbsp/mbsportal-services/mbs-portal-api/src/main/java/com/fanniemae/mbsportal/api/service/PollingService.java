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

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.po.PollingPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.dao.MBSEventDao;
import com.fanniemae.mbsportal.model.MBSEvent;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 20, 2017
 * @Time 3:30:21 PM com.fanniemae.mbsportal.api.service PollingService.java
 * @Description: Service class to interact with DAO for Polling Events
 */
@SuppressWarnings("rawtypes")
@Service
public class PollingService extends BaseProcessor {
    
    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * mBSEventDao MBSEventDao
     */
    @Autowired
    private MBSEventDao mBSEventDao;
    
    /**
     * 
     * This method responds with the Polling details for the input passed
     * 
     * @param userName the userName
     * @param reqDateTimestamp the reqDateTimestamp
     * @param roleType the roleType
     * @return PollingPO
     * @throws MBSBaseException
     */
    public PollingPO getPoollingReq(String userName, Optional<String> reqDateTimestamp, MBSRoleType roleType)
            throws MBSBaseException {

        PollingPO pollingPOResponse = new PollingPO();
        boolean lender = false;
        long dateTimestamp;
        String currentServerTime;
        String reqDateTimestampS;

        try {
            // Get server Time - Always
            currentServerTime = MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(
                    MBSPortalUtils.getLocalDateCurrentTimeStamp(), MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS);

            // If requested Date time not presents then send the server time
            // only
            if (!reqDateTimestamp.isPresent() || StringUtils.isEmpty(reqDateTimestamp.get())) {

                pollingPOResponse.setEventsAvailable(true);
                pollingPOResponse.setServerTime(currentServerTime);
                return pollingPOResponse;
            }
            // If requested Date time presents then do the calculation
            else {
                reqDateTimestampS = reqDateTimestamp.get();
                dateTimestamp = MBSPortalUtils.getLocalDateCurrentTimeStampFromGivenDateTime(reqDateTimestampS,
                        MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS);
            }
            if (roleType.equals(MBSRoleType.LENDER)) {
                lender = true;
            }
            if (!lender) {
                userName = TradeConstants.TRADE_EVENT_USRE_NAME;
            }
            MBSEvent event = mBSEventDao.getEvent(userName);

            if (Objects.equals(event, null)) {
                pollingPOResponse.setEventsAvailable(false);
                pollingPOResponse.setServerTime(currentServerTime);
                return pollingPOResponse;
            }
            
            LOGGER.debug("PollingService#getPoollingReq : (userName) :"+userName+", (Server Event Time):" +  MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(event.getEventTimeStamp(),
                        MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS)+ 
                    ",  (Requested Event Time) :"+  MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(dateTimestamp,
                            MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS)+
                    ", (Server Event Time - Requested Event Time):" + (event.getEventTimeStamp() - dateTimestamp)
                    );
            
            if (event.getEventTimeStamp() > dateTimestamp) {
                pollingPOResponse.setEventsAvailable(true);
            }
            pollingPOResponse.setServerTime(currentServerTime);
        } catch (Exception exe) {

            throw new MBSBusinessException("Failed to process Polling Request for input (" + userName + ", "
                    + reqDateTimestamp + " , " + roleType + ")", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        return pollingPOResponse;
    }
    
    /**
     * 
     * This method clears all the events
     * 
     * @throws MBSBaseException
     */
    public void clearAll() throws MBSBaseException {
        // clear transaction events
        Set<?> keySets = mBSEventDao.getStorageRegion().keySetOnServer();
        for (final Object key : keySets) {
            mBSEventDao.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared all events  ...");
    }

}
