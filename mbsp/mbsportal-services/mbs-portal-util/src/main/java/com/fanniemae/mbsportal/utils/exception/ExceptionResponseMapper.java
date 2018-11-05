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

package com.fanniemae.mbsportal.utils.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author g8upjv
 */
@Component
public class ExceptionResponseMapper {
    
    /**
     * 
     * 
     * 
     * @param exceptionLookupPOLst
     * @return ExceptionResponsePO
     */
    public ExceptionResponsePO transformResponse(List<ExceptionLookupPO> exceptionLookupPOLst){
        
        ExceptionResponsePO exceptionResponsePO = null;
        if(Objects.nonNull(exceptionLookupPOLst) && exceptionLookupPOLst.size() >0){
            exceptionResponsePO = new ExceptionResponsePO();
            List<ExceptionMessagesPO> messages = new ArrayList<ExceptionMessagesPO>();
            ExceptionMessagesPO exceptionMessagesPO;
            for(ExceptionLookupPO exceptionLookupPO: exceptionLookupPOLst){
                exceptionMessagesPO = new ExceptionMessagesPO();
                exceptionMessagesPO.setErrorMessage(exceptionLookupPO.getErrorMessage());
                exceptionMessagesPO.setMessageCode(exceptionLookupPO.getErrorCode());
                exceptionMessagesPO.setMessageType(exceptionLookupPO.getMessageType());
                messages.add(exceptionMessagesPO);
            }
            exceptionResponsePO.setMessages(messages);
        }
        return exceptionResponsePO;
        
    }
    
    

}
