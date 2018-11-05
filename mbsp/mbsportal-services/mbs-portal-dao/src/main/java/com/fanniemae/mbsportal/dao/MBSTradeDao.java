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
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.apache.geode.cache.query.SelectResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.util.DAOUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class is the DAO for the Trade domain
 * class
 *
 * date 07/20/2017
 * 
 * @author g8upjv
 *
 */
@Service
public class MBSTradeDao extends MBSBaseDao<MBSTrade> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MBSTradeDao.class);
	
	@Autowired
    RegionService cache;
    private String regionName = "MBSTrade";
    
	@Autowired
    DAOUtils daoUtils;

	/**
	 * @return Region<String, MBSTrade>
	 */
    @Override
	public Region<String, MBSTrade> getStorageRegion() {
		LOGGER.debug("Storage region for Trade request in MBSTradeDao class");
		return getBaseDaoWrapper().getCache().getRegion("MBSTrade");
	}
	
	/**
	 * This is post construct method where the region name is initialized 
	 */
    @PostConstruct
    public void initConfig() {
            LOGGER.debug("init called..MBSTradeDao" + cache + regionName);
            getBaseDaoWrapper().setCache(cache);
            getBaseDaoWrapper().setRegionName(regionName);
    }
	
	/**
	 *
	 * @param obj
	 * @throws MBSBaseException
	 */
	@Override
	public void saveOrUpdate(MBSTrade obj) throws MBSBaseException{
		if (!Objects.equals(obj.getTransReqNumber(), null)) {
			super.saveOrUpdate(obj);
			LOGGER.debug("saved in gemfire obj id {} in MBSTradeDao class", obj.getId());
		} else {
			LOGGER.error("Unable to save in gemfire as Transaction request id is empty or null in MBSTradeDao class",
					obj.getId());
		}
	}


	/**
	 * 
	 * @param transReqNumber
	 * @return MBSTrade
	 * @throws MBSBaseException
	 */
	public MBSTrade getByTransReqId(String transReqNumber) throws MBSBaseException{
		String queryString = "transReqNumber = $1";
		MBSTrade tdsTrade = null;
		List<MBSTrade> tradeLst = new ArrayList<MBSTrade>();

		SelectResults<MBSTrade> tdsEntities = (SelectResults<MBSTrade>) super.query(queryString, transReqNumber);
		if (CollectionUtils.isNotEmpty(tdsEntities)) {
			tradeLst = tdsEntities.asList();
		}
		if (CollectionUtils.isNotEmpty(tradeLst)) {
			tdsTrade = tradeLst.get(0);
		}
		return tdsTrade;
	}
	
	public List<MBSTrade> getTradesByTransReqIds(List<String> transReqNumbers) throws MBSBaseException {
		StringBuilder queryBuilder = new StringBuilder();
		if (!CollectionUtils.isEmpty(transReqNumbers)) {
			queryBuilder.append(daoUtils.generateInSetConditional("transReqNumber", transReqNumbers));
		}
		SelectResults<MBSTrade> tdsEntities = (SelectResults<MBSTrade>) super.query(queryBuilder.toString());
		if (tdsEntities == null) {
			return null;
		}
		return tdsEntities.asList();
	}

}