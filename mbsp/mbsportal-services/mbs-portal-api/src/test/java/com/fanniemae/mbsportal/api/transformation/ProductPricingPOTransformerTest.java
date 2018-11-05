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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;

/**
 * This class handles the test case for the ProductPricingPOTransformerTest
 * class
 * 
 * date 08/02/2017
 * 
 * @author g8upjv
 * 
 */

public class ProductPricingPOTransformerTest extends BaseServiceTest {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPricingPOTransformerTest.class);

    @SuppressWarnings("rawtypes")
    @InjectMocks
    private ProductPricingPOTransformer productPricingPOTransformer;

    @Mock
    MbspProperties mbspProperties;

    TransformationObject transformationObject;

    List<MBSProductPricingRequest> listMBSProductPricing;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * This method tests the call to transformation from ProductPO to
     * MBSTransaction for successful call
     * 
     * @throws Exception
     */
    @Test
    public void transform() throws Exception {
        createData();
        Calendar cal = Calendar.getInstance();
        Date currentDate = MBSPortalUtils.getCurrentDate();
        cal.setTimeInMillis(currentDate.getTime());
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int milliSecond = cal.get(Calendar.MILLISECOND);
        when(mbspProperties.getCutOffHour()).thenReturn(String.valueOf(hour));
        when(mbspProperties.getCutOffMinute()).thenReturn(String.valueOf(minute));
        when(mbspProperties.getCutOffSecond()).thenReturn(String.valueOf(second));
        when(mbspProperties.getCutOffMillisecond()).thenReturn(String.valueOf(milliSecond));
        productPricingPOTransformer.transform(transformationObject);
        @SuppressWarnings("unchecked")
        List<ProductPricingPO> listProductPricingPO = (List<ProductPricingPO>) transformationObject.getSourcePojo();
        assertTrue(listProductPricingPO.size() == 5);

    }
    
    @Test
    public void transform_filter_data() throws Exception {
        createData();
        Calendar cal = Calendar.getInstance();
        Date currentDate = MBSPortalUtils.getCurrentDate();
        cal.setTimeInMillis(currentDate.getTime());
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int milliSecond = cal.get(Calendar.MILLISECOND);
        when(mbspProperties.getCutOffHour()).thenReturn(String.valueOf(hour));
        when(mbspProperties.getCutOffMinute()).thenReturn(String.valueOf(minute));
        when(mbspProperties.getCutOffSecond()).thenReturn(String.valueOf(second));
        when(mbspProperties.getCutOffMillisecond()).thenReturn(String.valueOf(milliSecond));
        productPricingPOTransformer.transform(transformationObject);
        @SuppressWarnings("unchecked")
        List<ProductPricingPO> listProductPricingPO = (List<ProductPricingPO>) transformationObject.getSourcePojo();
        assertTrue(listProductPricingPO.size() == 5);
        assertEquals(true, listProductPricingPO.get(4).isMarkAsDelete());

    }

    public void createData() throws ParseException {
        transformationObject = new TransformationObject();
        listMBSProductPricing = new ArrayList<>();
        // Set past date
        MBSProductPricingRequest mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
        Calendar cal = Calendar.getInstance();
        String currentDateStr = "2017-11-03";
        DateFormat simpleDateFormat = new SimpleDateFormat(DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        Date currentDate = simpleDateFormat.parse(currentDateStr);
        cal.setTimeInMillis(currentDate.getTime());
        mbsProductPricingRequest.setCutOffDate(cal.getTime());
        mbsProductPricingRequest.setEffectiveDate(new Date());
        mbsProductPricingRequest.setPassThroughRate(new BigDecimal(4));
        mbsProductPricingRequest.setMktTermType(new Integer(360));
        mbsProductPricingRequest.setSettlementDate(cal.getTime());
        mbsProductPricingRequest.setProductNameCode("FN30");
        mbsProductPricingRequest.setBuySellInd("SELL");
        listMBSProductPricing.add(mbsProductPricingRequest);
        // set current date
        mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
        currentDate = MBSPortalUtils.getCurrentDate();
        mbsProductPricingRequest.setCutOffDate(currentDate);
        mbsProductPricingRequest.setEffectiveDate(new Date());
        mbsProductPricingRequest.setPassThroughRate(new BigDecimal(4));
        mbsProductPricingRequest.setMktTermType(new Integer(360));
        mbsProductPricingRequest.setSettlementDate(cal.getTime());
        mbsProductPricingRequest.setProductNameCode("FN30");
        mbsProductPricingRequest.setBuySellInd("SELL");
        listMBSProductPricing.add(mbsProductPricingRequest);
        // set current date + 1
        mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
        cal = Calendar.getInstance();
        currentDate = MBSPortalUtils.getCurrentDate();
        cal.setTimeInMillis(currentDate.getTime());
        cal.add(Calendar.MONTH, 1);
        mbsProductPricingRequest.setCutOffDate(cal.getTime());
        mbsProductPricingRequest.setEffectiveDate(new Date());
        mbsProductPricingRequest.setPassThroughRate(new BigDecimal(4));
        mbsProductPricingRequest.setMktTermType(new Integer(360));
        mbsProductPricingRequest.setSettlementDate(cal.getTime());
        mbsProductPricingRequest.setProductNameCode("FN30");
        mbsProductPricingRequest.setBuySellInd("SELL");
        listMBSProductPricing.add(mbsProductPricingRequest);
        // set current date + 2
        mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(12)));
        cal = Calendar.getInstance();
        currentDate = MBSPortalUtils.getCurrentDate();
        cal.setTimeInMillis(currentDate.getTime());
        cal.add(Calendar.MONTH, 2);
        mbsProductPricingRequest.setCutOffDate(cal.getTime());
        mbsProductPricingRequest.setEffectiveDate(new Date());
        mbsProductPricingRequest.setPassThroughRate(new BigDecimal(4));
        mbsProductPricingRequest.setMktTermType(new Integer(360));
        mbsProductPricingRequest.setSettlementDate(cal.getTime());
        mbsProductPricingRequest.setProductNameCode("FN30");
        mbsProductPricingRequest.setBuySellInd("SELL");
        listMBSProductPricing.add(mbsProductPricingRequest);
        // set current date + 1 year
        mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
        cal = Calendar.getInstance();
        currentDate = MBSPortalUtils.getCurrentDate();
        cal.setTimeInMillis(currentDate.getTime());
        cal.add(Calendar.YEAR, 1);
        mbsProductPricingRequest.setCutOffDate(cal.getTime());
        mbsProductPricingRequest.setEffectiveDate(new Date());
        mbsProductPricingRequest.setPassThroughRate(new BigDecimal(4));
        mbsProductPricingRequest.setMktTermType(new Integer(360));
        mbsProductPricingRequest.setSettlementDate(cal.getTime());
        mbsProductPricingRequest.setProductNameCode("FN30");
        mbsProductPricingRequest.setBuySellInd("SELL");
        mbsProductPricingRequest.setLogicalDeleteIndicator(MBSPServiceConstants.yesChar);
        listMBSProductPricing.add(mbsProductPricingRequest);
        transformationObject.setTargetPojo(listMBSProductPricing);

    }

    private ProductId createProductId(Long identifier) {
        ProductId productId = new ProductId();
        productId.setIdentifier(Long.valueOf(identifier));
        productId.setSourceType("PU");
        productId.setType("MBS");
        return productId;
    }

}
