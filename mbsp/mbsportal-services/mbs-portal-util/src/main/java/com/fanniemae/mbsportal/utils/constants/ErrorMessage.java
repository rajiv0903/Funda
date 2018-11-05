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

public enum ErrorMessage {

    LENDER_INVALID_TRADE_AMOUNT("Invalid amount. Amount must be between 25,000 to 250,000,000"),
    //INVALID_HANDLE_OR_TICK("Invalid Value"),
    INVALID_VERSION_TO_PERFORM_ACTION("INVALID_VERSION_TO_PERFORM_ACTION"),
    INVALID_MAPPED_LENDER_TO_PERFORM_ACTION("INVALID_MAPPED_LENDER_TO_PERFORM_ACTION"),
    INVALID_MAPPED_TRADER_TO_PERFORM_ACTION("INVALID_MAPPED_TRADER_TO_PERFORM_ACTION"),
    LENDER_TRADE_REQUEST_ALREADY_PRESENT("LENDER_TRADE_REQUEST_ALREADY_PRESENT"),
    FAILED_TO_AUTHENTICATE_FOR_HANDSHALKE("FAILED_TO_AUTHENTICATE_FOR_HANDSHALKE");
    
    private String value;

    ErrorMessage(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
