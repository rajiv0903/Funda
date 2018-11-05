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

package com.fanniemae.mbsportal.utils.epv;

import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date Nov 15, 2017
 * @Time 10:00:07 AM
 * 	com.fanniemae.mbsportal.utils.com.fanniemae.mbsportal.utils.epv
 * 	EpvConnectorTest.java
 */

public class EpvConnectorTest {

        
    @InjectMocks
    EpvConnector epvConnector;
    
    @Mock
    Class<?> vaultFactoryClass;
    
    private static String EPV_FACTORY = "com.fanniemae.sharedservices.evas.VaultFactory";
    private static String EPV_VAULT = "com.fanniemae.sharedservices.evas.vault.Vault";
    private static final String GET_VAULT = "getVault";
    private static final String GET_CONTENT = "getContent";
    
    @Before
    public void setUp() {
        
       
    }
    
    @Test
    @Ignore
    public void initialize() throws Exception{
        doReturn(vaultFactoryClass).when(Class.forName(Mockito.any()));
    }
        
}
