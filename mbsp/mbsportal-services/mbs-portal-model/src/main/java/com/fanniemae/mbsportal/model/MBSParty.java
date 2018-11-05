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
import java.util.List;

/**
 * 
 * The MBS Party domain class
 * 
 * @author g8upjv
 */
public class MBSParty extends MBSBaseEntity {

    /**
     * serialVersionUID  long
     */
    private static final long serialVersionUID = -8353079495592487838L;

    /**
     * 
     * sellerServicerNumber String
     */
    private String sellerServicerNumber;
    
    /**
     * 
     * dealerOrgName String
     * Name Comment
     */
    private String name; 
    
    /**
     * 
     * shortName String
     */
    private String shortName;
    
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
     * nameEffectiveDate Date
     */
    private Date nameEffectiveDate;
    
    /**
     * 
     * nameExpirationDate Date
     */
    private Date nameExpirationDate;
    
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
     * the tspSellerServiceNumber in this list should be the party ssn for mapping purposes.
     */
    private List<TspPartyLender> tspPartyLenders;
    
    
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
     * @return the nameEffectiveDate
     */
    public Date getNameEffectiveDate() {
        return nameEffectiveDate;
    }

    /**
     * 
     * @param nameEffectiveDate the nameEffectiveDate to set
     */
    public void setNameEffectiveDate(Date nameEffectiveDate) {
        this.nameEffectiveDate = nameEffectiveDate;
    }

    /**
     * 
     * @return the nameExpirationDate
     */
    public Date getNameExpirationDate() {
        return nameExpirationDate;
    }

    /**
     * 
     * @param nameExpirationDate the nameExpirationDate to set
     */
    public void setNameExpirationDate(Date nameExpirationDate) {
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
    public List<TspPartyLender> getTspPartyLenders() {
        return tspPartyLenders;
    }
    /**
     * 
     * @param tspPartyLenders the tspPartyLenders to set
     */
    public void setTspPartyLenders(List<TspPartyLender> tspPartyLenders) {
        this.tspPartyLenders = tspPartyLenders;
    }

    /**
     * 
     * @return String
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
     * @return String
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
     * @return Serializable
     */
    @Override
    public Serializable getId() {
        return sellerServicerNumber;
    }

    /**
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "MBSParty [sellerServicerNumber=" + sellerServicerNumber + ", name=" + name + ", shortName=" + shortName
                + ", effectiveDate=" + effectiveDate + ", expirationDate=" + expirationDate + ", nameEffectiveDate="
                + nameEffectiveDate + ", nameExpirationDate=" + nameExpirationDate + ", stateType=" + stateType
                + ", institutionType=" + institutionType + ", tspPartyLenders="
                + tspPartyLenders + ", mbspPortalCnameUrlBase=" + mbspPortalCnameUrlBase + ", mbspStreamingUrlBase="
                + mbspStreamingUrlBase + "]";
    }
    
    

    
}
