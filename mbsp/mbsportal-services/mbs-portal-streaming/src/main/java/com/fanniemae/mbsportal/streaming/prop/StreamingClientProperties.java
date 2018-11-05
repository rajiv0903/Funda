/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.prop;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 8, 2018
 * @File: com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties.java 
 * @Revision: 
 * @Description: StreamingClientProperties.java
 */
@Component
@ConfigurationProperties(prefix = "mbs.messaging.client", exceptionIfInvalid = false)
public class StreamingClientProperties {

    /**
     * 
     * publish publish pass through flag
     */
    private boolean publishTransaction;
    
    /**
     * 
     * publish publish pass through flag
     */
    private boolean publishPricing;
    
    /**
     * 
     * webSocketUrl Base URL
     */
    @NotEmpty
    private String webSocketUrl;

    /**
     * 
     * publishMsgContextPath Message Publish Context Path
     */
    @NotEmpty
    private String publishMsgContextPath;

    /**
     * 
     * transactionDestinationTopic Transaction Destination Topic
     */
    @NotEmpty
    private String transactionDestinationTopic;

    /**
     * 
     * pricingDestinationTopic Pricing Destination Topic
     */
    @NotEmpty
    private String pricingDestinationTopic;

    /**
     * 
     * retryCount retryCount
     */
    private Integer retryCount;
    
    
    /**
     * 
     * retryMaxAttempts retryMaxAttempts
     */
    private int retryMaxAttempts;
    
    
    /**
     * 
     * retryBackoff retryBackoff
     */
    private long retryBackoff;
    
    /**
     * retrySleep try in-between delay
     */
    private int retrySleep;
    
    /**
     * 
     * wsPublishEnabled wsPublishEnabled
     */
    private boolean wsPublishEnabled;
    
    /**
     * 
     * endPoint endPoint
     */
    private String endPoint;
    
    /**
     * 
     * inBoundMaxPool inBoundMaxPool
     */
    private int inBoundMaxPool;
    
    /**
     * 
     * inBoundCorePool inBoundCorePool
     */
    private int inBoundCorePool;
    
    /**
     * 
     * outBoundMaxPool outBoundMaxPool
     */
    private int outBoundMaxPool;
    
    /**
     * 
     * outBoundCorePool outBoundCorePool
     */
    private int outBoundCorePool;
    
    /**
     * 
     * @return the publishTransaction
     */
    public boolean isPublishTransaction() {
        return publishTransaction;
    }
    /**
     * 
     * @param publishTransaction
     *            the publishTransaction
     */
    public void setPublishTransaction(boolean publishTransaction) {
        this.publishTransaction = publishTransaction;
    }

    /**
     * 
     * @return the publishPricing
     */
    public boolean isPublishPricing() {
        return publishPricing;
    }

    /**
     * 
     * @param publishPricing
     *            the publishPricing
     */
    public void setPublishPricing(boolean publishPricing) {
        this.publishPricing = publishPricing;
    }

    /**
     * 
     * @return webSocketUrl
     */
    public String getWebSocketUrl() {
        return webSocketUrl;
    }
    /**
     * 
     * @param webSocketUrl the webSocketUrl
     */
    public void setWebSocketUrl(String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
    }
   
    /**
     * 
     * @return publishMsgContextPath
     */
    public String getPublishMsgContextPath() {
        return publishMsgContextPath;
    }

    /**
     * 
     * @param publishMsgContextPath the publishMsgContextPath
     */
    public void setPublishMsgContextPath(String publishMsgContextPath) {
        this.publishMsgContextPath = publishMsgContextPath;
    }

    /**
     * 
     * @return transactionDestinationTopic the transactionDestinationTopic
     */
    public String getTransactionDestinationTopic() {
        return transactionDestinationTopic;
    }

    /**
     * 
     * @param transactionDestinationTopic
     *            the transactionDestinationTopic
     */
    public void setTransactionDestinationTopic(String transactionDestinationTopic) {
        this.transactionDestinationTopic = transactionDestinationTopic;
    }

    /**
     * 
     * @return pricingDestinationTopic the pricingDestinationTopic
     */
    public String getPricingDestinationTopic() {
        return pricingDestinationTopic;
    }

    /**
     * 
     * @param pricingDestinationTopic
     *            the pricingDestinationTopic
     */
    public void setPricingDestinationTopic(String pricingDestinationTopic) {
        this.pricingDestinationTopic = pricingDestinationTopic;
    }

    /**
     * 
     * @return retryCount the retryCount
     */
    public Integer getRetryCount() {
        return retryCount;
    }

    /**
     * 
     * @param retryCount
     *            the retryCount
     */
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
    
    /**
     * 
     * @return retryMaxAttempts the retryMaxAttempts
     */
    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }
    /**
     * 
     * @param retryMaxAttempts
     *            the retryMaxAttempts
     */
    public void setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
    }
    /**
     * 
     * @return retryBackoff the retryBackoff
     */
    public long getRetryBackoff() {
        return retryBackoff;
    }
    /**
     * 
     * @param retryBackoff
     *            the retryBackoff
     */
    public void setRetryBackoff(long retryBackoff) {
        this.retryBackoff = retryBackoff;
    }
    
    /**
     * retrySleep
     * @return
     */
    public int getRetrySleep() {
        return retrySleep;
    }
    
    /**
     * retrySleep
     * @param retrySleep
     */
    public void setRetrySleep(int retrySleep) {
        this.retrySleep = retrySleep;
    }
    
    /**
     * 
     * @return wsPublishEnabled the wsPublishEnabled
     */
    public boolean isWsPublishEnabled() {
        return wsPublishEnabled;
    }
    
    /**
     * 
     * @param wsPublishEnabled
     *            the wsPublishEnabled
     */
    public void setWsPublishEnabled(boolean wsPublishEnabled) {
        this.wsPublishEnabled = wsPublishEnabled;
    }
    /**
     * 
     * @return endPoint the endPoint
     */
    public String getEndPoint() {
        return endPoint;
    }
    /**
     * 
     * @param endPoint
     *            the endPoint
     */
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
    /**
     * 
     * @return inBoundMaxPool the inBoundMaxPool
     */
    public int getInBoundMaxPool() {
        return inBoundMaxPool;
    }
    /**
     * 
     * @param inBoundMaxPool
     *            the inBoundMaxPool
     */
    public void setInBoundMaxPool(int inBoundMaxPool) {
        this.inBoundMaxPool = inBoundMaxPool;
    }
    /**
     * 
     * @return inBoundCorePool the inBoundCorePool
     */
    public int getInBoundCorePool() {
        return inBoundCorePool;
    }
    /**
     * 
     * @param inBoundCorePool
     *            the inBoundCorePool
     */
    public void setInBoundCorePool(int inBoundCorePool) {
        this.inBoundCorePool = inBoundCorePool;
    }
    /**
     * 
     * @return outBoundMaxPool the outBoundMaxPool
     */
    public int getOutBoundMaxPool() {
        return outBoundMaxPool;
    }
    /**
     * 
     * @param outBoundMaxPool
     *            the outBoundMaxPool
     */
    public void setOutBoundMaxPool(int outBoundMaxPool) {
        this.outBoundMaxPool = outBoundMaxPool;
    }
    /**
     * 
     * @return outBoundCorePool the outBoundCorePool
     */
    public int getOutBoundCorePool() {
        return outBoundCorePool;
    }
    /**
     * 
     * @param outBoundCorePool
     *            the outBoundCorePool
     */
    public void setOutBoundCorePool(int outBoundCorePool) {
        this.outBoundCorePool = outBoundCorePool;
    }
    
    
}
