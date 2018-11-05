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
 * @Time 11:24:01 AM
 * 	com.fanniemae.mbsportal.utils.exception
 * 	MBSDataAccessExceptionTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class MBSDataAccessExceptionTest {
        
        private String errorMsg = "Data Access Exception Occured!";
        
        private ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        
        @Test
        public void MBSDataAccessException_Constructor_Success() {
                MBSDataAccessException mbsData = new MBSDataAccessException(errorMsg); Assert.assertNotNull(mbsData);
        }
        
        @Test
        public void getRootException() {
                Exception exp = new Exception(errorMsg);
                MBSDataAccessException mbsDataAccessException = new MBSDataAccessException(errorMsg,
                        MBSExceptionConstants.SYSTEM_EXCEPTION, exp); Assert.assertNotNull(mbsDataAccessException);
                assertEquals(exp.getMessage(), mbsDataAccessException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mbsDataAccessException.getRootException().getClass().getName());
        }
        
        @Test
        public void getRootException_ListMsg() {
                Exception exp = new Exception(errorMsg);
                MBSDataAccessException mbsDataAccessException = new MBSDataAccessException(errorMsg, exceptionLookupPO);
                Assert.assertNotNull(mbsDataAccessException);
                assertEquals(exp.getMessage(), mbsDataAccessException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mbsDataAccessException.getRootException().getClass().getName());
        }
        
        @Test
        public void getExceptionProcessId() {
                Exception exp = new Exception(errorMsg);
                MBSDataAccessException mbsDataAccessException = new MBSDataAccessException(errorMsg,
                        MBSExceptionConstants.SYSTEM_EXCEPTION, exp);
                assertTrue(mbsDataAccessException.getProcessId() == MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        
}
