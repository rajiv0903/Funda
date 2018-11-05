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
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.pdx.internal.PdxInstanceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.util.DAOUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author gaur5c
 * @Description - Interation with MBSProfile Region
 */
@Service
public class MBSProfileDao extends MBSBaseDao<MBSProfile> {

	@InjectLog
	private Logger LOGGER;

	@Autowired
	RegionService cache;
	private String regionName = "MBSProfile";
	
	@Autowired
        DAOUtils daoUtils;

	/**
	 * @return Region<String, MBSProfile>
	 */
	@Override
	public Region<String, MBSProfile> getStorageRegion() {
		LOGGER.debug("Storage region for Trade request in MBSProfile class");
		return getBaseDaoWrapper().getCache().getRegion("MBSProfile");
	}

	/**
	 * This is post construct method where the region name is initialized
	 */
	@PostConstruct
	public void initConfig() {
		LOGGER.debug("init called..MBSProfile" + cache + regionName);
		getBaseDaoWrapper().setCache(cache);
		getBaseDaoWrapper().setRegionName(regionName);
	}

	/**
	 * @author gaur5c
	 * @param userName
	 * @return MBSProfile Object
	 * @throws MBSBaseException
	 */
	public MBSProfile getProfile(String userName) throws MBSBaseException {
		return (MBSProfile) super.getById(userName);
	}

	/**
	 * getMBSTransactionRequestObject
	 * 
	 * @param obj
	 * @return MBSProfile
	 */
	private MBSProfile getMBSProfileObject(Object obj) {
		if (obj instanceof PdxInstanceImpl) {
			PdxInstanceImpl pdxImpl = (PdxInstanceImpl) obj;
			return (MBSProfile) pdxImpl.getObject();
		} else
			return (MBSProfile) obj;
	}
	
	/**
	 * @param obj
	 * @throws MBSBaseException
	 */
	@Override
	public void saveOrUpdate(MBSProfile obj) throws MBSBaseException {

		if (StringUtils.isNotBlank(obj.getUserName())) {
			super.saveOrUpdate(obj);
			LOGGER.debug("saved in gemfire obj id {} in MBSProfile class", obj.getId());
		} else {
			LOGGER.debug("Failed to save in gemfire obj id {} in MBSProfile class", obj.getId());
		}
	}

	/**
	 * Get the List of MBSProfile for entity
	 * 
	 * @param entityId
	 * @throws MBSBaseException
	 * @return List<MBSProfile>
	 */
	public List<MBSProfile> getMBSProfile(String entityId) throws MBSBaseException {
		String queryString = "dealerOrgId = $1";
		List<MBSProfile> mbsProfileLst = new ArrayList<MBSProfile>();

		SelectResults<MBSProfile> profileEntities = (SelectResults<MBSProfile>) super.query(queryString, entityId);
		if (CollectionUtils.isNotEmpty(profileEntities)) {
			for (Object result : profileEntities) {
				mbsProfileLst.add(getMBSProfileObject(result));
			}
		}
		return mbsProfileLst;
	}
	

	/**
	 * @author e4umgc
	 * @param brsUserNames
	 * @return List<MBSProfile>
	 * @throws MBSBaseException
	 */
	public List<MBSProfile> getProfileByBRSNames(String[] brsUserNames) throws MBSBaseException {
		StringBuilder queryBuilder = new StringBuilder();
		List<MBSProfile> mbsProfileList = new ArrayList<MBSProfile>();
		if(null != brsUserNames && brsUserNames.length > 0 ) {
			List<String> brsUserNamesList = new ArrayList<String>(Arrays.asList(brsUserNames));
			if(!CollectionUtils.isEmpty(brsUserNamesList)){
				queryBuilder.append(daoUtils.generateInSetConditional("brsUserName", brsUserNamesList));    
			}
		}
		
		SelectResults<MBSProfile> profileEntities = (SelectResults<MBSProfile>)super.query(queryBuilder.toString());
		
		if (CollectionUtils.isNotEmpty(profileEntities)) {
			for (Object result : profileEntities) {
				mbsProfileList.add(getMBSProfileObject(result));
			}
		}
		return mbsProfileList;
	}
	
	public List<MBSProfile> getProfileByUserNames(List<String> userNames) throws MBSBaseException {
		StringBuilder queryBuilder = new StringBuilder();
		List<MBSProfile> mbsProfileList = new ArrayList<MBSProfile>();
		if(!CollectionUtils.isEmpty(userNames)){
			queryBuilder.append(daoUtils.generateInSetConditional("userName", userNames));    
		}
		
		SelectResults<MBSProfile> profileEntities = (SelectResults<MBSProfile>)super.query(queryBuilder.toString());
		
		if (CollectionUtils.isNotEmpty(profileEntities)) {
			for (Object result : profileEntities) {
				mbsProfileList.add(getMBSProfileObject(result));
			}
		}
		return mbsProfileList;
		
	}
	
	/**
	 * @author e4umgc
	 * @param brsUserNames
	 * @return List<String>
	 * @throws MBSBaseException
	 */
	public List<String> getUserNamesForBRSNames(String[] brsUserNames) throws MBSBaseException {

		List<MBSProfile> mbsProfileList = getProfileByBRSNames(brsUserNames);
		List<String> userNamesList = new ArrayList<String>();
    	if (null != mbsProfileList && mbsProfileList.size() > 0 ) {
	    	for(MBSProfile profile: mbsProfileList) {
	    		String userName = profile.getUserName();
	    		userNamesList.add(userName);
	    	}
    	}
		return userNamesList;
	}
	

}
