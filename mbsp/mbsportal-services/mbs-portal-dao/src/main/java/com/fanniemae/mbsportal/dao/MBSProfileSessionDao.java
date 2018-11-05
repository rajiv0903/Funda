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

package com.fanniemae.mbsportal.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.geode.cache.ExpirationAction;
import org.apache.geode.cache.ExpirationAttributes;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.pdx.internal.PdxInstanceImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.model.MBSProfileSession;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 12, 2018
 * @File: com.fanniemae.mbsportal.dao.MBSProfileSessionDao.java
 * @Revision :
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@Service
public class MBSProfileSessionDao extends BaseDaoImpl<MBSProfileSession> {

    @InjectLog
    private Logger LOGGER;
    
    private String regionName = "MBSSession";

    private Region<?, ?> region;
    
    private Query queryObj;

    @SuppressWarnings("unchecked")
    @Override
    public Region<String, MBSProfileSession> getStorageRegion() {
        LOGGER.debug("Storage region for Profile request in MBSProfileSessionDao class");
        if (region == null) {
            // create the region
            region = ClientCacheFactory.getAnyInstance().createClientRegionFactory(ClientRegionShortcut.LOCAL)
                    .setEntryTimeToLive(new ExpirationAttributes(1200, ExpirationAction.LOCAL_DESTROY)) //20 minutes - hence 1200 seconds
                    .create(regionName);
        }
        return (Region<String, MBSProfileSession>) region;
    }
    

    /**
     * @param sessionId the user sessionId
     * @return MBSProfileSession
     * @throws MBSBaseException
     */
    public MBSProfileSession getProfileSession(String sessionId) 
    {
        return (MBSProfileSession) super.getById(sessionId);
    }

    /**
     * 
     * saveOrUpdate
     * 
     * @param obj MBSProfileSession
     * 
     */
    @Override
    public void saveOrUpdate(MBSProfileSession obj) 
    {
        if (StringUtils.isNotBlank(obj.getSessionId()) && StringUtils.isNotBlank(obj.getUserName())){
            super.saveOrUpdate(obj);
            LOGGER.debug("saved in gemfire obj id {} in MBSProfileSession class", obj.getId());
        }
        else{
            LOGGER.debug("Failed to save in gemfire obj id {} in MBSProfileSession class", obj.getId());
        }
    }
    
    /**
     * 
     * 
     * @param userName
     * @return
     * @throws MBSBaseException
     */
    public List<MBSProfileSession> getSessionByUserName(String userName) throws MBSBaseException {
        String queryString = "select * from /MBSSession where userName = $1";
        LOGGER.debug("In getSessionByUserName() the userName : "+userName);
        List<MBSProfileSession> mbsProfileSessionLst = new ArrayList<MBSProfileSession>();
        try{
            if(null == queryObj){
                queryObj = ClientCacheFactory.getAnyInstance().getLocalQueryService().newQuery(queryString);
            }
            
            if(!StringUtils.isBlank(userName)){
                String[] queryParams = {userName};
                SelectResults<MBSProfileSession> mbsProfileSession = (SelectResults<MBSProfileSession>) queryObj.execute(queryParams);
                if (CollectionUtils.isNotEmpty(mbsProfileSession)) {
                        for (Object result : mbsProfileSession) {
                            mbsProfileSessionLst.add(getMBSProfileSessionObject(result));
                        }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("exception in MBSProfileSessionDao in method getSessionByUserName ", ex);
            throw new MBSSystemException("Exception when querying profile session for the user id "+userName, ex);
        }
        LOGGER.debug("Size of the returned list : "+mbsProfileSessionLst.size());
        return mbsProfileSessionLst;
    }
    
    /**
     * getMBSTransactionRequestObject
     * 
     * @param obj
     * @return MBSProfileSession
     */
    private MBSProfileSession getMBSProfileSessionObject(Object obj) {
            if (obj instanceof PdxInstanceImpl) {
                    PdxInstanceImpl pdxImpl = (PdxInstanceImpl) obj;
                    return (MBSProfileSession) pdxImpl.getObject();
            } else
                    return (MBSProfileSession) obj;
    }
}
