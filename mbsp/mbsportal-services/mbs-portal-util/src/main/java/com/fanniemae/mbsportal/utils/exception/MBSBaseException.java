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

package com.fanniemae.mbsportal.utils.exception;

import java.util.List;

import com.fanniemae.mbsportal.utils.constants.TradeConstants;

/**
 * Created by g8uaxt on 6/29/2017.
 */
public class MBSBaseException extends Exception{
    
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
    //Added for Exception Handling
    private long processId;
    
    /**
     * 
     * exceptionLookupPO ExceptionLookupPO
     */
    private ExceptionLookupPO exceptionLookupPO;
    
    
    private List<ExceptionLookupPO> exceptionLookupPOLst;
    
    
    public MBSBaseException(String errMsg,long processId, List<ExceptionLookupPO> exceptionLookupPOLst) {
    	this.processId = processId;
    	rootExp=new Exception(errMsg);
    	this.exceptionLookupPOLst = exceptionLookupPOLst;
    }
    
    /**
     * 
     * 
     * @param errMsg String
     * @param processId long
     */
    public MBSBaseException(String errMsg, long processId) {
        this.processId = processId;
        rootExp=new Exception(errMsg);
    }
    
    /**
     * 
     * 
     * @param errMsg String
     * @param processId long
     * @param exceptionLookupPO ExceptionLookupPO
     */
    public MBSBaseException(String errMsg, long processId, ExceptionLookupPO exceptionLookupPO) {
        this.processId = processId;
        this.exceptionLookupPO = exceptionLookupPO;
        rootExp=new Exception(errMsg);
    }
    
    /**
     * 
     * 
     * @param errMsg String
     * @param processId long
     * @param rootExp Exception
     * @param exceptionLookupPO ExceptionLookupPO
     */
    public MBSBaseException(String errMsg, long processId, Exception rootExp, ExceptionLookupPO exceptionLookupPO) {
        this.processId = processId;
        this.exceptionLookupPO = exceptionLookupPO;
        this.rootExp=new Exception(errMsg,rootExp);
    }
    
    public MBSBaseException(String errMsg, long processId, Exception rootExp) {
        this.processId = processId;
        this.rootExp=new Exception(errMsg,rootExp);
    }
    
    /**
     * 
     * @param errMsg String
     */
    public MBSBaseException(String errMsg) {
        rootExp=new Exception(errMsg);
    }
    
    /**
     * 
     * 
     * @param rootExp Exception
     */
    public MBSBaseException(Exception rootExp) {
        this.rootExp=rootExp;
    }
    
    /**
     * 
     * 
     * @param errorMsg String
     * @param rootExp Exception
     */
    public MBSBaseException(String errorMsg,Exception rootExp) {
        this.rootExp=new Exception(errorMsg,rootExp);
    }
    
    /**
     * 
     * 
     * @param errorMsg String
     * @param rootT Throwable
     */
    public MBSBaseException(String errorMsg,Throwable rootT) {
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
    
    
    
    
    /**
	 * @return the exceptionLookupPOLst
	 */
	public List<ExceptionLookupPO> getExceptionLookupPOLst() {
		return exceptionLookupPOLst;
	}

	/**
	 * @param exceptionLookupPOLst the exceptionLookupPOLst to set
	 */
	public void setExceptionLookupPOLst(List<ExceptionLookupPO> exceptionLookupPOLst) {
		this.exceptionLookupPOLst = exceptionLookupPOLst;
	}

	/**
     * 
     * @return HttpStatus
     */
    public String getExceptionCode() {
        if(this instanceof MBSSystemException){
            return TradeConstants.HTTP_STATUS_INTERNAL_SERVER;
        } else if (this instanceof MBSBusinessException){
            return TradeConstants.HTTP_STATUS_BAD_REQUEST;
        }else if (this instanceof MBSBusinessWarning){
            return TradeConstants.HTTP_STATUS_BAD_REQUEST;
        }else if (this instanceof MBSSystemWarning){
            return TradeConstants.HTTP_STATUS_BAD_REQUEST;
        } 
        //default
        return TradeConstants.HTTP_STATUS_INTERNAL_SERVER;
    }

    /**
     * @return the exceptionLookupPO
     */
    public ExceptionLookupPO getExceptionLookupPO() {
        return exceptionLookupPO;
    }

    /**
     * @param exceptionLookupPO the exceptionLookupPO to set
     */
    public void setExceptionLookupPO(ExceptionLookupPO exceptionLookupPO) {
        this.exceptionLookupPO = exceptionLookupPO;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MBSBaseException [rootExp=" + rootExp + ", processId=" + processId + ", exceptionLookupPO="
				+ exceptionLookupPO + ", exceptionLookupPOLst=" + exceptionLookupPOLst + "]";
	}
   
    
}
