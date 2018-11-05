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

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.publisher.MarketIndicativePriceMessagePublisher;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.dao.MBSProductTransactionXrefDao;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSProductTransactionXref;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8upjv Created on 7/30/2018.
 */
@Component
public class PriceEventService {

    /**
     *
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;

    /**
     *
     * isActive boolean
     */
    boolean isActive = true;

    /**
     * 
     * marketIndicativePriceMessagePublisher MarketIndicativePriceMessagePublisher
     */
    @Autowired
    private MarketIndicativePriceMessagePublisher marketIndicativePriceMessagePublisher;
    
    /**
     * 
     * mbsProductTransactionXrefDao MBSProductTransactionXrefDao
     */
    @Autowired
    MBSProductTransactionXrefDao mbsProductTransactionXrefDao;

    /**
     * 
     * @param key
     * @return MBSProductTransactionXref
     * @throws MBSBaseException
     */
    public MBSProductTransactionXref getProductTransXrefDataById(Object key) throws MBSBaseException {
        
        try {
            if (Objects.isNull(key)) {
                LOGGER.error("getProductTransXrefDataById: Key is null ");
                throw new MBSSystemException("getProductTransXrefDataById: Key is null");
            }
            MBSProductTransactionXref mbsProductTransactionXref = (MBSProductTransactionXref)mbsProductTransactionXrefDao.getById(key);
            return mbsProductTransactionXref;
        } catch (MBSBaseException ex) {
            LOGGER.error("Exception when retrieving MBSProductTransactionXref ", ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Exception when retrieving MBSProductTransactionXref in publish flow ", ex);
            throw new MBSDataAccessException("Exception when retrieving MBSProductTransactionXref in publish flow");
        }
    }
    
    /**
     * 
     * 
     * @param obj
     * @throws MBSBaseException
     */
    public void publishPrice(MBSMarketIndicativePrice obj) {

        try {
            if (obj != null) {
                LOGGER.debug("MBSMarketIndicativePrice publish method call ", obj);
                //Check if there any live trades
                MBSProductTransactionXref mbsProductTransactionXref = getProductTransXrefDataById(obj.getId());
                LOGGER.debug("MBSProductTransactionXref value for mbsProductTransactionXref {} for key {}",mbsProductTransactionXref,obj.getId());
                if(Objects.nonNull(mbsProductTransactionXref)){
                    TransformationObject transformationObject = new TransformationObject();
                    transformationObject.setMBSRoleType(MBSRoleType.TRADER);
                    transformationObject.setSourcePojo(obj);
                    LOGGER.info("Publish price: "+obj.getId()+" ask price "+obj.getAskPriceText()+" bid price: "+obj.getBidPriceText());
                    marketIndicativePriceMessagePublisher.publish(transformationObject);
                }
            } else {
                LOGGER.debug("publishPrice : The retrieved MBSMarketIndicativePrice is empty.");
            }
        } catch (Exception e) {
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                    "Failed to publish prices ", "publishPrice", "", "");
            LOGGER.error("Failed publishPrice", e);
            MBSExceptionConstants.resetLogAlert();
            // throw new MBSSystemException("Failed to publish trades",
            // MBSExceptionConstants.SYSTEM_EXCEPTION);
        }

    }
    
    /**
     * 
     * @param obj
     * @throws MBSBaseException
     */
    //@Override
    public void processEvent(MBSMarketIndicativePrice obj) throws MBSBaseException {
        publishPrice(obj);
    }

}
