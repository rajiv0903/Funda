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

package com.fanniemae.mbsportal.cdx.exception;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 31, 2018
 * @File: com.fanniemae.mbsportal.cdx.exception.CDXBaseException.java 
 * @Revision: 
 * @Description: CDXBaseException.java
 */
public class CDXBaseException extends Exception{
    
    /**
     * 
     * serialVersionUID long
     */
    private static final long serialVersionUID = 6407005224349091728L;
    
    /**
     * 
     * rootExp Exception
     */
    private Exception rootExp;
    
    /**
     * 
     * processId long
     */
    private long processId;
    
  
    /**
     * 
     * 
     * @param errMsg String
     * @param processId long
     */
    public CDXBaseException(String errMsg, long processId) {
        this.processId = processId;
        rootExp=new Exception(errMsg);
    }
 
    
    public CDXBaseException(String errMsg, long processId, Exception rootExp) {
        this.processId = processId;
        this.rootExp=new Exception(errMsg,rootExp);
    }
    
    /**
     * 
     * @param errMsg String
     */
    public CDXBaseException(String errMsg) {
        rootExp=new Exception(errMsg);
    }
    
    /**
     * 
     * 
     * @param rootExp Exception
     */
    public CDXBaseException(Exception rootExp) {
        this.rootExp=rootExp;
    }
    
    /**
     * 
     * 
     * @param errorMsg String
     * @param rootExp Exception
     */
    public CDXBaseException(String errorMsg,Exception rootExp) {
        this.rootExp=new Exception(errorMsg,rootExp);
    }
    
    /**
     * 
     * 
     * @param errorMsg String
     * @param rootT Throwable
     */
    public CDXBaseException(String errorMsg,Throwable rootT) {
        this.rootExp=new Exception(errorMsg,rootT);
    }
    
    /**
     * 
     * 
     * @return Exception
     */
    public Exception getRootException(){
        return this.rootExp;
    }
    
    /**
     * 
     * 
     * @return String
     */
    public String getRootExceptionMessage() {
        return this.rootExp.getMessage();
    }
    
    /**
     * 
     * 
     * @return long
     */
    public long getProcessId() {
        return processId;
    }
    
    /**
     * 
     * 
     * @param processId long
     */
    public void setProcessId(long processId) {
        this.processId = processId;
    }


    @Override
    public String toString() {
        return "CDXBaseException [rootExp=" + rootExp + ", processId=" + processId + "]";
    }
    
    
}
