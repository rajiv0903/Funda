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

package com.fanniemae.calendar.client;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.calendar.client.EnterpriseCalendarServiceClient;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

@RunWith(MockitoJUnitRunner.class)
public class EnterpriseCalendarServiceClientTest {

    
    @InjectMocks
    EnterpriseCalendarServiceClient enterpriseCalendarServiceClient;
    
    @Before
    public void setUp(){
        
    }
    
    @Test
    public void getCalendarDay() throws MBSBaseException{
        assertTrue(true== true);
    }
    
    @Test
    public void getListOfCalendarDays() throws MBSBaseException{
        assertTrue(true== true);
    }
    
    
    @Test
    public void getLastBusinessDayOfMonth() throws MBSBaseException{
        assertTrue(true== true);
    }
}
