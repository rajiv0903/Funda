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

package com.fanniemae.mbsportal.service;

import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This service interface has the Gemfire related operations
 * 
 * @author g8upjv
 *
 */
public interface ESBTransactionRequestService {
    
    /**
     * 
     * @param transReqId
     * @return
     * @throws MBSBaseException
     */
    public MBSTransactionRequest getMBSTransReq(String transReqId) throws MBSBaseException;
    
    /**
     * 
     * @param mbsTransactionRequest
     * @return
     * @throws MBSBaseException
     */
    public boolean updateMBSTransReq(MBSTransactionRequest mbsTransactionRequest) throws MBSBaseException;
    
    /**
     * 
     * @param mbsTransactionRequest
     * @return
     * @throws MBSBaseException
     */
    public boolean updateMBSTrade(MBSTrade mbsTrade) throws MBSBaseException;
    
    

}
