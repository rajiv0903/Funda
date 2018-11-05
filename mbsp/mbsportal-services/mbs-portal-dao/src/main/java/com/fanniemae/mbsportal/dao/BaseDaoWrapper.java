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

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.fnmpfj.gemfire.dao.BaseEntity;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author g8uaxt Created on 9/25/2017.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BaseDaoWrapper<T extends BaseEntity> extends BaseDaoImpl<T>{
        private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoWrapper.class);
        
        @PostConstruct
        public void init(){
               LOGGER.debug("BaseWrapper Object id"+this);
        }
        //TODO: verify this later
     /*   @Override
        public T getById(Object id) {
              return super.getById(id);
        }*/
       
}
