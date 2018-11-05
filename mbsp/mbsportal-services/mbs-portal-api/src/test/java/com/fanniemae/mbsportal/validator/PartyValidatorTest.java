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

package com.fanniemae.mbsportal.validator;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.validator.PartyValidator;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 29, 2018
 * @File: com.fanniemae.mbsportal.validator.PartyValidatorTest.java 
 * @Revision: 
 * @Description: PartyValidatorTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class PartyValidatorTest {

    private PartyValidator<TransformationObject> partyValidator;

    String sellerSerivcerNo;
    private PartyPO partyPOMock;


    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        sellerSerivcerNo = "1234";
        partyValidator = new PartyValidator<TransformationObject>();
        createParty();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void validate_Lender_Success() throws Exception {

        partyPOMock.setTspPartyLenders(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
        PartyPO partyPORet = (PartyPO) transformationObject.getSourcePojo();
        assertTrue(partyPORet.getSellerServicerNumber() == partyPORet.getSellerServicerNumber());
    }
    
    
    @Test
    public void validate_TSP_Success() throws Exception {

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
        PartyPO partyPORet = (PartyPO) transformationObject.getSourcePojo();
        assertTrue(partyPORet.getSellerServicerNumber().equals(partyPORet.getSellerServicerNumber()));
        assertTrue(partyPORet.getTspPartyLenders().size()== partyPORet.getTspPartyLenders().size());
    }



    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_Empty_PartyPO_Failure() throws Exception {

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(null);
        partyValidator.validate(transformationObject);
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_Seller_Service_Number_Failure() throws Exception {

        partyPOMock.setSellerServicerNumber(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_Name_Failure() throws Exception {

        partyPOMock.setName(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_Short_Name_Failure() throws Exception {

        partyPOMock.setShortName(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_Effective_Date_Failure() throws Exception {

        partyPOMock.setEffectiveDate(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_Name_Effective_Date_Failure() throws Exception {

        partyPOMock.setNameEffectiveDate(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_State_Type_Failure() throws Exception {

        partyPOMock.setStateType(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_Instituion_Type_Failure() throws Exception {

        partyPOMock.setInstitutionType(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_TSP_Lender_Effective_Date_Failure() throws Exception {

        
        TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO();
        tspPartyLenderPO.setEffectiveDate("");
        tspPartyLenderPO.setExpirationDate("2012-05-23");
        tspPartyLenderPO.setLenderSellerServicerNumber("1235");
        tspPartyLenderPO.setName("TEST-CIAM COMPANY");
        tspPartyLenderPO.setShortName("TCCO");
        
        partyPOMock.getTspPartyLenders().add(tspPartyLenderPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_TSP_Lender_Seller_Service_Number_Failure() throws Exception {

        
        TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO();
        tspPartyLenderPO.setEffectiveDate("2018-05-23");
        tspPartyLenderPO.setExpirationDate("2012-05-23");
        tspPartyLenderPO.setLenderSellerServicerNumber(null);
        tspPartyLenderPO.setName("TEST-CIAM COMPANY");
        tspPartyLenderPO.setShortName("TCCO");
        
        partyPOMock.getTspPartyLenders().add(tspPartyLenderPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_TSP_Lender_Name_Failure() throws Exception {

        
        TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO();
        tspPartyLenderPO.setEffectiveDate("2018-05-23");
        tspPartyLenderPO.setExpirationDate("2012-05-23");
        tspPartyLenderPO.setLenderSellerServicerNumber("1235");
        tspPartyLenderPO.setName("");
        tspPartyLenderPO.setShortName("TCCO");
        
        partyPOMock.getTspPartyLenders().add(tspPartyLenderPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
    
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_TSP_Lender_Short_Name_Failure() throws Exception {

        
        TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO();
        tspPartyLenderPO.setEffectiveDate("2018-05-23");
        tspPartyLenderPO.setExpirationDate("2012-05-23");
        tspPartyLenderPO.setLenderSellerServicerNumber("1235");
        tspPartyLenderPO.setName("TEST-CIAM COMPANY");
        tspPartyLenderPO.setShortName("");
        
        partyPOMock.getTspPartyLenders().add(tspPartyLenderPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPOMock);
        partyValidator.validate(transformationObject);
    }
   

    /**
     * 
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
