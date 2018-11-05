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

package com.fanniemae.mbsportal.pu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.all.messaging.message.ESBMessage;
import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceDao;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.pu.constants.MessageEventType;
import com.fanniemae.mbsportal.pu.transformer.PUPriceMessageTransformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * Process stream price in the class
 * @author g8upjv
 *
 */
@Service
public class PUPriceMessageProcessor {
    
    /**
     * 
     * LOGGER Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PUPriceMessageProcessor.class);
    
    private static final String LOGGING_METHOD_NAME = "prepareAndProcess";
    private static final String LOGGING_METHOD_SIGNATURE = "com.fanniemae.mbsportal.pu.service.PUPriceMessageProcessor.prepareAndProcess(ESBMessage)";
    
    /**
     * 
     * puPriceMessageTransformer PUPriceMessageTransformer
     */
    @Autowired
    private PUPriceMessageTransformer puPriceMessageTransformer;
    
    /**
     * 
     * mbsMarketIndicativePriceService MBSMarketIndicativePriceService
     */
    @Autowired
    private MarketIndicativePriceService mbsMarketIndicativePriceService;
    
    /**
     * 
     * mBSMarketIndicativePriceHistoryService MBSMarketIndicativePriceHistoryService
     */
    @Autowired
    private MarketIndicativePriceHistoryService mBSMarketIndicativePriceHistoryService;
    
    /**
     * 
     * mbsMarketIndicativePriceDao MBSMarketIndicativePriceDao
     */
    @Autowired
    private MBSMarketIndicativePriceDao mbsMarketIndicativePriceDao;
    
    /**
     * 
     * This method process the message
     * @param puEsbMessage
     * @return
     * @throws MBSBaseException
     */
    boolean processMessage(ESBMessage puEsbMessage) throws MBSBaseException {
        
        boolean handleMsgReturn = false;
        LOGGER.debug("Entering processMessage method in PUPriceMessageProcessor");
        try{
            if(StringUtils.isBlank(puEsbMessage.getEventType()) || StringUtils.isBlank(puEsbMessage.getPayload())){
                LOGGER.error("The event type/payload is missing in the message and hence not processing the message. Message id:"+puEsbMessage.getMessageId());
                handleMsgReturn = true;
                return handleMsgReturn;
            } else {
                List<MBSMarketIndicativePrice> mbsMarketIndicativePriceLst = puPriceMessageTransformer.transform
                        (puEsbMessage.getPayload(), MessageEventType.getEnum(puEsbMessage.getEventType()));
                
                //Filtered List Based on Price 
                List<MBSMarketIndicativePrice> filteredMarketIndicativePriceLst = getFilteredMarketIndicativePriceLst(mbsMarketIndicativePriceLst);
                for(MBSMarketIndicativePrice mbsMarketIndicativePrice: filteredMarketIndicativePriceLst){
                    mbsMarketIndicativePriceService.updateMarketIndicativePrice(mbsMarketIndicativePrice);
                }
                //Update the Price History Asynchronously - Original List
                TransformationObject transformationObj2 = new TransformationObject();
                transformationObj2.setSourcePojo(filteredMarketIndicativePriceLst);
                mBSMarketIndicativePriceHistoryService.saveMarketIndicativePriceHistoryAsync(transformationObj2);
            }
        } catch (MBSBaseException ex) {
            LOGGER.error("Error while processing message in the PU Price Listener ", ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Error while processing message in the PU Price Listener ", ex);
            throw new MBSSystemException("Error while processing message in the PU Price Listener",
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        LOGGER.debug("Exiting processMessage method in PUPriceMessageProcessor");
        return handleMsgReturn;
    }
    
    /**
     * 
     * Filter out the PU Message to be published
     * 
     * @param mbsMarketIndicativePriceLst List of MBSMarketIndicativePrice
     * @return List of MBSMarketIndicativePrice
     * @throws MBSBaseException
     */
    private List<MBSMarketIndicativePrice> getFilteredMarketIndicativePriceLst( List<MBSMarketIndicativePrice> mbsMarketIndicativePriceLst) throws MBSBaseException
    {
        List<MBSMarketIndicativePrice> filteredMarketIndicativePriceLst = new ArrayList<>();
        
        for(MBSMarketIndicativePrice mBSMarketIndicativePrice :mbsMarketIndicativePriceLst)
        {
            MBSMarketIndicativePrice existingMbsMarketIndicativePrice = (MBSMarketIndicativePrice) mbsMarketIndicativePriceDao.getById(mBSMarketIndicativePrice.getId());
            //If first time then publish
            if(Objects.isNull(existingMbsMarketIndicativePrice)){
                filteredMarketIndicativePriceLst.add(mBSMarketIndicativePrice);
            }
            //if change in price
            else if(mBSMarketIndicativePrice.getBidPricePercent().compareTo(existingMbsMarketIndicativePrice.getBidPricePercent()) != 0 
                    || mBSMarketIndicativePrice.getAskPricePercent().compareTo(existingMbsMarketIndicativePrice.getAskPricePercent()) != 0){
                
                filteredMarketIndicativePriceLst.add(mBSMarketIndicativePrice);
            }else{
                LOGGER.debug("Price is same for: Product Name: {}, Coupon: {}, Settlement Date: {}, Bid: {}, Ask: {} , hence discarded for publish and update", mBSMarketIndicativePrice.getProductNameCode(), 
                mBSMarketIndicativePrice.getPassThroughRate(), 
                mBSMarketIndicativePrice.getSettlementDate(), 
                mBSMarketIndicativePrice.getBidPricePercent(), 
                mBSMarketIndicativePrice.getAskPricePercent());
            }
        }
        
        return filteredMarketIndicativePriceLst;
    }
    
   

    /**
     * 
     * prepareAndProcess
     * 
     * @param xmlMessage
     */
    public void prepareAndProcess(ESBMessage xmlMessage) throws MBSBaseException {
        try {
            processMessage(xmlMessage);
            
        } catch (MBSBaseException exe) {  
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION.toString(), exe.getMessage(), LOGGING_METHOD_NAME, LOGGING_METHOD_SIGNATURE,
                    exe.getRootExceptionMessage());
            LOGGER.error("MBSBaseException: {}", exe);
            MBSExceptionConstants.resetLogAlert();
            
        }
    }
}
