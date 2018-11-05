/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.utils.exception;

/**
 * @author e4umgc
 *
 */
public class DataExceptionMessage {

	/**
	 * erroCode
	 */
	String errorCode;
	
	
	/**
	 * fieldMessage
	 */
	
	String fieldMessage;

	
	public DataExceptionMessage() {
		
	}
	
	public DataExceptionMessage(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public DataExceptionMessage(String errorCode, String fieldMessage) {
		this.errorCode = errorCode;
		this.fieldMessage = fieldMessage;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}


	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	/**
	 * @return the fieldMessage
	 */
	public String getFieldMessage() {
		return fieldMessage;
	}


	/**
	 * @param fieldMessage the fieldMessage to set
	 */
	public void setFieldMessage(String fieldMessage) {
		this.fieldMessage = fieldMessage;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataExceptionMessage [errorCode=" + errorCode + ", fieldMessage=" + fieldMessage + "]";
	}
	
	
	
	
}
