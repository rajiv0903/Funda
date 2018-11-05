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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.BaseController;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.ExceptionResponsePO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSDataException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author g8upjv
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseControllerTest {

    @InjectMocks
    BaseController baseController;
    
    @Mock
    private ExceptionLookupService exceptionLookupService;

    /**
     * Purpose: Creates the productPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(createData());
    }

    /**
     * Purpose: Test for case when media type is empty
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void getResponseEntityBussException() {
        
        MBSBusinessException mbsBusEx = new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION);
        ResponseEntity<Object> respObj = baseController.getResponseEntityException("BaseControllerTest", "getResponseEntityBussException", mbsBusEx);
        assertTrue(respObj.getStatusCode().toString().equals("400"));

    }
    
    /**
     * Purpose: Test for case when media type is empty
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void getResponseEntityBussExceptionWithExcepData() {
        
        MBSBusinessException mbsBusEx = new MBSBusinessException("Exception", (ExceptionLookupPO)(createData()).get(0));
        ResponseEntity<Object> respObj = baseController.getResponseEntityException("BaseControllerTest", "getResponseEntityBussException", mbsBusEx);
        assertTrue(respObj.getStatusCode().toString().equals("400"));

    }
    
    @Test
    public void getResponseEntityIntException() {
        
        MBSSystemException mbsBusEx = new MBSSystemException("Exception", MBSExceptionConstants.SYSTEM_EXCEPTION);
        ResponseEntity<Object> respObj = baseController.getResponseEntityException("BaseControllerTest", "getResponseEntityIntException", mbsBusEx);
        assertTrue(respObj.getStatusCode().toString().equals("500"));

    }
    @Test
    public void getResponseEntityIntExceptionErrorRetrieval() throws MBSBaseException {
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenThrow(new MBSBaseException("test"));
        MBSSystemException mbsBusEx = new MBSSystemException("Exception", MBSExceptionConstants.SYSTEM_EXCEPTION);
        ResponseEntity<Object> respObj = baseController.getResponseEntityException("BaseControllerTest", "getResponseEntityIntExceptionErrorRetrieval", mbsBusEx);
        assertTrue(respObj.getStatusCode().toString().equals("500"));

    }
    
    public List<ExceptionLookupPO> createData() {
        List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRANS_00001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPOLst.add(exceptionLookupPO);
        return exceptionLookupPOLst;
    }

//    @Test
//    public void testGetResponseEntityDataExceptionNullMBSDataException() {
//        ResponseEntity<Object> respObj = baseController.getResponseEntityDataException("BaseControllerTest", "getResponseEntityDataException", null);
//        assertEquals(HttpStatus.BAD_REQUEST, respObj.getStatusCode());   	
//    }

    @Test
    public void testGetResponseEntityDataExceptionNullExceptionResponsePO() {
        ResponseEntity<Object> respObj = baseController.getResponseEntityDataException("BaseControllerTest", "getResponseEntityDataException", new MBSDataException("BAD_INPUT", null));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respObj.getStatusCode());
        assertNotNull(respObj.getBody());
    }

    @Test
    public void testGetResponseEntityDataException() {
    	MBSDataException mbsDataException = new MBSDataException("BAD_INPUT", null);
    	ExceptionResponsePO exceptionResponsePO = new ExceptionResponsePO();
    	mbsDataException.setExcpRespPO(exceptionResponsePO);
        ResponseEntity<Object> respObj = baseController.getResponseEntityDataException("BaseControllerTest", "getResponseEntityDataException", mbsDataException);
        assertEquals(HttpStatus.BAD_REQUEST, respObj.getStatusCode());
        assertNotNull(respObj.getBody());
        assertEquals(exceptionResponsePO, respObj.getBody());
    }
}
