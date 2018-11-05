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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.utils.MBSUtils;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.dao.MBSQueryFunctionDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.enrichment.Enricher;
import com.fanniemae.mbsportal.gf.pojo.MBSFilter;
import com.fanniemae.mbsportal.gf.pojo.MBSOperator;
import com.fanniemae.mbsportal.gf.pojo.Sort;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.publisher.MessagePublisher;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * Service to get data for export
 * 
 * @author e3uikb
 *
 */
@SuppressWarnings("rawtypes")
@Service
public class TransactionExportService extends BaseProcessor {

    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * mbsQueryFunctionDao MBSQueryFunctionDao
     */
    @Autowired
	private MBSQueryFunctionDao mbsQueryFunctionDao;
    
    /**
     * 
     * mbsProfileDao MBSProfileDao
     */
    @Autowired
    private MBSProfileDao mbsProfileDao;
    
    /**
     * 
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
	private MBSObjectCreator mbsObjectCreator;
    
    /**
     * 
     * tradeServiceProperties TradeServiceProperties
     */
    @Autowired
    private TradeServiceProperties tradeServiceProperties;
    
    /**
     * 
     * transactionRequestService TransactionRequestService
     */
    @Autowired
    private TransactionRequestService transactionRequestService;
    
    /**
     * 
     * mbspProperties MbspProperties
     */
    @Autowired
    MbspProperties mbspProperties;
    
    
    @Autowired
    public TransactionExportService(Transformer transactionRequestTransformer, Validator transactionRequestPOValidator,
            Validator transactionRequestValidator, Persister transactionPersister,
            Transformer transactionRequestPOTransformer, Enricher transactionRequestEnrichment, MessagePublisher transactionRequestMessagePublisher) {
        super.transformer = transactionRequestTransformer;
        super.validator = transactionRequestPOValidator;
        super.enricher = transactionRequestEnrichment;
        super.persister = transactionPersister;
        super.domainValidator = transactionRequestValidator;
        super.poTransformer = transactionRequestPOTransformer;
        super.messagePublisher = transactionRequestMessagePublisher;
    }
    
    /**
     * 
     * @param mbsRoleType
     * @param stateTypeEnumLst
     * @param sellerServiceNumber
     * @param sortBy
     * @param sortOrder
     * @param dateStart
     * @param dateEnd
     * @param dateType
     * @return
     * @throws MBSBaseException
     */
	public List<TransactionRequestPO> getMBSTransReqHistory(MBSRoleType mbsRoleType, List<StateType> stateTypeEnumLst,
			String sellerServiceNumber, String sortBy, String sortOrder, String dateStart, String dateEnd,
			String dateType) throws MBSBaseException {
        LOGGER.debug("==============================Entering getMBSTransReqHistory method in TransactionRequestService");
        List<String> strStateTypeLst;
        if (Objects.equals(stateTypeEnumLst, null)) {
            LOGGER.error(MBSPServiceConstants.NO_STATETYPE_LIST);
            throw new MBSSystemException(MBSPServiceConstants.NO_STATETYPE_LIST, MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        strStateTypeLst = MBSUtils.convertStateTypeToStringList(stateTypeEnumLst);
        List<TransactionRequestPO> listTransReqPO = new ArrayList<TransactionRequestPO>();
        List<MBSTransactionRequest> listMBSTransReq = null;
        // create the param args here
        List<MBSFilter> mbsFilterLst = new ArrayList();
        
        //Filter for stateType
        mbsFilterLst.add(new MBSFilter("stateType", strStateTypeLst, MBSOperator.IN));
        
        //Filter for users
        createUserFilter(mbsFilterLst, mbsRoleType, sellerServiceNumber);

        //Filter based on date
        createDateFilter(mbsFilterLst, dateStart, dateEnd, dateType);
        
        //Filter shakeout trades
        mbsFilterLst.add(new MBSFilter("lenderShortName",Arrays.asList(tradeServiceProperties.getShakeOutCPartyName()), MBSOperator.NOT_IN));
        
        // Create sorting functions
        Sort sort = createSort(mbsRoleType, sortBy, sortOrder);

        listMBSTransReq = mbsQueryFunctionDao.getTransactionHistoryDetails(mbsFilterLst, sort);
        TransactionRequestPO mbsTransactionPO;
        TransformationObject transObj;
        if (!Objects.equals(listMBSTransReq, null)) {
            for (MBSTransactionRequest mbsTransReq : listMBSTransReq) {
                transObj = mbsObjectCreator.getTransformationObject();
                transObj.setMBSRoleType(mbsRoleType);
                transObj.setTargetPojo(mbsTransReq);
                mbsTransactionPO = (TransactionRequestPO) poTransformer.transform(transObj).getSourcePojo();
                mbsTransactionPO.setProduct(transactionRequestService.getProduct(mbsTransactionPO.getProduct().retrieveKey()));
                listTransReqPO.add(mbsTransactionPO);
            }
        }
        LOGGER.debug("Exiting getMBSTransReq method in TransactionRequestService");
        return listTransReqPO;
	}

	/**
	 * 
	 * @param mbsRoleType
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	private Sort createSort(MBSRoleType mbsRoleType, String sortBy, String sortOrder) {
        if (sortBy.equalsIgnoreCase("tradeBuySellType") && mbsRoleType == MBSRoleType.LENDER) {
            sortBy = "counterPartyBuySellType";
        }
        return new Sort(sortBy, SortBy.getEnum(sortOrder));
	}

	/**
	 * 
	 * @param mbsFilterLst
	 * @param dateStart
	 * @param dateEnd
	 * @param dateType
	 * @throws MBSBaseException
	 */
	private void createDateFilter(List<MBSFilter> mbsFilterLst, String dateStart, String dateEnd, String dateType) throws MBSBaseException {
		Date startDate = MBSPortalUtils.convertToDateWithFormatter(dateStart, DateFormats.DATE_FORMAT_NO_TIMESTAMP);
		Date endDate = MBSPortalUtils.convertToDateWithFormatter(dateEnd, DateFormats.DATE_FORMAT_NO_TIMESTAMP);
		endDate = MBSPortalUtils.addDays(endDate, 1);
        List<String> subDateLst = new ArrayList<>();
        subDateLst.add(MBSPortalUtils.convertDateToString(startDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mbsFilterLst.add(new MBSFilter(dateType, subDateLst, MBSOperator.GREATER_THAN_DATE));
        subDateLst = new ArrayList<>();
        subDateLst.add(MBSPortalUtils.convertDateToString(endDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mbsFilterLst.add(new MBSFilter(dateType, subDateLst, MBSOperator.LESSER_THAN_DATE));
	}

	/**
	 * 
	 * @param mbsFilter
	 * @param mbsRoleType
	 * @param sellerServicerNumber
	 */
	private void createUserFilter(List<MBSFilter> mbsFilter, MBSRoleType mbsRoleType, String sellerServicerNumber) {
        //CMMBSSTA01-1373 - Adding TSP name - Start
        List<String> sellerServiceNumberLst = new ArrayList<>();
        sellerServiceNumberLst.add(sellerServicerNumber);
        if(mbsRoleType.equals(MBSRoleType.LENDER)){
            mbsFilter.add(new MBSFilter("lenderSellerServiceNumber", sellerServiceNumberLst, MBSOperator.IN));
        } else if (mbsRoleType.equals(MBSRoleType.TSP)){
        	mbsFilter.add(new MBSFilter("tspSellerServiceNumber", sellerServiceNumberLst, MBSOperator.IN));
        }
        //CMMBSSTA01-1373 - Adding TSP name - End
	}

	/**
	 * 
	 * @param mbsRoleType
	 * @param stateTypeEnumLst
	 * @param sellerServiceNumber
	 * @return
	 * @throws MBSBaseException
	 */
	public Integer getMBSTransReqHistorySize(MBSRoleType mbsRoleType, List<StateType> stateTypeEnumLst,
			String sellerServiceNumber) throws MBSBaseException {
        LOGGER.debug("Entering getMBSTransReqHistory method in TransactionRequestService");
        List<String> strStateTypeLst;
        if (Objects.equals(stateTypeEnumLst, null)) {
            LOGGER.error(MBSPServiceConstants.NO_STATETYPE_LIST);
            throw new MBSSystemException(MBSPServiceConstants.NO_STATETYPE_LIST, MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        strStateTypeLst = MBSUtils.convertStateTypeToStringList(stateTypeEnumLst);
        
        // create the param args here
        List<MBSFilter> mbsFilterLst = new ArrayList<MBSFilter>();

        //Filter for stateType
        mbsFilterLst.add(new MBSFilter("stateType", strStateTypeLst, MBSOperator.IN));
        
        //Filter based on date - getting 90 days of data from submission date
        int cutOffDays = Integer.valueOf(mbspProperties.getTransHistActiveDays()).intValue();
        Date cutOffDate = MBSPortalUtils.addDays(MBSPortalUtils.getCurrentDate(), cutOffDays);
        List<String> subDateLst = new ArrayList<String>();
        subDateLst.add(MBSPortalUtils.convertDateToString(cutOffDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mbsFilterLst.add(new MBSFilter("submissionDate", subDateLst, MBSOperator.GREATER_THAN_DATE));
        
        
        //Filter for users
        createUserFilter(mbsFilterLst, mbsRoleType, sellerServiceNumber);

        //Filter shakeout trades
//        mbsFilterLst.add(new MBSFilter("dealerOrgName",Arrays.asList(tradeServiceProperties.getShakeOutCPartyName()), MBSOperator.NOT_IN));
        mbsFilterLst.add(new MBSFilter("lenderShortName",Arrays.asList(tradeServiceProperties.getShakeOutCPartyName()), MBSOperator.NOT_IN));

    	return ((MBSTransactionRequestDao) persister.getBaseDao()).getTransHistorySize(mbsFilterLst);
	}

}
