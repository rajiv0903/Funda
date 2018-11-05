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
package com.fanniemae.mbsportal.api.utils;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.UserConfigPO;
import com.fanniemae.mbsportal.dao.MBSPartyDao;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 25, 2018
 * @File: com.fanniemae.mbsportal.api.utils.ServiceUrlUtil.java 
 * @Revision: 
 * @Description: ServiceUrlUtil.java
 */
@Component
public class ServiceUrlUtil {
    /**
     *
     * LOGGER Logger
     */
    @InjectLog
    private Logger LOGGER;
    
    @Autowired
    private MbspProperties mbspProperties;
    
    @Autowired
    private StreamingClientProperties streamingClientProperties;

    @Autowired
    private MBSProfileDao mBSProfileDao;

    @Autowired
    private MBSPartyDao mbsPartyDao;
    
    /**
     * 
     * Get the Server URL to Publish the Message
     * 
     * @param userName the userName
     * @param fannieMaeUser the fannieMaeUser
     * @return String the URL 
     * @throws MBSBaseException
     */
    public UserConfigPO getUserConfigPO(String userName, boolean fannieMaeUser) throws MBSBaseException {

        UserConfigPO userConfigPO = new UserConfigPO();
        MBSParty mBSParty = null;
        MBSProfile mBSProfile = null;
        String webSocketUrl = streamingClientProperties.getWebSocketUrl();
        String directApiUrl = mbspProperties.getDirectApiUrl();

        //If not fannie user and 
        if(StringUtils.isNoneBlank(userName)){
            mBSProfile = mBSProfileDao.getProfile(userName);
            LOGGER.info("getUserConfigPO : profile {}",mBSProfile);
             if (Objects.nonNull(mBSProfile) && StringUtils.isNotBlank(mBSProfile.getSellerServicerNumber())) {
                mBSParty = mbsPartyDao.getParty(mBSProfile.getSellerServicerNumber());
                LOGGER.info("getUserConfigPO : party {}",mBSParty);
            }
        }else if(fannieMaeUser){
            mBSParty = mbsPartyDao.getParty(""+mbspProperties.getFnmSellerSerivcerNo());
            LOGGER.info("getUserConfigPO : fanniemaeuse party {}",mBSParty);
        }
        
        //webSocketUrl 
        //TODO: Get from party alternate URL
        if (Objects.nonNull(mBSProfile) && Objects.nonNull(mBSProfile.getUserConfig()) && StringUtils.isNotBlank(mBSProfile.getUserConfig().getWebSocketUrl())) {
            webSocketUrl = mBSProfile.getUserConfig().getWebSocketUrl();
            LOGGER.info("Setting user {} at profile level webSocketUrl to {} in getUserConfigPO",userName,webSocketUrl);
        }else if (Objects.nonNull(mBSParty) && StringUtils.isNotBlank(mBSParty.getMbspStreamingUrlBase())) {
            webSocketUrl = mBSParty.getMbspStreamingUrlBase();
            LOGGER.info("Setting user {} at party level webSocketUrl to {} in getUserConfigPO",userName,webSocketUrl);
        }else{
            webSocketUrl = streamingClientProperties.getWebSocketUrl();
            LOGGER.info("Setting user {} at property level webSocketUrl to {} in getUserConfigPO",userName,webSocketUrl);
        }
        //apiUrl
        if (Objects.nonNull(mBSProfile) && Objects.nonNull(mBSProfile.getUserConfig()) && StringUtils.isNotBlank(mBSProfile.getUserConfig().getDirectApiUrl())) {
            directApiUrl = mBSProfile.getUserConfig().getDirectApiUrl();
            LOGGER.info("Setting user {} at profile level directApiUrl to {} in getUserConfigPO",userName,directApiUrl);
        } else if (Objects.nonNull(mBSParty) && StringUtils.isNotBlank(mBSParty.getMbspPortalCnameUrlBase())) {
            directApiUrl = mBSParty.getMbspPortalCnameUrlBase();
            LOGGER.info("Setting user {} at party level directApiUrl to {} in getUserConfigPO",userName,directApiUrl);
        }else{
            directApiUrl = mbspProperties.getDirectApiUrl();
            LOGGER.info("Setting user {} at property level directApiUrl to {} in getUserConfigPO",userName,directApiUrl);
        }
        userConfigPO.setDirectApiUrl(directApiUrl);
        userConfigPO.setWebSocketUrl(webSocketUrl);
        LOGGER.info("userConfig : {}",userConfigPO);
        return userConfigPO;
    }
}
