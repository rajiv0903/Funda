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
package com.fanniemae.mbsportal.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.dao.MBSConfigPropDao;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author FannieMae
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

    @InjectMocks
    ConfigurationService configurationService;
    
    @Mock
    MBSConfigPropDao mbsConfigPropDao;
    
    @Before
    public void setUp() throws Exception {
        when(mbsConfigPropDao.getById(Mockito.any())).thenReturn(new MBSConfigProp());
    }
    
    @Test
    public void getPropValueAsStringNullInput() throws MBSBaseException {
        String propVal = configurationService.getPropValueAsString(null);
    	assertNull(propVal);
    }
    
    @Test
    public void getPropValueAsStringNullValue() throws MBSBaseException {
        when(mbsConfigPropDao.getById(Mockito.any())).thenReturn(null);
        String propVal = configurationService.getPropValueAsString("");
        assertNull(propVal);
    }
    
    @Test
    public void getPropValueAsString_success() throws MBSBaseException {
        MBSConfigProp mbsConfigProp = new MBSConfigProp();
        mbsConfigProp.setValue("test");
        when(mbsConfigPropDao.getById(Mockito.any())).thenReturn(mbsConfigProp);
        String propVal = configurationService.getPropValueAsString("");
        assertNotNull(propVal);
    }
}
