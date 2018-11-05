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

package com.fanniemae.mbsportal.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Created by g8uaxt on 8/3/2017.
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore("runtime unit test valdiation")
public class IDServiceDaoTest {
    @Mock
    private IDServiceDao idServiceDao;
    //TODO: make expected as runtime validatable until to ignore ?
    @Test
     public void getSeqIdForOneNumber() throws MBSBaseException {
        //IDServiceDao idServiceDao = new IDServiceDao();
        assertEquals("17H00001", idServiceDao.getSeqId(DAOConstants.IDTypes.TRANSACTION_ID));
    }

    @Test
    @Ignore
    public void getSeqIdForMultiple() throws MBSBaseException {
        //IDServiceDao idServiceDao = new IDServiceDao();
        assertEquals("17H00002", idServiceDao.getSeqId(DAOConstants.IDTypes.TRANSACTION_ID,3));
    }

}
