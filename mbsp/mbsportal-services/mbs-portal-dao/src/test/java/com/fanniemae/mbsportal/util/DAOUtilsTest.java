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

package com.fanniemae.mbsportal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Created by g8uaxt on 8/3/2017.
 */

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 11, 2017
 * @Time 1:33:29 PM
 * 	com.fanniemae.mbsportal.util
 * 	DAOUtilsTest.java
 * @Description: Revision 1.1
 */
public class DAOUtilsTest {
    private static String format="%05d";
    //TODO: make expected as runtime validatable until to ignore ?
    @Test
    @Ignore
    public void formatSeqNumber() throws MBSBaseException {
        assertEquals("17H00001",DAOUtils.formatSeqNumber(00000001, DAOConstants.IDTypes.TRANSACTION_ID,format));
    }
    @Test
    @Ignore
    public void formatSeqNumberFor2Digits() throws MBSBaseException {
        assertEquals("17H00010",DAOUtils.formatSeqNumber(10, DAOConstants.IDTypes.TRANSACTION_ID,format));
    }
    @Test
    @Ignore
    public void formatSeqNumberFor3Digits() throws MBSBaseException {
        assertEquals("17H00100",DAOUtils.formatSeqNumber(100, DAOConstants.IDTypes.TRANSACTION_ID,format));
    }
    @Test
    @Ignore
    public void formatSeqNumberFor4Digits() throws MBSBaseException {
        assertEquals("17H01000",DAOUtils.formatSeqNumber(1000, DAOConstants.IDTypes.TRANSACTION_ID,format));
    }
    @Test
    @Ignore
    public void formatSeqNumberFor5Digits() throws MBSBaseException {
        assertEquals("17H09999",DAOUtils.formatSeqNumber(9999, DAOConstants.IDTypes.TRANSACTION_ID,format));
    }

    @Test
    @Ignore
    public void formatSeqNumberForMoreThan5Digits() throws MBSBaseException {
        assertEquals("17H09999",DAOUtils.formatSeqNumber(8294130753066044888l, DAOConstants.IDTypes.TRANSACTION_ID,format));
    }
    
    @Test
    @Ignore
    public void formatSeqNumberForFormatNull() throws MBSBaseException {
        assertEquals("12",DAOUtils.formatSeqNumber(12, DAOConstants.IDTypes.TRANSACTION_ID,
                null));
    }
    
    @Test
    public void formatSeqNumberFor5Digits_Scuccess() throws MBSBaseException {
        assertNotNull(DAOUtils.formatSeqNumber(9999, DAOConstants.IDTypes.TRANSACTION_ID,format));
    }
    
    @Test
    public void formatSeqNumberForFormatNull_Scuccess() throws MBSBaseException {
        assertNotNull(DAOUtils.formatSeqNumber(12, DAOConstants.IDTypes.TRANSACTION_ID,  null));
    }
}
