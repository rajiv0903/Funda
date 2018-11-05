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
 * @File: com.fanniemae.mbsportal.api.transformation.ConfigPropTransformer.java 
 * @Revision: 
 * @Description: ConfigPropTransformer.java
 */
@Component
public class ConfigPropTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPropTransformer.class);

    /**
     * 
     * @param transformationObject
     *            TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
        
        LOGGER.debug("Entering transform method in ConfigPropTransformer");
        
        ConfigPropPO configPropPO = (ConfigPropPO) transformationObject.getSourcePojo();

        // Transform the Presentation object to domain object
        MBSConfigProp mBSConfigProp = convertToModel(configPropPO);
        transformationObject.setTargetPojo(mBSConfigProp);
        
        LOGGER.debug("Exiting transform method in ConfigPropTransformer");
        return transformationObject;
    }

    /**
     * 
     * Convert to Model object from ConfigPropPO
     * 
     * @param configPropPO  ConfigPropPO
     * @throws MBSBaseException
     */
    private MBSConfigProp convertToModel(ConfigPropPO configPropPO ) throws MBSBaseException {
        
        MBSConfigProp mBSConfigProp = new MBSConfigProp();
        
        mBSConfigProp.setKey(configPropPO.getKey());
        mBSConfigProp.setValue(configPropPO.getValue());
        mBSConfigProp.setParent(configPropPO.getParent());
        mBSConfigProp.setDataType(configPropPO.getDataType());
        
        return mBSConfigProp;
    }
}
