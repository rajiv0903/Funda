/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.enrichment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.TradeBuySell;
import com.fanniemae.mbsportal.enrichment.BaseEnricher;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.google.common.base.Objects;

/**
 * This class handles the enrichment for Transaction History object
 * 
 * 07/14/2017
 * 
 * @author g8upjv
 * 
 */
@Component
public class TransactionHistoryEnrichment<T extends TransformationObject> extends BaseEnricher<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryEnrichment.class);

	/**
	 * 
	 * Purpose: This method enriches the Transaction History object
	 * 
	 * @param object the TransformationObject
	 * @throws MBSBaseException
	 */
	@Override
	public void enrich(TransformationObject object) throws MBSBaseException {
		LOGGER.debug("Entering enrich method in TransactionHistoryEnrichment");
		TransactionHistoryPO transHist = new TransactionHistoryPO();
		try {
			transHist = enrichToHistoryObject(object);
			LOGGER.debug("TransactionHistoryEnrichment transHist " + transHist);
			object.setTargetPojo(transHist);
		} catch (MBSBaseException e) {
			LOGGER.error("Exception when enriching data", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Exception when enriching data", e);
			throw new MBSSystemException("History enrichment failed", MBSExceptionConstants.SYSTEM_EXCEPTION, e);
		}
		LOGGER.debug("Exiting enrich method in TransactionHistoryEnrichment");
	}

	/**
	 * 
	 * enriching the history object
	 * 
	 * @param object the TransformationObject
	 * @return TransactionHistoryPO
	 * @throws MBSBaseException
	 */
	private TransactionHistoryPO enrichToHistoryObject(TransformationObject object)
			throws MBSBaseException {
			LOGGER.debug("Entering enrichToHistoryObject method in TransactionHistoryEnrichment");
			TransactionHistoryPO transactionHistoryPO = new TransactionHistoryPO();
		try{
			TransactionRequestPO transactionRequestPO = (TransactionRequestPO) object.getTransformationDataMap()
					.get(MBSPServiceConstants.TRANSACTION_REQUEST);
			TradePO tradePO = (TradePO) object.getTransformationDataMap().get(MBSPServiceConstants.TRADE_REQUEST);
			transactionHistoryPO.setTransReqId(transactionRequestPO.getTransReqId());
			transactionHistoryPO.setPricePercent(transactionRequestPO.getPricePercent());
			transactionHistoryPO.setPricePercentHandleText(transactionRequestPO.getPricePercentHandleText());
			transactionHistoryPO.setPricePercentTicksText(transactionRequestPO.getPricePercentTicksText());
			transactionHistoryPO.setProduct(transactionRequestPO.getProduct());
			transactionHistoryPO.setStateType(transactionRequestPO.getStateType());
			transactionHistoryPO.setSubmissionDate(transactionRequestPO.getSubmissionDate());
			transactionHistoryPO.setTradeAmount(transactionRequestPO.getTradeAmount());
			transactionHistoryPO.setTradeBuySellType(flipBuySell(object.getMBSRoleType(), transactionRequestPO.getTradeBuySellType()));
			transactionHistoryPO.setTradeCouponRate(transactionRequestPO.getTradeCouponRate());
			transactionHistoryPO.setTradeSettlementDate(transactionRequestPO.getTradeSettlementDate());
			LOGGER.debug("TransactionHistoryEnrichment transactionRequestPO " + transactionRequestPO.getTransReqId());
			if(!Objects.equal(tradePO, null)){
				transactionHistoryPO.setTradeDate(tradePO.getTradeDate());
				transactionHistoryPO.setTradeSrcId(tradePO.getTradeSourceId());
				transactionHistoryPO.setTradeSrcPrimaryId(tradePO.getTradeSourcePrimaryId());
				transactionHistoryPO.setTradeSubPortfolioId(tradePO.getTradeSubPortfolioId());
				transactionHistoryPO.setTradeSubPortfolioShortName(tradePO.getTradeSubPortfolioShortName());
				LOGGER.debug("TransactionHistoryEnrichment tradePO " + tradePO.getTradeSourcePrimaryId());
	
			}
			transactionHistoryPO.setLenderName(transactionRequestPO.getLenderName());
			transactionHistoryPO.setTraderName(transactionRequestPO.getTraderName());
			//CMMBSSTA01-1373 - Adding TSP name - Start
            transactionHistoryPO.setTspShortName(transactionRequestPO.getTspShortName());
            transactionHistoryPO.setLenderEntityName(transactionRequestPO.getDealerOrgName());
            //CMMBSSTA01-1373 - Adding TSP name - End
			LOGGER.debug("Exiting enrichToHistoryObject method in TransactionHistoryEnrichment");
			
		} catch (Exception ex) {
			LOGGER.error("Exception when enriching history info, enrichToHistoryObject", ex);
			throw new MBSSystemException("Exception when enriching history info, enrichToHistoryObject", MBSExceptionConstants.SYSTEM_EXCEPTION, ex);
		}
		return transactionHistoryPO;
	}
	
	/**
	 * 
	 * To flip status before presenting data to the caller
	 * 
	 * @param mbsRoleType the mbsRoleType
	 * @param stateType the stateType
	 * @return String
	 */
	private String flipBuySell(MBSRoleType mbsRoleType, String stateType) {
		//For Trader the status in history needs to be changed. For Lender the status is valid as is.
		if (MBSRoleType.TRADER.equals(mbsRoleType)){
			if (TradeBuySell.BID.toString().equals(stateType)) {
				return TradeBuySell.BUY.toString();
			} else if (TradeBuySell.OFFER.toString().equals(stateType)) {
				return TradeBuySell.SELL.toString();
			}
		}
		return stateType;
	}
}
