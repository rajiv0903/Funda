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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Class Name: ExceptionLookupPO Purpose : This class is the presentation
 * model for Exception object
 * 
 * @author g8upjv
 * 
 */
@JsonInclude(Include.NON_NULL)
public class ExceptionLookupPO implements Serializable {

    /**
     *
     * serialVersionUID long
     */
    private static final long serialVersionUID = 2018120541964470822L;

    /**
     * 
     * errorCode String
     */
    private String errorCode;
    
    /**
     * 
     * errorMessage String
     */
    private String errorMessage;
    
    /**
     * 
     * errorCategory String, whether API, UI, generic or any other category
     */
    private String errorCategory;
    
    /**
     * 
     * errorField String
     */
    private String messageType;
    
    /**
     * 
     * loggerMessage String
     */
    private String loggerMessage;
    
    /**
     * 
     * classificationTypeCode String
     */
    private String classificationTypeCode;

    /**
     * 
     * logLevel String
     */
    private String logLevel;
    

    /**
     * 
     * @return the errorCode String
     */
    public String getErrorCode() {
        return errorCode;
    }


    /**
     * 
     * @param errorCode the errorCode to set String
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * 
     * @return the errorMessage String
     */
    public String getErrorMessage() {
        return errorMessage;
    }


    /**
     *  
     * @param errorMessage the errorMessage to set String
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    /**
     * 
     * @return the errorCategory String
     */
    public String getErrorCategory() {
        return errorCategory;
    }


    /**
     * @param errorCategory the errorCategory to set
     */
    public void setErrorCategory(String errorCategory) {
        this.errorCategory = errorCategory;
    }

    /**
     * @return the messageType
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the loggerMessage
     */
    public String getLoggerMessage() {
        return loggerMessage;
    }


    /**
     * @param loggerMessage the loggerMessage to set
     */
    public void setLoggerMessage(String loggerMessage) {
        this.loggerMessage = loggerMessage;
    }

    /**
     * @return the classificationTypeCode
     */
    public String getClassificationTypeCode() {
        return classificationTypeCode;
    }


    /**
     * @param classificationTypeCode the classificationTypeCode to set
     */
    public void setClassificationTypeCode(String classificationTypeCode) {
        this.classificationTypeCode = classificationTypeCode;
    }


    /**
     * @return the logLevel
     */
    public String getLogLevel() {
        return logLevel;
    }


    /**
     * @param logLevel the logLevel to set
     */
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     * @return String
     */
    @Override
    public String toString() {
        return "ExceptionLookupPO [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", errorCategory="
                + errorCategory + ", messageType=" + messageType + ", loggerMessage=" + loggerMessage
                + ", classificationTypeCode=" + classificationTypeCode + ", logLevel=" + logLevel + "]";
    }
	
}
