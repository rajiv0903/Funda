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
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.pojo.TransformationObject;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 30, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.PartyTransformerTest.java
 * @Revision:
 * @Description: PartyTransformerTest.java
 */
public class PartyTransformerTest {

    private PartyTransformer<TransformationObject> partyTransformer;

    private String sellerSerivcerNo;
    private PartyPO partyPOMock;

    @Before
    public void setUp() throws Exception {

        partyTransformer = new PartyTransformer<TransformationObject>();
        sellerSerivcerNo = "1234";
        createParty();
    }

    @Test
    public void transform_Internal_User_Success() throws Exception {

        partyPOMock.setTspPartyLenders(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        transformationObject.setTargetPojo(new MBSParty());
        partyTransformer.transform(transformationObject);
        MBSParty mBSPartyRet = (MBSParty) transformationObject.getTargetPojo();
        assertTrue(partyPOMock.getSellerServicerNumber().equals(mBSPartyRet.getSellerServicerNumber()));
        assertNull(mBSPartyRet.getTspPartyLenders());

    }

    @Test
    public void transform_Internal_User_System_Expiration_Date_Success() throws Exception {

        partyPOMock.setTspPartyLenders(null);
        partyPOMock.setExpirationDate(null);
        partyPOMock.setNameExpirationDate(null);

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        transformationObject.setTargetPojo(new MBSParty());
        partyTransformer.transform(transformationObject);
        MBSParty mBSPartyRet = (MBSParty) transformationObject.getTargetPojo();
        assertTrue(partyPOMock.getSellerServicerNumber().equals(mBSPartyRet.getSellerServicerNumber()));
        assertNull(mBSPartyRet.getTspPartyLenders());

    }

    @Test
    public void transform_TSP_User_Success() throws Exception {

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        transformationObject.setTargetPojo(new MBSParty());
        partyTransformer.transform(transformationObject);
        MBSParty mBSPartyRet = (MBSParty) transformationObject.getTargetPojo();
        assertTrue(partyPOMock.getSellerServicerNumber().equals(mBSPartyRet.getSellerServicerNumber()));
        assertTrue(mBSPartyRet.getTspPartyLenders().size() == 1);

    }

    @Test
    public void transform_TSP_User_System_Expiration_Date_Success() throws Exception {

        partyPOMock.getTspPartyLenders().get(0).setExpirationDate(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        transformationObject.setTargetPojo(new MBSParty());
        partyTransformer.transform(transformationObject);
        MBSParty mBSPartyRet = (MBSParty) transformationObject.getTargetPojo();
        assertTrue(partyPOMock.getSellerServicerNumber().equals(mBSPartyRet.getSellerServicerNumber()));
        assertTrue(mBSPartyRet.getTspPartyLenders().size() == 1);

    }

    /**
     * Create Party
     */
    private void createParty() {

        List<TspPartyLenderPO> tspPartyPOLenders = new ArrayList<>();

        TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO();
        tspPartyLenderPO.setEffectiveDate("2018-05-23");
        tspPartyLenderPO.setExpirationDate("2012-05-23");
        tspPartyLenderPO.setLenderSellerServicerNumber("1235");
        tspPartyLenderPO.setName("TEST-CIAM COMPANY");
        tspPartyLenderPO.setShortName("TCCO");

        tspPartyPOLenders.add(tspPartyLenderPO);

        partyPOMock = new PartyPO();
        partyPOMock.setEffectiveDate("2018-05-23");
        partyPOMock.setExpirationDate("2022-05-23");
        partyPOMock.setInstitutionType("TSP");
        partyPOMock.setTspPartyLenders(tspPartyPOLenders);
        partyPOMock.setName("UCDP-TEST NON-SSN");
        partyPOMock.setMbspPortalCnameUrlBase("https://mbsp01-devl.e003.fanniemae.com");
        partyPOMock.setMbspStreamingUrlBase("https://dilv-fmn-a001.fanniemae.com:8443/mbsp-streaming");
        partyPOMock.setNameEffectiveDate("2018-05-23");
        partyPOMock.setNameExpirationDate("2022-05-23");
        partyPOMock.setSellerServicerNumber(sellerSerivcerNo);
        partyPOMock.setShortName("UTNS");
        partyPOMock.setStateType("ACTIVE");
    }

}