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
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 27, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.model.StreamingMessage.java 
 * @Revision: 
 * @Description: StreamingMessage.java
 */

@JsonInclude(Include.NON_NULL)
public class StreamingMessage implements Serializable{


    /**
     * 
     */
    private static final long serialVersionUID = 4482947127349631077L;
    
    /**
     * 
     * messages List of StreamingMessageDetails
     */
    private List<StreamingMessageDetails> messages;

    /**
     * 
     * @return messages the List of message
     */
    public List<StreamingMessageDetails> getMessages() {
        return messages;
    }

    /**
     * 
     * @param messages the messages
     */
    public void setMessages(List<StreamingMessageDetails> messages) {
        this.messages = messages;
    }
    
    /**
     * 
     * @param streamingMessageDetails Add message to List
     */
    public void addMessages(StreamingMessageDetails streamingMessageDetails) {
        if(this.messages == null){
            this.messages = new LinkedList<>();
        }
        this.messages.add(streamingMessageDetails);
    }
    
    /**
     * 
     * Clear the messages 
     */
    public void clearMessages() {
        if(this.messages != null){
            this.messages.clear();
        }
    }

    @Override
    public String toString() {
        return "StreamingTransactionMessage [messages=" + messages + "]";
    }
    
    
}
