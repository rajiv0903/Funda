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

package com.fanniemae.mbsportal.api.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.fanniemae.mbsportal.api.persister.TradePersister;
import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.api.service.TradeService;
import com.fanniemae.mbsportal.api.transformation.TradePOTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSTradeDao;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.securitiesods.ods_core.domain.TradeParty;

/**
 * This class handles the test case for the TradeServiceTest class
 * 
 * @author g8upjv
 *
 */

public class TradeServiceTest extends BaseServiceTest {

	@InjectLog
	private Logger LOGGER;

	@Mock
	TradePersister tradePersister;

	@Mock
	TradePOTransformer tradePOTransformer;

	@Mock
	MBSTradeDao mbsTradeDao;
	
	    @Mock
	    MBSObjectCreator mbsObjectCreator;


	private MBSTrade mbsTrade;
	private TransformationObject transformationObject;
	private TradePO mbsTradePO;
	private List<TradePO> mbsTradePOLst;
	private List<MBSTrade> mbsMBSTradeLst;

	@InjectMocks
	TradeService tradeService;

	@Before
	public void setUp() throws Exception {
	}

	public void initMocks() throws Exception {
		tradeService = new TradeService(tradePersister, tradePOTransformer);
		MockitoAnnotations.initMocks(this);
		doNothing().when(tradePersister).clearAll();
		when(tradePersister.getBaseDao()).thenReturn(mbsTradeDao);

	}

	/**
	 * Purpose: This method test call to get the data from gemfire
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTrade() throws Exception {
		TradePOTransformer tradePOTransformer = Mockito.spy(TradePOTransformer.class);
		tradeService = new TradeService(tradePersister, tradePOTransformer);
		MockitoAnnotations.initMocks(this);
		doNothing().when(tradePersister).clearAll();
		when(tradePersister.getBaseDao()).thenReturn(mbsTradeDao);
		createData();
		when(mbsTradeDao.getAll()).thenReturn(mbsMBSTradeLst);
		when(mbsTradeDao.getByTransReqId(Mockito.any())).thenReturn(mbsTrade);
		Mockito.doReturn(transformationObject).when((Transformer) tradePOTransformer).transform(Mockito.any());
		when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
		List<TradePO> tradePOLst = tradeService.getMBSTrade("17J00004");
		assertNotNull(tradePOLst);
	}

	/**
	 * Purpose: This method test call to get the data from gemfire when no trans
	 * req id is present
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTradeNoTransReq() throws Exception {
		TradePOTransformer tradePOTransformer = Mockito.spy(TradePOTransformer.class);
		tradeService = new TradeService(tradePersister, tradePOTransformer);
		MockitoAnnotations.initMocks(this);
		doNothing().when(tradePersister).clearAll();
		when(tradePersister.getBaseDao()).thenReturn(mbsTradeDao);
		createData();
		when(mbsTradeDao.getAll()).thenReturn(mbsMBSTradeLst);
		when(mbsTradeDao.getByTransReqId(Mockito.any())).thenReturn(mbsTrade);
		Mockito.doReturn(transformationObject).when((Transformer) tradePOTransformer).transform(Mockito.any());
		when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
		List<TradePO> tradePOLst = tradeService.getMBSTrade("");
		assertNotNull(tradePOLst);
	}

	/**
	 * Purpose: This method is to clear all trade requests - temp use only.
	 *
	 * @throws Exception
	 */
	@Test
	public void clearAllMBSTransactionRequests() throws Exception {
		initMocks();
		tradeService.clearAll();
	}

	/**
	 * Method to create data for the test cases
	 * 
	 * @throws MBSBaseException
	 */
	public void createData() throws MBSBaseException {

		mbsTrade = new MBSTrade();
		TradeParty tradeParty = new TradeParty();
		tradeParty.setPartyShortName("TEST-C");
		mbsTrade.setTransReqNumber("17J00004");
		mbsTrade.setTradeDate(new Date());
		mbsTrade.setSourceId("BRS");
		mbsTrade.setSourcePrimaryTradeId(12345);
		mbsTrade.setSubPortfolioId(1111);
		mbsTrade.setSubPortfolioShortName("SIT_OPS");
		mbsTrade.setTradeParty(tradeParty);
		mbsTradePO = new TradePO();
		mbsTradePO.setTransactionRequestId(mbsTrade.getTransReqNumber());
		mbsTradePO.setTradeSourceId(mbsTrade.getSourceId());
		mbsTradePO.setTradeSourcePrimaryId(mbsTrade.getSourcePrimaryTradeId());
		mbsTradePO.setTradeSubPortfolioId(mbsTrade.getSubPortfolioId());
		mbsTradePO.setTradeDate("2017-11-11");
		mbsTradePO.setLenderEntityName(mbsTrade.getTradeParty().getPartyShortName());
		mbsTradePO.setTradeSubPortfolioShortName(mbsTrade.getSubPortfolioShortName());
		transformationObject = new TransformationObject();
		transformationObject.setTargetPojo(mbsTrade);
		transformationObject.setSourcePojo(mbsTradePO);
		mbsTradePOLst = new ArrayList<>();
		mbsMBSTradeLst = new ArrayList<>();
		mbsTradePOLst.add(mbsTradePO);
		mbsMBSTradeLst.add(mbsTrade);

	}

}
