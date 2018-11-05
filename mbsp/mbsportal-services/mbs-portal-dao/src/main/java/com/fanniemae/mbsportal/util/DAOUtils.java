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

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Created by g8uaxt on 8/3/2017.
 */
@Component
public class DAOUtils {

    /**
     * Purpose: This method generates the ID for Transaction request
     * @return
     */
    
    private DAOUtils(){
    }

    public static String formatSeqNumber(long transReqId,DAOConstants.IDTypes type,String format) throws MBSBaseException{
        //TODO - Generated id should be displayed & use types later
        if(format==null){
            return String.valueOf(transReqId);
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        StringBuilder transId = new StringBuilder();
        transId.append(String.valueOf(year).substring(2));
        transId.append(DAOConstants.MonthType.getMonthCode(month));
        transId.append(String.format(format, transReqId));//TODO: truncate to 5 digit only if not
        return transId.toString();
    }
    
  
    /**
     * 
     * @param args
     * @return List<Object>
     */
    public List<Object> queryfunction(Serializable[] args, String function) {
    	Execution exec = FunctionService.onServer(ClientCacheFactory.getAnyInstance().getDefaultPool())
                .withArgs(args);
        ResultCollector<?, ?> result = exec.execute(function);
        List<Object> selectedResults = (List<Object>) result.getResult();
        return selectedResults;
    }
    

    public String generateInSetConditional(String field, List<String> listItems) {
        StringBuilder conditionalBuilder = new StringBuilder();

        if (!StringUtils.isEmpty(field) && !CollectionUtils.isEmpty(listItems)) {
            conditionalBuilder.append(field);
            conditionalBuilder.append(" IN SET(");

            int size = listItems.size() - 1;
            for (int index = 0; index < size; index++) {
                if (!StringUtils.isEmpty(listItems.get(index))) {
                    conditionalBuilder.append("\'");
                    conditionalBuilder.append(listItems.get(index));
                    conditionalBuilder.append("\',");
                }
            }

            if (!StringUtils.isEmpty(listItems.get(listItems.size() - 1))) {
                conditionalBuilder.append("\'");
                conditionalBuilder.append(listItems.get(listItems.size() - 1));
                conditionalBuilder.append("\'");
            }
            conditionalBuilder.append(")");
        }
        return conditionalBuilder.toString();
    }
}
