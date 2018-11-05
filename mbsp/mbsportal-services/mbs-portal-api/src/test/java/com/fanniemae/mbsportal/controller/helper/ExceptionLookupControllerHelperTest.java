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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.controller.helper.ExceptionLookupControllerHelper;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.service.MBSExceptionService;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;




/**
 * @author g8upjv
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionLookupControllerHelperTest {

    @InjectMocks
    ExceptionLookupControllerHelper exceptionLookupControllerHelper;

    @Mock
    MBSExceptionService exceptionLookupService;

    ExceptionLookupPO exceptionLookupPO;

    List<ExceptionLookupPO> exceptionLookupPOLst;

    public void createData() {
        exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRANS_00001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPOLst.add(exceptionLookupPO);
    }

    /**
     * Purpose: Creates the data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

    }
    
    @Test
    public void testCreateException() throws MBSBaseException {
            createData();
            Mockito.when(exceptionLookupService.createExceptionLookupData(Mockito.any())).thenReturn(exceptionLookupPOLst);
            List<ExceptionLookupPO> exceptionLookupPOLstResp = exceptionLookupControllerHelper.createExceptionLookupData(exceptionLookupPOLst);
            assertEquals(exceptionLookupPOLstResp.size(), 1);
    }
    
    @Test(expected = MBSBusinessException.class)
    public void testCreateExceptionNoData() throws MBSBaseException {
            createData();
            Mockito.when(exceptionLookupService.createExceptionLookupData(Mockito.any())).thenReturn(exceptionLookupPOLst);
            List<ExceptionLookupPO> exceptionLookupPOLstResp = exceptionLookupControllerHelper.createExceptionLookupData(null);
    }
    
    @Test
    public void testGetExceptionData() throws MBSBaseException {
            createData();
            Optional<String> strErrorCode = Optional.of("TRANS_00001");
            Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(exceptionLookupPOLst);
            List<ExceptionLookupPO> exceptionLookupPOLstResp = exceptionLookupControllerHelper.getExceptionLookupData(strErrorCode);
            assertEquals(exceptionLookupPOLstResp.size(), 1);
    }
    @Test
    public void clearAllException() throws MBSBaseException {
        Optional<String> strErrorCode = Optional.of("TRANS_00001");    
        Mockito.doNothing().when(exceptionLookupService).clearData(strErrorCode);
        exceptionLookupControllerHelper.clearAllExceptionLookupData(strErrorCode);
    }

}
