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

package com.fanniemae.portal.listener;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.message.ESBMessage;
import com.fanniemae.all.messaging.message.ESBMessageFactory;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.ecf.ods.EventMessage;
import com.fanniemae.mbsportal.ecf.ods.TradeEventMessage;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.listener.ESBMessageListener;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.service.ESBTransactionRequestService;
import com.fanniemae.mbsportal.transformer.ECFTransformer;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.Files;
import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test class for ESB Listener
 * 
 * @author g8upjv
 *
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore("Need to fix the junit framework for this module")
public class ESBMessageListenerTest {
    
    @InjectMocks
	ESBMessageListener esbMessageListener = new ESBMessageListener();
   
	@Mock
	private ESBClient esbClient;
	
	@Mock
	private ECFTransformer ecfTransformer;

	@Mock
	private ESBTransactionRequestService esbTransactionRequestService;
	
	private ClassLoader classLoader;
    
	private String inputMsg;
    
	private String inputEntireMsg;
    
	protected static JAXBContext ecfJaxbContext;

	private Unmarshaller unmarshaller;
    
    @Before
    public void setUp() throws Exception {
    	classLoader = getClass().getClassLoader();
    	inputMsg = Files.toString(new File(classLoader.getResource("test_br_message.xml").getFile()), Charsets.UTF_8);
    	inputEntireMsg = Files.toString(new File(classLoader.getResource("test_br_message_full.xml").getFile()), Charsets.UTF_8);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void startESBClientAsyncConsumer() throws Exception {
    	doNothing().when(esbClient).startMessageConsumer(any());
    	esbMessageListener.startESBClientAsyncConsumer();
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void processMessage() throws Exception {
    	when(ecfTransformer.getMBSTrade(any()))
        .thenReturn(createTradeEventMsg());
    	doNothing().when(esbClient).suspendMessage(any());
    	when(ecfTransformer.transform(any()))
        .thenReturn(createSampleTrade());
    	when(esbTransactionRequestService.getMBSTransReq(any())).thenReturn(createSampleTransReq());
    	when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
    	when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
    	ESBMessage esbMessage = ESBMessageFactory.createESBMessage(inputEntireMsg);
    	assertTrue(esbMessageListener.processMessage(esbMessage));
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void processMessageEmptyTransReq() throws Exception {
    	when(ecfTransformer.getMBSTrade(any()))
        .thenReturn(createTradeEventMsg());
    	doNothing().when(esbClient).suspendMessage(any());
    	when(ecfTransformer.transform(any()))
        .thenReturn(createSampleTrade());
    	when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
    	when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
    	ESBMessage esbMessage = ESBMessageFactory.createESBMessage(inputEntireMsg);
    	assertTrue(esbMessageListener.processMessage(esbMessage));
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void processMessageEmptyTransId() throws Exception {
    	when(ecfTransformer.getMBSTrade(any()))
        .thenReturn(createTradeEventMsg());
    	doNothing().when(esbClient).suspendMessage(any());
    	when(ecfTransformer.transform(any()))
        .thenReturn(createSampleTradeNoTransId());
    	when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
    	when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
    	ESBMessage esbMessage = ESBMessageFactory.createESBMessage(inputEntireMsg);
    	assertTrue(esbMessageListener.processMessage(esbMessage));
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void processMessageNullEvent() throws Exception {
    	when(ecfTransformer.getMBSTrade(any()))
        .thenReturn(createTradeEventMsgNullEvent());
    	doNothing().when(esbClient).suspendMessage(any());
    	when(ecfTransformer.transform(any()))
        .thenReturn(createSampleTrade());
    	when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
    	when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
    	ESBMessage esbMessage = ESBMessageFactory.createESBMessage(inputEntireMsg); 
    	assertTrue(esbMessageListener.processMessage(esbMessage));
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void processMessageInvalidEventTypeCode() throws Exception {
    	when(ecfTransformer.getMBSTrade(any()))
        .thenReturn(createTradeEventMsgEmptyEventId());
    	doNothing().when(esbClient).suspendMessage(any());
    	when(ecfTransformer.transform(any()))
        .thenReturn(createSampleTrade());
    	when(esbTransactionRequestService.updateMBSTrade(any())).thenReturn(true);
    	when(esbTransactionRequestService.updateMBSTransReq(any())).thenReturn(true);
    	ESBMessage esbMessage = ESBMessageFactory.createESBMessage(inputEntireMsg); 
    	assertTrue(esbMessageListener.processMessage(esbMessage));
    }
    
    /**
     * This method creates a sample data for MBSTransactionRequest
     * @return
     */
    public MBSTransactionRequest createSampleTransReq(){
    	MBSTransactionRequest mbsTransactionRequest = new MBSTransactionRequest();
    	ProductId prodId = new ProductId();
		prodId.setIdentifier(new Long(1));
		prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
		prodId.setType(PRODUCT_TYPE.MBS.toString());
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
     * @return
     */
    public MBSTrade createSampleTrade(){
    	MBSTrade mbsTrade = new MBSTrade();
    	mbsTrade.setTransReqNumber("17K00001");
		return mbsTrade;
    }
    
    /**
     * This method creates a sample data for MBSTransactionRequest
     * @return
     */
    public MBSTrade createSampleTradeNoTransId(){
    	MBSTrade mbsTrade = new MBSTrade();
    	mbsTrade.setTransReqNumber("");
		return mbsTrade;
    }
    
    /**
     * This method creates a sample data for MBSTransactionRequest
     * @return
     */
    public TradeEventMessage createTradeEventMsg(){
    	TradeEventMessage tradeEventMessage = null;
		EventMessage eventMsg;
		try {
			if (Objects.equal(null, ecfJaxbContext)) {
				ecfJaxbContext = JAXBContext.newInstance(EventMessage.class);
			}
			if (Objects.equal(null, unmarshaller)) {
				unmarshaller = ecfJaxbContext.createUnmarshaller();
			}
			StringReader reader = new StringReader(inputMsg);
			eventMsg = (EventMessage) unmarshaller.unmarshal(reader);
			tradeEventMessage = eventMsg.getTradeEventMessage();

		} catch (JAXBException ex) {
		}
		return tradeEventMessage;
    }
    
    /**
     * This method creates a sample data for MBSTransactionRequest
     * @return
     */
    public TradeEventMessage createTradeEventMsgNullEvent(){
    	TradeEventMessage tradeEventMessage = null;
		EventMessage eventMsg;
		try {
			if (Objects.equal(null, ecfJaxbContext)) {
				ecfJaxbContext = JAXBContext.newInstance(EventMessage.class);
			}
			if (Objects.equal(null, unmarshaller)) {
				unmarshaller = ecfJaxbContext.createUnmarshaller();
			}
			StringReader reader = new StringReader(inputMsg);
			eventMsg = (EventMessage) unmarshaller.unmarshal(reader);
			tradeEventMessage = eventMsg.getTradeEventMessage();
			tradeEventMessage.setEvent(null);

		} catch (JAXBException ex) {
		}
		return tradeEventMessage;
    }
    
    /**
     * This method creates a sample data for MBSTransactionRequest
     * @return
     */
    public TradeEventMessage createTradeEventMsgEmptyEventId(){
    	TradeEventMessage tradeEventMessage = null;
		EventMessage eventMsg;
		try {
			if (Objects.equal(null, ecfJaxbContext)) {
				ecfJaxbContext = JAXBContext.newInstance(EventMessage.class);
			}
			if (Objects.equal(null, unmarshaller)) {
				unmarshaller = ecfJaxbContext.createUnmarshaller();
			}
			StringReader reader = new StringReader(inputMsg);
			eventMsg = (EventMessage) unmarshaller.unmarshal(reader);
			tradeEventMessage = eventMsg.getTradeEventMessage();
			tradeEventMessage.getEvent().setTypeCode("TRDCNF");;

		} catch (JAXBException ex) {
		}
		return tradeEventMessage;
    }
    
}
