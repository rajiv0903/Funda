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

package com.fanniemae.mbsportal.model;

import java.io.Serializable;

/**
 * 
 * This class stores the exception codes and messages
 * 
 * @author g8upjv
 *
 */
public class MBSExceptionLookup extends MBSBaseEntity {

    
    /**
     * 
     * serialVersionUID long
     */
    private static final long serialVersionUID = -4795295226781688928L;
    
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
     * errorMessage String
     */
    private String loggerMessage;
    
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
     * @see com.fanniemae.fnmpfj.gemfire.dao.BaseEntity#getId()
     * @return Serializable
     */
    @Override
    public Serializable getId() {
            return this.errorCode;
    }

    /**
     * 
     * Constructor
     * 
     * @param errorCode String
     * @param errorMessage String
     * @param errorCategory String
     * @param messageType String
     */
    public MBSExceptionLookup(String errorCode, String errorMessage, String errorCategory, String messageType){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorCategory = errorCategory;
        this.messageType = messageType;
    }

    /**
     * 
     * Default constructor
     */
    public MBSExceptionLookup() {
        // TODO Auto-generated constructor stub
    }

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
        return "MBSExceptionLookup [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", loggerMessage="
                + loggerMessage + ", errorCategory=" + errorCategory + ", messageType=" + messageType
                + ", classificationTypeCode=" + classificationTypeCode + ", logLevel=" + logLevel + "]";
    }

}
