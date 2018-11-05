package com.fanniemae.mbsportal.api.constants;

import com.fanniemae.mbsportal.constants.MBSRoleType;

public enum TransactionHistoryHeaders {
	
	REQUEST_TIME ("REQUEST TIME", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	ENTITY("ENTITY", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	TSP("TSP", MBSRoleType.TRADER),
	TRADE_DATE("TRADE DATE", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	BUY_SELL("B/S", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	AMOUNT("AMOUNT", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	PRODUCT("PROD", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	COUPON("CPN", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	SETTLEMENT_DATE("SETTLE", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	PRICE("PRICE", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	STATUS("STATUS", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	TRANSACTION_ID("TRANS ID", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	INVENTORY_NUMBER("INV#", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	PORTFOLIO("PORT", MBSRoleType.TRADER),
	USER_NAME("USER", MBSRoleType.TRADER, MBSRoleType.LENDER, MBSRoleType.TSP),
	TRADER_NAME("FM TRADER", MBSRoleType.TRADER);
	
	private String displayName;
	private MBSRoleType[] allowedRoles;
	
	private TransactionHistoryHeaders(String displayName, MBSRoleType... allowedRoles) {
		this.displayName = displayName;
		this.allowedRoles = allowedRoles;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	public MBSRoleType[] getAllowedRoles() {
		return allowedRoles;
	}
}
