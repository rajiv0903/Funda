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

package com.fanniemae.mbsportal.controller.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
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

import com.fanniemae.mbsportal.api.controller.helper.PartyControllerHelper;
import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.service.PartyService;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 29, 2018
 * @File: com.fanniemae.mbsportal.controller.helper.PartyControllerHelperTest.java
 * @Revision:
 * @Description: PartyControllerHelperTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class PartyControllerHelperTest {

    @InjectMocks
    PartyControllerHelper partyControllerHelper;

    @Mock
    PartyService partyService;

    String sellerSerivcerNo;
    private PartyPO partyPO;

    @Before
    public void setUp() throws Exception {

        sellerSerivcerNo = "1234";

        List<TspPartyLenderPO> tspPartyLenders = new ArrayList<>();

        TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO();
        tspPartyLenderPO.setEffectiveDate("2018-05-23");
        tspPartyLenderPO.setExpirationDate("2012-05-23");
        tspPartyLenderPO.setLenderSellerServicerNumber("1235");
        tspPartyLenderPO.setName("TEST-CIAM COMPANY");
        tspPartyLenderPO.setShortName("TCCO");

        partyPO = new PartyPO();
        partyPO.setEffectiveDate("2018-05-23");
        partyPO.setExpirationDate("2022-05-23");
        partyPO.setInstitutionType("TSP");
        partyPO.setTspPartyLenders(tspPartyLenders);
        partyPO.setName("UCDP-TEST NON-SSN");
        partyPO.setMbspPortalCnameUrlBase("https://mbsp01-devl.e003.fanniemae.com");
        partyPO.setMbspStreamingUrlBase("https://dilv-fmn-a001.fanniemae.com:8443/mbsp-streaming");
        partyPO.setNameEffectiveDate("2018-05-23");
        partyPO.setNameExpirationDate("2022-05-23");
        partyPO.setSellerServicerNumber(sellerSerivcerNo);
        partyPO.setShortName("UTNS");
        partyPO.setStateType("ACTIVE");
    }

    @Test
    public void saveOrUpdateParty_Lender_Success() throws MBSBaseException {

        partyPO.setTspPartyLenders(null);
        doReturn(partyPO).when(partyService).saveParty(any());
        PartyPO partyPORet = partyControllerHelper.saveOrUpdateParty(partyPO);
        assertEquals(partyPO.getShortName(), partyPORet.getShortName());
        assertNull(partyPORet.getTspPartyLenders());
    }
    
    @Test(expected = MBSBusinessException.class)
    public void saveOrUpdateParty_No_Party_Failure() throws MBSBaseException {

        partyControllerHelper.saveOrUpdateParty(null);
    }

    @Test(expected = MBSBusinessException.class)
    public void saveOrUpdateParty_Lender_Failure() throws MBSBaseException {

        partyPO.setTspPartyLenders(null);
        doThrow(MBSBusinessException.class).when(partyService).saveParty(any());
        partyControllerHelper.saveOrUpdateParty(partyPO);
    }

    @Test
    public void saveOrUpdateParty_TSP_Success() throws MBSBaseException {

        doReturn(partyPO).when(partyService).saveParty(any());
        PartyPO partyPORet = partyControllerHelper.saveOrUpdateParty(partyPO);
        assertEquals(partyPO.getShortName(), partyPORet.getShortName());
        assertEquals(partyPO.getTspPartyLenders().size(), partyPORet.getTspPartyLenders().size());
    }

    @Test(expected = MBSBusinessException.class)
    public void saveOrUpdateParty_TSP_Failure() throws MBSBaseException {

        doThrow(MBSBusinessException.class).when(partyService).saveParty(any());
        partyControllerHelper.saveOrUpdateParty(partyPO);
    }

    @Test
    public void getParty_Lender_Success() throws MBSBaseException {

        partyPO.setTspPartyLenders(null);
        doReturn(partyPO).when(partyService).getParty(anyString());
        PartyPO partyPORet = partyControllerHelper.getParty(sellerSerivcerNo);
        assertEquals(partyPO.getShortName(), partyPORet.getShortName());
        assertNull(partyPORet.getTspPartyLenders());
    }

    @Test(expected = MBSBusinessException.class)
    public void getParty_Lender_Failure() throws MBSBaseException {

        partyPO.setTspPartyLenders(null);
        doThrow(MBSBusinessException.class).when(partyService).getParty(anyString());
        partyControllerHelper.getParty(sellerSerivcerNo);
    }

    @Test
    public void getParty_TSP_Success() throws MBSBaseException {

        doReturn(partyPO).when(partyService).getParty(anyString());
        PartyPO partyPORet = partyControllerHelper.getParty(sellerSerivcerNo);
        assertEquals(partyPO.getShortName(), partyPORet.getShortName());
        assertEquals(partyPO.getTspPartyLenders().size(), partyPORet.getTspPartyLenders().size());
    }

    @Test(expected = MBSBusinessException.class)
    public void getParty_TSP_Failure() throws MBSBaseException {

        doThrow(MBSBusinessException.class).when(partyService).getParty(anyString());
        partyControllerHelper.getParty(sellerSerivcerNo);
    }
    
    @Test(expected = MBSBusinessException.class)
    public void getParty_Empty_SSN_Failure() throws MBSBaseException {
        
        partyControllerHelper.getParty(null);
    }

    @Test
    public void deleteParty_Success() throws MBSBaseException {

        doNothing().when(partyService).clear(anyString());
        partyControllerHelper.deleteParty(sellerSerivcerNo);
    }

    @Test(expected = MBSBusinessException.class)
    public void deleteParty_Failure() throws MBSBaseException {

        doThrow(MBSBusinessException.class).when(partyService).clear(anyString());
        partyControllerHelper.deleteParty(sellerSerivcerNo);
    }

    @Test
    public void deleteParties_Success() throws MBSBaseException {

        doNothing().when(partyService).clearAll();
        partyControllerHelper.deleteParties();
    }

    @Test(expected = MBSBusinessException.class)
    public void deleteParties_Failure() throws MBSBaseException {

        doThrow(MBSBusinessException.class).when(partyService).clearAll();
        partyControllerHelper.deleteParties();
    }

}
