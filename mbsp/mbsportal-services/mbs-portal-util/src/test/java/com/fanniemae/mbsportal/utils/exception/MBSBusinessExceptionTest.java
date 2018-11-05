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

package com.fanniemae.mbsportal.utils.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Rajiv Chaudhuri
 * @Date Nov 15, 2017
 * @Time 11:23:54 AM
 * 	com.fanniemae.mbsportal.utils.exception
 * 	MBSBusinessExceptionTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class MBSBusinessExceptionTest {
        
        private String errorMsg = "Business Exception Occured!";
        
        private ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        
        @Test
        public void MBSBusinessException_Constructors_Success() {
                long processID = 1234; Exception exp = new Exception();
                List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
                exceptionLookupPOLst.add(exceptionLookupPO);
                MBSBusinessException mbsBus_err_PO = new MBSBusinessException(errorMsg, exceptionLookupPO);
                Assert.assertNotNull(mbsBus_err_PO);
                MBSBusinessException mbsBus_PO = new MBSBusinessException(exceptionLookupPO);
                Assert.assertNotNull(mbsBus_PO);
                MBSBusinessException mbsBus_err_Lst = new MBSBusinessException(errorMsg, exceptionLookupPOLst);
                Assert.assertNotNull(mbsBus_err_Lst);
                MBSBusinessException mbsBus_err = new MBSBusinessException(errorMsg); Assert.assertNotNull(mbsBus_err);
                MBSBusinessException mbsBus_err_procID = new MBSBusinessException(errorMsg, processID);
                Assert.assertNotNull(mbsBus_err_procID);
                MBSBusinessException mbsBus_err_procID_root = new MBSBusinessException(errorMsg, processID, exp);
                Assert.assertNotNull(mbsBus_err_procID_root);
                MBSBusinessException mbsBus_err_root_PO = new MBSBusinessException(errorMsg, exp, exceptionLookupPO);
                Assert.assertNotNull(mbsBus_err_root_PO);
                MBSBusinessException mbsBus_root_PO = new MBSBusinessException(exp, exceptionLookupPO);
                Assert.assertNotNull(mbsBus_root_PO);
        }
        
        @Test
        public void getRootException() {
                Exception exp = new Exception(errorMsg);
                MBSBusinessException mbsBusinessException = new MBSBusinessException(errorMsg,
                        MBSExceptionConstants.BUSINESS_EXCEPTION, exp); Assert.assertNotNull(mbsBusinessException);
                assertEquals(exp.getMessage(), mbsBusinessException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mbsBusinessException.getRootException().getClass().getName());
        }
        
        @Test
        public void getRootException_List() {
                Exception exp = new Exception(errorMsg);
                MBSBusinessException mbsBusinessException = new MBSBusinessException(errorMsg, exp, exceptionLookupPO);
                Assert.assertNotNull(mbsBusinessException);
                assertEquals(exp.getMessage(), mbsBusinessException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mbsBusinessException.getRootException().getClass().getName());
        }
        
        @Test
        public void getExceptionProcessId() {
                Exception exp = new Exception(errorMsg);
                MBSBusinessException mbsBusinessException = new MBSBusinessException(errorMsg,
                        MBSExceptionConstants.BUSINESS_EXCEPTION, exp);
                assertTrue(mbsBusinessException.getProcessId() == MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        
        @Test
        public void getExceptionProcessId_List() {
                MBSBusinessException mbsBusinessException = new MBSBusinessException(errorMsg, exceptionLookupPO);
                assertTrue(mbsBusinessException.getProcessId() == MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        
}
