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

package com.fanniemae.mbsportal.aspect.pointcut;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MBSSystemPointcutTest {

    @InjectMocks
    MBSSystemPointcut mBSSystemPointcut;
    
    @Before
    public void setUp() throws Exception {

    }
    
    @Test
    public void annotationExceptionPointcutTest(){
        mBSSystemPointcut.annotationExceptionPointcut();
    }
}
