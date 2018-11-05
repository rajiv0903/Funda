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

package com.fanniemae.mbsportal.api.publisher;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProductMarketIndicativePricingVO;
import com.fanniemae.mbsportal.api.transformation.MarketIndicativePriceMessageTransformer;
import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.publisher.BaseMessagePublisher;
import com.fanniemae.mbsportal.service.ConfigurationService;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.socket.client.StreamingClientAPI;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessage;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessageDetails;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 13, 2018
 * @File: com.fanniemae.mbsportal.pu.publisher.MarketIndicativePriceMessagePublisher.java
 * @Revision:
 * @Description: MarketIndicativePriceMessagePublisher.java
 */

@SuppressWarnings("rawtypes")
@Component
public class MarketIndicativePriceMessagePublisher extends BaseMessagePublisher {
    
    /**
     * 
     * LOGGER Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketIndicativePriceMessagePublisher.class);
    
    /**
     * 
     * LOGGING_METHOD_NAME String
     */
    private static final String LOGGING_METHOD_NAME = "publish";
    
    /**
     * 
     * LOGGING_METHOD_SIGNATURE String
     */
    private static final String LOGGING_METHOD_SIGNATURE = "com.fanniemae.mbsportal.pu.publisher.MarketIndicativePriceMessagePublisher.publish(TransformationObject)";

    @Autowired
    private MarketIndicativePriceMessageTransformer marketIndicativePriceMessageTransformer;
    /**
     * 
     * streamingClientProperties the StreamingClientProperties
     */
    @Autowired
    private StreamingClientProperties streamingClientProperties;
    
    /**
     * 
     * streamingClientAPI the StreamingClientAPI
     */
    @Autowired
    private StreamingClientAPI streamingClientAPI;

    /**
     * 
     * mbspProperties the MbspProperties
     */
    @Autowired
    MbspProperties mbspProperties;
    
    /**
     * 
     * configurationService ConfigurationService
     */
    @Autowired
    private ConfigurationService configurationService;

    /**
     * 
     * Publish Pricing Message
     * 
     * @param transformationObj
     *            the TransformationObject
     */
    @SuppressWarnings("unchecked")
    @Override
    public void publish(TransformationObject transformationObj) throws MBSBaseException {

        LOGGER.debug("Entering publish method in MarketIndicativePriceMessagePublisher");

        TransformationObject transformationObjectLocalCp = null;
        ProductMarketIndicativePricingVO productMarketIndicativePricingVO = null;
        String msgPublishServerUrl;

        try {
            transformationObjectLocalCp = (TransformationObject) transformationObj.clone();
            transformationObjectLocalCp = marketIndicativePriceMessageTransformer.transform(transformationObjectLocalCp);
            if (streamingClientProperties.isPublishPricing()) {
                productMarketIndicativePricingVO = (ProductMarketIndicativePricingVO) transformationObjectLocalCp.getTargetPojo();
                if (Objects.isNull(productMarketIndicativePricingVO)) {
                    return;
                }
                //Fetch Streaming Server  URL
                msgPublishServerUrl = getMsgPublishServerUrl();
                StreamingMessage streamingMessagePrice = new StreamingMessage();
                StreamingMessageDetails streamingMessageDetailsPrice = getPricingMessageDetails(productMarketIndicativePricingVO);
                streamingMessagePrice.clearMessages();
                streamingMessagePrice.addMessages(streamingMessageDetailsPrice);
                if (streamingClientProperties.isWsPublishEnabled()) {
                    /*
                     * Trader: Send it by Template
                     */
                    streamingClientAPI.sendMsgByTemplate(streamingMessagePrice);
                } else {
                    /*
                     * Trader: Send by HTTP 
                     */
                    streamingClientAPI.sendMsgByHttp(streamingMessagePrice, msgPublishServerUrl);
                }
            }

        } catch (Exception exe) {

            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION.toString(), exe.getMessage(), LOGGING_METHOD_NAME,
                    LOGGING_METHOD_SIGNATURE, transformationObjectLocalCp.toString());
            LOGGER.error("Error Stack: {}", exe);
            MBSExceptionConstants.resetLogAlert();
        }
        LOGGER.debug("Exiting publish method in MarketIndicativePriceMessagePublisher");
    }
    
    /**
     * 
     * This will construct the Pricing Message Details 
     * 
     * @param productMarketIndicativePricingVO the ProductMarketIndicativePricingVO
     * @return streamingMessageDetails the StreamingMessageDetails 
     * @throws MBSBaseException
     * @throws JsonProcessingException
     * @throws Exception
     */
    private StreamingMessageDetails getPricingMessageDetails(ProductMarketIndicativePricingVO productMarketIndicativePricingVO) throws MBSBaseException, JsonProcessingException, Exception {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(productMarketIndicativePricingVO);

        // Create Pricing Message
        StreamingMessageDetails streamingMessageDetails = new StreamingMessageDetails();
        streamingMessageDetails.setMessage(jsonInString);
        streamingMessageDetails.addTopic(streamingClientProperties.getPricingDestinationTopic());
        
        //set the message identifier map
        Map<String, Object> messageIdentifierMap =  new LinkedHashMap<>();
        messageIdentifierMap.put("TOPIC", streamingClientProperties.getPricingDestinationTopic());
        messageIdentifierMap.put("PRODUCT_NAME_CODE", productMarketIndicativePricingVO.getProductNameCode());
        messageIdentifierMap.put("PRODUCT_COUPON", productMarketIndicativePricingVO.getPassThroughRate());
        messageIdentifierMap.put("PUBLISH_TIME_FROM_MBSP",  MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(MBSPortalUtils.getLocalDateCurrentTimeStamp(), 
                MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS) );
        
        streamingMessageDetails.setMessageIdentifierMap(messageIdentifierMap);

        return streamingMessageDetails;
    }
    
    /**
     * 
     * Fetch the Streaming Host URL from Party
     * 
     * @return webSocketUrl String
     * @throws MBSBaseException
     */
    private String getMsgPublishServerUrl() throws MBSBaseException {
        String streamingInternalUrl = configurationService.getPropValueAsString(DAOConstants.STREAMING_INTERNAL_URL);
        if(StringUtils.isBlank(streamingInternalUrl)){
            LOGGER.error("The streaming internal url is empty in config service");
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                MBSExceptionConstants.SYSTEM_EXCEPTION.toString(), "The streaming internal url is empty", LOGGING_METHOD_NAME,
                LOGGING_METHOD_SIGNATURE, DAOConstants.STREAMING_INTERNAL_URL);
                MBSExceptionConstants.resetLogAlert();
        }
        return streamingInternalUrl;
    }
}
