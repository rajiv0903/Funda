/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.service;

import com.fanniemae.mbsportal.pojo.MBSConfigPO;
import com.fanniemae.mbsportal.dao.MBSConfigPropDao;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Used for MBSP config service to change the prop at runtime
 * @author g8uaxt Created on 1/18/2018.
 */
//TODO: Move it to framework package to make it re-usable
@Service
public class ConfigService {
        //TODO: do we need BaseProcessor ??
        
       /**
        * 
        * mbsConfigPropDao MBSConfigPropDao
        */
        @Autowired
        MBSConfigPropDao mbsConfigPropDao;
        
       /**
        * 
        * LOGGER Logger variable
        */
        @InjectLog
        private Logger LOGGER;
        
        /**
         * 
         * Get method return value of the key as String
         * @param key the key
         * @return String
         * @throws MBSBaseException
         */
        public String getPropValueAsString(String key) throws MBSBaseException {
                if(key==null)
                        return null;
                MBSConfigPO mbsConfigPO = getConfigInfo(key);
                if(mbsConfigPO==null)
                        return null;
                return mbsConfigPO.getValue();
        }
        
        /**
         * 
         * get config key details
         * @param key the key
         * @return MBSConfigPO
         * @throws MBSBaseException
         */
        public MBSConfigPO getConfigInfo(String key) throws MBSBaseException {
                MBSConfigProp mbsConfigProp = (MBSConfigProp) mbsConfigPropDao.getById(key);
                if(mbsConfigProp==null)
                        return null;
                MBSConfigPO mbsConfigPO = new MBSConfigPO(mbsConfigProp.getKey(), mbsConfigProp.getValue(),
                        mbsConfigProp.getParent());
                return mbsConfigPO;
        }
        
        /**
         * 
         * simple method to save
         * @param key the key
         * @param value the value
         * @param parent the parent
         * @throws MBSBaseException
         */
        public void saveConfigInfo(String key, String value, String parent) throws MBSBaseException {
                saveConfigInfo(key, value, parent, "String");
        }
        
        /**
         * 
         * Saving config values
         * @param key the key
         * @param value the value
         * @param parent the parent
         * @param dataType the dataType
         * @throws MBSBaseException
         */
        public void saveConfigInfo(String key, String value, String parent, String dataType) throws MBSBaseException {
                MBSConfigProp mbsConfigProp = new MBSConfigProp();
                mbsConfigProp.setKey(key);
                mbsConfigProp.setValue(value);
                mbsConfigProp.setParent(parent);
                mbsConfigProp.setDataType(dataType);
                mbsConfigPropDao.saveOrUpdate(mbsConfigProp);
        }
        
}
