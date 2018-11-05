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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 23, 2018
 * @File: com.fanniemae.mbsportal.api.po.PartyPO.java 
 * @Revision: 
 * @Description: PartyPO.java
 */
@JsonInclude(Include.NON_NULL)
public class PartyPO implements Serializable {


   /**
     * 
     * serialVersionUID long
     */
    private static final long serialVersionUID = 6489962136258912121L;

    /**
     * 
     * sellerServicerNumber String
     */
    private String sellerServicerNumber;
    
    /**
     * 
     * name String - dealerOrgName
     */
    private String name; 
    
    /**
     * 
     * shortName String
     */
    private String shortName;
    
    /**
     * 
     * effectiveDate String
     */
    private String effectiveDate;
    
    /**
     * 
     * expirationDate String
     */
    private String expirationDate;
    
    /**
     * 
     * nameEffectiveDate String
     */
    private String nameEffectiveDate;
    
    /**
     * 
     * nameExpirationDate String
     */
    private String nameExpirationDate;
    
    /**
     * 
     * stateType String
     */
    private String stateType;
    
    /**
     * 
     * institutionType String
     */
    private String institutionType;
   
    
    /**
     * 
     * tspPartyLenders List
     * 
     */
    private List<TspPartyLenderPO> tspPartyLenders;
    
    /**
     * 
     * mbspPortalCnameUrlBase String
     */
    private String mbspPortalCnameUrlBase;
    
    /**
     * 
     * mbspStreamingUrlBase String
     */
    private String mbspStreamingUrlBase;


    
    

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
     * @return the sellerServicerNumber
     */
    public String getSellerServicerNumber() {
        return sellerServicerNumber;
    }

    /**
     * 
     * @param sellerServicerNumber the sellerServicerNumber to set
     */
    public void setSellerServicerNumber(String sellerServicerNumber) {
        this.sellerServicerNumber = sellerServicerNumber;
    }


    /**
     * 
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
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
     * @return the nameEffectiveDate
     */
    public String getNameEffectiveDate() {
        return nameEffectiveDate;
    }

    /**
     * 
     * @param nameEffectiveDate the nameEffectiveDate to set
     */
    public void setNameEffectiveDate(String nameEffectiveDate) {
        this.nameEffectiveDate = nameEffectiveDate;
    }

    /**
     * 
     * @return the nameExpirationDate
     */
    public String getNameExpirationDate() {
        return nameExpirationDate;
    }

    /**
     * 
     * @param nameExpirationDate the nameExpirationDate to set
     */
    public void setNameExpirationDate(String nameExpirationDate) {
        this.nameExpirationDate = nameExpirationDate;
    }

    /**
     * 
     * @return the stateType
     */
    public String getStateType() {
        return stateType;
    }

    /**
     * 
     * @param stateType the stateType to set
     */
    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    /**
     *
     * @return the institutionType
     */
    public String getInstitutionType() {
        return institutionType;
    }

    /**
     * 
     * @param institutionType the institutionType to set
     */
    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }
    
    /**
     * 
     * @return the tspPartyLenders
     */
    public List<TspPartyLenderPO> getTspPartyLenders() {
        return tspPartyLenders;
    }

    /**
     * @param tspPartyLenders the tspPartyLenders to set
     */
    public void setTspPartyLenders(List<TspPartyLenderPO> tspPartyLenders) {
        this.tspPartyLenders = tspPartyLenders;
    }

    /**
     * 
     * @return the mbspPortalCnameUrlBase
     */
    public String getMbspPortalCnameUrlBase() {
        return mbspPortalCnameUrlBase;
    }

    /**
     * 
     * @param mbspPortalCnameUrlBase the mbspPortalCnameUrlBase
     */
    public void setMbspPortalCnameUrlBase(String mbspPortalCnameUrlBase) {
        this.mbspPortalCnameUrlBase = mbspPortalCnameUrlBase;
    }

    /**
     * 
     * @return the mbspStreamingUrlBase
     */
    public String getMbspStreamingUrlBase() {
        return mbspStreamingUrlBase;
    }

    /**
     * 
     * @param mbspStreamingUrlBase the mbspStreamingUrlBase
     */
    public void setMbspStreamingUrlBase(String mbspStreamingUrlBase) {
        this.mbspStreamingUrlBase = mbspStreamingUrlBase;
    }

    /**
     * 
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PartyPO [sellerServicerNumber=" + sellerServicerNumber + ", name=" + name + ", shortName=" + shortName
                + ", effectiveDate=" + effectiveDate + ", expirationDate=" + expirationDate + ", nameEffectiveDate="
                + nameEffectiveDate + ", nameExpirationDate=" + nameExpirationDate + ", stateType=" + stateType
                + ", institutionType=" + institutionType + ", tspPartyLenders=" + tspPartyLenders
                + ", mbspPortalCnameUrlBase=" + mbspPortalCnameUrlBase + ", mbspStreamingUrlBase="
                + mbspStreamingUrlBase + "]";
    }

    
	
}
