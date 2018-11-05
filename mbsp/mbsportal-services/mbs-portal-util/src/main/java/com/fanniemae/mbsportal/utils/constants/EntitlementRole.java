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

package com.fanniemae.mbsportal.utils.constants;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 21, 2018
 * @File: com.fanniemae.mbsportal.utils.constants.EntitlementRole.java
 * @Revision : 
 * @Description: CMMBSSTA01-1293: (RAM Role) Migrate internal roles to Active Directory
 */
public class EntitlementRole {

    /*
     * Fannie Mae - External Lender Execute Role
     */
    public final static String LENDER_TRADE_EXECUTE = "MBS Trade - Execute";
    
    /*
     * Fannie Mae - Internal Trader Roles 
     */
    public final static String TRADER_TRADE_EXECUTE = "FM Trader - Execute";
    public final static String ADMIN = "Administrator";
    
    /*
     * Fannie Mae - Internal Trader Roles- Latest  
     */
    public final static String MBSP_FM_TRADER_EXECUTE_HE = "SG-MBSP-Prod-FM-TRADER-EXECUTE";
    public final static String MBSP_FM_ADMIN_HE = "SG-MBSP-Prod-ADMINISTRATOR";
    public final static String MBSP_FM_TRADER_EXECUTE_LE = "SG-MBSP-NonProd-FM-TRADER-EXECUTE";
    public final static String MBSP_FM_ADMIN_LE = "SG-MBSP-NonProd-ADMINISTRATOR";

    /*
     * Fannie Mae - Internal Trader Read Only Role - Latest  
     */
    public final static String MBSP_FM_TRADER_READ_ONLY_HE = "SG-MBSP-Prod-FM-TRADER-READ-ONLY";
    public final static String MBSP_FM_TRADER_READ_ONLY_LE = "SG-MBSP-NonProd-FM-TRADER-READ-ONLY";
    
    /*
     * Fannie Mae - External TSP Execute Role 
     */
    public final static String TSP_TRADE_EXECUTE = "MBS Trade_TSP - Execute";

    public final static String[] SESSION_ID_SUPPORTED_ROLES = { TRADER_TRADE_EXECUTE, ADMIN,
            MBSP_FM_TRADER_EXECUTE_HE, MBSP_FM_ADMIN_HE, MBSP_FM_TRADER_READ_ONLY_HE,
            MBSP_FM_TRADER_EXECUTE_LE, MBSP_FM_ADMIN_LE, MBSP_FM_TRADER_READ_ONLY_LE};
}
