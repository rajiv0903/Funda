/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * Util class for Pricing
 *
 * @author g8upjv
 *
 */

public class PricingUtil {

    /**
     * 
     * LOGGER Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingUtil.class);

    /**
     * 
     * TWO_ZERO String
     */
    private static final String TWO_ZERO = "00";

    /**
     * 
     * ONE_ZERO String
     */
    private static final String ONE_ZERO = "0";

    /**
     * 
     * getHandle from string
     * 
     * @param price
     * @return String
     */
    public static String getHandle(String price) {
        if (StringUtils.isBlank(price)) {
            return StringUtils.EMPTY;
        } else {
            if (NumberUtils.isCreatable(price)) {
                if (price.contains(".")) {
                    String[] str = price.split("\\.");
                    return str[0];
                } else {
                    return price;
                }
            } else {
                return StringUtils.EMPTY;
            }
        }
    }

    /**
     * getTicks from string
     * 
     * @param price
     * @return
     * @throws MBSBaseException
     */
    public static String getTicks(String price) throws MBSBaseException {
        try {
            if (StringUtils.isBlank(price)) {
                return StringUtils.EMPTY;
            } else {
                if (NumberUtils.isCreatable(price)) {
                    if (price.contains(".")) {
                        // Separate the decimals
                        String[] str = price.split("\\.");
                        // If the decimals value is 0, return "00"
                        if (Integer.valueOf(str[1]) != 0) {
                            // Multiply by 32
                            String ticksTemp = MBSPortalUtils.priceConvertToValues("." + str[1], 32, 9);
                            // Separate the decimals for the eights
                            if (ticksTemp.contains(".")) {
                                String[] ticks = ticksTemp.split("\\.");
                                // Multiply by 8
                                String eights = MBSPortalUtils.priceConvertToValues("." + ticks[1], 8, 0);
                                String ticksMain = ticks[0];
                                // Add a zero if the ticks main part has only one
                                // value
                                if (ticksMain.length() == 1) {
                                    ticksMain = ONE_ZERO + ticksMain;
                                }
                                // If eights has a zero ignore the zero, else return
                                // the ticks after the + sign check
                                if (ONE_ZERO.equalsIgnoreCase(eights)) {
                                    return ticksMain;
                                } else {
                                    return MBSPortalUtils.pricePercentTicksDisplayText(ticksMain + eights);
                                }
                            } else {
                                return ticksTemp;
                            }
                        } else {
                            return TWO_ZERO;
                        }
                    } else {
                        return TWO_ZERO;
                    }
                } else {
                    return StringUtils.EMPTY;
                }
            }
        } catch (MBSBaseException ex) {
            LOGGER.error("Error when parsing price from string price :", ex);
            throw ex;
        }
    }
}
