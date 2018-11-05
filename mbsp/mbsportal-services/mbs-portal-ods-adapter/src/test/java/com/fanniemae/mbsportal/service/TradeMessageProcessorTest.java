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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.ecf.ods.EventMessage;
import com.fanniemae.mbsportal.ecf.ods.TradeEventMessage;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.transformer.ECFTransformer;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.Files;

/**
 * @author g8uaxt Created on 11/16/2017.
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 29, 2017
 * @Time 1:06:45 PM
 * 	com.fanniemae.mbsportal.service
 * 	TradeMessageProcessorTest.java
 * @Description: Added full JUnit coverage and removed Ignore from class level
 */
@RunWith(MockitoJUnitRunner.class)
public class TradeMessageProcessorTest {

    protected static JAXBContext ecfJaxbContext;
    
    @Mock
    private ESBClient esbClient;
    @Mock
    private ECFTransformer ecfTransformer;
    @Mock
    private ESBTransactionRequestService esbTransactionRequestService;
    
    private ClassLoader classLoader;
    private String inputMsg;
    private String inputMsgTradeCreate;
    private String inputMsgEmptyEvent;
    private String invalidMsgPayload;
    private Unmarshaller unmarshaller;
    

    @InjectMocks
    private TradeMessageProcessor tradeMessageProcessor;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        classLoader = getClass().getClassLoader();
        inputMsg = Files.toString(new File(classLoader.getResource("test_br_message.xml").getFile()), Charsets.UTF_8);
        inputMsgTradeCreate = Files.toString(new File(classLoader.getResource("invalid_response/test_br_message_not_allowed_trade.xml").getFile()), Charsets.UTF_8);
        inputMsgEmptyEvent = Files.toString(new File(classLoader.getResource("invalid_response/test_br_message_empty_event.xml").getFile()), Charsets.UTF_8);
        invalidMsgPayload = Files.toString(new File(classLoader.getResource("invalid_response/test_br_message_invalid_xml.xml").getFile()), Charsets.UTF_8);
        tradeMessageProcessor.initialize();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void prepareAndProcess_Sccess() throws Exception {
        
        when(ecfTransformer.getMBSTrade(any())).thenReturn(createTradeEventMsg(inputMsg));
        doNothing().when(esbClient).suspendMessage(any());
        when(ecfTransformer.transform(any())).thenReturn(createSampleTrade());
        when(esbTransactionRequestService.getMBSTransReq(any())).thenReturn(createSampleTransReq());
        when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
        when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
        //ESBMessage esbMessage = ESBMessageFactory.createESBMessage(inputEntireMsg);
        tradeMessageProcessor.prepareAndProcess(inputMsg);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void prepareAndProcess_Sccess_Other_Than_Trade_Create_Events() throws Exception {
        
        when(ecfTransformer.getMBSTrade(any())).thenReturn(createTradeEventMsg(inputMsgTradeCreate));
        doNothing().when(esbClient).suspendMessage(any());
        when(ecfTransformer.transform(any())).thenReturn(createSampleTrade());
        when(esbTransactionRequestService.getMBSTransReq(any())).thenReturn(createSampleTransReq());
        when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
        when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
        tradeMessageProcessor.prepareAndProcess(inputMsgTradeCreate);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void prepareAndProcess_Sccess_For_Trade_Event_Empty() throws Exception {
        
        when(ecfTransformer.getMBSTrade(any())).thenReturn(createTradeEventMsg(inputMsgEmptyEvent));
        doNothing().when(esbClient).suspendMessage(any());
        doNothing().when(esbClient).suspendMessage(any());
        when(ecfTransformer.transform(any())).thenReturn(createSampleTrade());
        when(esbTransactionRequestService.getMBSTransReq(any())).thenReturn(createSampleTransReq());
        when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
        when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
        tradeMessageProcessor.prepareAndProcess(inputMsgEmptyEvent);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void prepareAndProcess_Sccess_For_Trade_With_Empty_Transaction_Number() throws Exception {
        
        MBSTrade mBSTrade = createSampleTrade();
        mBSTrade.setTransReqNumber(null);
        
        when(ecfTransformer.getMBSTrade(any())).thenReturn(createTradeEventMsg(inputMsg));
        doNothing().when(esbClient).suspendMessage(any());
        doNothing().when(esbClient).suspendMessage(any());
        when(ecfTransformer.transform(any())).thenReturn(mBSTrade);
        when(esbTransactionRequestService.getMBSTransReq(any())).thenReturn(createSampleTransReq());
        when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
        when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
        tradeMessageProcessor.prepareAndProcess(inputMsg);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSDataAccessException.class)
    public void prepareAndProcess_Throw_Error_For_Trade_Does_Not_Exists_At_System() throws Exception {
               
        when(ecfTransformer.getMBSTrade(any())).thenReturn(createTradeEventMsg(inputMsg));
        doNothing().when(esbClient).suspendMessage(any());
        doNothing().when(esbClient).suspendMessage(any());
        when(ecfTransformer.transform(any())).thenReturn(createSampleTrade());
        doThrow(MBSDataAccessException.class).when(esbTransactionRequestService).getMBSTransReq(any());
        when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
        when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
        tradeMessageProcessor.prepareAndProcess(inputMsg);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=Exception.class)
    public void prepareAndProcess_Throw_Runtime_Error_For_Trade_Does_Not_Exists_At_System() throws Exception {
               
        when(ecfTransformer.getMBSTrade(any())).thenReturn(createTradeEventMsg(inputMsg));
        doNothing().when(esbClient).suspendMessage(any());
        doNothing().when(esbClient).suspendMessage(any());
        when(ecfTransformer.transform(any())).thenReturn(createSampleTrade());
        doThrow(RuntimeException.class).when(esbTransactionRequestService).getMBSTransReq(any());
        when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
        when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
        tradeMessageProcessor.prepareAndProcess(inputMsg);
    }
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSBaseException.class)
    public void prepareAndProcess_Throw_Error() throws Exception {
        
        when(ecfTransformer.getMBSTrade(any())).thenReturn(createTradeEventMsg(inputMsg));
        doNothing().when(esbClient).suspendMessage(any());
        when(ecfTransformer.transform(any())).thenReturn(createSampleTrade());
        when(esbTransactionRequestService.getMBSTransReq(any())).thenReturn(createSampleTransReq());
        when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
        doThrow(MBSBaseException.class).when(esbTransactionRequestService).updateMBSTransReq(any());
        tradeMessageProcessor.prepareAndProcess(inputMsg);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void prepareAndProcess_Throw_Error_For_Invalid_XML_Payload() throws Exception {
        
        when(ecfTransformer.getMBSTrade(any())).thenReturn(createTradeEventMsg(inputMsg));
        doNothing().when(esbClient).suspendMessage(any());
        when(ecfTransformer.transform(any())).thenReturn(createSampleTrade());
        when(esbTransactionRequestService.getMBSTransReq(any())).thenReturn(createSampleTransReq());
        when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
        when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
        tradeMessageProcessor.prepareAndProcess(invalidMsgPayload);
    }

    /**
     * This method creates a sample data for MBSTransactionRequest
     * 
     * @return
     */
    private MBSTransactionRequest createSampleTransReq() {
        MBSTransactionRequest mbsTransactionRequest = new MBSTransactionRequest();
        ProductId prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(TradeConstants.PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setStateType(StateType.TRADER_CONFIRMED.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("SELL");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        return mbsTransactionRequest;
    }

    /**
     * This method creates a sample data for MBSTransactionRequest
     * 
     * @return
     */
    private MBSTrade createSampleTrade() {
        MBSTrade mbsTrade = new MBSTrade();
        mbsTrade.setTransReqNumber("17K00001");
        return mbsTrade;
    }

    /**
     * This method creates a sample data for MBSTransactionRequest
     * 
     * @return
     */
    private TradeEventMessage createTradeEventMsg(String msgPayload) {
        TradeEventMessage tradeEventMessage = null;
        EventMessage eventMsg;
        try {
            if (Objects.equal(null, ecfJaxbContext)) {
                ecfJaxbContext = JAXBContext.newInstance(EventMessage.class);
            }
            if (Objects.equal(null, unmarshaller)) {
                unmarshaller = ecfJaxbContext.createUnmarshaller();
            }
            StringReader reader = new StringReader(msgPayload);
            eventMsg = (EventMessage) unmarshaller.unmarshal(reader);
            tradeEventMessage = eventMsg.getTradeEventMessage();

        } catch (JAXBException ex) {
        }
        return tradeEventMessage;
    }

    

  
}
