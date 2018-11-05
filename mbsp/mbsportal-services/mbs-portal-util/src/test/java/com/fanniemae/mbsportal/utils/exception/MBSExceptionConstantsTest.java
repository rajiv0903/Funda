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

import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MBSExceptionConstantsTest {


    @Test
    public void logItForAlert_test() throws MBSBaseException {
        try{
            MBSExceptionConstants.logItForAlert("ERROR_TYPE", ""+MBSExceptionConstants.BUSINESS_EXCEPTION, "ERROR_MESSAGE", 
                        "ERROR_SOURCE_METHOD", "ERROR_SOURCE_METHOD_SIGNATURE", "SOURCE_METHOD_ARGS");
        }catch(Exception exe){
            fail("There should not be any exception!");
        }
    }
    
    @Test
    public void resetLogAlert_test() throws MBSBaseException {
        try{
            MBSExceptionConstants.resetLogAlert();
            
        }catch(Exception exe){
            fail("There should not be any exception!");
        }
    }
}
