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

import com.fanniemae.mbsportal.id.ProductId;

/**
 * 
 * The MBS Pricing Product domain class
 * 
 * @author g8upjv
 */
public class MBSProduct extends MBSBaseEntity {

    private static final long serialVersionUID = -2636473423763296551L;

    /**
     * The product id is the key for the Product
     */
    private ProductId productId;

    /**
     * The product name code value. Example: FN30 for Fannie Mae 30yr Product
     */
    private String productNameCode;

    /**
     * The blackrock product name code value. Example: PC30 will have different
     * BR name for Fannie Mae 30yr Product
     */
    private String productBRSCode;
    /**
     * The PU product name code value. Example: PC30 will have different
     * PU name FR30 and GN products are called G1 in PU
     */
    private String productPUCode;
    /**
     * Portfolio shortname
     */
    private String brsSubPortfolioShortName;
    /**
     * The product name code display order number.
     */
    private int productNameCodeDisplayOrderNum;
    /**
     * The agency type for product. Example: Fanniemae
     */
    private String productAgencyType;
    /**
     * Security term for the product. Example: 360 (months) for a 30 year term
     */
    private int securityTerm;
    /**
     * THe product long description
     */
    private String productDescription;

    /**
     * @return the productNameCode
     */
    public String getProductNameCode() {
        return productNameCode;
    }

    /**
     * @param productNameCode
     *            the productNameCode to set
     */
    public void setProductNameCode(String productNameCode) {
        this.productNameCode = productNameCode;
    }

    public String getBrsSubPortfolioShortName() {
        return brsSubPortfolioShortName;
    }

    public void setBrsSubPortfolioShortName(String brsSubPortfolioShortName) {
        this.brsSubPortfolioShortName = brsSubPortfolioShortName;
    }

    public String getProductBRSCode() {
        return productBRSCode;
    }

    public void setProductBRSCode(String productBRSCode) {
        this.productBRSCode = productBRSCode;
    }
    
    public String getProductPUCode() {
        return productPUCode;
    }
    
    public void setProductPUCode(String productPUCode) {
        this.productPUCode = productPUCode;
    }
    
    /**
     * @return the productAgencyType
     */
    public String getProductAgencyType() {
        return productAgencyType;
    }

    /**
     * @param productAgencyType
     *            the productAgencyType to set
     */
    public void setProductAgencyType(String productAgencyType) {
        this.productAgencyType = productAgencyType;
    }

    /**
     * @return the productDescription
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * @param productDescription
     *            the productDescription to set
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * @return the securityTerm
     */
    public int getSecurityTerm() {
        return securityTerm;
    }

    /**
     * @param securityTerm
     *            the securityTerm to set
     */
    public void setSecurityTerm(int securityTerm) {
        this.securityTerm = securityTerm;
    }

    /**
     * @return the productId
     */
    public ProductId getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    /**
     * @return Serializable
     */
    @Override
    public Serializable getId() {
        // Partition(0,prodctstr)
        return this.productId.getProductIdStr();
    }

    /**
     * @return the productNameCodeDisplayOrderNum
     */
    public int getProductNameCodeDisplayOrderNum() {
        return productNameCodeDisplayOrderNum;
    }

    /**
     * @param productNameCodeDisplayOrderNum
     *            the productNameCodeDisplayOrderNum to set
     */
    public void setProductNameCodeDisplayOrderNum(int productNameCodeDisplayOrderNum) {
        this.productNameCodeDisplayOrderNum = productNameCodeDisplayOrderNum;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MBSProduct [productId=" + productId + ", productNameCode=" + productNameCode + ", productBRSCode="
                + productBRSCode + ", brsSubPortfolioShortName=" + brsSubPortfolioShortName
                + ", productNameCodeDisplayOrderNum=" + productNameCodeDisplayOrderNum + ", productAgencyType="
                + productAgencyType + ", securityTerm=" + securityTerm + ", productDescription=" + productDescription
                + ", createdOn=" + createdOn + ", lastUpdated=" + lastUpdated + ", lastUpdatedBy=" + lastUpdatedBy
                + ", createdBy=" + createdBy + "]";
    }

    
}
