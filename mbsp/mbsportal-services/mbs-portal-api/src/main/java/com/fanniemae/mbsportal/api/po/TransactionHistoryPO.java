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

import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.fanniemae.mbsportal.constants.StateType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Class Name: TransactionHistoryPO Purpose : This class is the presentation
 * model for Transaction History object
 * 
 * @author g8upjv
 * 
 */
@JsonInclude(Include.NON_NULL)
public class TransactionHistoryPO {
    
    /**
     * 
     * transReqId String
     */
    private String transReqId;
    /**
     * 
     * tradeAmount String
     */ 
    private String tradeAmount;
    /**
     * 
     * product ProductPO
     */
    private ProductPO product;
    /**
     * 
     * tradeSettlementDate String
     */
    private String tradeSettlementDate;
    /**
     * 
     * tradeCouponRate String
     */
    private String tradeCouponRate;
    /**
     * 
     * tradeBuySellType String
     */
    private String tradeBuySellType;
    /**
     * 
     * stateType StateType
     */
    private StateType stateType;
    /**
     * 
     * pricePercent String
     */
    private String pricePercent;
    /**
     * 
     * pricePercentHandleText String
     */
    private String pricePercentHandleText;
    /**
     * 
     * pricePercentTicksText String
     */
    private String pricePercentTicksText;
    /**
     * 
     * submissionDate String
     */
    private String submissionDate;
    /**
     * 
     * traderName  String
     */
    //TODO: Change lender name and trader name to lender and trader profile objects
    private String traderName;
    
    /**
     * 
     * lenderName String
     */
    private String lenderName;
    
    /**
     * 
     * tradeSrcId String
     */
    private String tradeSrcId;
    
    /**
     * 
     * tradeSubPortfolioId long
     */
    private long tradeSubPortfolioId;
    
    /**
     * 
     * tradeSrcPrimaryId long
     */
    private long tradeSrcPrimaryId;
    
    /**
     * 
     * tradeSubPortfolioShortName String
     */
    private String tradeSubPortfolioShortName;
    
    /**
     * 
     * tradeDate String
     */
    private String tradeDate;
    
    /**
     * 
     * lenderEntityName String
     */
    private String lenderEntityName;
    
    /**
     * 
     * tspShortName String
     */
    private String tspShortName;
    
    
    /**
     * TransactionHistoryPO default constructor
     */
    public TransactionHistoryPO() {
    	
    }
    
    /**
     * TransactionHistoryPO constructor
     * 
     * @param submissionDate
     * @param lenderEntityName
     * @param tspShortName
     * @param tradeDate
     * @param tradeBuySellType
     * @param tradeAmount
     * @param product
     * @param tradeCouponRate
     * @param tradeSettlementDate
     * @param pricePercentHandleText
     * @param pricePercentTicksText
     * @param transReqId
     * @param stateType
     * @param tradeSrcPrimaryId
     * @param tradeSubPortfolioShortName
     * @param traderName
     * @param lenderName
     */
    public TransactionHistoryPO(String submissionDate, String lenderEntityName, String tspShortName, String tradeDate, 
    		String tradeBuySellType, String tradeAmount,  ProductPO product, 
    		String tradeCouponRate, String tradeSettlementDate, 
    		String pricePercentHandleText, String pricePercentTicksText, String transReqId, 
    		StateType stateType, long tradeSrcPrimaryId, String tradeSubPortfolioShortName,
    		String traderName, String lenderName) {
    	this.submissionDate = submissionDate;
    	this.lenderEntityName = lenderEntityName;
    	this.tspShortName = tspShortName;
    	this.tradeDate = tradeDate;
    	this.tradeBuySellType = tradeBuySellType;
    	this.tradeAmount = tradeAmount;
    	this.product = product;
    	this.tradeCouponRate = tradeCouponRate;
    	this.tradeSettlementDate = tradeSettlementDate;
    	this.pricePercentHandleText = pricePercentHandleText;
    	this.pricePercentTicksText = pricePercentTicksText;
    	this.transReqId = transReqId;
    	this.stateType = stateType;
    	this.tradeSrcPrimaryId = tradeSrcPrimaryId;
    	this.tradeSubPortfolioShortName = tradeSubPortfolioShortName;
    	this.traderName = traderName;
    	this.lenderName = lenderName;
    }
        
    
	/**
	 * 
	 * @return String the transReqId
	 */
	public String getTransReqId() {
		return transReqId;
	}
	
	/**
	 * 
	 * @param transReqId the transReqId to set
	 */
	public void setTransReqId(String transReqId) {
		this.transReqId = transReqId;
	}
	
	/**
	 * 
	 * @return String the tradeAmount
	 */
	public String getTradeAmount() {
		return tradeAmount;
	}
	
	/**
	 * @param tradeAmount the tradeAmount to set
	 */
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	
	/**
	 * 
	 * @return ProductPO the product
	 */
	public ProductPO getProduct() {
		return product;
	}
	
	/**
	 * 
	 * @param product the product to set
	 */
	public void setProduct(ProductPO product) {
		this.product = product;
	}
	
	/**
	 * 
	 * @return String the tradeSettlementDate
	 */
	public String getTradeSettlementDate() {
		return tradeSettlementDate;
	}
	
	/**
	 * 
	 * @param tradeSettlementDate the tradeSettlementDate to set
	 */
	public void setTradeSettlementDate(String tradeSettlementDate) {
		this.tradeSettlementDate = tradeSettlementDate;
	}
	
	/**
	 * @return String the tradeCouponRate
	 */
	public String getTradeCouponRate() {
		return tradeCouponRate;
	}
	
	/**
	 * 
	 * @param tradeCouponRate the tradeCouponRate to set
	 */
	public void setTradeCouponRate(String tradeCouponRate) {
		this.tradeCouponRate = tradeCouponRate;
	}
	
	/**
	 * 
	 * @return String the tradeBuySellType
	 */
	public String getTradeBuySellType() {
		return tradeBuySellType;
	}
	
	/**
	 * 
	 * @param tradeBuySellType the tradeBuySellType to set
	 */
	public void setTradeBuySellType(String tradeBuySellType) {
		this.tradeBuySellType = tradeBuySellType;
	}
	
	/**
	 * 
	 * @return StateType the stateType
	 */
	public StateType getStateType() {
		return stateType;
	}
	
	/**
	 * 
	 * @param stateType the stateType to set
	 */
	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}
	
	/**
	 * 
	 * @return String the pricePercent
	 */
	public String getPricePercent() {
		return pricePercent;
	}
	
	/**
	 * 
	 * @param pricePercent the pricePercent to set
	 */
	public void setPricePercent(String pricePercent) {
		this.pricePercent = pricePercent;
	}
	
	/**
	 * 
	 * @return String the pricePercentHandleText
	 */
	public String getPricePercentHandleText() {
		return pricePercentHandleText;
	}
	
	/**
	 * 
	 * @param pricePercentHandleText the pricePercentHandleText to set
	 */
	public void setPricePercentHandleText(String pricePercentHandleText) {
		this.pricePercentHandleText = pricePercentHandleText;
	}
	
	/**
	 * 
	 * @return String the pricePercentTicksText
	 */
	public String getPricePercentTicksText() {
		return pricePercentTicksText;
	}
	
	/**
	 * 
	 * @param pricePercentTicksText the pricePercentTicksText to set
	 */
	public void setPricePercentTicksText(String pricePercentTicksText) {
		this.pricePercentTicksText = pricePercentTicksText;
	}
	
	/**
	 * 
	 * @return String the submissionDate
	 */
	public String getSubmissionDate() {
		return submissionDate;
	}
	
	/**
	 * 
	 * @param submissionDate the submissionDate to set
	 */
	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}
	
	/**
	 * 
	 * @return String the traderName
	 */
	public String getTraderName() {
		return traderName;
	}
	
	/**
	 * 
	 * @param traderName the traderName to set
	 */
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}
	
	/**
	 * 
	 * @return String the lenderName
	 */
	public String getLenderName() {
		return lenderName;
	}
	
	/**
	 * 
	 * @param lenderName the lenderName to set
	 */
	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}
	
	/**
	 * 
	 * @return String the tradeSrcId
	 */
	public String getTradeSrcId() {
		return tradeSrcId;
	}
	
	/**
	 * 
	 * @param tradeSrcId the tradeSrcId to set
	 */
	public void setTradeSrcId(String tradeSrcId) {
		this.tradeSrcId = tradeSrcId;
	}
	
	/**
	 * 
	 * @return long the tradeSubPortfolioId
	 */
	public long getTradeSubPortfolioId() {
		return tradeSubPortfolioId;
	}
	
	/**
	 * 
	 * @param tradeSubPortfolioId the tradeSubPortfolioId to set
	 */
	public void setTradeSubPortfolioId(long tradeSubPortfolioId) {
		this.tradeSubPortfolioId = tradeSubPortfolioId;
	}
	
	/**
	 * 
	 * @return long the tradeSrcPrimaryId
	 */
	public long getTradeSrcPrimaryId() {
		return tradeSrcPrimaryId;
	}
	
	/**
	 * 
	 * @param tradeSrcPrimaryId the tradeSrcPrimaryId to set
	 */
	public void setTradeSrcPrimaryId(long tradeSrcPrimaryId) {
		this.tradeSrcPrimaryId = tradeSrcPrimaryId;
	}
	
	/**
	 * 
	 * @return String the tradeDate
	 */
	public String getTradeDate() {
		return tradeDate;
	}
	
	/**
	 * 
	 * @param tradeDate the tradeDate to set
	 */
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
	
	/**
	 * 
	 * @return String the lenderEntityName
	 */
	public String getLenderEntityName() {
		return lenderEntityName;
	}
	
	/**
	 * 
	 * @param lenderEntityName the lenderEntityName to set
	 */
	public void setLenderEntityName(String lenderEntityName) {
		this.lenderEntityName = lenderEntityName;
	}
	
	/**
	 * 
	 * @return String the tradeSubPortfolioShortName
	 */
	public String getTradeSubPortfolioShortName() {
		return tradeSubPortfolioShortName;
	}
	
	/**
	 * 
	 * @param tradeSubPortfolioShortName the tradeSubPortfolioShortName to set
	 */
	public void setTradeSubPortfolioShortName(String tradeSubPortfolioShortName) {
		this.tradeSubPortfolioShortName = tradeSubPortfolioShortName;
	}

    /**
     * @return the tspShortName
     */
    public String getTspShortName() {
        return tspShortName;
    }

    /**
     * @param tspShortName the tspShortName to set
     */
    public void setTspShortName(String tspShortName) {
        this.tspShortName = tspShortName;
    }

    /** 
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "TransactionHistoryPO [transReqId=" + transReqId + ", tradeAmount=" + tradeAmount + ", product="
                + product + ", tradeSettlementDate=" + tradeSettlementDate + ", tradeCouponRate=" + tradeCouponRate
                + ", tradeBuySellType=" + tradeBuySellType + ", stateType=" + stateType + ", pricePercent="
                + pricePercent + ", pricePercentHandleText=" + pricePercentHandleText + ", pricePercentTicksText="
                + pricePercentTicksText + ", submissionDate=" + submissionDate + ", traderName=" + traderName
                + ", lenderName=" + lenderName + ", tradeSrcId=" + tradeSrcId + ", tradeSubPortfolioId="
                + tradeSubPortfolioId + ", tradeSrcPrimaryId=" + tradeSrcPrimaryId + ", tradeSubPortfolioShortName="
                + tradeSubPortfolioShortName + ", tradeDate=" + tradeDate + ", lenderEntityName=" + lenderEntityName
                + ", tspShortName=" + tspShortName + "]";
    }
    
    

	/**
	 * convert the PO object to a csv string
	 * 
	 * @return
	 */
	public String toCSVString() {
		ArrayList<String> csvString = new ArrayList<String>();
		csvString.add(StringEscapeUtils.escapeCsv(submissionDate));
		csvString.add(StringEscapeUtils.escapeCsv(lenderEntityName));
		csvString.add(StringEscapeUtils.escapeCsv(tspShortName));
		csvString.add(StringEscapeUtils.escapeCsv(tradeDate));
		csvString.add(StringEscapeUtils.escapeCsv(tradeBuySellType));
		csvString.add(StringEscapeUtils.escapeCsv(tradeAmount));
		csvString.add(StringEscapeUtils.escapeCsv(product.getNameCode()));
		csvString.add(StringEscapeUtils.escapeCsv(tradeCouponRate));
		csvString.add(StringEscapeUtils.escapeCsv(tradeSettlementDate));
		csvString.add(StringEscapeUtils.escapeCsv(pricePercentHandleText + " - " + pricePercentTicksText));
		csvString.add(StringEscapeUtils.escapeCsv(transReqId));
		csvString.add(StringEscapeUtils.escapeCsv(stateType.getDisplayName()));
		csvString.add(StringEscapeUtils.escapeCsv(Long.toString(tradeSrcPrimaryId)));
		csvString.add(StringEscapeUtils.escapeCsv(tradeSubPortfolioShortName));
		csvString.add(StringEscapeUtils.escapeCsv(traderName));
		csvString.add(StringEscapeUtils.escapeCsv(lenderName));
		return StringUtils.join(csvString,",");
	}
}
