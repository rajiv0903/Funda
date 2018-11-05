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

/**
 * Class Name: TransactionHistoryPO Purpose : This class is the presentation
 * model for Transaction History object
 * 
 * @author g8upjv
 * 
 */
//@JsonInclude(Include.NON_NULL)
public class TradePO {
    
    /**
     * 
     * transactionRequestId String
     */
    private String transactionRequestId;
    /**
     * 
     * tradeSourceId String
     */
    private String tradeSourceId;
    /**
     * 
     * tradeSubPortfolioId long
     */
    private long tradeSubPortfolioId;
    /**
     * 
     * tradeSourcePrimaryId long
     */
    private long tradeSourcePrimaryId;
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
     * tradeSubPortfolioShortName String
     */
    private String tradeSubPortfolioShortName;
    
    
	/**
	 * 
	 * @return String the transactionRequestId
	 */
	public String getTransactionRequestId() {
		return transactionRequestId;
	}
	/**
	 * 
	 * @param transactionRequestId the transactionRequestId to set
	 */
	public void setTransactionRequestId(String transactionRequestId) {
		this.transactionRequestId = transactionRequestId;
	}
	/**
	 * 
	 * @return String the tradeSourceId
	 */
	public String getTradeSourceId() {
		return tradeSourceId;
	}
	
	/**
	 * 
	 * @param tradeSourceId the tradeSourceId to set
	 */
	public void setTradeSourceId(String tradeSourceId) {
		this.tradeSourceId = tradeSourceId;
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
	 * @return long the tradeSourcePrimaryId
	 */
	public long getTradeSourcePrimaryId() {
		return tradeSourcePrimaryId;
	}
	
	/**
	 * @param tradeSourcePrimaryId the tradeSourcePrimaryId to set
	 */
	public void setTradeSourcePrimaryId(long tradeSourcePrimaryId) {
		this.tradeSourcePrimaryId = tradeSourcePrimaryId;
	}
	
	/**
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
	 * @see java.lang.Object#toString()
	 * @return String
	 */
	@Override
	public String toString() {
		return "TradePO [transactionRequestId=" + transactionRequestId + ", tradeSourceId=" + tradeSourceId
				+ ", tradeSubPortfolioId=" + tradeSubPortfolioId + ", tradeSourcePrimaryId=" + tradeSourcePrimaryId
				+ ", tradeDate=" + tradeDate + ", lenderEntityName=" + lenderEntityName + "]";
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
	
 

}
