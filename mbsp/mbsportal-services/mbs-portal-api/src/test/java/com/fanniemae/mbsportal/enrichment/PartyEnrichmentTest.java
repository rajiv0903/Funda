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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.enrichment.PartyEnrichment;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.model.TspPartyLender;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 29, 2018
 * @File: com.fanniemae.mbsportal.enrichment.PartyEnrichmentTest.java
 * @Revision:
 * @Description: PartyEnrichmentTest.java
 */

public class PartyEnrichmentTest extends BaseServiceTest {

    @SuppressWarnings("rawtypes")
    @InjectMocks
    private PartyEnrichment partyEnrichment;

    @Mock
    MbspProperties mbspProperties;
    
    @Mock
    StreamingClientProperties streamingClientProperties;

    TransformationObject transformationObject;

    String sellerSerivcerNo;
    private MBSParty mBSPartyMock;
    String directApiUrl;
    String webSocketUrl;
    
    @Before
    public void setUp() throws Exception {

        directApiUrl = "https://mbsp01-devl.e003.fanniemae.com";
        webSocketUrl = "https://dilv-fmn-a001.fanniemae.com:8443/mbsp-streaming";
        
        sellerSerivcerNo = "1234";
        createMbsParty();
        transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSPartyMock);
    }

    @Test
    public void enrich_Urls_Not_Blank_Success() throws Exception {

        partyEnrichment.enrich(transformationObject);
        MBSParty mBSPartyRet = (MBSParty) transformationObject.getTargetPojo();
        assertEquals(directApiUrl, mBSPartyRet.getMbspPortalCnameUrlBase());
        assertEquals(webSocketUrl, mBSPartyRet.getMbspStreamingUrlBase());
    }
    
    @Test
    public void enrich_Urls_Blank_Success() throws Exception {

        mBSPartyMock.setMbspPortalCnameUrlBase(null);
        mBSPartyMock.setMbspStreamingUrlBase(null);
        
        when(mbspProperties.getDirectApiUrl()).thenReturn(directApiUrl);
        when(streamingClientProperties.getWebSocketUrl()).thenReturn(webSocketUrl);
        
        partyEnrichment.enrich(transformationObject);
        MBSParty mBSPartyRet = (MBSParty) transformationObject.getTargetPojo();
        assertEquals(directApiUrl, mBSPartyRet.getMbspPortalCnameUrlBase());
        assertEquals(webSocketUrl, mBSPartyRet.getMbspStreamingUrlBase());
    }
    
    @Test(expected=MBSSystemException.class)
    public void enrich_Failure() throws Exception {

        mBSPartyMock.setMbspPortalCnameUrlBase(null);
        doThrow(MBSSystemException.class).when(mbspProperties).getDirectApiUrl();
        
        partyEnrichment.enrich(transformationObject);
        MBSParty mBSPartyRet = (MBSParty) transformationObject.getTargetPojo();
        assertEquals(directApiUrl, mBSPartyRet.getMbspPortalCnameUrlBase());
        assertEquals(webSocketUrl, mBSPartyRet.getMbspStreamingUrlBase());
    }
    
    @Test(expected=MBSSystemException.class)
    public void enrich_Exception_Failure() throws Exception {

        partyEnrichment.enrich(null);
    }

    private void createMbsParty() throws MBSBaseException {
        List<TspPartyLender> tspPartyLenders = new ArrayList<>();

        TspPartyLender tspPartyLender = new TspPartyLender();
        tspPartyLender.setEffectiveDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        tspPartyLender.setExpirationDate(
                MBSPortalUtils.convertToDateWithFormatter("2022-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        tspPartyLender.setLenderSellerServicerNumber("1235");
        tspPartyLender.setName("TEST-CIAM COMPANY");
        tspPartyLender.setShortName("TCCO");

        tspPartyLenders.add(tspPartyLender);

        mBSPartyMock = new MBSParty();
        mBSPartyMock.setEffectiveDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setExpirationDate(
                MBSPortalUtils.convertToDateWithFormatter("2022-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setInstitutionType("TSP");
        mBSPartyMock.setTspPartyLenders(tspPartyLenders);
        mBSPartyMock.setName("UCDP-TEST NON-SSN");
        mBSPartyMock.setMbspPortalCnameUrlBase(directApiUrl);
        mBSPartyMock.setMbspStreamingUrlBase(webSocketUrl);
        mBSPartyMock.setNameEffectiveDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setNameExpirationDate(
                MBSPortalUtils.convertToDateWithFormatter("2022-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setSellerServicerNumber(sellerSerivcerNo);
        mBSPartyMock.setShortName("UTNS");
        mBSPartyMock.setStateType("ACTIVE");
    }
}
