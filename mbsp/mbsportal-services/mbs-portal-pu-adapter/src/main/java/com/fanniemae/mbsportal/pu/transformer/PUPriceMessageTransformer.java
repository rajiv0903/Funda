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

package com.fanniemae.mbsportal.pu.transformer;

import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.pu.constants.MessageEventType;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

import java.util.List;

/**
 * 
 * @author g8upjv
 *
 */
public interface PUPriceMessageTransformer {
    
    
    /**
     * 
     * Method to transform Price Message to MBSMarketIndicativePrice 
     * 
     * @param inputMessage
     * @return List
     * @throws MBSBaseException
     */
    public List<MBSMarketIndicativePrice> transform(String inputMessage, MessageEventType eventType) throws MBSBaseException;

}
