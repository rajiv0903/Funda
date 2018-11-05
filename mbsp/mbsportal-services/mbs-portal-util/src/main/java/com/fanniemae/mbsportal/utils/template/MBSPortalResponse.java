/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils.template;

import java.util.List;

import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;

/**
 * @author g8upjv
 *
 */
public class MBSPortalResponse {
    
    private Object respObj;
    
    private List<ExceptionLookupPO> errors;
    
    public MBSPortalResponse(Object respObj, List<ExceptionLookupPO> errors){
        this.respObj = respObj;
        this.errors = errors;
    }
    
    /**
     * 
     * Public constructor
     */
    public MBSPortalResponse() {

    }

    /**
     * @return the respObj
     */
    public Object getRespObj() {
        return respObj;
    }

    /**
     * @param respObj the respObj to set
     */
    public void setRespObj(Object respObj) {
        this.respObj = respObj;
    }

    /**
     * @return the errors
     */
    public List<ExceptionLookupPO> getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(List<ExceptionLookupPO> errors) {
        this.errors = errors;
    }

    /**
     * 
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder portResp = new StringBuilder();
        portResp.append("MBSPortalResponse [respObj=" + respObj +"]");
        for(ExceptionLookupPO excep : errors){
            portResp.append(" Exception values :"+excep);
        }
        return portResp.toString();
    }
    

}
