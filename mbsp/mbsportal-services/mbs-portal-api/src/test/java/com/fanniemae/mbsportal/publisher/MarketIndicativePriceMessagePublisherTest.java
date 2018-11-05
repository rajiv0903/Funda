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

package com.fanniemae.mbsportal.publisher;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProductMarketIndicativePricingVO;
import com.fanniemae.mbsportal.api.publisher.MarketIndicativePriceMessagePublisher;
import com.fanniemae.mbsportal.dao.MBSPartyDao;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.socket.client.StreamingClientAPI;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 14, 2018
 * @File: com.fanniemae.mbsportal.pu.publisher.MBSMarketIndicativePriceMessagePublisherTest.java 
 * @Revision: 
 * @Description: MBSMarketIndicativePriceMessagePublisherTest.java
 */

@RunWith(MockitoJUnitRunner.class)
public class MarketIndicativePriceMessagePublisherTest{


    @Mock
    private StreamingClientProperties streamingClientProperties;

    @Mock
    SimpMessagingTemplate simpMessagingTemplate;
    
    
    @Mock
    private StreamingClientAPI streamingClientAPI;

    @Mock
    private MBSPartyDao mbsPartyDao;

    @Mock
    EpvConnector epvConnector;

    @Mock
    MbspProperties mbspProperties;
    
    @InjectMocks
    MarketIndicativePriceMessagePublisher marketIndicativePriceMessagePublisher;
    
    
    private TransformationObject transformationObj;
    private MBSParty mBSParty;
    private String webSocketUrl;
    private String pricingDestinationTopic;
    
    @Before
    public void setUp() throws Exception {
        
        webSocketUrl = "http://localhost:8443/mbsp-streaming";
        pricingDestinationTopic = "/mbsp/topic/tbapricing";
        
        transformationObj = new TransformationObject();
        
        mBSParty = new MBSParty();

        List<ProductMarketIndicativePricingVO> productMarketIndicativePricingVOLst = getMarketIndicativePriceVOLst();
        transformationObj.setTargetPojo(productMarketIndicativePricingVOLst);
        
        mBSParty.setMbspStreamingUrlBase(webSocketUrl);
    }
    
    
    @Test
    public void publish_Success() throws Exception{
        
        doReturn(true).when(streamingClientProperties).isPublishPricing();
        doReturn(pricingDestinationTopic).when(streamingClientProperties).getPricingDestinationTopic();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        marketIndicativePriceMessagePublisher.publish(transformationObj);
    }
    
    @Test
    public void publish_Passing_Empty_List_Success() throws Exception{
        
        transformationObj.setTargetPojo(null);
        doReturn(true).when(streamingClientProperties).isPublishPricing();
        doReturn(pricingDestinationTopic).when(streamingClientProperties).getPricingDestinationTopic();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        marketIndicativePriceMessagePublisher.publish(transformationObj);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void publish_Partial_Message_Success() throws Exception{
        
        ((List<ProductMarketIndicativePricingVO>)transformationObj.getTargetPojo()).set(0, null);
        doReturn(true).when(streamingClientProperties).isPublishPricing();
        doReturn(pricingDestinationTopic).when(streamingClientProperties).getPricingDestinationTopic();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        marketIndicativePriceMessagePublisher.publish(transformationObj);
    }
    
    @Test
    public void publish_Skip_Success() throws Exception{
        
        doReturn(false).when(streamingClientProperties).isPublishPricing();
        doReturn(pricingDestinationTopic).when(streamingClientProperties).getPricingDestinationTopic();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        marketIndicativePriceMessagePublisher.publish(transformationObj);
    }
    
    @Test
    public void publish_Failed_to_Fetch_Party_Failure() throws Exception{
        
        doReturn(true).when(streamingClientProperties).isPublishPricing();
        doReturn(pricingDestinationTopic).when(streamingClientProperties).getPricingDestinationTopic();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doThrow(MBSBaseException.class).when(mbsPartyDao).getParty(anyString());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        marketIndicativePriceMessagePublisher.publish(transformationObj);
    }
    
    
    /**
     * 
     * @return List of MBSMarketIndicativePrice
     * @throws Exception
     */
    private List<ProductMarketIndicativePricingVO> getMarketIndicativePriceVOLst() throws Exception {

        List<ProductMarketIndicativePricingVO> productMarketIndicativePricingVOLst = new ArrayList<>();
        
        
        ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO bidPrice = new ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO("102", "21");
        ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO askPrice = new ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO("102", "21");
        ProductMarketIndicativePricingVO productMarketIndicativePricingVO = new ProductMarketIndicativePricingVO(
                "FN15", "2018-06-18", "2018-06-18", "4.00", bidPrice, askPrice);
        

        productMarketIndicativePricingVOLst.add(productMarketIndicativePricingVO);
        
        bidPrice = new ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO(null, null);
        askPrice = new ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO(null, null);
        productMarketIndicativePricingVO = new ProductMarketIndicativePricingVO(
                null, null, null, null, bidPrice, askPrice);

        productMarketIndicativePricingVOLst.add(productMarketIndicativePricingVO);

        return productMarketIndicativePricingVOLst;

    }

}
