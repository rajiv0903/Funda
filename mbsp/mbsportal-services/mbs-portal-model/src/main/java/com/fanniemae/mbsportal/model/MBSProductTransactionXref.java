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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.fanniemae.fnmpfj.gemfire.dao.PartitionResolverCompoundKey;

/**
 * 
 * Model class for cross reference for active prod and transaction
 * Created by g8upjv on 7/19/2018.
 */
public class MBSProductTransactionXref extends MBSBaseEntity {
    
    /**
     *
     * serialVersionUID long
     */
    private static final long serialVersionUID = 8301472395643679974L;
    
    /**
     * 
     * productNameCode String
     */
    private String productNameCode;
    
    /**
     * 
     * passThroughRate BigDecimal
     */
    private BigDecimal passThroughRate;
    
    /**
     * 
     * settlementDate Date
     */
    private Date settlementDate;
    
    /**
     * 
     * transReqIdSet String
     */
    private Set<String> transReqIdSet;
    
    /**
     * 
     * MBSProductTransactionXref constructor
     */
    public MBSProductTransactionXref() {
    }
    
    /**
     * 
     * @param productNameCode the productNameCode to set
     * @param passThroughRate the observationDate to set
     * @param settlementDate the settlementDate to set
     */
    public MBSProductTransactionXref(String productNameCode, BigDecimal passThroughRate, Date settlementDate) {
        
       this.productNameCode = productNameCode;
       this.passThroughRate = passThroughRate;
       this.settlementDate = settlementDate;
    }
    
    /**
     * 
     * @return Serializable
     */
    @Override
    public Serializable getId() {
        return new PartitionResolverCompoundKey(0, this.getProductNameCode(), 
                this.getPassThroughRate(), this.getSettlementDate());
    }

    /**
     * @return the productNameCode
     */
    public String getProductNameCode() {
        return productNameCode;
    }


    /**
     * @param productNameCode the productNameCode to set
     */
    public void setProductNameCode(String productNameCode) {
        this.productNameCode = productNameCode;
    }


    /**
     * @return the passThroughRate
     */
    public BigDecimal getPassThroughRate() {
        return passThroughRate;
    }


    /**
     * @param passThroughRate the passThroughRate to set
     */
    public void setPassThroughRate(BigDecimal passThroughRate) {
        this.passThroughRate = passThroughRate;
    }


    /**
     * @return the settlementDate
     */
    public Date getSettlementDate() {
        return settlementDate;
    }


    /**
     * @param settlementDate the settlementDate to set
     */
    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }


    /**
     * @return the transReqIdSet
     */
    public Set<String> getTransReqIdSet() {
        return transReqIdSet;
    }


    /**
     * @param transReqIdSet the transReqIdSet to set
     */
    public void setTransReqIdSet(Set<String> transReqIdSet) {
        this.transReqIdSet = transReqIdSet;
    }


    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MBSProductTransactionXref [productNameCode=" + productNameCode + ", passThroughRate=" + passThroughRate
                + ", settlementDate=" + settlementDate + ", transReqIdSet=" + transReqIdSet + "]";
    }
    
   
}
