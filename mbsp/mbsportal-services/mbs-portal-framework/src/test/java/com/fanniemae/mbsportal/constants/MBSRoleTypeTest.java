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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date Nov 15, 2017
 * @Time 11:34:10 AM
 * 	com.fanniemae.mbsportal.constants
 * 	MBSRoleTypeTest.java
 */
public class MBSRoleTypeTest{
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @Test
    public void testValueOf(){
        Assert.assertTrue(MBSRoleType.valueOf("LENDER") != null);
        Assert.assertTrue(MBSRoleType.valueOf("TRADER") != null);
        Assert.assertTrue(MBSRoleType.valueOf("TSP") != null);
    }
    
    @Test
    public void testGetValue(){
        
        Assert.assertTrue(MBSRoleType.LENDER.getRole() != null);
        Assert.assertTrue(MBSRoleType.TRADER.getRole() != null);
        Assert.assertTrue(MBSRoleType.TSP.getRole() != null);
    }
}
