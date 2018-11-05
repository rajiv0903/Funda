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

package com.fanniemae.mbsportal.dao;

import static com.fanniemae.mbsportal.constants.DAOConstants.IDTypes.PRODUCT_PRICING_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.geode.cache.query.internal.ResultsSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * @author g8uaxt Created on 9/8/2017.
 */

public class MBSProductPricingDaoTest extends BaseDaoTest {
        private static String regionName = "MBSProductPricing";
        @Autowired
        private MBSProductPricingDao mbsProductPricingDao;
        private ResultsSet selectResults;
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Before
        public void setUp() throws Exception {
                
                when(mockRegion.getName()).thenReturn(regionName);
                when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);//TODO: check it later?
                when(idServiceDao.getSeqId(PRODUCT_PRICING_ID)).thenReturn("1234");
                selectResults = new ResultsSet();
                List<MBSProductPricingRequest> lstMBSProductPricingt = new ArrayList<>();
                selectResults.asList().addAll(lstMBSProductPricingt);
                when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
                
        }
        
        @Test
        public void saveOrUpdateForNew() throws MBSBaseException {
                MBSProductPricingRequest mbsProductPricingRequest = createMBSProductPricingRequest();
                //call it
                mbsProductPricingDao.saveOrUpdate(mbsProductPricingRequest);
                Assert.assertNotNull(mbsProductPricingRequest);
                //assertThat(mbsProductPricingRequest.getPricingIndicativeIdentifier().is(equalTo(new Long(1234))));
                assertEquals(new Long(1234), mbsProductPricingRequest.getPricingIndicativeIdentifier());
                assertEquals(new BigDecimal(4), mbsProductPricingRequest.getPassThroughRate());
                
        }
        
        @Test(expected = MBSBaseException.class)
        @Ignore
        public void saveOrUpdateExeception() throws MBSBaseException {
                doThrow(new MBSBusinessException("Invalide Id", MBSExceptionConstants.BUSINESS_EXCEPTION)).when(idServiceDao).getSeqId(PRODUCT_PRICING_ID);
                MBSProductPricingRequest mbsProductPricingRequest = createMBSProductPricingRequest();
                mbsProductPricingDao.saveOrUpdate(mbsProductPricingRequest);
        }
        
        @Test
        public void getAllByProductKey() throws MBSBaseException {
                Long identifier = 1l;
                String sourceType = TradeConstants.PRODUCT_SOURCE_TYPE.PU.getSrcType();
                String type = TradeConstants.PRODUCT_TYPE.MBS.getType();
                List<MBSProductPricingRequest> acutal = mbsProductPricingDao
                        .getAllByProductKey(identifier, sourceType, type);
                assertNotNull(acutal);
                //assertNotNull(acutal.get(0));
                
        }
        
        /**
         * utility method to create mock data
         * @return MBSProductPricingRequest
         */
        private MBSProductPricingRequest createMBSProductPricingRequest() {
                MBSProductPricingRequest mbsProductPricingRequest = new MBSProductPricingRequest();
                //mbsProductPricingRequest.setPricingIndicativeIdentifier(1213245);
                ProductId productId = new ProductId();
                productId.setIdentifier(Long.valueOf(10));
                productId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU.name());
                productId.setType(TradeConstants.PRODUCT_TYPE.MBS.name());
                mbsProductPricingRequest.setProductId(productId);
                mbsProductPricingRequest.setEffectiveDate(new Date());
                mbsProductPricingRequest.setPassThroughRate(new BigDecimal(4));
                mbsProductPricingRequest.setMktTermType(TradeConstants.MktTermType.FN30.getTerm());
                mbsProductPricingRequest.setSettlementDate(new Date());
                mbsProductPricingRequest.setMktPricePercent(new BigDecimal(0.1213));
                mbsProductPricingRequest.setBuySellInd("BUY");
                //mbsProductPricingRequest.setAskPricePercent();
                return mbsProductPricingRequest;
        }
}
