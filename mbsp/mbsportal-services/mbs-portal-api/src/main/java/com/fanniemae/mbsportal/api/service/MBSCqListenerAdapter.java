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

import java.rmi.dgc.VMID;
import java.util.List;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.query.CqAttributes;
import org.apache.geode.cache.query.CqAttributesFactory;
import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.apache.geode.cache.util.CqListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author g8uaxt Created on 12/5/2017.
 */
@Component
public class MBSCqListenerAdapter extends CqListenerAdapter {
        
        /**
         * 
         * LOGGER Logger variable
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(MBSCqListenerAdapter.class);
        
        /**
         * 
         * TRANSACTION_CQ_QUERY String
         */
        private static final String TRANSACTION_CQ_QUERY = "Select *  from /MBSTransaction where sourceSystem = '";
        
        /**
         * 
         * transactionEventService TransactionEventService
         */
        @Autowired
        TransactionEventService transactionEventService;
        
        /**
         * 
         * tradeServiceProperties TradeServiceProperties
         */
        @Autowired
        TradeServiceProperties tradeServiceProperties;
        
        /**
         * 
         * added for Junit set //TODO: find way to mock it
         * 
         * @param transactionEventService TransactionEventService
         * 
         */
        public void setTransactionEventService(TransactionEventService transactionEventService) {
                this.transactionEventService = transactionEventService;
        }
        
        /**
         * 
         * added for Junit set //TODO: find way to mock it
         * 
         * @param tradeServiceProperties TradeServiceProperties
         */
        public void setTradeServiceProperties(TradeServiceProperties tradeServiceProperties) {
                this.tradeServiceProperties = tradeServiceProperties;
        }
        
        
        /**
         * 
         * startCqListener
         * 
         * @throws CqException
         * @throws CqExistsException
         * @throws RegionNotFoundException
         * @throws InterruptedException
         */
        public void startCqListener()
                throws CqException, CqExistsException, RegionNotFoundException, InterruptedException, Exception {
                LOGGER.info("MBSCqListenerAdapter startCqListener called");
                //CMMBSSTA01-787: Added source system to query to filter blue/green
                //Move the query part to a constant
                String query = TRANSACTION_CQ_QUERY+tradeServiceProperties.getSourceSystem().toUpperCase()+"'";
                //CMMBSSTA01-787: End -change
                CqAttributesFactory cqAttributesFactory = new CqAttributesFactory();
                cqAttributesFactory.addCqListener(this);
                CqAttributes cqAttributes = cqAttributesFactory.create();
                String cqName = "TransactionListener" + new VMID().toString();
                QueryService queryService = getQueryService();
                queryService.newCq(cqName, query, cqAttributes).execute();
                //ClientCacheFactory.getAnyInstance().readyForEvents();
                //transactionEventService.scanStatusAndTimedOut();
                transactionEventService.scanStatusAndTimedOut();
                LOGGER.info("MBSCqListenerAdapter startCqListener readyForEvents: " + cqName);
        }
        
        /**
         * 
         * Added for jUnit mock
         * 
         * @return QueryService
         */
        public QueryService getQueryService() {
                return ClientCacheFactory.getAnyInstance().getQueryService();
        }
        
        /**
         * 
         * onEvent
         * 
         * @param aCqEvent CqEvent
         */
        public void onEvent(CqEvent aCqEvent) {
            if(aCqEvent.getBaseOperation() == Operation.NET_LOAD_UPDATE
                    || aCqEvent.getBaseOperation() == Operation.UPDATE
                    || aCqEvent.getBaseOperation() == Operation.CREATE) {

                MBSTransactionRequest obj = (MBSTransactionRequest) aCqEvent.getNewValue();
                LOGGER.info("MBSCqListenerAdapter onEvent called for key:" + aCqEvent.getKey() + " Lender:"
                        + obj.getCounterpartyTraderName()+ " State Type : "+obj.getStateType() +
                        " Trader : "+obj.getTraderName()+ "Active version : "+obj.getActiveVersion());
                try {
                    transactionEventService.processEvent(obj);
                } catch (Exception ex){
                    LOGGER.error("onEvent: with TxReq#" + obj.getTransReqNumber(), ex);
                }
            }
        }
    
        
        /**
         * 
         * onError
         * 
         * @param aCqEvent CqEvent
         * 
         */
        public void onError(CqEvent aCqEvent) {
                LOGGER.info("TransactionListener onError called" + aCqEvent.getQueryOperation());
        }
        
        /**
         * 
         * close method
         */
        public void close() {
                LOGGER.info("TransactionListener close called");
                ClientCacheFactory.getAnyInstance().close();
        }
        
        /**
         * 
         * Update the map with latest data
         * 
         * @throws MBSBaseException
         */
        public void refreshActiveTrades() throws MBSBaseException{
            List<MBSTransactionRequest>  mbsTransReqList = (List<MBSTransactionRequest>)transactionEventService.getActiveTransactions(tradeServiceProperties.getSourceSystem().toUpperCase());
            for(MBSTransactionRequest mbsTransactionRequest: mbsTransReqList){
                //transactionEventService.add((String)mbsTransactionRequest.getTransReqNumber(), mbsTransactionRequest);
                transactionEventService.add((String)mbsTransactionRequest.getTransReqNumber(), mbsTransactionRequest);
                transactionEventService.updateTransToCrossRef(mbsTransactionRequest);
            }
            transactionEventService.publishOnStartup();
        }
        
}
