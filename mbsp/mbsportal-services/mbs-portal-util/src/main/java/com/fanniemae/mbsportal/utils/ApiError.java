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

package com.fanniemae.mbsportal.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class Name: ApiError 
 * Purpose : This class has the API error response and message
 * 
 * @author g8upjv
 *
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiError {

    /**
     * The messsage to accompany the error
     */
    @XmlElement
    private String message;

    /**
     * Constructor
     * @param message
     */
    public ApiError(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
