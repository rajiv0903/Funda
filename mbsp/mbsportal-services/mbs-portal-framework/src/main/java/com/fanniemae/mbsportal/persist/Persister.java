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

package com.fanniemae.mbsportal.persist;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Created by g8uaxt on 8/18/2017.
 */
public interface Persister<T extends TransformationObject> {
        
        public void persist(T obj)  throws MBSBaseException;
        public void clearAll()  throws MBSBaseException;
        public void clear(String key)  throws MBSBaseException;
        @SuppressWarnings("rawtypes")
        @Deprecated
        public BaseDaoImpl getDao() throws MBSBaseException;
        @SuppressWarnings("rawtypes")
        public MBSBaseDao getBaseDao() throws MBSBaseException;

}
