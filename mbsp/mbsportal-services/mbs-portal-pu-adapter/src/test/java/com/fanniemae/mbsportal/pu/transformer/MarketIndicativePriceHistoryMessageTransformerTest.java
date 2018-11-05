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

package com.fanniemae.mbsportal.pu.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePriceHistory;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 9, 2018
 * @File: com.fanniemae.mbsportal.pu.transformer.MarketIndicativePriceHistoryMessageTransformerTest.java 
 * @Revision: 
 * @Description: MarketIndicativePriceHistoryMessageTransformerTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class MarketIndicativePriceHistoryMessageTransformerTest {

    private MarketIndicativePriceHistoryMessageTransformer<TransformationObject> marketIndicativePriceHistoryMessageTransformer;
    private TransformationObject transformationObj;

    @Before
    public void setUp() throws Exception {

        marketIndicativePriceHistoryMessageTransformer = new MarketIndicativePriceHistoryMessageTransformer<TransformationObject>();
        transformationObj = new TransformationObject();

        List<MBSMarketIndicativePrice> mbsMarketIndicativePriceLst = getMbsMarketIndicativePriceLst();
        transformationObj.setSourcePojo(mbsMarketIndicativePriceLst);
    }

    @SuppressWarnings({ "unchecked" })
    @Test
    public void transform_Success() throws Exception, MBSBaseException, JsonProcessingException {

        TransformationObject transformationObjRet = marketIndicativePriceHistoryMessageTransformer.transform(transformationObj);
        List<MBSMarketIndicativePriceHistory> mBSMarketIndicativePriceHistory = (List<MBSMarketIndicativePriceHistory>) transformationObjRet.getTargetPojo();

        assertEquals(2, mBSMarketIndicativePriceHistory.size());
        
        assertEquals("FN15", mBSMarketIndicativePriceHistory.get(0).getProductNameCode());
        assertTrue(new BigDecimal("4.00").compareTo(mBSMarketIndicativePriceHistory.get(0).getPassThroughRate()) == 0);
        assertTrue(new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-18").compareTo( mBSMarketIndicativePriceHistory.get(0).getSettlementDate()) ==0);
        assertTrue(new BigDecimal("102.578125").compareTo(mBSMarketIndicativePriceHistory.get(0).getBidPricePercent()) == 0);
        assertEquals("102-18+", mBSMarketIndicativePriceHistory.get(0).getBidPriceText());
        assertTrue(new BigDecimal("102.65625").compareTo(mBSMarketIndicativePriceHistory.get(0).getAskPricePercent()) == 0);
        assertEquals("102-21", mBSMarketIndicativePriceHistory.get(0).getAskPriceText());
        assertTrue(new BigDecimal("102.6171875").compareTo(mBSMarketIndicativePriceHistory.get(0).getMidPricePercent()) == 0);
        assertEquals("102-196", mBSMarketIndicativePriceHistory.get(0).getMidPriceText());
        
        assertNull(mBSMarketIndicativePriceHistory.get(1).getProductNameCode());
        assertNull(mBSMarketIndicativePriceHistory.get(1).getBidPriceText());
        assertNull(mBSMarketIndicativePriceHistory.get(1).getMidPriceText());
        assertNull(mBSMarketIndicativePriceHistory.get(1).getAskPriceText());
        assertNull(mBSMarketIndicativePriceHistory.get(1).getSettlementDate());
        assertNull(mBSMarketIndicativePriceHistory.get(1).getPassThroughRate());
    }
    

    /**
     * 
     * @return List of MBSMarketIndicativePrice
     * @throws Exception
     */
    private List<MBSMarketIndicativePrice> getMbsMarketIndicativePriceLst() throws Exception {

        List<MBSMarketIndicativePrice> mbsMarketIndicativePriceLst = new ArrayList<>();

        MBSMarketIndicativePrice mBSMarketIndicativePrice = new MBSMarketIndicativePrice();
        mBSMarketIndicativePrice.setProductNameCode("FN15");
        mBSMarketIndicativePrice.setBidPricePercent(new BigDecimal("102.578125"));
        mBSMarketIndicativePrice.setBidPriceText("102-18+");
        mBSMarketIndicativePrice.setAskPricePercent(new BigDecimal("102.65625"));
        mBSMarketIndicativePrice.setAskPriceText("102-21");
        mBSMarketIndicativePrice.setMidPricePercent(new BigDecimal("102.6171875"));
        mBSMarketIndicativePrice.setMidPriceText("102-196");
        mBSMarketIndicativePrice.setSettlementDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-06-18", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSMarketIndicativePrice.setObservationDate(1528898940858L);
        mBSMarketIndicativePrice.setPassThroughRate(MBSPortalUtils.convertToBigDecimal("0.04", 5, 4).scaleByPowerOfTen(2));
        mBSMarketIndicativePrice.setForward(0);

        mbsMarketIndicativePriceLst.add(mBSMarketIndicativePrice);

        mBSMarketIndicativePrice = new MBSMarketIndicativePrice();
        mBSMarketIndicativePrice.setProductNameCode(null);
        mBSMarketIndicativePrice.setBidPricePercent(new BigDecimal("102.578125"));
        mBSMarketIndicativePrice.setBidPriceText(null);
        mBSMarketIndicativePrice.setAskPricePercent(new BigDecimal("102.65625"));
        mBSMarketIndicativePrice.setAskPriceText(null);
        mBSMarketIndicativePrice.setMidPricePercent(new BigDecimal("102.6171875"));
        mBSMarketIndicativePrice.setMidPriceText(null);
        mBSMarketIndicativePrice.setSettlementDate(null);
        mBSMarketIndicativePrice.setObservationDate(1528898940858L);
        mBSMarketIndicativePrice.setPassThroughRate(null);
        mBSMarketIndicativePrice.setForward(1);

        mbsMarketIndicativePriceLst.add(mBSMarketIndicativePrice);

        return mbsMarketIndicativePriceLst;

    }
}
