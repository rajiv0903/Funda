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

package com.fanniemae.mbsportal.cdx;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.cdx.constants.CDXApiProfileEnum;

@RunWith(MockitoJUnitRunner.class)
public class CDXApiProfileEnumTest {

    @Before
    public void setUp() throws Exception {
        
    }
    
    @Test
    public void testValueOf(){
        
        Assert.assertTrue(CDXApiProfileEnum.valueOf("FIRSTNAME") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("LASTNAME") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("USER_NAME") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("CUSTOMER_NAME") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("WORK_NUMBER") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("MOBILE_NUMBER") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("DEFAULT_SELLER_SERVICE_NUMBER") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("SELLER_SERVICE_NUMBER") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("DEALER_ORG_NAME") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("DEALER_ORG_ID") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("INSTITUTION_ID") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("FANNIEMAE_USER") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("ROLES") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("SELLER_SERVICE_DETAILS") != null);
        Assert.assertTrue(CDXApiProfileEnum.valueOf("LENDER_DETAILS") != null);
    }
    
    @Test
    public void testGetValue(){
        
        Assert.assertTrue(CDXApiProfileEnum.FIRSTNAME.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.LASTNAME.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.USER_NAME.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.CUSTOMER_NAME.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.WORK_NUMBER.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.MOBILE_NUMBER.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.DEFAULT_SELLER_SERVICE_NUMBER.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.SELLER_SERVICE_NUMBER.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.DEALER_ORG_NAME.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.DEALER_ORG_ID.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.INSTITUTION_ID.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.FANNIEMAE_USER.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.ROLES.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.SELLER_SERVICE_DETAILS.getValue() != null);
        Assert.assertTrue(CDXApiProfileEnum.LENDER_DETAILS.getValue() != null);
    }
}
