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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Rajiv Chaudhuri
 * @Date Nov 15, 2017
 * @Time 11:24:07 AM
 * 	com.fanniemae.mbsportal.utils.exception
 * 	MBSSystemExceptionTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class MBSSystemExceptionTest {
        
        private String errorMsg = "System Exception Occured!";
        
        private ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        
        @Test
        public void MBSSystemException_Constructor_exceptionLookupPO() {
                MBSSystemException mbsSystemException = new MBSSystemException(exceptionLookupPO);
                Assert.assertNotNull(mbsSystemException);
        }
        
        @Test
        public void MBSSystemException_Constructor_ProcessID() {
                long processID = 1234;
                MBSSystemException mbsSystemException = new MBSSystemException(errorMsg, processID);
                Assert.assertNotNull(mbsSystemException);
        }
        
        @Test
        public void MBSSystemException_Constructor_rootException() {
                Exception exp = new Exception();
                MBSSystemException mbsSystemException = new MBSSystemException(exp, exceptionLookupPO);
                Assert.assertNotNull(mbsSystemException);
                MBSSystemException mbsSystemException_errormsg = new MBSSystemException(errorMsg, exp);
                Assert.assertNotNull(mbsSystemException_errormsg);
        }
        
        @Test
        public void getRootException() {
                Exception exp = new Exception(errorMsg);
                MBSSystemException mbsSystemException = new MBSSystemException(errorMsg,
                        MBSExceptionConstants.SYSTEM_EXCEPTION, exp); Assert.assertNotNull(mbsSystemException);
                assertEquals(exp.getMessage(), mbsSystemException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mbsSystemException.getRootException().getClass().getName());
        }
        
        @Test
        public void getRootException_List() {
                Exception exp = new Exception(errorMsg);
                MBSSystemException mbsSystemException = new MBSSystemException(errorMsg, exp, exceptionLookupPO);
                Assert.assertNotNull(mbsSystemException);
                assertEquals(exp.getMessage(), mbsSystemException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mbsSystemException.getRootException().getClass().getName());
        }
        
        @Test
        public void getExceptionProcessId() {
                Exception exp = new Exception(errorMsg);
                MBSSystemException mbsSystemException = new MBSSystemException(errorMsg,
                        MBSExceptionConstants.SYSTEM_EXCEPTION, exp);
                assertTrue(mbsSystemException.getProcessId() == MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        
        @Test
        public void getErrorMsg() {
                MBSSystemException mbsSystemException = new MBSSystemException(errorMsg);
                assertTrue(mbsSystemException.getProcessId() == MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        
        @Test
        public void getErrorMsg_List() {
                MBSSystemException mbsSystemException = new MBSSystemException(errorMsg, exceptionLookupPO);
                assertTrue(mbsSystemException.getProcessId() == MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        
}
