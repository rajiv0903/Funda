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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class Name: ExceptionResponsePO Purpose : This class is the presentation
 * model for Exception object
 * 
 * @author g8upjv
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExceptionResponsePO {

    /**
     *
     * serialVersionUID long
     */
    //private static final long serialVersionUID = 2018120541964470822L;

    /**
     * 
     * Constructor
     * 
     * @param errorCode String
     * @param errorMessage String
     * @param errorCategory String
     * @param messageType String
     */
    public ExceptionResponsePO(List<ExceptionMessagesPO> messages, String responseEntity, String status){
        this.messages = messages;
        this.responseEntity = responseEntity;
        this.status = status;
    }
    
    /**
     * 
     * Contructor
     */
    public ExceptionResponsePO() {
        // TODO Auto-generated constructor stub
    }
    /**
     * 
     * messages List
     */
    @XmlElement
    private List<ExceptionMessagesPO> messages;

    /**
     * 
     * responseEntity String
     */
    @XmlElement
    private String responseEntity;
	
    /**
     * 
     * status String
     */
    @XmlElement
    private String status;
    
    /**
     * 
     * @return String the responseEntity
     */
    public String getResponseEntity() {
        return responseEntity;
    }

    /**
     * 
     * @param responseEntity String the responseEntity to set
     */
    public void setResponseEntity(String responseEntity) {
        this.responseEntity = responseEntity;
    }

    /**
     * 
     * @return String the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param errorCategory String the errorCategory to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the messages
     */
    public List<ExceptionMessagesPO> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<ExceptionMessagesPO> messages) {
        this.messages = messages;
    }

    /** 
     * 
     * 
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     * @return String
     */
//    @Override
//    public String toString() {
//        return "ExceptionResponsePO [messages=" + messages + ", responseEntity="
//                + responseEntity + ", status=" + status + "]";
//    }
	
}
