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

package com.fanniemae.mbsportal.aspect.pointcut;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 
 * THis class defines the point cut 
 * 
 * @author g8upjv
 * 
 *
 */
public class MBSSystemPointcut {
    
    /**
     * 
     * This method defines the packages where the point cut is applied.
     * 
     */
    @Pointcut("execution(@com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation * *..*ControllerHelper.*(..))")
    public void annotationExceptionPointcut() {
        //Annotate for Aspect pointcut
    }
}
