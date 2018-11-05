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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * @author g8uaxt Created on 1/4/2018.
 */

public class RegionColumnListTest {
        
        @Test
        public void getEnum(){
                assertNotNull(RegionColumnList.getEnum("transReqNumber"));
                assertSame(RegionColumnList.transReqId,RegionColumnList.getEnum("transReqNumber"));
        }
        @Test
        public void getSortName(){
                assertNotNull(RegionColumnList.transReqId.getSortName());
                assertSame("transReqNumber",RegionColumnList.transReqId.getSortName());
        }
}
