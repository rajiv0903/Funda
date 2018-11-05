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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.model.TspPartyLender;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 31, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.PartyPOTransformerTest.java 
 * @Revision: 
 * @Description: PartyPOTransformerTest.java
 */
public class PartyPOTransformerTest {

    private PartyPOTransformer<TransformationObject> partyPOTransformer;
    
    private String sellerSerivcerNo;
    private MBSParty mBSPartyMock;
    
    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        partyPOTransformer = new PartyPOTransformer<>();
        sellerSerivcerNo = "1234";
        createMbsParty();
        
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void transform_Internal_User_Success() throws Exception {
        
        mBSPartyMock.setTspPartyLenders(null);
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSPartyMock);
        partyPOTransformer.transform(transformationObject);
        PartyPO partyPORet = (PartyPO) transformationObject.getSourcePojo();
        
        assertTrue(mBSPartyMock.getSellerServicerNumber().equals(partyPORet.getSellerServicerNumber()));
        assertNull(partyPORet.getTspPartyLenders());
    }
    
    @Test
    public void transform_Internal_User_System_Expiration_Date_Success() throws Exception {

        mBSPartyMock.setTspPartyLenders(null);
        mBSPartyMock.setExpirationDate(null);
        mBSPartyMock.setNameExpirationDate(null);

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSPartyMock);
        partyPOTransformer.transform(transformationObject);
        PartyPO partyPORet = (PartyPO) transformationObject.getSourcePojo();
        
        assertTrue(mBSPartyMock.getSellerServicerNumber().equals(partyPORet.getSellerServicerNumber()));
        assertNull(partyPORet.getTspPartyLenders());

    }

    @Test
    public void transform_TSP_User_Success() throws Exception {

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSPartyMock);
        partyPOTransformer.transform(transformationObject);
        PartyPO partyPORet = (PartyPO) transformationObject.getSourcePojo();
        
        assertTrue(mBSPartyMock.getSellerServicerNumber().equals(partyPORet.getSellerServicerNumber()));
        assertTrue(partyPORet.getTspPartyLenders().size() == 1);

    }

    @Test
    public void transform_TSP_User_System_Expiration_Date_Success() throws Exception {

        mBSPartyMock.getTspPartyLenders().get(0).setExpirationDate(null);
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSPartyMock);
        partyPOTransformer.transform(transformationObject);
        PartyPO partyPORet = (PartyPO) transformationObject.getSourcePojo();
        
        assertTrue(mBSPartyMock.getSellerServicerNumber().equals(partyPORet.getSellerServicerNumber()));
        assertTrue(partyPORet.getTspPartyLenders().size() == 1);

    }
    
    
    /**
     * 
     * Create MBS Party
     * @throws MBSBaseException
     */
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
        mBSPartyMock.setMbspPortalCnameUrlBase("https://mbsp01-devl.e003.fanniemae.com");
        mBSPartyMock.setMbspStreamingUrlBase("https://dilv-fmn-a001.fanniemae.com:8443/mbsp-streaming");
        mBSPartyMock.setNameEffectiveDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setNameExpirationDate(
                MBSPortalUtils.convertToDateWithFormatter("2022-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setSellerServicerNumber(sellerSerivcerNo);
        mBSPartyMock.setShortName("UTNS");
        mBSPartyMock.setStateType("ACTIVE");
    }

}
