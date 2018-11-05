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


package com.fanniemae.mbsportal.transformer;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.fanniemae.mbsportal.ecf.ods.TradeEventMessage;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * ECF Transformer Test class
 * 
 * @author g8upjv
 *
 */
public class ECFTransformerImplTest {
    
    private ECFTransformerImpl ecfTransformerImpl = new ECFTransformerImpl();
    ClassLoader classLoader;
    String inputMsg;
    String invalidInputMsg;
    
    @Before
    public void setUp() throws Exception {
    	classLoader = getClass().getClassLoader();
    	inputMsg = Files.toString(new File(classLoader.getResource("test_br_message.xml").getFile()), Charsets.UTF_8);
    	invalidInputMsg = Files.toString(new File(classLoader.getResource("test_br_message_invalid.xml").getFile()), Charsets.UTF_8);
    	ecfTransformerImpl.sourceSystem = "MBSP";
    }
    
    /**
     * Test case for getMBSTrade 
     * @throws Exception
     */
    @Test
    public void testGetMBSTrade() throws MBSBaseException {
        TradeEventMessage tradeMsg = ecfTransformerImpl.getMBSTrade(inputMsg);
        assertTrue(tradeMsg.getEvent().getEventId() == 3473604);
    }
    
    /**
     * Test case for getMBSTrade for exception scenario
     * @throws Exception
     */
    @Test(expected=MBSBaseException.class)
    public void testGetMBSTradeException() throws MBSBaseException {
        TradeEventMessage tradeMsg = ecfTransformerImpl.getMBSTrade(StringUtils.EMPTY);
        assertTrue(tradeMsg.getEvent().getEventId() == 3473604);
    }
    
    /**
     * Test case for transform for exception scenario
     * @throws Exception
     */
    @Test(expected=MBSBaseException.class)
    public void transformException() throws MBSBaseException {
    	TradeEventMessage tradeMsg = ecfTransformerImpl.getMBSTrade(inputMsg);
    	tradeMsg.setTrade(null);
    	MBSTrade mbsTrade = ecfTransformerImpl.transform(tradeMsg);
    	assertTrue(mbsTrade.getTransReqNumber().equals("17J00030"));
    }
    
    /**
     * Test case for transform for exception scenario
     * @throws Exception
     */
    @Test
    public void transformExceptionTransId_For_Other_System() throws MBSBaseException {
    	TradeEventMessage tradeMsg = ecfTransformerImpl.getMBSTrade(invalidInputMsg);
    	MBSTrade mbsTrade = ecfTransformerImpl.transform(tradeMsg);
    	assertNull(mbsTrade.getTransReqNumber());
    }
    
}
