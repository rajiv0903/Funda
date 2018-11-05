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

import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;



@RunWith(MockitoJUnitRunner.class)
public class CDXHeaderMapTest {

    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @Test
    public void testValueOf(){
        
        Assert.assertTrue(CDXHeaderMap.valueOf("ACCEPT") != null);
        Assert.assertTrue(CDXHeaderMap.valueOf("CHANNEL") != null);
        Assert.assertTrue(CDXHeaderMap.valueOf("SUB_CHANNEL") != null);
        Assert.assertTrue(CDXHeaderMap.valueOf("SESSION_ID") != null);
        Assert.assertTrue(CDXHeaderMap.valueOf("JWS_TOKEN") != null);
    }
    
    @Test
    public void testGetValue(){
        
        Assert.assertTrue(CDXHeaderMap.ACCEPT.getValue() != null);
        Assert.assertTrue(CDXHeaderMap.CHANNEL.getValue() != null);
        Assert.assertTrue(CDXHeaderMap.SUB_CHANNEL.getValue() != null);
        Assert.assertTrue(CDXHeaderMap.SESSION_ID.getValue() != null);
        Assert.assertTrue(CDXHeaderMap.JWS_TOKEN.getValue() != null);
    }
}
