/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.dao.MBSConfigPropDao;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * 
 * @author g8upjv
 *
 */
//TODO: The ConfigService class in api package needs to be moved to this class
@Service
public class ConfigurationService {
    
    /**
     * 
     * mbsConfigPropDao MBSConfigPropDao
     */
     @Autowired
     MBSConfigPropDao mbsConfigPropDao;
     
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
             MBSConfigProp mbsConfigProp = (MBSConfigProp) mbsConfigPropDao.getById(key);
             if(mbsConfigProp==null)
                     return null;
             return mbsConfigProp.getValue();
     }

}
