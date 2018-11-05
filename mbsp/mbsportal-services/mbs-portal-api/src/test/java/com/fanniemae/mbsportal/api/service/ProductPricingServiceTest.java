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

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.api.enrichment.ProductPricingRequestEnrichment;
import com.fanniemae.mbsportal.api.persister.ProductPricingPersister;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.service.ProductPricingService;
import com.fanniemae.mbsportal.api.transformation.ProductPricingPOTransformer;
import com.fanniemae.mbsportal.api.transformation.ProductPricingRequestTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.validator.ProductPricingPOValidator;
import com.fanniemae.mbsportal.calendar.client.EnterpriseCalendarServiceClient;
import com.fanniemae.mbsportal.calendar.schema.v1.GetCalendarDayResponse;
import com.fanniemae.mbsportal.calendar.schema.v1.GetCalendarDayResponse.CalendarDays;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSProductPricingDao;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * @author g8uaxt Created on 9/15/2017.
 * @author g8upjv Updated on 11/30/2017.
 */

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class ProductPricingServiceTest extends BaseServiceTest {

        @InjectMocks
        ProductPricingService productPricingService;
        @Mock
        ProductPricingRequestEnrichment<TransformationObject> productPricingRequestEnrichment;
        
        @Mock
        ProductPricingPOValidator<TransformationObject>  productPricingPOValidator;
        @Mock
        ProductPricingRequestTransformer<TransformationObject>  productPricingRequestTransformer;
        @Mock
        ProductPricingPersister productPricingPersister;
        @Mock
        ProductPricingPOTransformer<TransformationObject>  productPricingPOTransformer;
        @Mock
        MBSProductPricingDao mbsProductPricingDao;
        @Mock
        EnterpriseCalendarServiceClient enterpriseCalendarServiceClient;
        
        @Mock
        MBSObjectCreator mbsObjectCreator;
        
		TransformationObject transformationObject;
		
		List<MBSProductPricingRequest> listMBSProductPricing;
		
		GetCalendarDayResponse getCalendarDayResponse;
        
        @Before
        public void setUp() throws Exception {
                productPricingService = new ProductPricingService(productPricingPOValidator,
                        productPricingRequestTransformer, productPricingPersister, productPricingPOTransformer);
                MockitoAnnotations.initMocks(this);
                doNothing().when((Persister) productPricingPersister).persist(any());
                doNothing().when(mbsProductPricingDao).saveOrUpdate(any());
                when(productPricingPersister.getBaseDao()).thenReturn(mbsProductPricingDao);
                when(((Persister) productPricingPersister).getBaseDao()).thenReturn(mbsProductPricingDao);
                List<MBSProductPricingRequest> listMBSProductPricing = new ArrayList<>();
                MBSProductPricingRequest mbsProductPricingRequest = new MBSProductPricingRequest();
                mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
                listMBSProductPricing.add(mbsProductPricingRequest);
                when(mbsProductPricingDao.getAllByProductKey(any(), any(), any())).thenReturn(listMBSProductPricing);
                doNothing().when(productPricingRequestEnrichment).enrich(any());
                TransformationObject transformationObject = new TransformationObject();
                List<ProductPricingPO> productPricingPOList = new ArrayList<>();
                productPricingPOList.add(createProductPricingPO(Long.valueOf(10)));
                //set source object
                transformationObject.setSourcePojo(productPricingPOList);
                when(productPricingPOTransformer.transform(any())).thenReturn(transformationObject);
                when(((Transformer) productPricingPOTransformer).transform(any())).thenReturn(transformationObject);
                when(productPricingPOTransformer.transform(any()).getSourcePojo()).thenReturn(transformationObject);
                when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
                doNothing().when(productPricingPersister).clearAll();
        }
        
        @After
        public void tearDown() throws Exception {
        }
        
        @Test
        public void clearAll() throws Exception {
                productPricingService.clearAll();
        }
        
        @Test
        public void createProductPricing() throws Exception {
            createData();    
        	List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
            //set records
            ProductPricingPO productPricingPO = new ProductPricingPO();
            productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
            productPricingPO.setProductNameCode("FN30");
            productPricingPO.setBuySellIndicator("BUY");
            productPricingPO.setEffectiveDate("2017-10-11");
            productPricingPO.setSettlementDate("2017-09-21");
            productPricingPO.setPassThroughRate("4.000");
            productPricingPO.setMarketTermType(360);
            productPricingPORequest.add(productPricingPO);
            when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(getCalendarDayResponse);
            productPricingService.createProductPricing(productPricingPORequest);
        }
        
        @Test
        public void createProductPricing_Filtered() throws Exception {
            createData();    
                List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
            //set records
            ProductPricingPO productPricingPO = new ProductPricingPO();
            productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
            productPricingPO.setProductNameCode("FN30");
            productPricingPO.setBuySellIndicator("BUY");
            productPricingPO.setEffectiveDate("2017-10-11");
            productPricingPO.setSettlementDate("2017-09-21");
            productPricingPO.setPassThroughRate("4.000");
            productPricingPO.setMarketTermType(360);
            productPricingPO.setMarkAsDelete(true);
            productPricingPORequest.add(productPricingPO);
            when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(getCalendarDayResponse);
            productPricingService.createProductPricing(productPricingPORequest);
        }
        
        @Test(expected = MBSBaseException.class)
        public void createProductPricingCalendarServiceMBSException() throws Exception {
            createData();    
        	List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
            //set records
            ProductPricingPO productPricingPO = new ProductPricingPO();
            productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
            productPricingPO.setProductNameCode("FN30");
            productPricingPO.setBuySellIndicator("BUY");
            productPricingPO.setEffectiveDate("2017-10-11");
            productPricingPO.setSettlementDate("2017-09-21");
            productPricingPO.setPassThroughRate("4.000");
            productPricingPO.setMarketTermType(360);
            productPricingPORequest.add(productPricingPO);
            when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenThrow(MBSBaseException.class);
            productPricingService.createProductPricing(productPricingPORequest);
        }
        
        @Test(expected = MBSBaseException.class)
        public void createProductPricingCalendarServiceException() throws Exception {
            createData();    
        	List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
            //set records
            ProductPricingPO productPricingPO = new ProductPricingPO();
            productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
            productPricingPO.setProductNameCode("FN30");
            productPricingPO.setBuySellIndicator("BUY");
            productPricingPO.setEffectiveDate("2017-10-11");
            productPricingPO.setSettlementDate("2017-09-21");
            productPricingPO.setPassThroughRate("4.000");
            productPricingPO.setMarketTermType(360);
            productPricingPORequest.add(productPricingPO);
            when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenThrow(Exception.class);
            productPricingService.createProductPricing(productPricingPORequest);
        }

        
        @Test(expected = MBSBaseException.class)
        public void createProductPricingInvalid() throws Exception {
                doThrow(new MBSBusinessException("Not able to transform", MBSExceptionConstants.BUSINESS_EXCEPTION)).when((Transformer) productPricingPOTransformer)
                        .transform(any());
                when(((Transformer) productPricingPOTransformer).transform(any()))
                        .thenThrow(new MBSBusinessException("Not able to transform", MBSExceptionConstants.BUSINESS_EXCEPTION));
                List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
                //set records
                ProductPricingPO productPricingPO = new ProductPricingPO();
                productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
                productPricingPO.setProductNameCode("FN30");
                productPricingPO.setBuySellIndicator("BUY");
                productPricingPO.setEffectiveDate(""); //empty to fail test case
                productPricingPO.setSettlementDate("");
                productPricingPO.setPassThroughRate("4.000");
                productPricingPO.setMarketTermType(360);
                productPricingPORequest.add(productPricingPO);
                productPricingService.createProductPricing(productPricingPORequest);
        }
        
        /**
         * Exception when no settlement date is passed
         * @throws Exception
         */
        @Test(expected = MBSBaseException.class)
        public void createProductPricingNoSettlementDate() throws Exception {
                List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
                //set records
                ProductPricingPO productPricingPO = new ProductPricingPO();
                productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
                productPricingPO.setProductNameCode("FN30");
                productPricingPO.setBuySellIndicator("BUY");
                productPricingPO.setEffectiveDate("2017-10-11"); 
                productPricingPO.setSettlementDate("");//empty to fail test case
                productPricingPO.setPassThroughRate("4.000");
                productPricingPO.setMarketTermType(360);
                productPricingPORequest.add(productPricingPO);
                productPricingService.createProductPricing(productPricingPORequest);
        }
        
        @Test
        public void getProductPricing() throws Exception {
        	createData();    
        	when(mbsProductPricingDao.getAllByProductKey(any(), any(), any())).thenReturn(listMBSProductPricing);
        	when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(getCalendarDayResponse);        	
            List<ProductPricingPO> result = productPricingService
                    .getProductPricing(createProductIdPO(Long.valueOf(10)), false);
            assertNotNull(result);
                
        }
        
        @Test
        public void getProductPricing_Filtered() throws Exception {
                createData();    
                when(mbsProductPricingDao.getAllByProductKey(any(), any(), any())).thenReturn(listMBSProductPricing);
                when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(getCalendarDayResponse);           
            List<ProductPricingPO> result = productPricingService
                    .getProductPricing(createProductIdPO(Long.valueOf(10)), true);
            assertNotNull(result);
                
        }
        
        @Test(expected = MBSBaseException.class)
        public void getProductPricingCalendarException() throws Exception {
        	createData();    
        	when(mbsProductPricingDao.getAllByProductKey(any(), any(), any())).thenReturn(listMBSProductPricing);
        	when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenThrow(Exception.class);  	
            List<ProductPricingPO> result = productPricingService
                    .getProductPricing(createProductIdPO(Long.valueOf(10)), false);
            assertNull(result);
                
        }
        
        @Test(expected = MBSBaseException.class)
        public void getProductPricingCalendarMBSException() throws Exception {
        	createData();    
        	when(mbsProductPricingDao.getAllByProductKey(any(), any(), any())).thenReturn(listMBSProductPricing);
        	when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenThrow(MBSBaseException.class);  	
            List<ProductPricingPO> result = productPricingService
                    .getProductPricing(createProductIdPO(Long.valueOf(10)), false);
            assertNull(result);
                
        }
        
        @Test(expected = MBSBaseException.class)
        public void getProductPricingTransaformerException() throws Exception {
                doThrow(new MBSBusinessException("Not able to transform", MBSExceptionConstants.BUSINESS_EXCEPTION)).when((Transformer) productPricingPOTransformer)
                        .transform(any());
                when(((Transformer) productPricingPOTransformer).transform(any()))
                        .thenThrow(new MBSBusinessException("Not able to transform", MBSExceptionConstants.BUSINESS_EXCEPTION));
                List<ProductPricingPO> listExpected = new ArrayList<>();
                ProductPricingPO productPricingPO = new ProductPricingPO();
                productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
                listExpected.add(productPricingPO);
                List<ProductPricingPO> result = productPricingService
                        .getProductPricing(createProductIdPO(Long.valueOf(10)), false);
        }
        
        @Test
        public void getAllProductPricing() throws Exception {
        	createData();
            when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(getCalendarDayResponse);    
            when(mbsProductPricingDao.getAll()).thenReturn(listMBSProductPricing);
        	List<ProductPricingPO> result = productPricingService.getAllProductPricing(false);
        	assertNotNull(result);
        }
        
        @Test
        public void getAllProductPricing_Filtered() throws Exception {
                createData();
            when(enterpriseCalendarServiceClient.getCalendarDay(any(), any(),  Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyBoolean())).thenReturn(getCalendarDayResponse);    
            when(mbsProductPricingDao.getAll()).thenReturn(listMBSProductPricing);
                List<ProductPricingPO> result = productPricingService.getAllProductPricing(true);
                assertNotNull(result);
        }
        
        @Test
        public void getAllProductPricingNoRecords() throws Exception {
        	createData();
            when(mbsProductPricingDao.getAll()).thenReturn(new ArrayList<MBSProductPricingRequest>());    
        	List<ProductPricingPO> result = productPricingService.getAllProductPricing(false);
            assertNotNull(result);
        }
        
        /**
         * utility method to create stub data
         */
        private ProductIdPO createProductIdPO(Long identifier) {
                ProductIdPO productId = new ProductIdPO();
                productId.setIdentifier(Long.valueOf(identifier));
                productId.setType(TradeConstants.PRODUCT_TYPE.MBS);
                productId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU);
                return productId;
        }
        
        /**
         * utility method to create stub data
         */
        private ProductId createProductId(Long identifier) {
                ProductId productId = new ProductId();
                productId.setIdentifier(Long.valueOf(identifier));
                productId.setSourceType("PU");
                productId.setType("MBS");
                return productId;
        }
        
        /**
         * utility method to create stub data
         */
        private ProductPricingPO createProductPricingPO(Long identifier) {
                ProductIdPO productId = new ProductIdPO();
                productId.setIdentifier(Long.valueOf(identifier));
                productId.setType(TradeConstants.PRODUCT_TYPE.MBS);
                productId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU);
                ProductPricingPO productPricingPO = new ProductPricingPO();
                productPricingPO.setProductId(productId);
                return productPricingPO;
        }
        
        /**
         * utility method to create stub data
         * @throws DatatypeConfigurationException 
         */
        public void createData() throws ParseException, DatatypeConfigurationException{
        	transformationObject = new TransformationObject(); 
        	listMBSProductPricing = new ArrayList<>();
        	getCalendarDayResponse = new GetCalendarDayResponse();
        	//Set past date
            MBSProductPricingRequest mbsProductPricingRequest = new MBSProductPricingRequest();
            mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
            Calendar cal = Calendar.getInstance();
            String currentDateStr = "2017-11-03";
            DateFormat simpleDateFormat = new SimpleDateFormat(DateFormats.DATE_FORMAT_NO_TIMESTAMP);
            Date currentDate = simpleDateFormat.parse(currentDateStr);
            cal.setTimeInMillis(currentDate.getTime());
            mbsProductPricingRequest.setCutOffDate(cal.getTime());
            listMBSProductPricing.add(mbsProductPricingRequest);
            //set current date
            mbsProductPricingRequest = new MBSProductPricingRequest();
            mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
            currentDate = MBSPortalUtils.getCurrentDate();
            mbsProductPricingRequest.setCutOffDate(currentDate);
            listMBSProductPricing.add(mbsProductPricingRequest);
            //set current date + 1 - SettlementDate
            mbsProductPricingRequest = new MBSProductPricingRequest();
            mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
            cal = Calendar.getInstance();
            currentDate = MBSPortalUtils.getCurrentDate();
            cal.setTimeInMillis(currentDate.getTime());
            cal.add(Calendar.MONTH, 1);
            mbsProductPricingRequest.setSettlementDate(cal.getTime());
            listMBSProductPricing.add(mbsProductPricingRequest);
            //set current date + 2
            mbsProductPricingRequest = new MBSProductPricingRequest();
            mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(12)));
            cal = Calendar.getInstance();
            currentDate = MBSPortalUtils.getCurrentDate();
            cal.setTimeInMillis(currentDate.getTime());
            cal.add(Calendar.MONTH, 2);
            mbsProductPricingRequest.setCutOffDate(cal.getTime());
            listMBSProductPricing.add(mbsProductPricingRequest);
            //set current date + 1 year
            mbsProductPricingRequest = new MBSProductPricingRequest();
            mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
            cal = Calendar.getInstance();
            currentDate = MBSPortalUtils.getCurrentDate();
            cal.setTimeInMillis(currentDate.getTime());
            cal.add(Calendar.YEAR, 1);
            mbsProductPricingRequest.setCutOffDate(cal.getTime());
            listMBSProductPricing.add(mbsProductPricingRequest);
            transformationObject.setTargetPojo(listMBSProductPricing);
            CalendarDays calendarDays = new CalendarDays();
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date());
            XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
            calendarDays.setDate(date2);
            getCalendarDayResponse.getCalendarDays().add(calendarDays);


        }
}