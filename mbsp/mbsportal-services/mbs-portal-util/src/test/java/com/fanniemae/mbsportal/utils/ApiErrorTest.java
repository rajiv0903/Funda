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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

/**
 * @author Rajiv Chaudhuri
 * @Date Nov 15, 2017
 * @Time 10:24:28 AM
 * 	com.fanniemae.mbsportal.utils
 * 	ApiErrorTest.java
 */
public class ApiErrorTest {

    @InjectMocks
    ApiError apiError;
    
    private String errorMsg= "Exception Occured!";
    
    @Before
    public void setUp() {
        
    }
    
    @Test
    public void getMessageFromConstructor(){
        
        apiError = new ApiError(errorMsg);
        String actualErrorMsg = apiError.getMessage();
        Assert.assertTrue(errorMsg.equals(actualErrorMsg));
    }
    
    @Test
    public void getMessageSetter(){
        
        apiError = new ApiError(errorMsg);
        apiError.setMessage(errorMsg);
        String actualErrorMsg = apiError.getMessage();
        Assert.assertTrue(errorMsg.equals(actualErrorMsg));
    }
}
