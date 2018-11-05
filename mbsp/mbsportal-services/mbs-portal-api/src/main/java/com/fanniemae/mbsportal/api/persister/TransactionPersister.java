/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.persister;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.persist.BasePersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.ErrorMessage;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * Created by g8uaxt on 8/18/2017.
 * 
 * @author g8upjv
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class TransactionPersister extends BasePersister {
    
    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog 
    private Logger LOGGER;
    
    /**
     * 
     *  mbsTransactionRequestDao MBSTransactionRequestDao
     */
    @Autowired
    private MBSTransactionRequestDao mbsTransactionRequestDao;

    /**
     * 
     * Overriding the base method to persist the record(s)
     * 
     * @param transformationObject TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void persist(TransformationObject transformationObject) throws MBSBaseException {
       
        MBSTransactionRequest mBSTransactionRequest = (MBSTransactionRequest) transformationObject.getTargetPojo();
        
        //CMMBSSTA01-1108: API- Prevent Concurrent Request from Same Lender
        if(StateType.LENDER_OPEN.name().equalsIgnoreCase(mBSTransactionRequest.getStateType())
                && (MBSRoleType.LENDER.equals(transformationObject.getMBSRoleType()) || MBSRoleType.TSP.equals(transformationObject.getMBSRoleType()))){
            
            List<MBSTransactionRequest> transReqLst = mbsTransactionRequestDao.getConcurrentLenderTradeRequest(mBSTransactionRequest);
            if(CollectionUtils.isNotEmpty(transReqLst)){
                
                LOGGER.error(ErrorMessage.LENDER_TRADE_REQUEST_ALREADY_PRESENT.value() + ", Same Trade Request under certain condition");
                throw new MBSBusinessException(ErrorMessage.LENDER_TRADE_REQUEST_ALREADY_PRESENT.value(),
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        }
        //end
        
        mbsTransactionRequestDao.saveOrUpdate(mBSTransactionRequest);
        
        if(transformationObject.getMBSRoleType() != null && StringUtils.isNoneBlank(transformationObject.getMBSRoleType().getRole()))
        {
            String mBSRole = transformationObject.getMBSRoleType().getRole();
            mbsTransactionRequestDao.saveEvents((MBSTransactionRequest) transformationObject.getTargetPojo(), mBSRole);
        }
    }

    /**
     * 
     * Overriding the clearAll() method
     * 
     * @throws MBSBaseException
     */
    @Override
    public void clearAll() throws MBSBaseException {
        // clear transaction requests
        Set<String> keySets = mbsTransactionRequestDao.getStorageRegion().keySetOnServer();
        for (final String key : keySets) {
            mbsTransactionRequestDao.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared all transactions  ...");
    }
    
    /**
     * 
     * Overriding the clear() method
     * 
     * @param key String
     * @throws MBSBaseException
     */
    @Override
    public void clear(String key) throws MBSBaseException {
        if(!StringUtils.isEmpty(key)){
            mbsTransactionRequestDao.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared records for key  ..."+key);
    }

    /**
     * This method is not being used
     * 
     * @return BaseDaoImpl
     * @throws MBSBaseException
     */
    @Override
    public BaseDaoImpl getDao() throws MBSBaseException {
        return null;
    }

    /**
     * This method returns the MBSBaseDao version of MBSTransactionRequestDao
     * 
     * @throws MBSBaseException
     * @return MBSBaseDao
     */
    @Override
    public MBSBaseDao getBaseDao() throws MBSBaseException {
        return mbsTransactionRequestDao;
    }
}
