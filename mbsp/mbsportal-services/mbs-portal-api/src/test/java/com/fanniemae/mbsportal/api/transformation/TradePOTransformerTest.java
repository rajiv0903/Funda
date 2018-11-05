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
package com.fanniemae.mbsportal.api.transformation;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.api.transformation.TradePOTransformer;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.securitiesods.ods_core.domain.TradeParty;

/**
 * This class handles the test case for the TradePOTransformerTest
 * class
 * 
 * date 08/02/2017
 * 
 * @author g8upjv
 * 
 */

public class TradePOTransformerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradePOTransformerTest.class);

	private TradePOTransformer tradePOTransformer = new TradePOTransformer();

	private MBSTrade mbsTrade;

	/**
	 * Purpose: This method tests the call to transformation from
	 * TransactionRequestPO to MBSTransaction for successful call
	 * 
	 * @throws Exception
	 */
	@Test
	public void transform() throws Exception {
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
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setTargetPojo(mbsTrade);
		tradePOTransformer.transform(transformationObject);
		TradePO tradePO = (TradePO) transformationObject.getSourcePojo();
		assertTrue(tradePO.getTradeSourceId().equals("BRS"));
	}
	
	/**
	 * Purpose: This method tests the call to transformation from
	 * TransactionRequestPO to MBSTransaction for successful call
	 * 
	 * @throws Exception
	 */
	@Test(expected=MBSBaseException.class)
	public void transformException() throws Exception {
		mbsTrade = new MBSTrade();
		mbsTrade.setTransReqNumber("17J00004");
		mbsTrade.setTradeDate(new Date());
		mbsTrade.setSourceId("BRS");
		mbsTrade.setSourcePrimaryTradeId(12345);
		mbsTrade.setSubPortfolioId(1111);
		mbsTrade.setSubPortfolioShortName("SIT_OPS");
		mbsTrade.setTradeParty(null);
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setTargetPojo(mbsTrade);
		tradePOTransformer.transform(transformationObject);
		TradePO tradePO = (TradePO) transformationObject.getSourcePojo();
		assertTrue(tradePO.getTradeSourceId().equals("BRS"));
	}
}
