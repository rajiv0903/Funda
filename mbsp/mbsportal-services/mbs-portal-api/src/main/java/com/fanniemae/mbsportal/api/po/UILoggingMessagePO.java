/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.po;

import java.io.Serializable;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 18, 2018
 * @Time 11:44:01 AM com.fanniemae.mbsportal.api.po UILoggingMessagePO.java
 * @Description: CMMBSSTA01-941 : (Tech) Logging API to capture UI logs
 */
public class UILoggingMessagePO implements Serializable {
    
    /**
     * 
     * serialVersionUID long
     */
    private static final long serialVersionUID = -7391732330415321183L;
    
    /**
     * 
     * userName
     */
    private String userName;
    
    /**
     * 
     * code
     */
    private String code;
    
    /**
     * 
     * channel
     */
    private String channel;
    
    /**
     * 
     * transReqId
     */
    private String transReqId;
    
    /**
     * 
     * type
     */
    private String type;
    
    /**
     * 
     * module
     */
    private String module;
    
    /**
     * 
     * page
     */
    private String page;
    
    /**
     * 
     * functionality
     */
    private String functionality;
    
    /**
     * 
     * message
     */
    private String message;
    
    /**
     * 
     * messageDescription
     */
    private String messageDescription;
    
    /**
     * 
     * timeStamp
     */
    private String timeStamp;
    
    /**
     * 
     * @return String
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * 
     * @param userName the userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * 
     * @return String
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }
    
    /**
     * 
     * @return String
     */
    public String getChannel() {
        return channel;
    }
    
    /**
     * 
     * @param channel the channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    /**
     * 
     * @return String
     */
    public String getTransReqId() {
        return transReqId;
    }
    
    /**
     * 
     * @param transReqId the transReqId
     */
    public void setTransReqId(String transReqId) {
        this.transReqId = transReqId;
    }
    
    /**
     * 
     * @return String
     */
    public String getType() {
        return type;
    }
    
    /**
     * 
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * 
     * @return String
     */
    public String getModule() {
        return module;
    }
    
    /**
     * 
     * @param module the module
     */
    public void setModule(String module) {
        this.module = module;
    }
    
    /**
     * 
     * @return String
     */
    public String getPage() {
        return page;
    }

    /**
     * 
     * @param page the page
     */
    public void setPage(String page) {
        this.page = page;
    }
    
    /**
     * 
     * @return String
     */
    public String getFunctionality() {
        return functionality;
    }
    
    /**
     * 
     * @param functionality the functionality
     */
    public void setFunctionality(String functionality) {
        this.functionality = functionality;
    }
    
    /**
     * 
     * @return String
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 
     * @return String
     */
    public String getMessageDescription() {
        return messageDescription;
    }
    
    /**
     * 
     * @param messageDescription the messageDescription
     */
    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }

    /**
     * 
     * @return String
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * 
     * @param timeStamp the timeStamp
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    
}
