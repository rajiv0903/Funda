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
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.dao.MBSTradeDao;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Class Name: TradeService Purpose : This class handles the service part for
 * the Trade requests
 * 
 * @author g8upjv
 *
 */
@SuppressWarnings("rawtypes")
@Service
public class TradeService extends BaseProcessor {
	//TODO: "To be converted to use GF function dao"
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeService.class);
	
	       /**
         * 
         * mbsObjectCreator MBSObjectCreator
         */
        @Autowired
        MBSObjectCreator mbsObjectCreator;

	/**
	 * 
	 * @param tradePersister
	 * @param tradePOTransformer
	 */
	@SuppressWarnings("unchecked")
	@Autowired
	public TradeService(Persister tradePersister, Transformer tradePOTransformer) {
		super.persister = tradePersister;
		super.poTransformer = tradePOTransformer;
	}

	/**
	 * 
	 * @throws MBSBaseException
	 */
	public void clearAll() throws MBSBaseException {
		persister.clearAll();
	}

	/**
	 * 
	 * Purpose: This method returns the list of all the Trade object from the
	 * Gemfire data store for lender
	 *
	 * @param transReqId the transReqId
	 * @return List<TradePO> The list of Trade values
	 * @throws MBSBaseException
	 */
	@SuppressWarnings("unchecked")
	public List<TradePO> getMBSTrade(String transReqId) throws MBSBaseException {
		LOGGER.debug("Entering getMBSTrade method in TradeService");
		List<TradePO> listTradePO = new ArrayList<TradePO>();
		List<MBSTrade> listMBSTrade = new ArrayList<MBSTrade>();
		MBSTrade mbsTrade;
		TradePO tradePO;
		if (StringUtils.isEmpty(transReqId)) {
			listMBSTrade = persister.getBaseDao().getAll();
		} else {
			mbsTrade = ((MBSTradeDao) persister.getBaseDao()).getByTransReqId(transReqId);
			listMBSTrade.add(mbsTrade);
		}
		TransformationObject transObj;
		if (!Objects.equals(listMBSTrade, null)) {
			LOGGER.debug("Size of Trade list: " + listMBSTrade.size());
			for (MBSTrade mbsTradeObj : listMBSTrade) {
				transObj = mbsObjectCreator.getTransformationObject();
				transObj.setTargetPojo(mbsTradeObj);
				tradePO = (TradePO) poTransformer.transform(transObj).getSourcePojo();
				listTradePO.add(tradePO);
			}
		}
		LOGGER.debug("Exiting getMBSTrade method in TradeService");
		return listTradePO;
	}
	
	@SuppressWarnings("unchecked")
	public List<TradePO> getMBSTrades(List<String> transReqId) throws MBSBaseException {
		LOGGER.debug("Entering getMBSTrade method in TradeService");
		List<TradePO> listTradePO = new ArrayList<TradePO>();
		List<MBSTrade> listMBSTrade = new ArrayList<MBSTrade>();
		MBSTrade mbsTrade;
		TradePO tradePO;
		if (CollectionUtils.isEmpty(transReqId)) {
			listMBSTrade = persister.getBaseDao().getAll();
		} else {
			listMBSTrade = ((MBSTradeDao) persister.getBaseDao()).getTradesByTransReqIds(transReqId);
		}
		TransformationObject transObj;
		if (!Objects.equals(listMBSTrade, null)) {
			LOGGER.debug("Size of Trade list: " + listMBSTrade.size());
			for (MBSTrade mbsTradeObj : listMBSTrade) {
				transObj = mbsObjectCreator.getTransformationObject();
				transObj.setTargetPojo(mbsTradeObj);
				tradePO = (TradePO) poTransformer.transform(transObj).getSourcePojo();
				listTradePO.add(tradePO);
			}
		}
		LOGGER.debug("Exiting getMBSTrade method in TradeService");
		return listTradePO;
	}
}
