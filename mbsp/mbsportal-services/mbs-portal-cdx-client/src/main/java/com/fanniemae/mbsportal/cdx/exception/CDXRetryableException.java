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
 * @File: com.fanniemae.mbsportal.cdx.exception.CDXRetryableException.java
 * @Revision:
 * @Description: CDXRetryableException.java
 */
public class CDXRetryableException extends CDXBaseException {

    private static final long serialVersionUID = 6407005224349091728L;

    /**
     *
     * @param errMsg
     *            String
     */
    public CDXRetryableException(String errMsg) {
        super(errMsg, CDXExceptionConstants.SYSTEM_EXCEPTION);
    }

    /**
     *
     * @param errMsg
     *            String
     * @param processId
     *            long
     */
    public CDXRetryableException(String errMsg, long processId) {
        super(errMsg, processId);
    }

    /**
     * 
     * @param errMsg
     * @param rootExp
     */
    public CDXRetryableException(String errMsg, Exception rootExp) {
        super(errMsg, CDXExceptionConstants.SYSTEM_EXCEPTION, rootExp);
    }

    /**
     *
     * @param errMsg
     * @param processId
     * @param rootExp
     */
    public CDXRetryableException(String errMsg, long processId, Exception rootExp) {
        super(errMsg, processId, rootExp);
    }

}
