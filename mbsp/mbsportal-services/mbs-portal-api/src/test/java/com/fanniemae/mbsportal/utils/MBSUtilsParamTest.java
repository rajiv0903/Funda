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

package com.fanniemae.mbsportal.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Created by g8uaxt on 8/10/2017.
 */
@RunWith(Parameterized.class)
public class MBSUtilsParamTest {
    
    //private static final Logger LOGGER = LoggerFactory.getLogger(MBSPortalUtils.class);
    @Parameterized.Parameter(value = 0)
    public String numberInString;
 
    @Parameterized.Parameter(value = 1)
    public int precision;
    
    @Parameterized.Parameter(value = 2)
    public int scale;
    
    @Parameterized.Parameter(value =3)
    public BigDecimal number;
    

    
    @Parameterized.Parameters(name = "{index}: convertToBigDecimal({0},{1},{2} = {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(
            new Object[][] {
                { "4.010",5,3, new BigDecimal(4.010, new MathContext(5, RoundingMode.HALF_EVEN)).setScale(3) },
                { "4.011",5,3, new BigDecimal(4.011, new MathContext(5, RoundingMode.HALF_EVEN)).setScale(3) },
                { "12.000",5,3, new BigDecimal(12.000, new MathContext(5, RoundingMode.HALF_EVEN)).setScale(3) },
                { "99.999",5,3, new BigDecimal(99.999, new MathContext(5, RoundingMode.HALF_EVEN)).setScale(3) },
                { "3.500",5,3, new BigDecimal(3.500, new MathContext(5, RoundingMode.HALF_EVEN)).setScale(3) },
                { "4.000",5,3, new BigDecimal(4.000, new MathContext(5, RoundingMode.HALF_EVEN)).setScale(3) },
                { "9000000.000",13,3, new BigDecimal(9000000, new MathContext(13, RoundingMode.HALF_EVEN))
                    .setScale(3) },
               // { "90.000",13,0, new BigDecimal(90.001, new MathContext(13, RoundingMode.HALF_EVEN))}
             });
    }
    
    @Test
    public void convertToBigDecimal() throws MBSBaseException {
        assertThat(MBSPortalUtils.convertToBigDecimal(numberInString, precision,scale), is(number));
    }
    
    
    @Test
    public void convertString() throws MBSBaseException {
        assertThat(MBSPortalUtils.convertToString(number, precision,scale), is(numberInString));
    }
    
    
}
