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

package com.fanniemae.mbsportal.utils.logging;

import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Created by g8uaxt on 8/22/2017.
 * 
 * @author g8upjv
 */
@Component
public class MBSLogger implements BeanPostProcessor {
        
        /**
         * 
         * This method does the post processes after the initialization
         * 
         * @param bean
         * @param beanName
         * @return Object
         * @throws BeansException
         */
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
        }
        
        /**
         * 
         * This method does the post processes before the initialization
         * 
         * @param bean
         * @param name
         * @throws BeansException
         */
        @Override
        public Object postProcessBeforeInitialization(final Object bean, String name) throws BeansException {
                ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
                        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                                // make the field accessible if defined private
                                ReflectionUtils.makeAccessible(field);
                                if(field.getAnnotation(InjectLog.class) != null) {
                                        Logger log = LoggerFactory.getLogger(bean.getClass());
                                        field.set(bean, log);
                                }
                        }
                });
                return bean;
        }
        
}
