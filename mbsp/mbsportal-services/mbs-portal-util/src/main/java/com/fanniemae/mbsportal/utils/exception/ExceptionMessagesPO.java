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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;

/**
 * Class Name: ExceptionMessagesPO Purpose : This class is the presentation
 * model for Exception object
 * 
 * @author g8upjv
 * 
 */
@JsonInclude(Include.NON_NULL)
public class ExceptionMessagesPO implements Serializable {

    /**
     *
     * serialVersionUID long
     */
    private static final long serialVersionUID = 2018120541964470822L;

    /**
     * 
     * messageCode String The error code value. Example: TRANS_00001
     */
    private String messageCode;

    /**
     * 
     * errorMessage String
     */
    private String errorMessage;
	
    /**
     * 
     * errorCategory String
     */
    private String messageType;

    /**
	 * Default constructor
	 */
	public ExceptionMessagesPO() {
		
	}

	/**
	 * @param messageCode
	 * @param errorMessage
	 * @param messageType
	 */
	public ExceptionMessagesPO(String messageCode, String errorMessage, String messageType) {
		super();
		this.messageCode = messageCode;
		this.errorMessage = errorMessage;
		this.messageType = messageType;
	}
    /**
     * 
     * @return String the messageCode
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * 
     * @param messageCode String the messageCode to set
     */
    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    /**
     * 
     * @return String the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 
     * @param errorMessage String the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 
     * @return String the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * 
     * @param messageType String the messageType to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    

    /** 
     * 
     * 
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     * @return String
     */
    @Override
    public String toString() {
        return "ExceptionLookupPO [messageCode=" + messageCode + ", errorMessage=" + errorMessage + ", messageType="
                + messageType + "]";
    }
	
}
