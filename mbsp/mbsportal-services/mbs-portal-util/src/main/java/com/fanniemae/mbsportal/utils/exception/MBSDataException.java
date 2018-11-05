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

/**
 * 
 * This is the base class for data validation exceptions
 * 
 * @author FannieMae
 *
 */
public class MBSDataException extends MBSBaseException {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;
	
	private ExceptionResponsePO excpRespPO;

	/**
	 * 
	 * @param errMsg
	 *            String
	 */
	public MBSDataException(String errMsg) {
		super(errMsg, MBSExceptionConstants.BUSINESS_EXCEPTION);
	}
	
	/**
	 * @param excpMsgPO
	 */
	public MBSDataException(String errMsg, ExceptionResponsePO excpRespPO) {
		super(errMsg, MBSExceptionConstants.BUSINESS_EXCEPTION);
		this.excpRespPO = excpRespPO;
	}

	/**
	 * @return the excpRespPO
	 */
	public ExceptionResponsePO getExcpRespPO() {
		return excpRespPO;
	}

	/**
	 * @param excpRespPO the excpRespPO to set
	 */
	public void setExcpRespPO(ExceptionResponsePO excpRespPO) {
		this.excpRespPO = excpRespPO;
	}
}
