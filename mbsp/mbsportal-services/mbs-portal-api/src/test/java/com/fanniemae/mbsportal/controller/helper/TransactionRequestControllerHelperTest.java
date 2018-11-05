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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.controller.helper.TransactionRequestControllerHelper;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.service.TransactionRequestService;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 29, 2018
 * @Time 1:33:24 PM
 * 	com.fanniemae.mbsportal.api.controller.helper
 * 	TransactionRequestControllerHelperTest.java
 * @Description: CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestControllerHelperTest {

    @InjectMocks
    TransactionRequestControllerHelper transactionRequestControllerHelper;

    @Mock
    TransactionRequestService transactionRequestService;

    @Mock
    CDXClientApi cdxClientApi;

    TransactionRequestPO transactionRequestPO;

    ProductPO prodPO;

    ProductIdPO productId;

    List<TransactionRequestPO> lstMbsTrans;

    Map<String, String> headerMap;

    ProfileEntitlementPO profileEntitlementPO;

    public void createData() {
        headerMap = new HashMap<String, String>();
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("500000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2017-10-12");
        transactionRequestPO.setLenderId("testLender");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);

        lstMbsTrans = new ArrayList<TransactionRequestPO>();
        TransactionRequestPO mbsTrans = new TransactionRequestPO();
        mbsTrans.setTransReqId("17I" + String.valueOf(new Random().nextInt(90000) + 10000));
        mbsTrans.setTradeAmount("10101010");
        mbsTrans.setTradeBuySellType("SELL");
        mbsTrans.setTradeCouponRate("10.1");
        mbsTrans.setTradeSettlementDate("2018-03-04");
        lstMbsTrans.add(mbsTrans);

        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("username");
        profileEntitlementPO.setFirstName("firstName");
        profileEntitlementPO.setLastName("lastName");
    }

    /**
     * Purpose: Creates the transactionRequestPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testCreateTransactionRequestLender() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.createMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .createTransactionRequestLender(transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testCreateTransactionRequestExceptionServiceCall() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.createMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .createTransactionRequestLender(transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);
    }
    
    @Test(expected = MBSBusinessException.class)
    public void testCreateTransactionRequestLenderEmptyRequest() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.createMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .createTransactionRequestLender(null, headerMap);
        assertNotNull(transactionRequestPORet);
    }
    
    @Test(expected = MBSBusinessException.class)
    public void testCreateTransactionRequestLenderEmptyProfile() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.createMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(null);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .createTransactionRequestLender(transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);
    }
    
    @Test(expected = MBSBusinessException.class)
    public void testCreateTransactionRequestTraderException() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.createMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        profileEntitlementPO.setFannieMaeUser(true);
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .createTransactionRequestLender(transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testCreateTransactionRequestTspNoLendersAssociated() throws MBSBaseException {
        createData();
        transactionRequestPO.setOboLenderSellerServicerNumber("121212");
        Mockito.when(transactionRequestService.createMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        profileEntitlementPO.setTspUser(true);
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .createTransactionRequestLender(transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testCreateTransactionRequestTsp() throws MBSBaseException {
        createData();
        transactionRequestPO.setOboLenderSellerServicerNumber("121212");
        Mockito.when(transactionRequestService.createMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        profileEntitlementPO.setTspUser(true);
        List<TspPartyLenderPO> tspLenders = new ArrayList<>();
        TspPartyLenderPO tspLender = new TspPartyLenderPO();
        tspLender.setName("TEST COMPANY");
        tspLender.setShortName("TC");
        tspLender.setLenderSellerServicerNumber("121212");
        tspLenders.add(tspLender);
        profileEntitlementPO.setTspLenders(tspLenders);
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .createTransactionRequestLender(transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetTransactionRequest() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(lstMbsTrans);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        Optional<String> transReqid = Optional.of("");
        List<TransactionRequestPO> lstTransactionPORet = (List<TransactionRequestPO>) transactionRequestControllerHelper
                .getTransactionRequestsLender(transReqid, headerMap);
        assertEquals(lstMbsTrans.size(), lstTransactionPORet.size());
    }
    
    @Test
    public void testGetTransactionRequestReturnEmptyList() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<TransactionRequestPO>());
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        Optional<String> transReqid = Optional.of("18I00000");
        TransactionRequestPO transactionPORet = (TransactionRequestPO) transactionRequestControllerHelper
                .getTransactionRequestsLender(transReqid, headerMap);
        assertNotNull(transactionPORet);
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetTransactionRequestTsp() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(lstMbsTrans);
        profileEntitlementPO.setTspUser(true);
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        Optional<String> transReqid = Optional.of("");
        List<TransactionRequestPO> lstTransactionPORet = (List<TransactionRequestPO>) transactionRequestControllerHelper
                .getTransactionRequestsLender(transReqid, headerMap);
        assertEquals(lstMbsTrans.size(), lstTransactionPORet.size());
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testGetTransactionRequestException() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        Optional<String> transReqid = Optional.of("");
        transactionRequestControllerHelper.getTransactionRequestsLender(transReqid, headerMap);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testGetTransactionRequestTransId() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(lstMbsTrans);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        Optional<String> transReqid = Optional.of("17I00001");
        TransactionRequestPO transactionRequestPORet = (TransactionRequestPO) transactionRequestControllerHelper
                .getTransactionRequestsLender(transReqid, headerMap);
        assertNotNull(transactionRequestPORet);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testGetTransactionRequestTransIdException() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        Optional<String> transReqid = Optional.of("17I00001");
        transactionRequestControllerHelper.getTransactionRequestsLender(transReqid, headerMap);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testGetTransactionRequestEmptyProfile() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(lstMbsTrans);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(null);
        Optional<String> transReqid = Optional.of("");
        transactionRequestControllerHelper.getTransactionRequestsLender(transReqid, headerMap);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testGetTransactionRequestTrader() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(lstMbsTrans);
        Optional<String> transReqid = Optional.of("");
        List<TransactionRequestPO> lstMbsTransRet = (List<TransactionRequestPO>) transactionRequestControllerHelper
                .getTransactionRequestsTrader(transReqid);
        assertEquals(lstMbsTrans.size(), lstMbsTransRet.size());
    }
    
    @Test
    public void testGetTransactionRequestTraderEmptyList() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(lstMbsTrans);
        Optional<String> transReqid = Optional.of("18I00000");
        TransactionRequestPO mbsTransRet = (TransactionRequestPO) transactionRequestControllerHelper
                .getTransactionRequestsTrader(transReqid);
        assertNotNull(mbsTransRet);
    }
    
    @Test
    public void testGetTransactionRequestTraderReturnListSize1() throws MBSBaseException {
        createData();
        Mockito.when(
                transactionRequestService.getMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<TransactionRequestPO>());
        Optional<String> transReqid = Optional.of("18I00000");
        TransactionRequestPO mbsTransRet = (TransactionRequestPO) transactionRequestControllerHelper
                .getTransactionRequestsTrader(transReqid);
        assertNotNull(mbsTransRet);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testPutTransactionRequestTrader() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .submitPriceRequest(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestTraderExceptionTraderNameMismatch() throws MBSBaseException {
        createData();
        transactionRequestPO.setTraderId("testTrader");
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .submitPriceRequest(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testPutTransactionRequestTraderTraderNameMatch() throws MBSBaseException {
        createData();
        transactionRequestPO.setTraderId("username");
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .submitPriceRequest(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testPutTransactionRequestTraderNotLenderOpen() throws MBSBaseException {
        createData();
        transactionRequestPO.setTraderId("username");
        transactionRequestPO.setStateType(StateType.TRADER_PASSED);
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .submitPriceRequest(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestTraderException() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        transactionRequestControllerHelper.submitPriceRequest(transactionRequestPO.getTransReqId(),
                transactionRequestPO, headerMap);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestTraderNullInput() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .submitPriceRequest(transactionRequestPO.getTransReqId(), null, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testPutTransactionRequestLender() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testPutTransactionRequestTsp() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        profileEntitlementPO.setTspUser(true);
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestLenderException() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestTspException() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        profileEntitlementPO.setTspUser(true);
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestLenderEmptyProfile() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(null);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestTspEmptyProfile() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(null);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestLenderNullInput() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), null, headerMap);
        assertNotNull(transactionRequestPORet);

    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBusinessException.class)
    public void testPutTransactionRequestTspNullInput() throws MBSBaseException {
        createData();
        Mockito.when(transactionRequestService.updateMBSTransReq(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(transactionRequestPO);
        profileEntitlementPO.setTspUser(true);
        Mockito.when(cdxClientApi.getProfileEntitlementPO(Mockito.any(), any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestControllerHelper
                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), null, headerMap);
        assertNotNull(transactionRequestPORet);

    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void clearAllMBSTransactionRequests() throws Exception {
        Mockito.doNothing().when(transactionRequestService).clearAll();
        transactionRequestControllerHelper.clearAllTransactionRequests();
    }

}
