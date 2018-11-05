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

package com.fanniemae.mbsportal.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * Created by g8uaxt on 8/7/2017.
 * Test for MBSBaseExceptionTest
 */
/**
 * @author Rajiv Chaudhuri
 * @Date Nov 15, 2017
 * @Time 10:52:21 AM
 * 	com.fanniemae.mbsportal.utils
 * 	MBSBaseExceptionTest.java
 */
public class MBSBaseExceptionTest {
        
        private String errorMsg = "Exception Occured!";
        
        private ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        
        @Test
        public void MBSBaseExceptionConstructor_First_Success() {
                List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong, exceptionLookupPOLst);
                Assert.assertNotNull(mbsBase);
        }
        
        @Test
        public void MBSBaseExceptionConstructor_Second_Success() {
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong);
                Assert.assertNotNull(mbsBase);
        }
        
        @Test
        public void MBSBaseExceptionConstructor_Third_Success() {
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong, exceptionLookupPO);
                Assert.assertNotNull(mbsBase);
        }
        
        @Test
        public void MBSBaseExceptionConstructor_Fourth_Success() {
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong, new Exception());
                Assert.assertNotNull(mbsBase);
        }
        
        @Test
        public void MBSBaseExceptionConstructor_Fifth_Success() {
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, new Throwable());
                Assert.assertNotNull(mbsBase);
        }
        
        @Test
        public void getExceptionLookupPO_Success() {
                List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong, exceptionLookupPOLst);
                Assert.assertNull(mbsBase.getExceptionLookupPO());
        }
        
        @Test
        public void getExceptionLookupPOLst_Success() {
                List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong, exceptionLookupPOLst);
                Assert.assertNotNull(mbsBase.getExceptionLookupPOLst());
        }
        
        @Test
        public void setExceptionLookupPOLst_Success() {
                List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong);
                mbsBase.setExceptionLookupPOLst(exceptionLookupPOLst);
                Assert.assertNotNull(mbsBase.getExceptionLookupPOLst());
        }
        
        @Test
        public void setExceptionLookupPO_Success() {
               ExceptionLookupPO exceptionPO = new ExceptionLookupPO();
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong);
                mbsBase.setExceptionLookupPO(exceptionPO);
                Assert.assertNotNull(mbsBase.getExceptionLookupPO());
        }
        
        @Test
        public void getExceptionCode_Success() {
                MBSBaseException mbsBase = new MBSBaseException(errorMsg);
                Assert.assertNotNull(mbsBase.getExceptionCode());
        }
        
        @Test
        public void getExceptionCode_Business() {
                MBSBusinessException mbsBus = new MBSBusinessException(errorMsg);
                Assert.assertNotNull(mbsBus.getExceptionCode());
        }
        
        @Test
        public void getExceptionCode_System() {
                MBSSystemException mbsSys = new MBSSystemException(errorMsg);
                Assert.assertNotNull(mbsSys.getExceptionCode());
        }
        
        @Test
        public void toString_Success() {
                List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
                exceptionLookupPOLst.add(exceptionLookupPO);
                long testLong = 1234;
                MBSBaseException mbsBase = new MBSBaseException(errorMsg, testLong, exceptionLookupPOLst);
                Assert.assertNotNull(mbsBase.toString());
        }
        
        @Test
        public void getRootException() {
                Exception exp = new Exception(errorMsg); MBSBaseException mbsBaseException = new MBSBaseException(exp);
                Assert.assertNotNull(mbsBaseException);
                assertEquals(exp.getMessage(), mbsBaseException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mbsBaseException.getRootException().getClass().getName());
        }
        
        @Test
        public void getRootExceptionMessage() {
                MBSBaseException mbsBaseException = new MBSBaseException(errorMsg);
                Assert.assertNotNull(mbsBaseException);
                assertEquals(errorMsg, mbsBaseException.getRootExceptionMessage());
        }
        
        @Test
        public void getExceptionProcessId() {
                Exception exp = new Exception("Unique Id Not Found");
                MBSBaseException mbsBaseException = new MBSBaseException(errorMsg, exp);
                mbsBaseException.setProcessId(MBSExceptionConstants.BUSINESS_EXCEPTION);
                assertTrue(mbsBaseException.getProcessId() == MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        
        @Test
        public void getRootExceptionMessage_Throwable() {
                Exception exe = new Exception(errorMsg);
                MBSBaseException mbsBaseException = new MBSBaseException(errorMsg, exe);
                Assert.assertNotNull(mbsBaseException);
                assertEquals(exe.getMessage(), mbsBaseException.getRootException().getMessage());
        }
        
        @Test
        public void getRootExceptionMessage_ExpList() {
                Exception exe = new Exception(errorMsg);
                MBSBaseException mbsBaseException = new MBSBaseException(errorMsg, 10000, exe, exceptionLookupPO);
                Assert.assertNotNull(mbsBaseException);
                assertEquals(exe.getMessage(), mbsBaseException.getRootException().getMessage());
        }
        
        @Test
        public void getRootExceptionMessage_ExpList_Msg() {
                MBSBaseException mbsBaseException = new MBSBaseException(errorMsg, 10000, exceptionLookupPO);
                Assert.assertNotNull(mbsBaseException);
                assertEquals(errorMsg, mbsBaseException.getRootExceptionMessage());
        }
        
        @Test
        public void getRootExceptionMessage_ExpList_Throwable() {
                Exception exe = new Exception(errorMsg);
                MBSBaseException mbsBaseException = new MBSBaseException(errorMsg, exe);
                Assert.assertNotNull(mbsBaseException);
                assertEquals(errorMsg, mbsBaseException.getRootExceptionMessage());
        }
}
