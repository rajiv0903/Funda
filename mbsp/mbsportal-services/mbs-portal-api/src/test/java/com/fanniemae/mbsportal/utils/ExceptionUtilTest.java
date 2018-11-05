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



import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.fanniemae.mbsportal.api.persister.ExceptionLookupPersister;
import com.fanniemae.mbsportal.api.utils.ExceptionUtil;
import com.fanniemae.mbsportal.dao.MBSExceptionLookupDao;
import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author g8upjv
 *  Created on 3/7/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionUtilTest {
         
        @InjectMocks
        ExceptionUtil exceptionUtil;
        
        @Mock
        ExceptionLookupPersister exceptionLookupPersister;
        
        @Mock
        MBSExceptionLookupDao mbsExceptionLookupDao;
        
        @Mock
        protected Logger LOGGER;
        
        MBSExceptionLookup mbsExceptionLookup;
        
        @Before
        public void setup() {
                doNothing().when(LOGGER).debug(any());
                doNothing().when(LOGGER).info(any());
                doNothing().when(LOGGER).warn(any());
                doNothing().when(LOGGER).error(any());
        }
        
        @Test
        public void getMBSExceptionData() throws MBSBaseException {
            when(((Persister) exceptionLookupPersister).getBaseDao()).thenReturn(mbsExceptionLookupDao);
            when(mbsExceptionLookupDao.getById(any())).thenReturn(createData());
            MBSExceptionLookup mbsExceptionLookupResp =  exceptionUtil.getMBSExceptionData("TRANS_00001");
            assertTrue(mbsExceptionLookupResp.getErrorMessage().equals(mbsExceptionLookup.getErrorMessage()));
            
        }
        
        @Test
        public void getMBSExceptionDataEmptyInput() throws MBSBaseException {
            when(((Persister) exceptionLookupPersister).getBaseDao()).thenReturn(mbsExceptionLookupDao);
            when(mbsExceptionLookupDao.getById(any())).thenReturn(null);
            MBSExceptionLookup mbsExceptionLookupResp =  exceptionUtil.getMBSExceptionData("");
            assertNull(mbsExceptionLookupResp);
            
        }
        
        @Test(expected=MBSBaseException.class)
        public void getMBSExceptionDataThrowException() throws MBSBaseException {
            when(((Persister) exceptionLookupPersister).getBaseDao()).thenReturn(mbsExceptionLookupDao);
            when(mbsExceptionLookupDao.getById(any())).thenThrow(new MBSBusinessException("Test"));
            MBSExceptionLookup mbsExceptionLookupResp =  exceptionUtil.getMBSExceptionData("TRANS_00001");
            //Mockito.assertTrue(mbsExceptionLookupResp.getErrorMessage().equals(mbsExceptionLookup.getErrorMessage()));
            
        }
        
        public MBSExceptionLookup createData() {
            mbsExceptionLookup = new MBSExceptionLookup();
            mbsExceptionLookup.setErrorCategory("API");
            mbsExceptionLookup.setErrorCode("TRANS_00001");
            mbsExceptionLookup.setMessageType("DISP_ERROR");
            mbsExceptionLookup.setErrorMessage("Test message");
            return mbsExceptionLookup;
        }
        
}
