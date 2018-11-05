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


package com.fanniemae.mbsportal.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * Class Name: ProductPO Purpose : This class is the presentation
 * model for Product object
 * 
 * @author g8upjv
 * 
 */
@JsonInclude(Include.NON_NULL)
public class ProductPO implements Serializable {

	/**
	 * generated Serial id
	 */
	private static final long serialVersionUID = -7391732330415321183L;

	/**
	 * Product identifier
	 */
	private ProductIdPO productId;

	/**
	 * The product name code value. Example: FN30 for Fannie Mae 30yr Product
	 */
	private String nameCode;

	/**
	 * The blackrock product name code value. Example: PC30 will have different BR name for Fannie Mae 30yr Product
	 */
	private String brsNameCode;

	
	/**
	 * portfolio short name
	 */
	private String brsSubPortfolioShortName;
	
	/**
	 * The product name code sort order. 
	 */
	private int nameCodeSortOrder;
	/**
	 * The agency type for product. Example: Fanniemae
	 */
	private String agencyType;
	/**
	 * Security term for the product. Example: 360 (months) for a 30 year term
	 */
	private int securityTerm;
	/**
	 * THe product long description
	 */
	private String description;

	/**
	 * @return the NameCode
	 */
	public String getNameCode() {
		return nameCode;
	}

	/**
	 * @param nameCode
	 *            the NameCode to set
	 */
	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
	}
	
	/**
	 *
	 * @return
	 */
	public String getBrsNameCode() {
		return brsNameCode;
	}
	
	/**
	 *
	 * @param brsNameCode
	 */
	public void setBrsNameCode(String brsNameCode) {
		this.brsNameCode = brsNameCode;
	}
	
	public String getBrsSubPortfolioShortName() {
		return brsSubPortfolioShortName;
	}
	
	public void setBrsSubPortfolioShortName(String brsSubPortfolioShortName) {
		this.brsSubPortfolioShortName = brsSubPortfolioShortName;
	}
	/**
	 * @return the AgencyType
	 */
	public String getAgencyType() {
		return agencyType;
	}

	/**
	 * @param agencyType
	 *            the AgencyType to set
	 */
	public void setAgencyType(String agencyType) {
		this.agencyType = agencyType;
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
	 * @return the Description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the Description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the nameCodeSortOrder
	 */
	public int getNameCodeSortOrder() {
		return nameCodeSortOrder;
	}

	/**
	 * @param nameCodeSortOrder the nameCodeSortOrder to set
	 */
	public void setNameCodeSortOrder(int nameCodeSortOrder) {
		this.nameCodeSortOrder = nameCodeSortOrder;
	}

	/**
	 * @return the productIdPO
	 */
	public ProductIdPO getProductId() {
		return productId;
	}

	/**
	 * @param productId the productIdPO to set
	 */
	public void setProductId(ProductIdPO productId) {
		this.productId = productId;
	}
	
	
	/**
	 * @return String
	 */
	public String retrieveKey() {
		if(!Objects.equals(getProductId().getIdentifier(), null) && !Objects.equals(getProductId().getSourceType(), null) && !Objects.equals(getProductId().getType(), null)){
			return getProductId().getIdentifier()+"."+getProductId().getSourceType().toString()+"."+getProductId().getType().toString();
		} else {
			return StringUtils.EMPTY;
		}
	}

    @Override
    public String toString() {
        return "ProductPO [productId=" + productId + ", nameCode=" + nameCode + ", brsNameCode=" + brsNameCode
                + ", nameCodeSortOrder=" + nameCodeSortOrder + ", agencyType=" + agencyType + ", securityTerm="
                + securityTerm + ", description=" + description + "]";
    }
	
	
	
}
