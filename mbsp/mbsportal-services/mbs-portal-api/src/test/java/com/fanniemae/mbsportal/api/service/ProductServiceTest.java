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

import com.fanniemae.mbsportal.api.persister.ProductPersister;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.transformation.ProductPOTransformer;
import com.fanniemae.mbsportal.api.transformation.ProductTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSProductDao;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSBaseEntity;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.cache.AppCacheKeys;
import com.fanniemae.mbsportal.utils.cache.MBSPAppCache;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class handles the test case for the ProductService class
 *
 * @author g8upjv
 *
 */

public class ProductServiceTest extends BaseServiceTest {
        @Mock
        MBSProductDao mbsProductDao;
        
        @InjectMocks
        ProductService productService;
        
        @Mock
        MBSObjectCreator mbsObjectCreator;
        
        @Autowired
        private MBSPAppCache mbspAppCache;
        
        ProductPO prodPO;
        ProductIdPO prodId;
        List<MBSProduct> lstProd;
        
        @Before
        public void setUp() throws Exception {
                prodPO = createPO();
                lstProd = createProductList();
                when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
                Collection<? extends MBSBaseEntity> mbspProducts = (List<MBSProduct>) createProductList();
                doReturn(mbspProducts).when(mbspAppCache).get(AppCacheKeys.PRODUCT_CACHE_KEY);
                
        }
        
        /**
         * Init mocks here not use Mock annotation
         * @throws MBSBaseException
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void initMocks() throws MBSBaseException {
                ProductTransformer<TransformationObject> productTransformer = Mockito.mock(ProductTransformer.class);
                ProductPersister productPersister = Mockito.mock(ProductPersister.class);
                ProductPOTransformer<TransformationObject> productPOTransformer = Mockito.mock(ProductPOTransformer.class);
                productService = new ProductService(productTransformer, productPersister, productPOTransformer);
                doNothing().when(productPersister).clearAll();
                when(mbsProductDao.getAll()).thenReturn(lstProd);
                when(((Persister) productPersister).getBaseDao()).thenReturn(mbsProductDao);
                when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
                MockitoAnnotations.initMocks(this);
                
        }
        
        /**
         * Purpose: Test for case for getting the products
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test(expected=MBSBaseException.class)
        public void testCreateProductsException() throws MBSBaseException {
                initMocks();
                ProductPO productsPO = productService.createProducts(prodPO);
                assertNotNull(productsPO);
                assertNotNull(productsPO.getProductId().toString());
        }
        
        /**
         * Purpose: Test for case for getting the products
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test
        public void testCreateProducts() throws MBSBaseException {
                initMocks();
                ProductPOTransformer productPOTransformer = Mockito.spy(ProductPOTransformer.class);
                ProductPersister productPersister = Mockito.mock(ProductPersister.class);
                ProductTransformer productTransformer = Mockito.mock(ProductTransformer.class);
                productService = new ProductService(productTransformer, productPersister, productPOTransformer);
                MockitoAnnotations.initMocks(this);
                when(mbsProductDao.getById(any())).thenReturn(createProduct());
                when(((Persister) productPersister).getBaseDao()).thenReturn(mbsProductDao);
                TransformationObject transformationObject = new TransformationObject();
                transformationObject.setSourcePojo(createPO());
                doReturn(transformationObject).when((Transformer) productPOTransformer).transform(any());
                when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
                ProductPO productsPO = productService.createProducts(prodPO);
                assertNotNull(productsPO);
                assertNotNull(productsPO.getProductId().toString());
        }
        
        @Test
        public void clearAllMBSProducts() throws Exception {
                initMocks();
                productService.clearAll();
        }
        
        /**
         * Purpose: Test for case for successful call to get list of products
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test
        public void testGetProducts() throws MBSBaseException {
                ProductPOTransformer productPOTransformer = Mockito.spy(ProductPOTransformer.class);
                ProductPersister productPersister = Mockito.mock(ProductPersister.class);
                ProductTransformer productTransformer = Mockito.mock(ProductTransformer.class);
                productService = new ProductService(productTransformer, productPersister, productPOTransformer);
                MockitoAnnotations.initMocks(this);
                doNothing().when(productPersister).clearAll();
                when(mbsProductDao.getAll()).thenReturn(lstProd);
                when(((Persister) productPersister).getBaseDao()).thenReturn(mbsProductDao);
                TransformationObject transformationObject = new TransformationObject();
                transformationObject.setSourcePojo(createPO());
                doReturn(transformationObject).when((Transformer) productPOTransformer).transform(any());
                when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
                List<ProductPO> lstPOResult = productService.getMBSProducts();
                assertNotNull(lstPOResult);
                assertThat(lstPOResult, hasSize(1));
                assertThat(lstPOResult.get(0).getNameCode(), equalTo("FN30"));
        }
        
        /**
         * Purpose: Test for case for successful call to get list of products
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test
        public void testGetProductsById() throws MBSBaseException {
                ProductPOTransformer productPOTransformer = Mockito.spy(ProductPOTransformer.class);
                ProductPersister productPersister = Mockito.mock(ProductPersister.class);
                ProductTransformer productTransformer = Mockito.mock(ProductTransformer.class);
                productService = new ProductService(productTransformer, productPersister, productPOTransformer);
                MockitoAnnotations.initMocks(this);
                doNothing().when(productPersister).clearAll();
                when(mbsProductDao.getById(any())).thenReturn(createProduct());
                when(((Persister) productPersister).getBaseDao()).thenReturn(mbsProductDao);
                TransformationObject transformationObject = new TransformationObject();
                transformationObject.setSourcePojo(createPO());
                doReturn(transformationObject).when((Transformer) productPOTransformer).transform(any());
                when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
                ProductPO lstPOResult = productService.getByProductId("1.PU.MBS");
                assertNotNull(lstPOResult);
                assertThat(lstPOResult.getNameCode(), equalTo("FN30"));
        }
        
        /**
         * Utility method to create objects
         * @return
         */
        private ProductPO createPO() {
                prodPO = new ProductPO();
                prodId = new ProductIdPO();
                prodId.setIdentifier(new Long(1));
                prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
                prodId.setType(PRODUCT_TYPE.MBS);
                prodPO.setProductId(prodId);
                prodPO.setAgencyType("FN");
                prodPO.setDescription("FN30 Fixed");
                prodPO.setNameCode("FN30");
                prodPO.setNameCodeSortOrder(1);
                prodPO.setSecurityTerm(360);
                return prodPO;
        }
        
        /**
         * Utility for createProductList
         * @return
         */
        private List<MBSProduct> createProductList() {
                List<MBSProduct> lstProd = new ArrayList<MBSProduct>();
                MBSProduct mbsProduct = new MBSProduct();
                mbsProduct.setProductBRSCode("anbut");
                mbsProduct.setProductNameCode("FN30");
                ProductId productId = new ProductId();
                productId.setSourceType("MBS");
                productId.setType("PU");
                mbsProduct.setProductId(productId);
                lstProd.add(mbsProduct);
                return lstProd;
        }
        
        /**
         * Utility for createProductList
         * @return
         */
        private MBSProduct createProduct() {
                MBSProduct mbsProduct = new MBSProduct();
                mbsProduct.setProductBRSCode("anbut");
                mbsProduct.setProductNameCode("FN30");
                ProductId productId = new ProductId();
                productId.setIdentifier(new Long(1));
                productId.setSourceType("MBS");
                productId.setType("PU");
                mbsProduct.setProductId(productId);
                return mbsProduct;
        }
        
}
