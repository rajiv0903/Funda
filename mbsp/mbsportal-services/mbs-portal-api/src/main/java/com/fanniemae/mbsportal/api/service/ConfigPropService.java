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

package com.fanniemae.mbsportal.api.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.po.ConfigPropPO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 6, 2018
 * @File: com.fanniemae.mbsportal.api.service.ConfigPropService.java
 * @Revision:
 * @Description: ConfigPropService.java
 */
//TODO: Merge this class to the configurationservice class in framework
@SuppressWarnings("rawtypes")
@Service
public class ConfigPropService extends BaseProcessor {

    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;

    /**
     * 
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
    MBSObjectCreator mbsObjectCreator;

    @SuppressWarnings("unchecked")
    @Autowired
    public ConfigPropService(Validator configPropValidator, Transformer configPropTransformer,
            Persister configPropPersister, Transformer configPropPOTransformer) {

        super.validator = configPropValidator;
        super.transformer = configPropTransformer;
        super.persister = configPropPersister;
        super.poTransformer = configPropPOTransformer;
    }

    /**
     * 
     * @param sellerSerivcerNumber
     *            the sellerSerivcerNumber
     * @return PartyPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public ConfigPropPO getConfigProp(String key) throws MBSBaseException {

        LOGGER.debug("Entering getConfigProp method in ConfigPropService");
        LOGGER.debug("Entering getConfigProp method in ConfigPropService: key:" + key);
        MBSConfigProp mBSConfigProp = this.getMBSConfigPropFromRepo(key);
        if (mBSConfigProp == null) {
            throw new MBSBusinessException("No Config information for the key:" + key,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        transformationObject.setTargetPojo(mBSConfigProp);
        transformationObject = super.poTransformer.transform(transformationObject);
        LOGGER.debug("Exiting getConfigProp method in ConfigPropService");
        return (ConfigPropPO) transformationObject.getSourcePojo();
    }

    /**
     * 
     * @param partyPO
     *            the PartyPO
     * @return PartyPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public ConfigPropPO saveOrUpdateConfigProp(ConfigPropPO configPropPO) throws MBSBaseException {

        LOGGER.debug("Entering saveOrUpdateConfigProp method in ConfigPropService");
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        transformationObject.setSourcePojo(configPropPO);
        super.processRequest(transformationObject);
        LOGGER.debug("Exiting saveOrUpdateConfigProp method in ConfigPropService");
        return (ConfigPropPO) transformationObject.getSourcePojo();
    }

    /**
     * 
     * @param key
     *            the key
     * @return MBSConfigProp
     * @throws MBSBaseException
     */
    protected MBSConfigProp getMBSConfigPropFromRepo(String key) throws MBSBaseException {
        return (MBSConfigProp) super.persister.getBaseDao().getById(key);
    }
}
