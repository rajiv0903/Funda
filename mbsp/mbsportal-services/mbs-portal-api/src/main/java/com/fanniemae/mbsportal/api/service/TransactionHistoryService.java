/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.utils.ExportUtils;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.enrichment.Enricher;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * This class handles the service part for the history trade requests
 *
 * @author g8upjv
 *
 */

@SuppressWarnings("rawtypes")
@Service
public class TransactionHistoryService extends BaseProcessor {
    
    /**
     * 
     * profileEntitlementService ProfileEntitlementService
     */
    @Autowired
    ProfileEntitlementService profileEntitlementService;
    
    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * transactionRequestService TransactionRequestService
     */
    @Autowired
    private TransactionRequestService transactionRequestService;
    
    /**
     * transactionExportService TransactionExportService
     */
    @Autowired
    private TransactionExportService transactionExportService;
    
    /**
     * 
     * tradeService TradeService
     */
    @Autowired
    private TradeService tradeService;
    
    /**
     * 
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
    MBSObjectCreator mbsObjectCreator;
    
    /**
     * 
     * tradeServiceProperties TradeServiceProperties
     */
    @Autowired
    TradeServiceProperties tradeServiceProperties;
    
    /**
     * 
     * NO_TRANSACTIONS String error message
     */
    private String NO_TRANSACTIONS = "There are no transaction requests in Gemfire data";
    
    /**
     * 
     * NO_TRADE String error message
     */
    private String NO_TRADE = "There are no trade request in Gemfire data for executed trade for trans id ";
    

    /*
     * @Autowired
     * 
     * @Qualifier("transactionRequestPOTransformer") private Transformer
     * poTransformer;
     */

    /**
     * 
     * @param transactionHistoryEnrichment Enricher
     */
    @SuppressWarnings("unchecked")
    @Autowired
    public TransactionHistoryService(Enricher transactionHistoryEnrichment) {
        super.enricher = transactionHistoryEnrichment;
    }
   
    /**
     * 
     * This method gets the records for showing in history screen using sort params
     * 
     * @param mbsRoleType MBSRoleType
     * @param sellerServiceNumber the sellerServiceNumber
     * @param sortBy the sortBy
     * @param sortOrder the sortOrder
     * @param pageIndex the pageIndex
     * @param pageSize the pageSize
     * @return List<TransactionHistoryPO>
     * @throws MBSBaseException
     */
    public List<TransactionHistoryPO> getTransactionRequestSorted(MBSRoleType mbsRoleType, String sellerServiceNumber, boolean acceptedTrades,
            String sortBy, String sortOrder, Integer pageIndex, Integer pageSize) throws MBSBaseException {

        LOGGER.debug("Entering getTransactionRequest method in TransactionHistoryService");
        List<TransactionHistoryPO> transHistPOLst = new ArrayList<TransactionHistoryPO>();
        List<StateType> stateTypeLst;
        if(acceptedTrades){
            stateTypeLst = StateType.getFlowList("HISTORY_FLOW_ACCEPTED");
        } else {
            stateTypeLst = StateType.getFlowList("HISTORY_FLOW");
        }
        List<TransactionRequestPO> transReqLst = transactionRequestService.getMBSTransReqHistory(mbsRoleType,
                stateTypeLst, sellerServiceNumber, sortBy, sortOrder, pageIndex, pageSize);
        if (Objects.equals(transReqLst, null) || transReqLst.size() == 0) {
            LOGGER.debug(NO_TRANSACTIONS);
            return transHistPOLst;
        }
        populateTransHistDetails(transHistPOLst, transReqLst, mbsRoleType);

        LOGGER.debug("Exiting getTransactionRequest method in TransactionHistoryService");
        return transHistPOLst;
    }
    
    /**
     * 
     * 
     * @param transHistPOLst
     * @param transReqLst
     * @param mbsRoleType
     * @throws MBSBaseException
     */
     public void populateTransHistDetails(List<TransactionHistoryPO> transHistPOLst, List<TransactionRequestPO> transReqLst, MBSRoleType mbsRoleType) throws MBSBaseException{
         LOGGER.debug("Starts populateTransHistDetails() method");
         List<TradePO> tradePOLst;
         TransformationObject transObj;
         TransactionHistoryPO transactionHistoryPO;
         boolean filterRequest;
         List<String> transReqIds = new ArrayList<>();
         for (TransactionRequestPO transactionRequestPO : transReqLst) {
        	 transReqIds.add(transactionRequestPO.getTransReqId());
         }
		List<TradePO> mbsTrades;
		try {
			 mbsTrades = tradeService.getMBSTrades(transReqIds);
		} catch (MBSBaseException e) {
			 mbsTrades = new ArrayList<>();
		}

         for (TransactionRequestPO transactionRequestPO : transReqLst) {
             transObj = mbsObjectCreator.getTransformationObject();
             transObj.setMBSRoleType(mbsRoleType);
//             filterRequest = getProfilesAndFilter(transObj, transactionRequestPO);
             //Filter shakeout trades
//             if(!filterRequest){
	             if (transactionRequestPO.getStateType().equals(StateType.EXECUTED)) {
	            	 
	                 // Get the trade details
	            	 tradePOLst = getMBSTrade(transactionRequestPO.getTransReqId(), mbsTrades);
	                 // TODO: move it to GF server function
//	                 tradePOLst = tradeService.getMBSTrade(transactionRequestPO.getTransReqId());
	                 if (Objects.equals(tradePOLst, null) || tradePOLst.size() < 1) {
	                     LOGGER.error(NO_TRADE + transactionRequestPO.getTransReqId());
	                     throw new MBSBusinessException(NO_TRADE, MBSExceptionConstants.BUSINESS_EXCEPTION);
	                 } else {
	                     transObj.getTransformationDataMap().put(MBSPServiceConstants.TRADE_REQUEST, tradePOLst.get(0));
	                 }
	             } else {
	                 transObj.getTransformationDataMap().put(MBSPServiceConstants.TRADE_REQUEST, null);
	             }
	             transObj.getTransformationDataMap().put(MBSPServiceConstants.TRANSACTION_REQUEST, transactionRequestPO);
                 getTransHistoryEnricher().enrich(transObj);
                 transactionHistoryPO = (TransactionHistoryPO) transObj.getTargetPojo();
                 transHistPOLst.add(transactionHistoryPO);
             
         }
         LOGGER.debug("Ends populateTransHistDetails() method");
         
     }

     /**
      * 
      * @param transReqId
      * @param mbsTrades
      * @return
      */
	private List<TradePO> getMBSTrade(String transReqId, List<TradePO> mbsTrades) {
		if (mbsTrades == null) {
			return null;
		}
		List<TradePO> trades = new ArrayList<>();
		for (TradePO trade : mbsTrades) {
			if (trade.getTransactionRequestId().equals(transReqId)) {
				trades.add(trade);
			}
		}
		return trades;
	}

	/**
      * 
      * @param transObj TransformationObject
      * @param transactionRequestPO TransactionRequestPO
      * @return boolean
      */
    public boolean getProfilesAndFilter(TransformationObject transObj, TransactionRequestPO transactionRequestPO){
     // Get the Lender/Trader profile details
        boolean toBeFiltered = false;
        if (StringUtils.isNotEmpty(transactionRequestPO.getLenderId())) {
        	if (transactionRequestPO.getLenderEntityShortName().equalsIgnoreCase(tradeServiceProperties.getShakeOutCPartyName())) {
        		return true;
        	}
        }
        return toBeFiltered;
    }

	/**
     * 
     * Returns the enricher object set in the constructor
     * 
     * @return Enricher
     */
    public Enricher getTransHistoryEnricher(){
        return enricher;
    }

    /**
     * gets the transaction history data in the form of a binary as indicated by the export type.   
     * 
     * @param mbsRoleType
     * @param sellerServiceNumber
     * @param params
     * @return
     * @throws MBSBaseException
     */
    public byte[] getTransactionRequestExportSorted(MBSRoleType mbsRoleType, String sellerServiceNumber, Map<String, String> params) throws MBSBaseException {
        LOGGER.debug("Entering getTransactionRequestExportSorted method in TransactionHistoryService");

    	boolean acceptedTrades = params.get("acceptedTrades").equals("true");
        String sortBy = params.get("sortBy");
    	String sortOrder = params.get("sortOrder");
    	String dateStart = params.get("dateStart");
    	String dateEnd = params.get("dateEnd");
    	String exportType = params.get("exportType");
    	String dateType = params.get("dateType");
    	String userName = params.get("userName");
    	String orgName = params.get("org");
        List<TransactionHistoryPO> transHistPOLst = new ArrayList<TransactionHistoryPO>();
        List<StateType> stateTypeLst;
        if(acceptedTrades){
            stateTypeLst = StateType.getFlowList("HISTORY_FLOW_ACCEPTED");
        } else {
            stateTypeLst = StateType.getFlowList("HISTORY_FLOW");
        }
        List<TransactionRequestPO> transReqLst = transactionExportService.getMBSTransReqHistory(mbsRoleType,
                stateTypeLst, sellerServiceNumber, sortBy, sortOrder, dateStart, dateEnd, dateType);
        
        
        
        
        if (!Objects.equals(transReqLst, null) && transReqLst.size() != 0) {
            populateTransHistDetails(transHistPOLst, transReqLst, mbsRoleType);
        } else {
            LOGGER.debug(NO_TRANSACTIONS);
        }
        LOGGER.debug("Exiting getTransactionRequest method in TransactionHistoryService");
        if (exportType.equals("CSV")) {
        	return ExportUtils.getCSVFromHistory(transHistPOLst, 
        			MBSPortalUtils.convertToDateWithFormatter(dateStart, DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
        			MBSPortalUtils.convertToDateWithFormatter(dateEnd, DateFormats.DATE_FORMAT_NO_TIMESTAMP),
        			userName, mbsRoleType, orgName); 
        } else if (exportType.equals("EXCEL")) {
        	return ExportUtils.getXLFromHistory(transHistPOLst, 
        			MBSPortalUtils.convertToDateWithFormatter(dateStart, DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
        			MBSPortalUtils.convertToDateWithFormatter(dateEnd, DateFormats.DATE_FORMAT_NO_TIMESTAMP),
        			userName, mbsRoleType, orgName);
        } else {
        	return new byte[0];
        }
    }

    /**
     * 
     * @param mbsRoleType
     * @param sellerServiceNumber
     * @param acceptedTrades
     * @return
     * @throws MBSBaseException
     */
	public Integer getTransactionRequestSortedSize(MBSRoleType mbsRoleType, String sellerServiceNumber,
			boolean acceptedTrades) throws MBSBaseException {
        List<StateType> stateTypeLst;
        if(acceptedTrades){
            stateTypeLst = StateType.getFlowList("HISTORY_FLOW_ACCEPTED");
        } else {
            stateTypeLst = StateType.getFlowList("HISTORY_FLOW");
        }
		return transactionExportService.getMBSTransReqHistorySize(mbsRoleType, stateTypeLst, sellerServiceNumber);
	}
}
