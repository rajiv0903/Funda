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

package com.fanniemae.mbsportal.streaming.socket.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 27, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.model.StreamingMessageDetails.java 
 * @Revision: 
 * @Description: StreamingMessageDetails.java
 */

public class StreamingMessageDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8604525003036960203L;
    
    /**
     * 
     * messageHeader Map
     */
    private Map<String, Object> messageIdentifierMap;
    
    /**
     * 
     * topics List
     */
    private List<String> topics;
    /**
     * 
     * messageHeader Map
     */
    private Map<String, Object> messageHeader;
    /**
     * 
     * message String
     */
    private String message;
    /**
     * 
     * extraInfo String
     */
    private String extraInfo;
    
    
    
    /**
     * 
     * @return messageIdentifierMap
     */
    public Map<String, Object> getMessageIdentifierMap() {
        return messageIdentifierMap;
    }

    /**
     * 
     * @param messageIdentifierMap the Message Identifier Map
     */
    public void setMessageIdentifierMap(Map<String, Object> messageIdentifierMap) {
        this.messageIdentifierMap = messageIdentifierMap;
    }
    
    /**
     * Add message Identifier 
     * 
     * @param key
     * @param value
     */
    public void addMessageIdentifierMap(String key, Object value) {
        if(this.messageIdentifierMap == null){
            messageIdentifierMap = new LinkedHashMap<>();
        }
        this.messageIdentifierMap.put(key, value);
    }

    /**
     * 
     * @return List
     */
    public List<String> getTopics() {
        return topics;
    }

    /**
     * 
     * @param topics
     */
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    /**
     * 
     * @return Map
     */
    public Map<String, Object> getMessageHeader() {
        return messageHeader;
    }

    /**
     * 
     * @param messageHeader
     *            the messageHeader
     */
    public void setMessageHeader(Map<String, Object> messageHeader) {
        this.messageHeader = messageHeader;
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
     * @param message
     *            the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return String
     */
    public String getExtraInfo() {
        return extraInfo;
    }

    /**
     * 
     * @param firstName
     *            the firstName
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    /**
     * 
     * @param topic to add at topic list
     */
    public void addTopic(String topic) {
        if(this.topics == null){
            this.topics = new LinkedList<>();
        }
        this.topics.add(topic);
    }

    @Override
    public String toString() {
        return "MBSPMessagePO{" + "topics='" + topics + '\'' + ", messageHeader=" + messageHeader + ", message='"
                + message + '\'' + ", extraInfo='" + extraInfo + '\'' + '}';
    }

}

