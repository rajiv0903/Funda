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
/**
 * @author g8uaxt Created on 9/7/2017.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.apache.geode.cache.query.SelectResults;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 *
 * @author g8uaxt
 *
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MBSProductPricingDao extends MBSBaseDao<MBSProductPricingRequest> {

    @Autowired
    RegionService cache;
    private String regionName = "MBSProductPricing";
    @InjectLog
    private Logger LOGGER;
    @Autowired
    private IDServiceDao idServiceDao;

    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSProductPricingDao" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }

    @Override
    public Region<Long, MBSProductPricingRequest> getStorageRegion() {
        return getBaseDaoWrapper().getCache().getRegion(regionName);
    }

    /**
     * saveOrUpdate
     * 
     * @param obj
     */
    @Override
    public void saveOrUpdate(MBSProductPricingRequest obj) throws MBSBaseException {
        if (Objects.equals(obj.getPricingIndicativeIdentifier(), null)) {

            LOGGER.debug("going for generting seqId from gemfire {} in MBSProductPricingDao class", obj.getId());
            String seqId = idServiceDao.getSeqId(DAOConstants.IDTypes.PRODUCT_PRICING_ID);
            if (null == seqId || "".equals(seqId)) {
                LOGGER.error("seqId from gemfire {} in {} class", seqId, MBSProductPricingDao.class);
                throw new MBSDataAccessException("Seq Id is null for " + obj);
            }
            LOGGER.debug("seqId from gemfire {} in {} class", seqId, MBSProductPricingDao.class);
            obj.setPricingIndicativeIdentifier(Long.valueOf(seqId));
            obj.setPricingProductIdStr(obj.getProductId().getProductIdStr());
        }
        super.saveOrUpdate(obj);
        LOGGER.debug("saved in gemfire obj id {} in MBSProductPricingDao class", obj.getId());
    }

    /**
     * getAllByProductKey
     * 
     * @param identifier
     * @param sourceType
     * @param type
     * @return
     */
    public List<MBSProductPricingRequest> getAllByProductKey(Long identifier, String sourceType, String type) throws MBSBaseException {
        String pricingProductIdStr = identifier.toString() + "." + sourceType + "." + type;
        String query = "pricingProductIdStr= $1 order by productId.identifier,settlementDate";
        SelectResults<MBSProductPricingRequest> tdsEntities = (SelectResults<MBSProductPricingRequest>) super.query(
                query, pricingProductIdStr);
        List<MBSProductPricingRequest> list = new ArrayList<>();
        if (tdsEntities != null) {
            list = tdsEntities.asList();
        }
        LOGGER.debug("returning product pricing infos result size" + list.size());
        return list;
    }
}
