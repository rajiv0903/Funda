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

package com.fanniemae.mbsportal.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.PartyController;
import com.fanniemae.mbsportal.api.controller.helper.PartyControllerHelper;
import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 30, 2018
 * @File: com.fanniemae.mbsportal.controller.PartyControllerTest.java
 * @Revision:
 * @Description: PartyControllerTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class PartyControllerTest {

    @InjectMocks
    PartyController partyController;

    @Mock
    PartyControllerHelper partyControllerHelper;
    
    @Mock
    ExceptionLookupService exceptionLookupService;


    String sellerSerivcerNo;
    private PartyPO partyPOMock;

    @Before
    public void setUp() throws Exception {

        sellerSerivcerNo = "1234";
        createParty();
    }

    @Test
    public void saveOrUpdateParty_Success() throws Exception {

        doReturn(partyPOMock).when(partyControllerHelper).saveOrUpdateParty(anyObject());
        ResponseEntity<Object> responseObj = partyController.saveOrUpdateParty(partyPOMock);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void saveOrUpdateParty_Business_Error_Failure() throws Exception {

        doThrow((new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION))).when(partyControllerHelper).saveOrUpdateParty(anyObject());
        ResponseEntity<Object> responseObj = partyController.saveOrUpdateParty(partyPOMock);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void saveOrUpdateParty_Exception_Failure() throws Exception {

        doThrow(Exception.class).when(partyControllerHelper).saveOrUpdateParty(anyObject());
        ResponseEntity<Object> responseObj = partyController.saveOrUpdateParty(partyPOMock);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void getParty_Success() throws Exception {

        doReturn(partyPOMock).when(partyControllerHelper).getParty(anyString());
        ResponseEntity<Object> responseObj = partyController.getParty(sellerSerivcerNo);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void getParty_Business_Error_Failure() throws Exception {

        doThrow((new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION))).when(partyControllerHelper).getParty(anyString());
        ResponseEntity<Object> responseObj = partyController.getParty(sellerSerivcerNo);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void getParty_Exception_Failure() throws Exception {

        doThrow(Exception.class).when(partyControllerHelper).getParty(anyString());
        ResponseEntity<Object> responseObj = partyController.getParty(sellerSerivcerNo);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void deleteParty_Success() throws Exception {

        doNothing().when(partyControllerHelper).deleteParty(anyString());
        partyController.deleteParty(sellerSerivcerNo);
    }

    @Test(expected = MBSBusinessException.class)
    public void deleteParty_Business_Error_Failure() throws Exception {

        doThrow(MBSBusinessException.class).when(partyControllerHelper).deleteParty(anyString());
        partyController.deleteParty(sellerSerivcerNo);
    }

    @Test(expected = Exception.class)
    public void deleteParty_Exception_Failure() throws Exception {

        doThrow(Exception.class).when(partyControllerHelper).deleteParty(anyString());
        partyController.deleteParty(sellerSerivcerNo);
    }

    @Test
    public void deleteParties_Success() throws Exception {

        doNothing().when(partyControllerHelper).deleteParties();
        partyController.deleteParties();
    }

    @Test(expected = MBSBusinessException.class)
    public void deleteParties_Business_Error_Failure() throws Exception {

        doThrow(MBSBusinessException.class).when(partyControllerHelper).deleteParties();
        partyController.deleteParties();
    }

    @Test(expected = Exception.class)
    public void deleteParties_Exception_Failure() throws Exception {

        doThrow(Exception.class).when(partyControllerHelper).deleteParties();
        partyController.deleteParties();
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
