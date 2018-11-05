/*
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
 * @author FannieMae
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MBSDataExceptionTest {
        
        private String errorMsg = "Data Exception Occured!";
        
        private ExceptionResponsePO excpRespPO = new ExceptionResponsePO();
        
        @Test
        public void getRootException() {
                Exception exp = new Exception(errorMsg);
                MBSDataException mBSDataException = new MBSDataException(errorMsg);
                Assert.assertNotNull(mBSDataException);
                assertEquals(exp.getMessage(), mBSDataException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mBSDataException.getRootException().getClass().getName());
                assertTrue(mBSDataException.getProcessId() == MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        
        @Test
        public void getRootException_List() {
                Exception exp = new Exception(errorMsg);
                MBSDataException mBSDataException = new MBSDataException(errorMsg, excpRespPO);
                Assert.assertNotNull(mBSDataException);
                assertEquals(exp.getMessage(), mBSDataException.getRootException().getMessage());
                assertEquals(exp.getClass().getName(), mBSDataException.getRootException().getClass().getName());
        }
        
        @Test
        public void getExcpRespPO_Success() {
                MBSDataException mbsDataException = new MBSDataException(errorMsg, excpRespPO);
                Assert.assertNotNull(mbsDataException.getExcpRespPO());
        }
        
        @Test
        public void setExcpRespPO_Success() {
                MBSDataException mbsDataException = new MBSDataException(errorMsg);
                mbsDataException.setExcpRespPO(excpRespPO); Assert.assertNotNull(mbsDataException.getExcpRespPO());
        }
}
