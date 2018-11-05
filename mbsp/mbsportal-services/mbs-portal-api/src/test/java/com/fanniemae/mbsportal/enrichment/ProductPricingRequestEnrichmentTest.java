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

package com.fanniemae.mbsportal.enrichment;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.enrichment.ProductPricingRequestEnrichment;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;

/**
 * @author g8uaxt Created on 9/14/2017.
 * @author g8upjv Updated on 11/27/2017
 */

public class ProductPricingRequestEnrichmentTest extends BaseServiceTest {

    
    @InjectMocks
    private ProductPricingRequestEnrichment productPricingRequestEnrichment;

    @Mock
    MbspProperties mbspProperties;

    TransformationObject transformationObject;

    List<MBSProductPricingRequest> listMBSProductPricing;

    
    @Test
    public void enrich() throws ParseException {
        createData();
        when(mbspProperties.getNumberOfDisplayMonths()).thenReturn("3");
        productPricingRequestEnrichment.enrich(transformationObject);
        List<MBSProductPricingRequest> listMBSProductPricingResp = (List<MBSProductPricingRequest>) transformationObject
                .getTargetPojo();
        assertTrue(listMBSProductPricingResp.size() == 3);

    }

    @Test
    public void enrich_filter() throws ParseException {
        createData();
        when(mbspProperties.getNumberOfDisplayMonths()).thenReturn("3");
        transformationObject.setFilterDeletedRecord(true);
        productPricingRequestEnrichment.enrich(transformationObject);
        List<MBSProductPricingRequest> listMBSProductPricingResp = (List<MBSProductPricingRequest>) transformationObject
                .getTargetPojo();
        assertTrue(listMBSProductPricingResp.size() == 2);

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
        listMBSProductPricing.add(mbsProductPricingRequest);
        // set current date
        mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
        currentDate = MBSPortalUtils.getCurrentDate();
        mbsProductPricingRequest.setCutOffDate(currentDate);
        listMBSProductPricing.add(mbsProductPricingRequest);
        // set current date + 1
        mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
        mbsProductPricingRequest.setLogicalDeleteIndicator('Y');
        cal = Calendar.getInstance();
        currentDate = MBSPortalUtils.getCurrentDate();
        cal.setTimeInMillis(currentDate.getTime());
        cal.add(Calendar.MONTH, 1);
        mbsProductPricingRequest.setCutOffDate(cal.getTime());
        listMBSProductPricing.add(mbsProductPricingRequest);
        // set current date + 2
        mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(12)));
        cal = Calendar.getInstance();
        currentDate = MBSPortalUtils.getCurrentDate();
        cal.setTimeInMillis(currentDate.getTime());
        cal.add(Calendar.MONTH, 2);
        mbsProductPricingRequest.setCutOffDate(cal.getTime());
        listMBSProductPricing.add(mbsProductPricingRequest);
        // set current date + 1 year
        mbsProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequest.setProductId(createProductId(Long.valueOf(11)));
        cal = Calendar.getInstance();
        currentDate = MBSPortalUtils.getCurrentDate();
        cal.setTimeInMillis(currentDate.getTime());
        cal.add(Calendar.YEAR, 1);
        mbsProductPricingRequest.setCutOffDate(cal.getTime());
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
