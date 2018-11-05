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

import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;

/**
 * This class is the presentation
 * model for Product object
 * 
 * @author g8upjv
 * 
 */
public class ProductIdPO implements Serializable {

	/**
	 * 
	 * serialVersionUID long value
	 */
	private static final long serialVersionUID = -3553433810461830541L;
	/**
	 * 
	 * identifier Product identifier
	 */
	private Long identifier;
	/**
	 * 
	 * sourceType Source Type of the Product
	 */
	private PRODUCT_SOURCE_TYPE sourceType;
	/**
	 * 
	 * type Product type
	 */
	private PRODUCT_TYPE type;

	/**
	 * 
	 * @return long the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 * @return Long the identifier
	 */
	public Long getIdentifier() {
		return identifier;
	}

	/**
	 * 
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	/**
	 * 
	 * @return PRODUCT_SOURCE_TYPE the sourceType
	 */
	public PRODUCT_SOURCE_TYPE getSourceType() {
		return sourceType;
	}

	/**
	 * 
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(PRODUCT_SOURCE_TYPE sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * 
	 * @return PRODUCT_TYPE the type
	 */
	public PRODUCT_TYPE getType() {
		return type;
	}

	/**
	 * 
	 * @param type the type to set
	 */
	public void setType(PRODUCT_TYPE type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return "ProductIdPO{" + "identifier=" + identifier + ", sourceType=" + sourceType + ", type=" + type + '}';
	}
}
