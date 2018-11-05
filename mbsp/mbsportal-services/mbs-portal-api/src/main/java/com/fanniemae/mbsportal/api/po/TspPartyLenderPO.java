/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.po;

import java.io.Serializable;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 23, 2018
 * @File: com.fanniemae.mbsportal.api.po.TspPartyLenderPO.java 
 * @Revision: 
 * @Description: TspPartyLenderPO.java
 */
public class TspPartyLenderPO implements Serializable {

    /**
     * 
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1084657019431660997L;
    
    /**
     * 
     * lenderSellerServicerNumber String
     */
    private String lenderSellerServicerNumber;
    /**
     * 
     * effectiveDate String
     */
    private String effectiveDate;
    /**
     * 
     * expirationDate Date
     */
    private String expirationDate;
    
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
    
  
    public TspPartyLenderPO(){}
    /**
     * 
     * Constructor to initialize the object
     * 
     * @param lenderSellerServicerNumber the lenderSellerServicerNumber
     * @param effectiveDate the effectiveDate
     * @param expirationDate the expirationDate
     * @param name the name
     * @param shortName the shortName
     */
    public TspPartyLenderPO(String lenderSellerServicerNumber, String effectiveDate, String expirationDate, String name, String shortName){
        
        this.lenderSellerServicerNumber = lenderSellerServicerNumber;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
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
    public String getEffectiveDate() {
        return effectiveDate;
    }
    
    /**
     * 
     * @param effectiveDate the effectiveDate to set
     */
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    /**
     * 
     * @return the expirationDate
     */
    public String getExpirationDate() {
        return expirationDate;
    }
    
    /**
     * 
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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
     * 
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TspPartyLenderPO [lenderSellerServicerNumber=" + lenderSellerServicerNumber + ", effectiveDate="
                + effectiveDate + ", expirationDate=" + expirationDate + ", name=" + name + ", shortName=" + shortName
                + "]";
    }
    

}
