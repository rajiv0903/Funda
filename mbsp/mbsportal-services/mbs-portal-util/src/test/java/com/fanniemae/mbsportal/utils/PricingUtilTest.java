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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Test case for Pricing Util
 * @author g8uaxt
 * Created on 6/8/2018.
 */

public class PricingUtilTest {
        
        @Test
        public void getHandleWhenNull() throws MBSBaseException {
                String result=PricingUtil.getHandle(null);
                assertEquals("",result);
        }
        @Test
        public void getHandleWhenBlank() throws MBSBaseException{
                String result=PricingUtil.getHandle("");
                assertEquals("",result);
        }
        @Test
        public void getHandleForRoundPrice() throws MBSBaseException{
                String result=PricingUtil.getHandle("105.60546875");
                assertNotNull(result);
                assertEquals("105",result);
        }
        @Test
        public void getHandleForPrice() throws MBSBaseException{
                String result=PricingUtil.getHandle("101");
                assertNotNull(result);
                assertEquals("101",result);
        }
        @Test
        public void getHandleForInvalidData() throws MBSBaseException{
                String result=PricingUtil.getHandle("????");
                assertNotNull(result);
                assertEquals("",result);
        }
        
        @Test
        public void getTicksWhenNull() throws MBSBaseException {
                String result=PricingUtil.getTicks(null);
                assertEquals("",result);
        }
        @Test
        public void getTicksWhenBlank() throws MBSBaseException{
                String result=PricingUtil.getTicks("");
                assertEquals("",result);
        }
        @Test
        public void getTicks() throws MBSBaseException{
                String result=PricingUtil.getTicks("105.60546875"); //105-193
                assertNotNull(result);
                assertEquals("193",result);
        }

        @Test
        public void getTicksForPlus() throws MBSBaseException{
                String result=PricingUtil.getTicks("102.265625"); //102-08+
                assertNotNull(result);
                assertEquals("08+",result); 
        }
        
        @Test
        public void getTicksForZeroEights() throws MBSBaseException{
                String result=PricingUtil.getTicks("102.3125"); //102-08+
                assertNotNull(result);
                assertEquals("10",result); 
        }
        
        @Test
        public void getTicksForZero() throws MBSBaseException{
                String result=PricingUtil.getTicks("102.0000"); //102-08+
                assertNotNull(result);
                assertEquals("00",result); 
        }
        
        @Test
        public void getTicksForRoundPrice() throws MBSBaseException{
                String result=PricingUtil.getTicks("101");
                assertNotNull(result);
                assertEquals("00",result);
        }
        
        @Test
        public void getTicksForInvalidData() throws MBSBaseException{
                String result=PricingUtil.getTicks("????");
                assertNotNull(result);
                assertEquals("",result);
        }
}
