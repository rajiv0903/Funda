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

package com.fanniemae.mbsportal.constants;

import java.io.Serializable;

/**
 * @author g8uaxt Created on 12/21/2017.
 */

public enum RegionColumnList implements Serializable {
	// table column for Transaction request
	transReqId("transReqNumber"), productNameCode("productNameCode"), tradeAmount("tradeAmount"), stateType(
			"stateTypeOrder"), pricePercentHandleText("pricePercentTicksText"), pricePercent(
					"pricePercent"), submissionDate("submissionDate"), tradeDate("acceptanceDate"), tradeSettlementDate(
							"tradeSettlementDate"), tradeCouponRate("tradeCouponRate"), tradeBuySellType(
									"tradeBuySellType"), lenderBuySellType("counterPartyBuySellType"), traderName(
											"traderName"), lenderName("counterpartyTraderName"), lenderEntityName(
													"dealerOrgName"), tradeSrcPrimaryId(
															"acceptanceDate"), tradeSubPortfolioShortName(
																	"subPortfolioShortName"),
	// CMMBSSTA01-1373 - Adding TSP name - Start
	tspShortName("tspShortName"), empty("");
	// CMMBSSTA01-1373 - Adding TSP name - End

	// Add TSP field

	private String sortName;

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	RegionColumnList(String name) {
		this.sortName = name;
	}

	/**
	 * get sortName
	 * 
	 * @param name
	 * @return
	 */
	public static RegionColumnList getEnum(String name) {
		if (name == null) {
			return null;
		}
		if (name.equals("") || "empty".equals(name)) {
			return RegionColumnList.empty;
		}
		for (RegionColumnList regionColumnList : RegionColumnList.values()) {
			if (regionColumnList.getSortName().equalsIgnoreCase(name)) {
				return regionColumnList;
			}
		}
		return null;
	}

	/**
	 *
	 * @return String
	 */
	public String getSortName() {
		return this.sortName;
	}

}
