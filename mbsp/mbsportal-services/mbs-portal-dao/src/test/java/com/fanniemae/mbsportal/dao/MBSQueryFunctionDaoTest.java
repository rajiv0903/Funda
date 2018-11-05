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

package com.fanniemae.mbsportal.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.gf.pojo.MBSFilter;
import com.fanniemae.mbsportal.gf.pojo.MBSOperator;
import com.fanniemae.mbsportal.gf.pojo.Page;
import com.fanniemae.mbsportal.gf.pojo.Sort;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8uaxt Created on 12/15/2017.
 */
@Ignore
public class MBSQueryFunctionDaoTest extends IntegrationDaoTest {
        @Autowired
        MBSQueryFunctionDao mbsQueryFunctionDao;
        @InjectLog
        private Logger LOGGER;
        
        @Test
        public void getExportTransactionHistoryDetails() {
        	List<MBSFilter> mbsFilterLst = new ArrayList<>();
            mbsFilterLst.add(new MBSFilter("stateType", Arrays.asList("LENDER_REJECTED"), MBSOperator.IN));
            mbsFilterLst.add(new MBSFilter("lenderShortName", Arrays.asList("TEST-C"), MBSOperator.NOT_IN));
            Date startDate = MBSPortalUtils.addDays(MBSPortalUtils.getCurrentDate(), -90);
            Date endDate = MBSPortalUtils.addDays(MBSPortalUtils.getCurrentDate(), -30);
            List<String> subDateLst = new ArrayList<>();
            List<String> endDateLst = new ArrayList<>();
            Page page = new Page(2000, 1);
            try {
                subDateLst.add(MBSPortalUtils.convertDateToString(startDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
                endDateLst.add(MBSPortalUtils.convertDateToString(endDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
            } catch (MBSBaseException e) {
                e.printStackTrace();
            }
            mbsFilterLst.add(new MBSFilter("submissionDate", subDateLst, MBSOperator.GREATER_THAN_DATE));
            mbsFilterLst.add(new MBSFilter("submissionDate", endDateLst, MBSOperator.LESSER_THAN_DATE));

            Sort sort = new Sort("submissionDate", SortBy.desc);
            List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao.getTransactionHistoryDetails(mbsFilterLst, sort);
            System.out.println("Results size:" + listMBSTransReq.size());
            Assert.assertNotNull(listMBSTransReq);
        }
        
        @Test
        public void getTransactionHistoryForRequest() {
                //System.out.println("test::"+SortBy.getEnum("desc"));
                List<MBSFilter> mbsFilterLst = new ArrayList<>();
                mbsFilterLst.add(new MBSFilter("stateType", Arrays.asList("LENDER_REJECTED"), MBSOperator.IN));
                mbsFilterLst.add(new MBSFilter("counterpartyTraderIdentifier", Arrays.asList("p3hbrmxe1"), MBSOperator.IN));
                Date cutOffDate = MBSPortalUtils.addDays(MBSPortalUtils.getCurrentDate(), -90);
                List<String> subDateLst = new ArrayList<>();
                try {
                    subDateLst.add(MBSPortalUtils.convertDateToString(cutOffDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
                } catch (MBSBaseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mbsFilterLst.add(new MBSFilter("submissionDate", subDateLst, MBSOperator.GREATER_THAN_DATE));
                Page page = new Page(20, 3);
                Sort sort = new Sort("tradeBuySellType", SortBy.asc);
                Integer limit = new Integer(10);
                List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao
                        .getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
                Assert.assertNotNull(listMBSTransReq);
                //LOGGER.debug("listMBSTransReq size" + listMBSTransReq.size());
        }
        
        @Test
        public void getTransactionHistoryForRequest_tradeAmount() {
            List<MBSFilter> mbsFilterLst = new ArrayList<>();
            mbsFilterLst.add(new MBSFilter("stateType", Arrays.asList("LENDER_REJECTED", "TRADER_TIMEOUT"), MBSOperator.IN));
            mbsFilterLst.add(new MBSFilter("counterpartyTraderIdentifier", Arrays.asList("p3hbrmxe", "p3hbrmxe1"), MBSOperator.IN));
            Date cutOffDate = MBSPortalUtils.addDays(MBSPortalUtils.getCurrentDate(), -90);
            List<String> subDateLst = new ArrayList<>();
            try {
                subDateLst.add(MBSPortalUtils.convertDateToString(cutOffDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
            } catch (MBSBaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mbsFilterLst.add(new MBSFilter("submissionDate", subDateLst, MBSOperator.GREATER_THAN_DATE));
            Page page = new Page(20, 3);
            Sort sort = new Sort("tradeAmount", SortBy.desc);
            Integer limit = new Integer(10);
            List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao
                    .getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
            Assert.assertNotNull(listMBSTransReq);
        }
        
        @Test
        public void getTransactionHistoryForRequestAsc() {
                
            List<MBSFilter> mbsFilterLst = new ArrayList<>();
            mbsFilterLst.add(new MBSFilter("stateType", Arrays.asList("LENDER_REJECTED", "TRADER_TIMEOUT"), MBSOperator.IN));
            mbsFilterLst.add(new MBSFilter("counterpartyTraderIdentifier", Arrays.asList("p3hbrmxe", "p3hbrmxe1"), MBSOperator.IN));
            Date cutOffDate = MBSPortalUtils.addDays(MBSPortalUtils.getCurrentDate(), -90);
            List<String> subDateLst = new ArrayList<>();
            try {
                subDateLst.add(MBSPortalUtils.convertDateToString(cutOffDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
            } catch (MBSBaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mbsFilterLst.add(new MBSFilter("submissionDate", subDateLst, MBSOperator.GREATER_THAN_DATE));
            Page page = new Page(20, 3);
            Sort sort = new Sort("submissionDate", SortBy.asc);
            Integer limit = new Integer(10);
            List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao
                    .getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
            Assert.assertNotNull(listMBSTransReq);
        }
        
        @Test
        public void getTransactionHistoryForRequestDesc() {
            
            List<MBSFilter> mbsFilterLst = new ArrayList<>();
            mbsFilterLst.add(new MBSFilter("stateType", Arrays.asList("LENDER_REJECTED", "TRADER_TIMEOUT"), MBSOperator.IN));
            mbsFilterLst.add(new MBSFilter("counterpartyTraderIdentifier", Arrays.asList("p3hbrmxe", "p3hbrmxe1"), MBSOperator.IN));
            Date cutOffDate = MBSPortalUtils.addDays(MBSPortalUtils.getCurrentDate(), -90);
            List<String> subDateLst = new ArrayList<>();
            try {
                subDateLst.add(MBSPortalUtils.convertDateToString(cutOffDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
            } catch (MBSBaseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mbsFilterLst.add(new MBSFilter("submissionDate", subDateLst, MBSOperator.GREATER_THAN_DATE));
            Page page = new Page(20, 3);
            Sort sort = new Sort("submissionDate", SortBy.desc);
            Integer limit = new Integer(10);
            List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao
                    .getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
            Assert.assertNotNull(listMBSTransReq);
        }
        
        @Test
        public void getTransactionHistoryForRequestNoFilterDesc() {
                List<MBSFilter> mbsFilterLst = new ArrayList<>();
                Page page = new Page(20, 3);
                Sort sort = new Sort("tradeBuySellType", SortBy.desc);
                Integer limit = new Integer(10);
                List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao
                        .getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
                Assert.assertNotNull(listMBSTransReq);
        }
        @Test
        public void getTransactionHistoryForRequestNoSort() {
                List<MBSFilter> mbsFilterLst = new ArrayList<>();
                Page page = new Page(20, 3);
//                Sort sort = new Sort();
//                sort.setSortBy(SortBy.asc);
//                sort.setFields(null);
                Sort sort = new Sort(null,SortBy.asc);
                //Sort sort = new Sort(new String[] { "tradeBuySellType" }, SortBy.desc);
                Integer limit = new Integer(10);
                List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao
                        .getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
                Assert.assertNotNull(listMBSTransReq);
        }
        @Test
        public void getTransactionHistoryForRequestINVsortDesc() {
                List<MBSFilter> mbsFilterLst = new ArrayList<>();
                Page page = new Page(20, 3);
                Sort sort = new Sort("sourcePrimaryTradeId", SortBy.desc);
                //Sort sort = new Sort(new String[] { "tradeBuySellType" }, SortBy.desc);
                Integer limit = new Integer(10);
                List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao
                        .getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
                Assert.assertNotNull(listMBSTransReq);
        }
        @Test
        public void getTransactionHistoryForRequestINVsortAsc() {
                List<MBSFilter> mbsFilterLst = new ArrayList<>();
                Page page = new Page(20, 3);
                Sort sort = new Sort("sourcePrimaryTradeId", SortBy.asc);
                //Sort sort = new Sort(new String[] { "tradeBuySellType" }, SortBy.desc);
                Integer limit = new Integer(10);
                List<MBSTransactionRequest> listMBSTransReq = mbsQueryFunctionDao
                        .getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
                Assert.assertNotNull(listMBSTransReq);
        }
}
