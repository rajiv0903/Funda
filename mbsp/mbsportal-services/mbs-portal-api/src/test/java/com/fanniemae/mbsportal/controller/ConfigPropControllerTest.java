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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.ConfigPropController;
import com.fanniemae.mbsportal.api.controller.helper.ConfigPropControllerHelper;
import com.fanniemae.mbsportal.api.po.ConfigPropPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * 
 * @author g8upjv
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigPropControllerTest {

    @InjectMocks
    ConfigPropController configPropController;

    @Mock
    ConfigPropControllerHelper configPropControllerHelper;
    
    @Mock
    ExceptionLookupService exceptionLookupService;
    

    ConfigPropPO configPropPO = null;
    
    Map<String, String> headerMap;


    /**
     * Purpose: Creates the productPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        configPropPO = new ConfigPropPO();
    }

    /**
     * Purpose: Test for case when media type is empty
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testGetKey() throws MBSBaseException {
        doReturn(configPropPO).when(configPropControllerHelper).getConfigProp(anyString());
        ResponseEntity<Object> responseObj = configPropController.getConfigProp(headerMap, "test");
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }
    
    @Test
    public void testGetKeyBadReqExcep() throws MBSBaseException {
        Mockito.when(configPropControllerHelper.getConfigProp(Mockito.any())).thenThrow(new MBSBusinessException("error"));
        ResponseEntity<Object> responseObj = configPropController.getConfigProp(headerMap, "test");
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }
    
    @Test
    public void testGetKeyIntSerExcep() throws MBSBaseException {
        doThrow(Exception.class).when(configPropControllerHelper).getConfigProp(anyString());
        ResponseEntity<Object> responseObj = configPropController.getConfigProp(headerMap, "test");
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void saveOrUpdateParty_Success() throws Exception {

        doReturn(configPropPO).when(configPropControllerHelper).saveOrUpdateConfigProp(anyObject());
        ResponseEntity<Object> responseObj = configPropController.saveOrUpdateConfigProp(configPropPO);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void saveOrUpdateParty_Business_Error_Failure() throws Exception {

        doThrow((new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION))).when(configPropControllerHelper).saveOrUpdateConfigProp(anyObject());
        ResponseEntity<Object> responseObj = configPropController.saveOrUpdateConfigProp(configPropPO);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void saveOrUpdateParty_Exception_Failure() throws Exception {

        doThrow(Exception.class).when(configPropControllerHelper).saveOrUpdateConfigProp(anyObject());
        ResponseEntity<Object> responseObj = configPropController.saveOrUpdateConfigProp(configPropPO);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    
    public List<ExceptionLookupPO> createData() {
        List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("SYSM_0002");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPO.setLoggerMessage("Test Message");
        exceptionLookupPOLst.add(exceptionLookupPO);
        return exceptionLookupPOLst;
    }

}
