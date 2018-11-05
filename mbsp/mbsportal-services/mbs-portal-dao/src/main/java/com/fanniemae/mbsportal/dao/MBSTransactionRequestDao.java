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
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.apache.geode.cache.client.Pool;
import org.apache.geode.cache.client.PoolManager;
import org.apache.geode.cache.query.FunctionDomainException;
import org.apache.geode.cache.query.NameResolutionException;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryInvocationTargetException;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.gf.pojo.MBSFilter;
import com.fanniemae.mbsportal.gf.pojo.MBSOperator;
import com.fanniemae.mbsportal.model.MBSEvent;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;

/**
 * This class is the DAO for the Transaction Request domain class
 * <p>
 *
 * @author g8upjv Initial version. - 07/20/2017
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 20, 2017
 * @Time 3:28:00 PM com.fanniemae.mbsportal.dao MBSTransactionRequestDao.java
 * @Description: For Polling changes
 */
@Service
public class MBSTransactionRequestDao extends MBSBaseDao<MBSTransactionRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MBSTransactionRequestDao.class);

    @Autowired
    private IDServiceDao idServiceDao;

    @Autowired
    private MBSEventDao mBSEventDao;

    @Autowired
    RegionService cache;
    private String regionName = "MBSTransaction";

    /**
     *
     */
    @Override
    public Region<String, MBSTransactionRequest> getStorageRegion() {
        LOGGER.debug("Storage region for Transaction request in MBSTransactionRequestDao class");
        return getBaseDaoWrapper().getCache().getRegion("MBSTransaction");
    }

    /**
     * This is post construct method where the region name is initialized
     */
    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSTransactionRequestDao" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }
    
    /**
     * get list of records based on key
     * @param keys
     * @return
     */
    public List<MBSTransactionRequest> getAll(List<String> keys) throws MBSBaseException {
        
        List<MBSTransactionRequest> result = new ArrayList<>();
        for(String key : keys) {
            LOGGER.debug("getAll: going to get key: " +key);
            result.add((MBSTransactionRequest)getTransReqByTransReqNumber(key));
        }
        LOGGER.debug("MBSTransactionRequestDao getAll List: " + result);
        return result;
    }
    
    /**
     * Update list of records
     * @param objList
     * @throws MBSBaseException
     */
    public void updateAll(List<MBSTransactionRequest> objList) throws MBSBaseException {
        //TODO: putall
        super.putAll(objList);
        
    }
    
    /**
     * This overridden method does the save or update.
     * 
     * @param obj
     */
    @Override
    public void saveOrUpdate(MBSTransactionRequest obj) throws MBSBaseException {
        if (Objects.equals(obj.getTransReqNumber(), null)) {
            LOGGER.debug("going for generting seqId from gemfire {} in MBSTransactionRequestDao class", obj.getId());
            String seqId = idServiceDao.getSeqId(DAOConstants.IDTypes.TRANSACTION_ID);
            if (null == seqId || "".equals(seqId)) {
                LOGGER.error("seqId from gemfire {} in {} class", seqId, MBSTransactionRequestDao.class);
                throw new MBSDataAccessException("Seq Id is null for " + obj);
            }
            LOGGER.debug("seqId from gemfire {} in MBSTransactionRequestDao class", seqId);
            obj.setTransReqNumber(seqId);
        }
        /*
         * CMMBSSTA01-1022: (Tech) Maintain versions for transaction request
         * Increase the version by 1 - We need to increase the version even if
         * Timed Out or Executed Work only with Transaction having version info
         */
        Long existingValueAtSystem = obj.getActiveVersion();
        if (!Objects.isNull(existingValueAtSystem)) {

            obj.setActiveVersion(obj.getActiveVersion() + 1);
        }
        super.saveOrUpdate(obj);
       
        LOGGER.debug("saved in gemfire obj id {} in MBSTransactionRequestDao class", obj.getId());

    }

    /**
     * @Rajiv Start Saving the Events
     */
    public void saveEvents(MBSTransactionRequest obj, String mBSRole) throws MBSBaseException {

        Long currentTimeStamp = MBSPortalUtils.getLocalDateCurrentTimeStamp();
        if (StringUtils.isNotEmpty(mBSRole)) {
            // If Trader
            if ("trader".equalsIgnoreCase(mBSRole)) {
                mBSEventDao.saveOrUpdate(new MBSEvent(obj.getCounterpartyTraderIdentifier(), currentTimeStamp));
                // For Trader Event - Other Trader Should be able to see the
                // Event
                mBSEventDao.saveOrUpdate(new MBSEvent(TradeConstants.TRADE_EVENT_USRE_NAME, currentTimeStamp));
            } else {
                // If Lender then Only visible to Trader
                mBSEventDao.saveOrUpdate(new MBSEvent(TradeConstants.TRADE_EVENT_USRE_NAME, currentTimeStamp));
            }
        } else {
            // If System Event Generate for both Trader and Lender
            mBSEventDao.saveOrUpdate(new MBSEvent(obj.getCounterpartyTraderIdentifier(), currentTimeStamp));
            mBSEventDao.saveOrUpdate(new MBSEvent(TradeConstants.TRADE_EVENT_USRE_NAME, currentTimeStamp));
        }
    }

    /**
     * Get the List of transaction request by stateType and lender id
     * 
     * @param stateTypeLst
     * @return
     */
    public List<MBSTransactionRequest> getTransReqStateType(List<String> stateTypeLst, List<String> userIdLst,
            String orderType, String sourceSystem) throws MBSBaseException {
        StringBuilder queryString = new StringBuilder();
        int counter = 0;
        List<String> params = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userIdLst)) {
            queryString.append("counterpartyTraderIdentifier in SET(");
            for (int k = 0; k < userIdLst.size(); k++) {
                queryString.append("$").append(k + 1);
                counter++;
                if (k < userIdLst.size() - 1) {
                    queryString.append(",");
                }
            }
            params.addAll(userIdLst);
            queryString.append(") and ");
        }
        queryString.append("stateType in SET(");
        int size = stateTypeLst.size() + counter;
        for (int i = counter; i < size; i++) {
            queryString.append("$").append(i + 1);
            if (i < size - 1) {
                queryString.append(",");
            }
        }
        queryString.append(")");
        //CMMBSSTA01-787: Validate if the source system matches
        if(StringUtils.isNotEmpty(sourceSystem)){
            queryString.append(" and sourceSystem = '"+sourceSystem+"' ");
        }
        //CMMBSSTA01-787: End -change
        queryString.append(" order by submissionDate ");
        if (StringUtils.isNotEmpty(orderType)) {
            queryString.append(orderType.toLowerCase());
        }
        params.addAll(stateTypeLst);
        List<MBSTransactionRequest> transReqLst = new ArrayList<MBSTransactionRequest>();

        SelectResults<MBSTransactionRequest> tdsEntities = (SelectResults<MBSTransactionRequest>) super.query(
                queryString.toString(), params.toArray());
        if (CollectionUtils.isNotEmpty(tdsEntities)) {
            transReqLst = tdsEntities.asList();
        }
        return transReqLst;
    }
    
    /**
     * Get the List of transaction request by transReqNumber
     * 
     * @param transReqNumber
     * @return MBSTransactionRequest
     */
    public MBSTransactionRequest getTransReqByTransReqNumber(String transReqNumber) throws MBSBaseException {
        String queryString = "transReqNumber = $1";
        MBSTransactionRequest mbsTransactionRequest = null;
        List<MBSTransactionRequest> transReqLst = new ArrayList<MBSTransactionRequest>();
        if(StringUtils.isNotEmpty(transReqNumber)){
            SelectResults<MBSTransactionRequest> tdsEntities = (SelectResults<MBSTransactionRequest>) super.query(queryString, transReqNumber);
            if (CollectionUtils.isNotEmpty(tdsEntities)) {
                transReqLst = tdsEntities.asList();
            }
            if (CollectionUtils.isNotEmpty(transReqLst)) {
                mbsTransactionRequest = transReqLst.get(0);
            }
        } else {
            return null;
        }
        
        return mbsTransactionRequest;
    }
    
    /**
     * CMMBSSTA01-1108 : API- Prevent Concurrent Request from Same Lender
     * @author Rajiv Chaudhuri
     * @param mBSTransactionRequest The Trade Request Details
     * @return List of Existing Trade Request that may come because of concurrency issue 
     * @throws MBSBaseException 
     */
    public List<MBSTransactionRequest> getConcurrentLenderTradeRequest (MBSTransactionRequest mBSTransactionRequest) throws MBSBaseException {
        
        LOGGER.debug("getConcurrentLenderTradeRequest:", mBSTransactionRequest.toString());
        
        StringBuilder queryString = new StringBuilder();
        
        //queryString.append("dealerOrgName.toLowerCase() = ($1) ");
        //CMMBSSTA01-1119: Check for multiple trades request with same product, coupon amount should be at user level
        queryString.append("counterpartyTraderIdentifier.toLowerCase() = ($1) ");
        queryString.append("and counterPartyBuySellType = ($2) ");
        queryString.append("and tradeCouponRate = ($3) ");
        queryString.append("and tradeAmount = ($4) ");
        queryString.append("and stateType = ($5) ");
        queryString.append("and productId.identifier  = ($6) ");
        queryString.append("and productId.sourceType = ($7) ");
//        queryString.append("and productId.type = ($8) ");//Syntax error in query- unexpected token- type
        
        Object[] params = { 
                mBSTransactionRequest.getCounterpartyTraderIdentifier().toLowerCase(),
                //mBSTransactionRequest.getDealerOrgName().toLowerCase(), 
                mBSTransactionRequest.getCounterPartyBuySellType(),
                mBSTransactionRequest.getTradeCouponRate(), 
                mBSTransactionRequest.getTradeAmount(), 
                mBSTransactionRequest.getStateType(), 
                mBSTransactionRequest.getProductId().getIdentifier(),
                mBSTransactionRequest.getProductId().getSourceType()}; 
               // obj.getProductId().getType() }; //Syntax error in query- unexpected token- type
        
        LOGGER.debug("getConcurrentLenderTradeRequest: queryString: ", queryString.toString());
        LOGGER.debug("getConcurrentLenderTradeRequest: params: ", params);
        
        List<MBSTransactionRequest> transReqLst = new ArrayList<MBSTransactionRequest>();
        SelectResults<MBSTransactionRequest> tdsEntities = (SelectResults<MBSTransactionRequest>) super.query(
                queryString.toString(), params);
        if (CollectionUtils.isNotEmpty(tdsEntities)) {
            transReqLst = tdsEntities.asList();
        }
        return transReqLst;

    }
    
    /**
     * This method returns the total number of records that meet the filter criteria
     * 
     * @param filter
     * @return
     * @throws MBSBaseException
     */
    public Integer getTransHistorySize(List<MBSFilter> filter) throws MBSBaseException {
    	StringBuilder queryBuilder = new StringBuilder();
    	int size = 0;
    	queryBuilder.append("Select count(*) from ");
        Region region = super.getStorageRegion();
        // TODO - use prepared statement to create the query
        // TODO - use the following instead of pool to get the queryService
        // 		QueryService queryService = super.getCache().getQueryService();
        // TODO - create a query function to run in the gemfire server that returns the count
        Pool pool = PoolManager.find(region);
        if (pool == null) {
        	LOGGER.debug("pool not found");
        } else {
        	QueryService queryService = pool.getQueryService();
        	queryBuilder.append(region.getFullPath());
        	queryBuilder.append(" where ");
    	
	    	List<Object> queryArgsLst = new ArrayList();
	        if (Objects.nonNull(filter) && filter.size() > 0) {
	            LOGGER.debug("inside buildQuery, inside  " + filter.size());
	            for (MBSFilter mbsFilter : filter) {
	                if (Objects.nonNull(mbsFilter.getValuesLst()) && mbsFilter.getValuesLst().size() > 0) {
	                    queryBuilder.append(buildFilterQuery(mbsFilter, queryArgsLst));
	                    queryBuilder.append(" and ");
	                }
	            }
	            // remove last "and"
	            queryBuilder.delete(queryBuilder.length() - 5, queryBuilder.length());
	        }
	        LOGGER.debug("The query is: " + queryBuilder.toString() );
	        Query query = queryService.newQuery(queryBuilder.toString());
	        try {
				SelectResults result = (SelectResults)query.execute();
				size = (int)result.asList().get(0);
			} catch (FunctionDomainException | TypeMismatchException | NameResolutionException
					| QueryInvocationTargetException e) {
				LOGGER.error("Query failed to execute " + e.getMessage());
				throw new MBSBaseException(e);
			}
	    }
	        
    	return size;
    }

    /**
     * 
     * buildFilterQuery CMMBSSTA01-1202 - Created method for the filtering story
     * 
     * @param mbsFilter
     *            MBSFilter
     * @return String
     */
    protected String buildFilterQuery(MBSFilter mbsFilter, List<Object> queryArgsLst) {
        StringBuilder queryWhereString = new StringBuilder();
        int counter = 1;
        if (Objects.nonNull(queryArgsLst)) {
            counter = queryArgsLst.size() + 1;
        }

        if (mbsFilter.getOperator().equals(MBSOperator.IN)) {
            queryWhereString.append(" ").append(mbsFilter.getColumnName()).append(" in SET( "); // "counterpartyTraderIdentifier
                                                                                                // in
            for (String value : mbsFilter.getValuesLst()) {
                queryWhereString.append("'").append(value).append("', ");
            }
            // remove the last comma
            queryWhereString.deleteCharAt(queryWhereString.length() - 2);
            queryWhereString.append(" ) ");
        } else if (mbsFilter.getOperator().equals(MBSOperator.NOT_IN)) {
        	queryWhereString.append(" ").append("NOT (").append(mbsFilter.getColumnName()).append(" in SET( "); 
			for (String value : mbsFilter.getValuesLst()) {
                queryWhereString.append("'").append(value).append("', ");
			}
			// remove the last comma
			queryWhereString.deleteCharAt(queryWhereString.length() - 2);
			queryWhereString.append(" )) ");
        } else if (mbsFilter.getOperator().equals(MBSOperator.EQUAL)) {
        	queryWhereString.append(" ").append(mbsFilter.getColumnName()).append(" = ( ");
            queryWhereString.append("'").append(mbsFilter.getValuesLst().get(0)).append("' ");
			// remove the last comma
			queryWhereString.append(" ) ");
        } else if (mbsFilter.getOperator().equals(MBSOperator.GREATER_THAN_DATE)) {
        	queryWhereString.append(" ").append(mbsFilter.getColumnName());                 
            queryWhereString.append(" > DATE '").append(mbsFilter.getValuesLst().get(0)).append("' ");
        } else if (mbsFilter.getOperator().equals(MBSOperator.LESSER_THAN_DATE)) {
        	queryWhereString.append(" ").append(mbsFilter.getColumnName());            
            queryWhereString.append(" < DATE '").append(mbsFilter.getValuesLst().get(0)).append("' ");
        }
        
        return queryWhereString.toString();
    }
}