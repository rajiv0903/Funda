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

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author g8upjv
 */
@Service
public class MBSProductDao extends MBSBaseDao<MBSProduct> {
    @InjectLog
    private Logger LOGGER;
    /**
     * ID service dao initialised for
     */
    @Autowired
    private IDServiceDao idServiceDao;

    @Autowired
    RegionService cache;
    private String regionName = "MBSProduct";

    /**
     * This is post construct method where the region name is initialized
     */
    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSProduct" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }

    /**
     * @return Region<String, MBSProduct>
     */
    @Override
    public Region<String, MBSProduct> getStorageRegion() {
        LOGGER.debug("Storage region for Product request in MBSProductDao class");
        return getBaseDaoWrapper().getCache().getRegion("MBSProduct");
    }

    /**
     * Save or Update overridden for inserting generated sequence
     * @param obj
     * @throws MBSBaseException
     */
    @Override
    public void saveOrUpdate(MBSProduct obj) throws MBSBaseException {

        if (!Objects.equals(obj.getProductId(), null)) {
        	if(Objects.equals(obj.getProductId().getIdentifier(), null)){
	            LOGGER.debug("going for generting seqId from gemfire {} in MBSProductDao class", obj.getId());
	            String seqId = idServiceDao.getSeqId(DAOConstants.IDTypes.PRODUCT_ID);
	            if (null == seqId || "".equals(seqId)) {
	                LOGGER.error("seqId from gemfire {} in MBSProductDao class", seqId);
	                throw new MBSDataAccessException("Seq Id is null for " + obj);
	            }
	            LOGGER.debug("seqId from gemfire {} in MBSProductDao class", seqId);
	            obj.getProductId().setIdentifier(Long.valueOf(seqId));
        	}
            obj.getProductId().setProductIdStr(obj.getProductId().getProductIdStr());
            super.saveOrUpdate(obj);

        }
        LOGGER.debug("saved in gemfire obj id {} in MBSTransactionRequestDao class", obj.getId());
    }
}
