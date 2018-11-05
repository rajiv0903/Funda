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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSBaseEntity;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.pu.constants.MessageEventType;
import com.fanniemae.mbsportal.utils.cache.AppCacheKeys;
import com.fanniemae.mbsportal.utils.cache.MBSPAppCache;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Test case for Transformer
 * @author g8uaxt Created on 6/8/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class PUPriceMessageTransformerTest {

        @InjectMocks
        private PUPriceMessageTransformerImpl puPriceMessageTransformer;
        private ClassLoader classLoader;
        private String inputMsg;
        @Mock
        private MBSPAppCache mbspAppCache;
        @Mock
        Logger LOGGER;
        
        @Before
        public void setUp() throws Exception {
                classLoader = getClass().getClassLoader();
                inputMsg = Files.toString(new File(classLoader.getResource("payload.json").getFile()), Charsets.UTF_8);
                Collection<? extends MBSBaseEntity> mbspProducts = (List<MBSProduct>) createProductList();
                doReturn(mbspProducts).when(mbspAppCache).get(AppCacheKeys.PRODUCT_CACHE_KEY);
        }
        
 
        /*
         * Test case for transform
         * @throws Exception
         */
        @Test
        public void transform() throws MBSBaseException {
                List<MBSMarketIndicativePrice> result = puPriceMessageTransformer.transform(inputMsg, MessageEventType
                        .TBA);
                assertNotNull(result);
                assertEquals(35,result.size());
               
        }
        
        /**
         * test for product mapping
         * @throws MBSBaseException
         */
        @Test
        public void transformForProdMapping() throws MBSBaseException {
                List<MBSMarketIndicativePrice> result = puPriceMessageTransformer.transform(inputMsg, MessageEventType
                        .TBA);
                assertNotNull(result);
                assertEquals(result.get(31).getProductNameCode(),"PC15");
                assertEquals(result.get(31).getPuNameCode(),"FR15");
        }
        
        
        @Test(expected = MBSDataAccessException.class)
        public void transformForNullPayload() throws MBSBaseException {
                List<MBSMarketIndicativePrice> result = puPriceMessageTransformer.transform(null, MessageEventType
                        .TBA);
                assertNotNull(result);
        }
        
        /**
         * Utility for createProductList
         * @return
         */
        private Collection<? extends MBSBaseEntity> createProductList() {
                List<MBSProduct> lstProd = new ArrayList<MBSProduct>();
                MBSProduct mbsProduct = new MBSProduct();
                mbsProduct.setProductBRSCode("anbut");
                mbsProduct.setProductNameCode("PC15");
                mbsProduct.setProductPUCode("FR15");
                ProductId productId = new ProductId();
                productId.setSourceType("MBS");
                productId.setType("PU");
                mbsProduct.setProductId(productId);
                lstProd.add(mbsProduct);
                return lstProd;
        }
        
}
