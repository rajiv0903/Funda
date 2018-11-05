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


package com.fanniemae.mbsportal.constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: StateType Purpose : This enum has the different statuses of the
 * trade
 *
 * @author g8upjv
 *
 */
public enum StateType implements Serializable {
        // Lender requests for trade.
        LENDER_OPEN(1, 7, ""), // Trader submits the request for pricing
        TRADER_PRICED(2, 8, ""), // Trader priced for lender request
        TRADER_PASSED(3, 4, "FM Passed"), // Trader passes the trade
        TRADER_REPRICED(4, 9, ""), // Trader submits the request for re-pricing
        LENDER_ACCEPTED(5, 10, ""), // Lender accepts the trade (hit/lift)
        LENDER_REJECTED(6, 2, "Cancelled"), //Lender rejects the trade (reject/cxl)
        TRADER_CONFIRMED(7, 1, "Accepted"), // Trader final acknowledgement after trader confirms
        TRADER_REJECTED(8, 3, "FM Cancelled"), // Rejected by Trader
        EXECUTION_IN_PROGRESS(9, 1, "Accepted"),//Intermediate state of trading
        PENDING_EXECUTION(10, 1, "Accepted"), // Send Successfully to Aladdin/TradeService
        EXECUTED(11, 1, "Accepted"), // Status after receiving acknowledgement from Aladdin/TradeService
        LENDER_TIMEOUT(12, 6, "Timed Out"), // The trade expires before the trader action
        TRADER_TIMEOUT(13, 5, "FM Timed Out"), // The trade expires before lender action
        ERROR(14, 11, "");//Any error or exception scenario
        
	
        private int step = 0;
        private int sortOrder = 0;
        private String displayName = "";
        
        StateType(int step, int sortOrder, String displayName) {
                this.step = step;
                this.sortOrder = sortOrder;
                this.displayName = displayName;
        }
        
        /**
         *
         * @param status
         * @return
         */
        public static String getStateType(String status) {
                
                for(StateType stateType : StateType.values()) {
                        if(stateType.toString().equals(status)) {
                                return stateType.toString();
                        }
                }
                return "";
        }
        
        /**
         * create enum based on status
         *
         * @param status
         * @return
         */
        public static StateType getEnum(String status) {
                
                for(StateType stateType : StateType.values()) {
                        if(stateType.toString().equals(status)) {
                                return stateType;
                        }
                }
                return null;
        }
        
        /**
         * Return enum list based on the different flows
         * @param flow
         * @return
         */
        public static List<StateType> getFlowList(String flow) {
                List<StateType> stateLst = new ArrayList<>();
                switch(flow.toUpperCase()) {
                case "LENDER_FLOW": {
                        stateLst.add(LENDER_OPEN);
                        stateLst.add(TRADER_PRICED);
                        stateLst.add(LENDER_ACCEPTED);
                        stateLst.add(TRADER_REPRICED);
                        break;
                }
                case "TRADER_FLOW": {
                        stateLst.add(LENDER_OPEN);
                        stateLst.add(TRADER_PRICED);
                        stateLst.add(LENDER_ACCEPTED);
                        stateLst.add(TRADER_REPRICED);
                        break;
                }
                case "HISTORY_FLOW": {
                        stateLst.add(LENDER_REJECTED);
                        stateLst.add(TRADER_PASSED);
                        stateLst.add(TRADER_REJECTED);
                        stateLst.add(TRADER_CONFIRMED);
                        stateLst.add(EXECUTION_IN_PROGRESS);
                        stateLst.add(PENDING_EXECUTION);
                        stateLst.add(EXECUTED);
                        stateLst.add(LENDER_TIMEOUT);
                        stateLst.add(TRADER_TIMEOUT);
                        break;
                }
                case "HISTORY_FLOW_ACCEPTED": {
                        stateLst.add(TRADER_CONFIRMED);
                        stateLst.add(EXECUTION_IN_PROGRESS);
                        stateLst.add(PENDING_EXECUTION);
                        stateLst.add(EXECUTED);
                        break;
                }
                default: {
                        stateLst.add(LENDER_OPEN);
                        stateLst.add(TRADER_PRICED);
                        stateLst.add(TRADER_PASSED);
                        stateLst.add(LENDER_ACCEPTED);
                        stateLst.add(LENDER_REJECTED);
                        stateLst.add(TRADER_REPRICED);
                        stateLst.add(TRADER_CONFIRMED);
                        stateLst.add(TRADER_REJECTED);
                        stateLst.add(EXECUTION_IN_PROGRESS);
                        stateLst.add(PENDING_EXECUTION);
                        stateLst.add(EXECUTED);
                        stateLst.add(LENDER_TIMEOUT);
                        stateLst.add(TRADER_TIMEOUT);
                        break;
                }
                }
                return stateLst;
        }
        
        public int getStep() {
                return step;
        }
        
        public void setStep(int step) {
                this.step = step;
        }
        
        public int getSortOrder() {
                return sortOrder;
        }
        
        public String getDisplayName() {
			return displayName;
		}
        
        /**
         * Check the workflow status
         * @param currentType
         * @return
         */
        public boolean isValidState(StateType currentType) {
                switch(this) {
                case LENDER_OPEN:
                case TRADER_PRICED:
                case TRADER_PASSED:
                        return (currentType.equals(LENDER_OPEN));
                case LENDER_ACCEPTED:
                        return (currentType.equals(TRADER_PRICED) || currentType.equals(TRADER_REPRICED));
                case TRADER_REPRICED:
                        return (currentType.equals(TRADER_PRICED) || currentType.equals(LENDER_ACCEPTED) || currentType
                                .equals(TRADER_REPRICED));
                case LENDER_REJECTED:
                        return (currentType.equals(TRADER_PRICED) || currentType.equals(LENDER_OPEN) || currentType
                                .equals(TRADER_REPRICED) || currentType.equals(LENDER_ACCEPTED));
                case TRADER_REJECTED:
                        return (currentType.equals(TRADER_PRICED) || currentType.equals(LENDER_ACCEPTED) || currentType
                                .equals(TRADER_REPRICED));
                case TRADER_CONFIRMED:
                        return (currentType.equals(LENDER_ACCEPTED));
                case PENDING_EXECUTION:
                        return (currentType.equals(TRADER_CONFIRMED));
                case EXECUTED:
                        return (currentType.equals(PENDING_EXECUTION));
                case LENDER_TIMEOUT:
                case TRADER_TIMEOUT:
                case ERROR:
                        //TODO: Later when developing for other state type
                        return false;
                default:
                        return false;
                }
        }
        
}
