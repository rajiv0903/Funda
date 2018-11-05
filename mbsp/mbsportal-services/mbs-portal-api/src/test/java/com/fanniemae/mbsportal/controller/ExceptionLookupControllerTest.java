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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.ExceptionLookupController;
import com.fanniemae.mbsportal.api.controller.helper.ExceptionLookupControllerHelper;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author g8upjv
 * @Version:
 * @Date Mar 3, 2018
 * @Time 10:53:07 AM com.fanniemae.mbsportal.api.controller
 *       ProductControllerTest.java
 * @Description: Incorporate the Internal Server Error - Status Code 500
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionLookupControllerTest {

    @InjectMocks
    ExceptionLookupController exceptionLookupController;

    @Mock
    ExceptionLookupControllerHelper exceptionLookupControllerHelper;

    ExceptionLookupPO exceptionLookupPO;

    List<ExceptionLookupPO> exceptionLookupPOLst;
    
    Map<String, String> headers;
    
    @Mock
    ExceptionLookupService exceptionLookupService;


    /**
     * Create PO data
     */
    public void createData() {
        exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRAN_0001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPO.setLoggerMessage("Test message");
        exceptionLookupPOLst.add(exceptionLookupPO);
    }
    
    /**
     * Create PO data
     */
    public void createSys002Data() {
        exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("SYSM_0002s");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPO.setLoggerMessage("Test message");
        exceptionLookupPOLst.add(exceptionLookupPO);
    }


    /**
     * Purpose: Creates the productPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * Purpose: Test for case when media type is empty
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testCreateExceptionData() throws MBSBaseException {
        createData();
        Mockito.when(exceptionLookupControllerHelper.createExceptionLookupData(Mockito.any())).thenReturn(exceptionLookupPOLst);
        ResponseEntity<Object> responseObj = exceptionLookupController.createExceptionLookupData(headers, exceptionLookupPOLst);
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
        assertTrue(responseObj.hasBody());
    }


    /**
     * Purpose: Test for case for exception call
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testCreateException() throws MBSBaseException {
        createData();
        Mockito.when(exceptionLookupControllerHelper.createExceptionLookupData(Mockito.any()))
                .thenThrow(new MBSBusinessException("test Exception"));
        ResponseEntity<Object> responseObj = exceptionLookupController.createExceptionLookupData(headers, exceptionLookupPOLst);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void testCreateExceptionInternalServerError() throws MBSBaseException {
        createSys002Data();
        Mockito.when(exceptionLookupControllerHelper.createExceptionLookupData(Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        ResponseEntity<Object> responseObj = exceptionLookupController.createExceptionLookupData(headers, exceptionLookupPOLst);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    /**
     * Purpose: Test for case for successful call to get list of products
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testGetExceptionData() throws MBSBaseException {
        createData();
        Optional<String> strErrorCode = Optional.of("TRANS_00001");
        Mockito.when(exceptionLookupControllerHelper.getExceptionLookupData(Mockito.any())).thenReturn(exceptionLookupPOLst);
        ResponseEntity<Object> responseObj = exceptionLookupController.getExceptionLookupData(headers, strErrorCode);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    /**
     * Purpose: Test for case for successful call to get list of products
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testGetProductsException() throws MBSBaseException {
        createData();
        Optional<String> strErrorCode = Optional.of("TRANS_00001");
        Mockito.when(exceptionLookupControllerHelper.getExceptionLookupData(Mockito.any()))
                .thenThrow(new MBSBusinessException("test Exception"));
        ResponseEntity<Object> responseObj = exceptionLookupController.getExceptionLookupData(headers, strErrorCode);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void testGetProductsInternalServerError() throws MBSBaseException {
        createSys002Data();
        Optional<String> strErrorCode = Optional.of("SYSM_0001");
        Mockito.when(exceptionLookupControllerHelper.getExceptionLookupData(Mockito.any())).thenThrow(new RuntimeException("Runtine Exception!"));
        ResponseEntity<Object> responseObj = exceptionLookupController.getExceptionLookupData(headers, strErrorCode);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void clearException() throws Exception {
        Optional<String> strErrorCode = Optional.of("TRANS_00001");
        Mockito.doNothing().when(exceptionLookupControllerHelper).clearAllExceptionLookupData(Mockito.any());
        exceptionLookupController.clearExceptionLookupData(headers, strErrorCode);
    }

}
