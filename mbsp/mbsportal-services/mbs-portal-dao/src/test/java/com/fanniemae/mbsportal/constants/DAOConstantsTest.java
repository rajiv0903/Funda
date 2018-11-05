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

package com.fanniemae.mbsportal.constants;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fanniemae.mbsportal.constants.DAOConstants.MonthType;

/**
 * Created by g8uaxt on 8/3/2017.
 */

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 11, 2017
 * @Time 1:22:19 PM
 * 	com.fanniemae.mbsportal.constants
 * 	DAOConstantsTest.java
 * @Description: Revision 1.1
 */
public class DAOConstantsTest {

    @Test
    public void IDTypesTestName(){
        assertEquals("MBSP_TransactionRequestID",DAOConstants.IDTypes.TRANSACTION_ID.getName());
    }
    @Test
    public void IDTypesTestId(){
        assertEquals(1,DAOConstants.IDTypes.TRANSACTION_ID.getId());
    }
    
    @Test
    public void MonthTypeGetMonthCodeValue_Sucess(){
        assertEquals(MonthType.JANUARY.getMonthValue(),MonthType.getMonthCode(0));
    }
    
    @Test
    public void MonthTypeGetMonthCodeEmpty_Value_Sucess(){
        assertEquals("",MonthType.getMonthCode(12));
    }
}
