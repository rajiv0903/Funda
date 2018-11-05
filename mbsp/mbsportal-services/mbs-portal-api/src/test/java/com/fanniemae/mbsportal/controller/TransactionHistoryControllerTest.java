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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.TransactionHistoryController;
import com.fanniemae.mbsportal.api.controller.helper.TransactionHistoryControllerHelper;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.constants.AcceptedTradesBoolean;
import com.fanniemae.mbsportal.constants.RegionColumnList;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.ExceptionResponsePO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSDataException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * Class Name: TransactionRequestControllerTest Purpose : This class is the test
 * class for the Transaction controller
 * 
 * @author g8upjv
 *
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 22, 2017
 * @Time 10:53:07 AM com.fanniemae.mbsportal.api.controller
 *       ProductControllerTest.java
 * @Description: Incorporate the Internal Server Error - Status Code 500
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionHistoryControllerTest {

    @InjectMocks
    TransactionHistoryController transactionHistoryController;

    @Mock
    TransactionHistoryControllerHelper transactionHistoryControllerHelper;

    @Mock
    ExceptionLookupService exceptionLookupService;

    private HttpServletResponse mockHttpResponse = Mockito.mock(HttpServletResponse.class);

    TransactionHistoryPO transactionHistoryPO;

    ProductPO prodPO;

    ProductIdPO productId;

    List<TransactionHistoryPO> lstMbsTransHist;

    Map<String, String> headerMap;

    private RegionColumnList sortBy;
    private SortBy sortOrder;
    private Integer pageIndex;
    private Integer pageSize;

    /**
     * 
     */
    public void createData() {
        transactionHistoryPO = new TransactionHistoryPO();
        headerMap = new HashMap<String, String>();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionHistoryPO.setProduct(prodPO);
        transactionHistoryPO.setTradeAmount("500000");
        transactionHistoryPO.setTradeBuySellType("SELL");
        transactionHistoryPO.setTradeCouponRate("2.0");
        transactionHistoryPO.setTradeSettlementDate("2017-10-12");
        transactionHistoryPO.setLenderName("98765432");
        transactionHistoryPO.setStateType(StateType.LENDER_OPEN);

        lstMbsTransHist = new ArrayList<TransactionHistoryPO>();
    }

    public List<ExceptionLookupPO> createExceptionData() {
        List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRANS_00001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPOLst.add(exceptionLookupPO);
        return exceptionLookupPOLst;
    }

    /**
     * Purpose: Creates the transactionRequestPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        sortBy = RegionColumnList.transReqId;
        sortOrder = SortBy.asc;
        pageIndex = 1;
        pageSize = 20;
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(createExceptionData());
    }

    /**
     * Purpose: Test for case for successful call to get list of transactions
     * for trader
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testGetTransactionRequestTrader() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistorySorted(Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(lstMbsTransHist);
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryBySortAndPage(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, pageIndex, pageSize);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    /**
     * Purpose: Test for case for successful call to get list of transactions
     * for trader
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testGetTransactionRequestTraderAcceptedTrades() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistorySorted(Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any())).thenReturn(lstMbsTransHist);
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryBySortAndPage(headerMap,
        		AcceptedTradesBoolean.trueValue, sortBy, sortOrder, pageIndex, pageSize);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    /**
     * Purpose: Test for case for successful call to get list of transactions
     * for trader
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testGetTransactionRequestTraderException() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistorySorted(Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any()))
                .thenThrow(new MBSBusinessException("test", MBSExceptionConstants.BUSINESS_EXCEPTION));
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryBySortAndPage(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, pageIndex, pageSize);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void testGetTransactionRequestTraderInternalServerError() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistorySorted(Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryBySortAndPage(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, pageIndex, pageSize);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void testGetTransactionHistoryExportCSV() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistoryExportBySort(Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(" Body ");
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryExportBySort(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, "", "", "CSV", "", mockHttpResponse);
        assertEquals(HttpStatus.OK, responseObj.getStatusCode());
    }

    @Test
    public void testGetTransactionHistoryExportExcel() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistoryExportBySort(Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any())).thenReturn(" Body ");
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryExportBySort(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, "", "", "EXCEL", "", mockHttpResponse);
        assertEquals(HttpStatus.OK, responseObj.getStatusCode());
    }

    @Test
    public void testGetTransactionHistoryExportMBSSystemException() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistoryExportBySort(Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any())).thenThrow(new MBSSystemException("I have an error!!!!"));
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryExportBySort(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, "", "", "CSV", "", mockHttpResponse);
        assertTrue(responseObj.hasBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseObj.getStatusCode());
    }

    @Test
    public void testGetTransactionHistoryExportMBSDataException() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistoryExportBySort(Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any())).thenThrow(new MBSDataException(MBSPServiceConstants.BAD_INPUT,
                		new ExceptionResponsePO()));
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryExportBySort(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, "", "", "CSV", "", mockHttpResponse);
        assertTrue(responseObj.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseObj.getStatusCode());
    }

    @Test
    public void testGetTransactionHistoryExportInternalServerException() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistoryExportBySort(Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any())).thenThrow(new RuntimeException("Runtine Exception!"));
        
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryExportBySort(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, "", "", "CSV", "", mockHttpResponse);
        assertTrue(responseObj.hasBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseObj.getStatusCode());
    }
    @Test
    public void testGetTransactionRequestTraderInternalServerErrorExceptionMsgRetrieval() throws MBSBaseException {
        createData();
        Mockito.when(transactionHistoryControllerHelper.getTransactionHistorySorted(Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any()))
                .thenThrow(new MBSBaseException("test"));
        ResponseEntity<Object> responseObj = transactionHistoryController.getTransactionHistoryBySortAndPage(headerMap,
        		AcceptedTradesBoolean.falseValue, sortBy, sortOrder, pageIndex, pageSize);
        assertTrue(responseObj.hasBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseObj.getStatusCode());
    }
}
