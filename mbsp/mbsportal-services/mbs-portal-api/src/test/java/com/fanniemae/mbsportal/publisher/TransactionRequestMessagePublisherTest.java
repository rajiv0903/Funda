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

import com.fanniemae.mbsportal.service.ConfigurationService;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessage;
import com.fanniemae.mbsportal.streaming.stomp.MBSStompUtil;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSStompClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.validation.constraints.Null;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.po.UserConfigPO;
import com.fanniemae.mbsportal.api.publisher.TransactionRequestMessagePublisher;
import com.fanniemae.mbsportal.api.service.ProductService;
import com.fanniemae.mbsportal.api.transformation.TransactionRequestPOTransformer;
import com.fanniemae.mbsportal.api.utils.ServiceUrlUtil;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.ConfigurationService;
import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.socket.client.StreamingClientAPI;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSStompClient;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Apr 26, 2018
 * @File: com.fanniemae.mbsportal.publisher.TransactionRequestMessagePublisherTest.java
 * @Revision :
 * @Description: TransactionRequestMessagePublisherTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestMessagePublisherTest {

    @Mock
    private StreamingClientProperties streamingClientProperties;
    
    @Mock
    private MBSStompClient mbsStompClient;
    @Mock
    private TokenService tokenService;
    @Mock
    private CDXClientApi cdxClientApi;
    @Mock
    SimpMessagingTemplate simpMessagingTemplate;
    
    @Mock
    private ConfigurationService configurationService;
    
    @Mock
    private StreamingClientAPI streamingClientAPI;

    @Mock
    private TransactionRequestPOTransformer<TransformationObject> transactionRequestPOTransformer;

    @Mock
    private ProductService productService;
    
    @Mock
    private ServiceUrlUtil serviceUrlUtil;

    @InjectMocks
    private TransactionRequestMessagePublisher transactionRequestMessagePublisher;

    private TransformationObject transformationObject;
    private MBSTransactionRequest mbsTransactionRequest;
    private TransactionRequestPO transactionRequestPO;
    private ProductPO productPO;
    private String transactionDestinationTopic = "/mbsp/topic/transaction/";
    private UserConfigPO userConfigPO;
    private String webSocketUrl;
    private String directApiUrl;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        webSocketUrl = "https://localhost:8443/mbsp-streaming";
        directApiUrl = "https://localhost:8080";
        
        userConfigPO = new UserConfigPO();
        userConfigPO.setWebSocketUrl(webSocketUrl);
        userConfigPO.setDirectApiUrl(directApiUrl);

        productPO = new ProductPO();
        ProductIdPO productIdPO = new ProductIdPO();
        productIdPO.setIdentifier(1L);
        productIdPO.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productIdPO.setType(PRODUCT_TYPE.MBS);
        productPO.setProductId(productIdPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setTransReqNumber("18D00792");
        mbsTransactionRequest.setCounterpartyTraderName("trader");
        mbsTransactionRequest.setCounterpartyTraderIdentifier("lender");
        mbsTransactionRequest.setTradeAmount(new BigDecimal(10000000));
        mbsTransactionRequest.setStateType(StateType.TRADER_PRICED.toString());

        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("18D00792");
        transactionRequestPO.setProduct(productPO);
        transactionRequestPO.setTraderId("trader");
        transactionRequestPO.setLenderId("lender");
        transactionRequestPO.setTradeAmount(new BigDecimal(10000000).toString());
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);

        transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setSourcePojo(transactionRequestPO);
        doReturn("dummy-id").when(tokenService).getCurrentSessionId();
        doReturn(true).when(cdxClientApi).isSessionValid(any());

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void publish_Bypass_Success() throws MBSBaseException {

        doReturn(false).when(streamingClientProperties).isPublishTransaction();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        transactionRequestMessagePublisher.publish(transformationObject);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void publish_Bypass_Empty_Transaction_Object_Success() throws MBSBaseException {

        transformationObject.setTargetPojo(null);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        transactionRequestMessagePublisher.publish(transformationObject);
    }

    /**
     *
     * @throws MBSBaseException
     */
    @Test
    public void publish_Lender_Transaction_Success() throws MBSBaseException {

        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doReturn(productPO).when(productService).getByProductId(any());
        doReturn(transactionDestinationTopic).when(streamingClientProperties).getTransactionDestinationTopic();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());

        transactionRequestMessagePublisher.publish(transformationObject);
    }
    
    /**
     *
     * @throws MBSBaseException
     * @throws NullPointerException
     */
    @Test
    public void publish_Lender_Transaction_EmptyLenderServer_Success() throws MBSBaseException, NullPointerException {
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        userConfigPO.setWebSocketUrl("");
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doReturn(productPO).when(productService).getByProductId(any());
        doReturn(transactionDestinationTopic).when(streamingClientProperties).getTransactionDestinationTopic();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        
        transactionRequestMessagePublisher.publish(transformationObject);
    }
    
    /**
     *
     * @throws MBSBaseException
     */
    @Test
    public void publish_LenderAndTrader_Transaction_Success() throws MBSBaseException {
        
        transformationObject.setMBSRoleType(MBSRoleType.LENDER_AND_TRADER);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doReturn(productPO).when(productService).getByProductId(any());
        doReturn(transactionDestinationTopic).when(streamingClientProperties).getTransactionDestinationTopic();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        
        transactionRequestMessagePublisher.publish(transformationObject);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void publish_TSP_Transaction_Success() throws MBSBaseException {

        transformationObject.setMBSRoleType(MBSRoleType.TSP);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doReturn(productPO).when(productService).getByProductId(any());
        doReturn(transactionDestinationTopic).when(streamingClientProperties).getTransactionDestinationTopic();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        transactionRequestMessagePublisher.publish(transformationObject);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void publish_Trader_Transaction_Success() throws MBSBaseException {

        transformationObject.setMBSRoleType(MBSRoleType.TRADER);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doReturn(productPO).when(productService).getByProductId(any());
        doReturn(transactionDestinationTopic).when(streamingClientProperties).getTransactionDestinationTopic();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        transactionRequestMessagePublisher.publish(transformationObject);
    }

    /**
     *
     * @throws MBSBaseException
     */
    @Test
    public void publish_Trader_WSPPublishEnabled_Transaction_Success() throws MBSBaseException {
        
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        doReturn(true).when(streamingClientProperties).isWsPublishEnabled();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doReturn(productPO).when(productService).getByProductId(any());
        doReturn(transactionDestinationTopic).when(streamingClientProperties).getTransactionDestinationTopic();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        transactionRequestMessagePublisher.publish(transformationObject);
    }
    
    /**
     *
     * @throws MBSBaseException
     */
    @Test
    public void publish_Lender_WSPPublishEnabled_Transaction_Success() throws MBSBaseException {
        MBSStompUtil.addClient("" , mbsStompClient);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        doReturn(true).when(streamingClientProperties).isWsPublishEnabled();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doReturn(productPO).when(productService).getByProductId(any());
        doReturn(transactionDestinationTopic).when(streamingClientProperties).getTransactionDestinationTopic();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        transactionRequestMessagePublisher.publish(transformationObject);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void publish_Throw_Error_Failure() throws MBSBaseException {

        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        doReturn(true).when(streamingClientProperties).isPublishTransaction();
        doReturn(null).when(transactionRequestPOTransformer).transform(any());
        doReturn(productPO).when(productService).getByProductId(any());
        doReturn(transactionDestinationTopic).when(streamingClientProperties).getTransactionDestinationTopic();
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());
        doNothing().when(streamingClientAPI).sendMsgByHttp(anyObject(), anyString());
        transactionRequestMessagePublisher.publish(transformationObject);
    }

}
