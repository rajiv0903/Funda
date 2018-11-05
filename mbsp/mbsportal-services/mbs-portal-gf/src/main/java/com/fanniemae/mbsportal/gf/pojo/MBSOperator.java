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

package com.fanniemae.mbsportal.gf.pojo;

/**
 * 
 * This enum stores the operation params
 * 
 * @author g8upjv
 *
 */
public enum MBSOperator {
    
    /**
     * 
     * Greater than for date
     */
    GREATER_THAN_DATE,
    /**
     * 
     * Lesser than for Date
     */
    LESSER_THAN_DATE,
    /**
     * 
     * For equal to operation, =
     */
    EQUAL,
    /**
     * 
     * For IN clause
     * 
     */
    IN,
    /**
     * 
     * For NOT IN clause
     * 
     */
    NOT_IN

}
