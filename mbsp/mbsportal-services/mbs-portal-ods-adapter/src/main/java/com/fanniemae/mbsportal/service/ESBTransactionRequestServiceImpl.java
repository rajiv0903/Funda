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

package com.fanniemae.mbsportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.dao.MBSTradeDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.google.common.base.Objects;

/**
 * This service class does the Gemfire related operations
 * 
 * @author g8upjv
 *
 */
@Service
public class ESBTransactionRequestServiceImpl implements ESBTransactionRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESBTransactionRequestServiceImpl.class);

    @Autowired
    private MBSTransactionRequestDao mbsTransactionRequestDao;
    
    @Autowired
    private MBSTradeDao mbsTradeDao;

    /**
     * This method returns the MBS Transaction Request records from Gemfire
     * 
     * @param transReqId
     * @return List<MBSTransactionRequest>
     * @exception MBSBaseException
     */
    @Override
    public MBSTransactionRequest getMBSTransReq(String transReqId) throws MBSBaseException {
        LOGGER.debug("Trans req id <{}>", transReqId);
        MBSTransactionRequest mbsTransactionRequest = null;
        try {
          //CMMBSSTA01-1982 - start
            //mbsTransactionRequest = (MBSTransactionRequest) mbsTransactionRequestDao.getById(transReqId);
            mbsTransactionRequest = (MBSTransactionRequest) mbsTransactionRequestDao.getTransReqByTransReqNumber(transReqId);
          //CMMBSSTA01-1982 - end
        } catch (MBSBaseException ex) {
            LOGGER.error("Exception when retrieving transaction request in Listener flow ", ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Exception when retrieving transaction request in Listener flow ", ex);
            throw new MBSDataAccessException("Exception when retrieving transaction request");
        }
        return mbsTransactionRequest;
    }
    
    /**
     * This method updates the MBS Transaction Request records to Gemfire
     * 
     * @param mbsTransactionRequest
     * @return boolean
     * @exception MBSBaseException
     */
    @Override
    public boolean updateMBSTransReq(MBSTransactionRequest mbsTransactionRequest) throws MBSBaseException {
        
        boolean updateStatus = false;
        try {
            if (Objects.equal(null, mbsTransactionRequest)) {
                LOGGER.error("Exception when updating transaction request in Listener flow. Input object is null ");
                throw new MBSBaseException("Exception when updating transaction request. Input object is null");
            }
            mbsTransactionRequestDao.saveOrUpdate(mbsTransactionRequest);
            updateStatus = true;
            return updateStatus;
        } catch (MBSBaseException ex) {
            LOGGER.error("Exception when updating transaction request status in Listener flow ", ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Exception when updating transaction request status in Listener flow ", ex);
            throw new MBSDataAccessException("Exception when updating transaction request");
        }
    }


    /**
     * This method updates the MBS Transaction Request records to Gemfire
     * 
     * @param mbsTransactionRequest
     * @return boolean
     * @exception MBSBaseException
     */
    @Override
    public boolean updateMBSTrade(MBSTrade mbsTrade) throws MBSBaseException {
        
        boolean updateStatus = false;
        try {
            if (Objects.equal(null, mbsTrade)) {
                LOGGER.error("Exception when updating transaction request in Listener flow. Input object is null ");
                throw new MBSBaseException("Exception when updating trade. Input object is null");
            }
            mbsTradeDao.saveOrUpdate(mbsTrade);
            updateStatus = true;
            return updateStatus;
        } catch (MBSBaseException ex) {
            LOGGER.error("Exception when updating trade in Listener flow ", ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Exception when updating trade in Listener flow ", ex);
            throw new MBSDataAccessException("Exception when updating trade");
        }
    }

}
