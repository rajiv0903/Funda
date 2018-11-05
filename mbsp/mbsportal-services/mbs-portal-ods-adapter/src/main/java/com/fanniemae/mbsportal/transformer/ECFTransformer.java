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

import com.fanniemae.mbsportal.ecf.ods.TradeEventMessage;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author g8upjv
 *
 */
public interface ECFTransformer {
    
    /**
     * Method to get MBSTrade 
     * 
     * @param eventMessages event message to convert
     * 
     * @return String
     * @throws MBSBaseException 
     */
    public TradeEventMessage getMBSTrade(String inputMessage) throws MBSBaseException;
    
    /**
     * Method to transform TradeEvent to MBSTrade 
     * 
     * @param inputMessage
     * @return
     * @throws MBSBaseException
     */
    public MBSTrade transform(TradeEventMessage inputMessage) throws MBSBaseException;

}
