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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.ProductMarketIndicativePricingVO;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jun 13, 2018
 * @File: com.fanniemae.mbsportal.pu.transformer.MarketIndicativePriceMessageTransformerTest.java
 * @Revision:
 * @Description: MarketIndicativePriceMessageTransformerTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class MarketIndicativePriceMessageTransformerTest {

    private MarketIndicativePriceMessageTransformer<TransformationObject> mrketIndicativePriceMessageTransformer;
    private TransformationObject transformationObj;

    @Before
    public void setUp() throws Exception {

        mrketIndicativePriceMessageTransformer = new MarketIndicativePriceMessageTransformer<TransformationObject>();
        transformationObj = new TransformationObject();

        MBSMarketIndicativePrice mbsMarketIndicativePriceLst = getMbsMarketIndicativePrice();
        transformationObj.setSourcePojo(mbsMarketIndicativePriceLst);
    }

    @SuppressWarnings({ "unchecked" })
    @Test
    public void transform_Success() throws MBSBaseException, JsonProcessingException {

        TransformationObject transformationObjRet = mrketIndicativePriceMessageTransformer.transform(transformationObj);
        ProductMarketIndicativePricingVO productMarketIndicativePricingVO = (ProductMarketIndicativePricingVO) transformationObjRet.getTargetPojo();
        assertEquals("FN15", productMarketIndicativePricingVO.getProductNameCode());
        assertEquals("4.00", productMarketIndicativePricingVO.getPassThroughRate());
        assertEquals("2018-06-18", productMarketIndicativePricingVO.getSettlementDate());
        assertEquals("102", productMarketIndicativePricingVO.getBidPrice().getHandle());
        assertEquals("18+", productMarketIndicativePricingVO.getBidPrice().getTick());
        assertEquals("102", productMarketIndicativePricingVO.getAskPrice().getHandle());
        assertEquals("21", productMarketIndicativePricingVO.getAskPrice().getTick());

    }
    

    /**
     * 
     * @return List of MBSMarketIndicativePrice
     * @throws Exception
     */
    private MBSMarketIndicativePrice getMbsMarketIndicativePrice() throws Exception {

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

         return mBSMarketIndicativePrice;

    }
}
