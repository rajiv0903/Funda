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


package com.fanniemae.mbsportal.pu.service;

import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 2, 2018
 * @File: com.fanniemae.mbsportal.pu.service.MarketIndicativePriceHistoryService.java 
 * @Revision: 
 * @Description: MarketIndicativePriceHistoryService.java
 */
public interface MarketIndicativePriceHistoryService {

    /**
     * Save the data in the same thread 
     * @param transformationObj TransformationObject
     * @return true or false
     * @throws MBSBaseException
     */
    public boolean saveMarketIndicativePriceHistory(TransformationObject transformationObj) throws MBSBaseException;
    
    
    /**
     * Save the data asynchronously 
     * @param transformationObj TransformationObject
     * @return void
     * @throws MBSBaseException
     */
    public void saveMarketIndicativePriceHistoryAsync(TransformationObject transformationObj) throws MBSBaseException;
}
