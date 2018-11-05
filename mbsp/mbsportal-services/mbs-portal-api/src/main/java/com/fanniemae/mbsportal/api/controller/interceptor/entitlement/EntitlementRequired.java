/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller.interceptor.entitlement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author g8upjv
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention (RetentionPolicy.RUNTIME)
public @interface EntitlementRequired {
    
    /**
     * 
     * return roles
     * 
     * @return String[]
     */
    String[] roles();
    
    /**
     * 
     * return tokenRequired
     * 
     * @return Boolean
     */
    boolean tokenRequired() default false;
}
