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

package com.fanniemae.mbsportal.aspect.exception;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemWarning;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessWarning;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 22, 2018
 * @File: com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAspect.java
 * @Revision : 
 * @Description: ExceptionTracingAspect.java
 */
@Component
@Aspect
public class ExceptionTracingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionTracingAspect.class);

    @Around("com.fanniemae.mbsportal.aspect.pointcut.MBSSystemPointcut.annotationExceptionPointcut()")
    public Object trace(ProceedingJoinPoint proceedingJP) throws Throwable {
        
        String methodInformation = proceedingJP.getStaticPart().getSignature().toString();
        String methodName = proceedingJP.getStaticPart().getSignature().getName();
        List<String> arguments = new ArrayList<>();
        StringBuilder strBuild = new StringBuilder();
        LOGGER.debug("Entering : " + methodInformation);
        
        for (Object arg : proceedingJP.getArgs()) {
            arguments.add(arg.toString());
            strBuild.append(arg.toString()+" : ");
        }
        LOGGER.debug("Method : "+methodName+", Arguments : " + arguments);
        try {
            return proceedingJP.proceed();

        } catch (MBSBusinessWarning ex) {
            MDC.put(MBSExceptionConstants.ERROR_TYPE, MBSExceptionConstants.BUSINESS_WARNING_IDENTIFIER);
            MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, String.valueOf(ex.getProcessId()));
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, methodName);
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, methodInformation);
            MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, strBuild.toString());
            MDC.put(MBSExceptionConstants.ERROR_MESSGE, ex.getRootExceptionMessage());
            
            LOGGER.warn("Warning Message: {}", ex.getRootException().getMessage());
            throw ex;
            
        }catch (MBSSystemWarning ex) {
            
            MDC.put(MBSExceptionConstants.ERROR_TYPE, MBSExceptionConstants.SYSTEM_WARNING_IDENTIFIER);
            MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, String.valueOf(ex.getProcessId()));
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, methodName);
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, methodInformation);
            MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, strBuild.toString());
            MDC.put(MBSExceptionConstants.ERROR_MESSGE, ex.getRootExceptionMessage());
            
            LOGGER.warn("Warning Message: {} ", ex.getRootException().getMessage());
            throw ex;
            
        }catch (MBSBusinessException ex) {
            //TODO: add the error code to the logger
            MDC.put(MBSExceptionConstants.ERROR_TYPE, MBSExceptionConstants.BUSINESS_EXCEPTION_IDENTIFIER);
            MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, String.valueOf(ex.getProcessId()));
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, methodName);
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, methodInformation);
            MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, strBuild.toString());
            MDC.put(MBSExceptionConstants.ERROR_MESSGE, ex.getRootExceptionMessage());
            
            LOGGER.error("Error Stack: ", ex.getRootException());
            throw ex;
            
        }catch (MBSDataAccessException ex) {
            //TODO: add the error code to the logger
            MDC.put(MBSExceptionConstants.ERROR_TYPE, MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER);
            MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, String.valueOf(ex.getProcessId()));
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, methodName);
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, methodInformation);
            MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, strBuild.toString());
            MDC.put(MBSExceptionConstants.ERROR_MESSGE, ex.getRootExceptionMessage());
            
            LOGGER.error("Error Stack: ", ex.getRootException());
            throw ex;
        }catch (MBSSystemException ex) {
            //TODO: add the error code to the logger
            MDC.put(MBSExceptionConstants.ERROR_TYPE, MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER);
            MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, String.valueOf(ex.getProcessId()));
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, methodName);
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, methodInformation);
            MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, strBuild.toString());
            MDC.put(MBSExceptionConstants.ERROR_MESSGE, ex.getRootExceptionMessage());
            
            LOGGER.error("Error Stack: ", ex.getRootException());
            throw ex;
        } catch (MBSBaseException ex) {
            //TODO: add the error code to the logger
            MDC.put(MBSExceptionConstants.ERROR_TYPE, MBSExceptionConstants.BASE_EXCEPTION_IDENTIFIER);
            MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, String.valueOf(ex.getProcessId()));
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, methodName);
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, methodInformation);
            MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, strBuild.toString());
            MDC.put(MBSExceptionConstants.ERROR_MESSGE, ex.getRootExceptionMessage());
            
            LOGGER.error("Error Stack: ", ex.getRootException());
            throw ex;
        } 
        finally {
            LOGGER.debug("Exiting : " + methodInformation);
            MDC.remove(MBSExceptionConstants.ERROR_TYPE);
            MDC.remove(MBSExceptionConstants.ERROR_PROCESS_ID);
            MDC.remove(MBSExceptionConstants.ERROR_SOURCE_METHOD);
            MDC.remove(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE);
            MDC.remove(MBSExceptionConstants.SOURCE_METHOD_ARGS);
            MDC.remove(MBSExceptionConstants.ERROR_MESSGE);
        }
    }
}
