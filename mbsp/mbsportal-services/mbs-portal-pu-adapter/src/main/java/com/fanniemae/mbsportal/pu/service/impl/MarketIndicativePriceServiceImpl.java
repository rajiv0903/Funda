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

package com.fanniemae.mbsportal.pu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceDao;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.pu.service.MarketIndicativePriceService;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.google.common.base.Objects;

/**
 * 
 * This service class does the Gemfire related operations
 * 
 * @author g8upjv
 *
 */
@Service
public class MarketIndicativePriceServiceImpl implements MarketIndicativePriceService {
    
    /**
     * 
     * LOGGER Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketIndicativePriceServiceImpl.class);
    
    /**
     * 
     * marketIndicativePriceDao MarketIndicativePriceDao
     */
    @Autowired
    private MBSMarketIndicativePriceDao mBSMarketIndicativePriceDao;
    
    /**
     * 
     * This method updates the MBS Transaction Request records to Gemfire
     * 
     * @param mbsMarketIndicativePrice
     * @return boolean
     * @exception MBSBaseException
     */
    @Override
    public boolean updateMarketIndicativePrice(MBSMarketIndicativePrice mbsMarketIndicativePrice) throws MBSBaseException {
        
        boolean updateStatus = false;
        try {
            if (Objects.equal(null, mbsMarketIndicativePrice)) {
                LOGGER.error("Exception when updating transaction request in Listener flow. Input object is null ");
                throw new MBSSystemException("Exception when updating transaction request. Input object is null");
            }
            mBSMarketIndicativePriceDao.saveOrUpdate(mbsMarketIndicativePrice);
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

}
