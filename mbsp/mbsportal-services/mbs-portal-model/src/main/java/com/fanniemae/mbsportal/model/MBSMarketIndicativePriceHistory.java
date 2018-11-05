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
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 2, 2018
 * @File: com.fanniemae.mbsportal.model.MBSMarketIndicativePriceHistory.java
 * @Revision:
 * @Description: MBSMarketIndicativePriceHistory.java
 */
public class MBSMarketIndicativePriceHistory extends MBSMarketIndicativePrice {

    /**
     * serialVersionUID serialVersionUID
     */
    private static final long serialVersionUID = 1578678591771211854L;

    /**
     * 
     * MBSMarketIndicativePriceHistory constructor
     */
    public MBSMarketIndicativePriceHistory() {
    }

    /**
     * 
     * @param historyId
     *            the historyId to set
     * @param productNameCode
     *            the productNameCode to set
     * @param puNameCode
     *            the puNameCode to set
     * @param observationDate
     *            the historyId to set
     * @param passThroughRate
     *            the observationDate to set
     * @param forward
     *            the forward to set
     * @param settlementDate
     *            the settlementDate to set
     * @param cutOffDate
     *            the cutOffDate to set
     * @param bidPricePercent
     *            the bidPricePercent to set
     * @param bidPriceText
     *            the bidPriceText to set
     * @param askPricePercent
     *            the askPricePercent to set
     * @param askPriceText
     *            the askPriceText to set
     * @param eventType
     *            the eventType to set
     * @param midPricePercent
     *            the midPricePercent to set
     * @param midPriceText
     *            the midPriceText to set
     * @param observationDateTime
     *            the observationDateTime to set
     */
    public MBSMarketIndicativePriceHistory(Long historyId, String productNameCode, String puNameCode,
            Long observationDate, BigDecimal passThroughRate, Integer forward, Date settlementDate, Date cutOffDate,
            BigDecimal bidPricePercent, String bidPriceText, BigDecimal askPricePercent, String askPriceText,
            String eventType, BigDecimal midPricePercent, String midPriceText, Date observationDateTime) {

        super(productNameCode, puNameCode, observationDate, passThroughRate, forward, settlementDate, cutOffDate,
                bidPricePercent, bidPriceText, askPricePercent, askPriceText, eventType, midPricePercent, midPriceText);

        setHistoryId(historyId);
        setObservationDateTime(observationDateTime);
    }

    /**
     * 
     * historyId Long
     */
    private Long historyId;

    /**
     * 
     * observationDate Long
     */
    private Date observationDateTime;

    /**
     * 
     * 
     * @return long
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * 
     * @return the historyId Long
     */
    public Long getHistoryId() {
        return historyId;
    }

    /**
     * 
     * @param historyId
     *            the historyId to set
     */
    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    /**
     * 
     * @return the observationDateTime String
     */
    public Date getObservationDateTime() {
        return observationDateTime;
    }

    /**
     * 
     * @param observationDateTime
     *            the observationDateTime to set
     */
    public void setObservationDateTime(Date observationDateTime) {
        this.observationDateTime = observationDateTime;
    }

    /**
     * @see com.fanniemae.fnmpfj.gemfire.dao.BaseEntity#getId()
     */
    @Override
    public Serializable getId() {
        return this.historyId;
    }

}
