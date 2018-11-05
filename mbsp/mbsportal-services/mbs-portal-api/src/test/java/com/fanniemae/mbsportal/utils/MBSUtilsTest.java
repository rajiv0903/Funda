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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.fanniemae.mbsportal.api.utils.MBSUtils;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author g8uaxt Created on 9/29/2017.
 */
public class MBSUtilsTest {

    @Test
    public void convertStateTypeToStringList() throws MBSBaseException {
        List<StateType> stateTypeLst = new ArrayList<StateType>();
        stateTypeLst.add(StateType.LENDER_OPEN);
        assertNotNull(MBSUtils.convertStateTypeToStringList(stateTypeLst));
    }

}
