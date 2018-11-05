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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;
import org.apache.geode.cache.query.SelectResults;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.gf.functions.MBSQueryFunction;
import com.fanniemae.mbsportal.gf.pojo.MBSFilter;
import com.fanniemae.mbsportal.gf.pojo.Page;
import com.fanniemae.mbsportal.gf.pojo.Sort;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.util.DAOUtils;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8uaxt Created on 12/14/2017.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MBSQueryFunctionDao extends MBSBaseDao<MBSTransactionRequest> {
        
        @Autowired
        RegionService cache;
        @InjectLog
        private Logger LOGGER;
        private String regionName = "MBSTransaction";
        
        @Autowired
        DAOUtils daoUtils;
        
        /**
         *
         */
        @Override
        public Region<String, MBSTransactionRequest> getStorageRegion() {
                LOGGER.debug("Storage region for Transaction request in MBSQueryFunctionDao class");
                return getBaseDaoWrapper().getCache().getRegion("MBSTransaction");
        }
        
        /**
         * This is post construct method where the region name is initialized
         */
        @PostConstruct
        public void initConfig() {
                LOGGER.debug("init called..MBSQueryFunctionDao" + cache + regionName);
                getBaseDaoWrapper().setCache(cache);
                getBaseDaoWrapper().setRegionName(regionName);
        }
        
        /**
         * 
         * CMMBSSTA01-1202 - Created method for the filtering story
         * 
         * @param filters
         * @param page
         * @param sort
         * @param limit
         * @return List<MBSTransactionRequest>
         */
        public List<MBSTransactionRequest> getTransactionHistoryDetails(List<MBSFilter> filters, Page page,
                Sort sort, Integer limit) {
        	try {
                String QUERY_FUNCTION = MBSQueryFunction.class.getName();
                for(MBSFilter filter: filters) {
            		LOGGER.debug("getTransactionHistoryDetails MBS Filter:" + filter.toString());
            	}
                //TODO: to be implemented
                /*Map<String,List<String>> fields = new HashMap<>();
                fields.put("transReqNumber","AJ0003");*/
                //for trade sort - workaround to be joined later
//                if(sort == null || sort.getFields().isEmpty()) {
                    if(sort == null || ArrayUtils.isEmpty(sort.getFields()) || StringUtils.isEmpty(sort.getFields()[0])) {
                        //default sort
                        LOGGER.warn("default sorting due to null " + sort);
                        sort = new Sort("transReqNumber", SortBy.desc);
                }
                String[] columns = sort.getFields();
                //CMMBSSTA01-1373 - Secondary Sort - Start
                boolean hasTransReqIdSort = false;
                //CMMBSSTA01-1373 - Secondary Sort - End
                
                if(!ArrayUtils.isEmpty(columns)) {
//                    for(int index = 0; index < columns.size(); index++) {
                        for(int index = 0; index < columns.length; index++) {
                            if(columns[index].equalsIgnoreCase("sourcePrimaryTradeId")) {
                            	columns[index] =  "transReqNumber";
                                    if(sort.getSortBy().getName().equalsIgnoreCase("asc")) {
                                            sort.setSortBy(SortBy.desc);
                                    } else {
                                            sort.setSortBy(SortBy.asc);
                                    }
                            }
                            //CMMBSSTA01-1373 - Secondary Sort - Start
                            if(columns[index].equalsIgnoreCase("transReqNumber")){
                                hasTransReqIdSort = true;
                            }
                          //CMMBSSTA01-1373 - Secondary Sort- End
                    }
            }
                        
                
                //CMMBSSTA01-1373 - Secondary Sort - Start
                if(!hasTransReqIdSort){
                	sort.addField("transReqNumber");
//                	((ArrayList<String>) sort.getFields()).add("transReqNumber");
                }
                //CMMBSSTA01-1373 - Secondary Sort - End

                
                //prepare args
                Serializable[] args = new Serializable[] { "TRANSACTION_HISTORY", (Serializable) filters, page, (Serializable) sort, limit };
                System.out.println("Before calling FunctionService...");
                //TODO: region or node?
                //Execution exec = FunctionService.onRegion(cache.getRegion(regionName)).withArgs(args);
                Execution exec = FunctionService.onServer(ClientCacheFactory.getAnyInstance().getDefaultPool())
                        .withArgs(args);
                LOGGER.debug("calling FunctionService exec..." + exec.toString());
                ResultCollector<?, ?> result = exec.execute(QUERY_FUNCTION);
                LOGGER.debug("result: " + result);
                List<MBSTransactionRequest> mbsTransactionRequests = new ArrayList<>();
                List<Object> selectedResults = (List<Object>) result.getResult();
                for(Object selectedResult : selectedResults) {
                        List<MBSTransactionRequest> sr = (List<MBSTransactionRequest>) selectedResult;
                        mbsTransactionRequests.addAll(sr);
                }
                return mbsTransactionRequests;
        	} catch (Exception e) {
        		e.printStackTrace();
        		throw e;
        	}
        }
        
        public List<MBSTransactionRequest> getTransactionHistoryDetails(List<MBSFilter> filters, Sort sort) {
        	List<MBSTransactionRequest> mbsTransactionRequests = new ArrayList<>();
        	String QUERY_FUNCTION = MBSQueryFunction.class.getName();
        	LOGGER.debug("QUERY_FUNCTION:::::" + QUERY_FUNCTION );
        	sort = getSortedOrder(sort);
        	
        	System.out.println("Sort by: " + sort.toString());
        	
        	for(MBSFilter filter: filters) {
        		System.out.println("MBS Filter:" + filter.toString());
        	}

        	Integer limit = -1;
        	Page page = new Page(limit,1);
        	
        	mbsTransactionRequests = getTransactionHistoryDetails(filters, page, sort, limit );
			return mbsTransactionRequests;
        }
        
        
        private Sort getSortedOrder(Sort sort) {
        	if(sort == null || sort.getFields() == null) {
                //default sort
                LOGGER.warn("default sorting due to null " + sort);
                sort = new Sort("submissionDate", SortBy.asc);
        	}
        	return sort;
        }
}
