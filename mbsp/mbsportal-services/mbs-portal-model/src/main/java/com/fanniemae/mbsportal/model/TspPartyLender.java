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

package com.fanniemae.mbsportal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 23, 2018
 * @File: com.fanniemae.mbsportal.model.TspPartyLender.java 
 * @Revision: 
 * @Description: TspPartyLender.java
 */
public class TspPartyLender implements Serializable {

    /**
     * 
     * serialVersionUID long
     */
    private static final long serialVersionUID = 2066411634554600194L;
    /**
     * 
     * lenderSellerServicerNumber String
     */
    private String lenderSellerServicerNumber;
    /**
     * 
     * effectiveDate Date
     */
    private Date effectiveDate;
    /**
     * 
     * expirationDate Date
     */
    private Date expirationDate;
    /**
     * 
     * tspSellerServicerNumber String
     */
    private String tspSellerServicerNumber;
    
    /**
     * 
     * name String
     */
    private String name;
    
    /**
     * 
     * shortName String
     */
    private String shortName;
    
    public TspPartyLender(){}
    
    
    /**
     * 
     * Constructor to initialize the object
     * 
     * @param lenderSellerServicerNumber the lenderSellerServicerNumber
     * @param effectiveDate the effectiveDate
     * @param expirationDate the expirationDate
     * @param tspSellerServiceNumber the tspSellerServiceNumber
     */
    public TspPartyLender(String lenderSellerServicerNumber, Date effectiveDate, Date expirationDate, String tspSellerServicerNumber, String name, String shortName){
        
        this.lenderSellerServicerNumber = lenderSellerServicerNumber;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.tspSellerServicerNumber = tspSellerServicerNumber;
        this.name = name;
        this.shortName = shortName;
    }
    
    
    /**
     * 
     * @return the lenderSellerServicerNumber
     */
    public String getLenderSellerServicerNumber() {
        return lenderSellerServicerNumber;
    }

    /**
     * 
     * @param lenderSellerServicerNumber the lenderSellerServicerNumber to set
     */
    public void setLenderSellerServicerNumber(String lenderSellerServicerNumber) {
        this.lenderSellerServicerNumber = lenderSellerServicerNumber;
    }


    /**
     * 
     * @return the effectiveDate
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    
    /**
     * 
     * @param effectiveDate the effectiveDate to set
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    /**
     * 
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }
    
    /**
     * 
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    

    /**
     * 
     * @return the tspSellerServicerNumber
     */
    public String getTspSellerServicerNumber() {
        return tspSellerServicerNumber;
    }

    /**
     * 
     * @param tspSellerServicerNumber the tspSellerServicerNumber to set
     */
    public void setTspSellerServicerNumber(String tspSellerServicerNumber) {
        this.tspSellerServicerNumber = tspSellerServicerNumber;
    }


    /**
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @return shortName
     */
    public String getShortName() {
        return shortName;
    }
    /**
     * 
     * @param shortName the shortName
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    /**
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */


    @Override
    public String toString() {
        return "TspPartyLender [lenderSellerServicerNumber=" + lenderSellerServicerNumber + ", effectiveDate="
                + effectiveDate + ", expirationDate=" + expirationDate + ", tspSellerServicerNumber="
                + tspSellerServicerNumber + ", name=" + name + ", shortName=" + shortName + "]";
    }
    
    
    

}
