/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.utils;

import java.util.ArrayList;
import java.util.List;

import com.fanniemae.mbsportal.constants.StateType;

/**
 *
 * Class Name: MBSUtils Purpose : This class has the utility methods for MBS
 * Application
 *
 * @author g8upjv
 * @date 07/26/2017 Initial version.
 *
 */
public class MBSUtils {

    private MBSUtils() {

    }

    /**
     *
     * This method returns the List of String of state type values
     *
     * @param stateTypeLst
     *            List<StateType>
     * @return List<String>
     */
    public static List<String> convertStateTypeToStringList(List<StateType> stateTypeLst) {
        List<String> strStateTypeLst = new ArrayList<String>();
        for (StateType stateType : stateTypeLst) {
            strStateTypeLst.add(stateType.toString());
        }
        return strStateTypeLst;
    }

}
