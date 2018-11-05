/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ConfigPropPO;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 6, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.ConfigPropPOTransformer.java 
 * @Revision: 
 * @Description: ConfigPropPOTransformer.java
 */
@Component
public class ConfigPropPOTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPropPOTransformer.class);

    /**
     * 
     * This method does the transformation of object from domain to PO
     * 
     * @param transformationObject
     *            TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering transform method in ConfigPropPOTransformer");
        MBSConfigProp mBSConfigProp = (MBSConfigProp) transformationObject.getTargetPojo();
        ConfigPropPO partyPO = convertToPO(mBSConfigProp);
        transformationObject.setSourcePojo(partyPO);
        LOGGER.debug("Exiting transform method in ConfigPropPOTransformer");
        return transformationObject;
    }

    /**
     * 
     * Purpose: This does the conversion from MBSConfigProp to ConfigPropPO object
     *
     * @param mBSConfigProp
     *            The TransactionRequest object
     * @return ConfigPropPO The presentation object of MBSConfigProp object
     * @throws MBSBaseException
     */
    private ConfigPropPO convertToPO(MBSConfigProp mBSConfigProp) throws MBSBaseException {
        
        ConfigPropPO configPropPO = new ConfigPropPO();

        configPropPO.setKey(mBSConfigProp.getKey());
        configPropPO.setValue(mBSConfigProp.getValue());
        configPropPO.setParent(mBSConfigProp.getParent());
        configPropPO.setDataType(mBSConfigProp.getDataType());
        
        LOGGER.debug(configPropPO.toString());

        return configPropPO;
    }
}
