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

package com.fanniemae.mbsportal.calendar.client;

import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.calendar.schema.v1.EnterpriseCalendarService;
import com.fanniemae.mbsportal.calendar.schema.v1.EnterpriseCalendarServicePortType;
import com.fanniemae.mbsportal.calendar.schema.v1.GetCalendarDayRequest;
import com.fanniemae.mbsportal.calendar.schema.v1.GetCalendarDayResponse;
import com.fanniemae.mbsportal.calendar.schema.v1.GetLastBusinessDayOfMonthRequest;
import com.fanniemae.mbsportal.calendar.schema.v1.GetLastBusinessDayOfMonthResponse;
import com.fanniemae.mbsportal.calendar.schema.v1.GetListOfCalendarDaysRequest;
import com.fanniemae.mbsportal.calendar.schema.v1.GetListOfCalendarDaysResponse;
import com.fanniemae.mbsportal.calendar.schema.v1.ObjectFactory;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 30, 2017
 * @Time 1:37:11 PM com.fanniemae.mbsportal.calendar.client
 *       EnterpriseCalendarServiceClient.java
 * @Description:
 */
@Component
public class EnterpriseCalendarServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseCalendarServiceClient.class);

    @Autowired
    EpvConnector epvConnector;

    @Value("${mbsp.calendar.client.userId}")
    private String userId;

    @Value("${mbsp.calendar.client.wsdllocaltion}")
    private String wsdllocaltion;

    /**
     * The service that will connect and retrieve calendar data.
     */
    private EnterpriseCalendarService service;

    @EventListener(ApplicationReadyEvent.class)
    public void setUp() throws MBSBaseException {

        try {
            URL baseUrl = com.fanniemae.mbsportal.calendar.schema.v1.EnterpriseCalendarService.class.getResource(".");
            URL url = new URL(baseUrl, wsdllocaltion);
            this.service = new EnterpriseCalendarService(url,
                    new QName("http://www.fanniemae.com/services/wsdl/enterpriseCalendarServiceConcrete_v1.0",
                            "EnterpriseCalendarService"));
            this.setupHandlerResolver(userId, epvConnector.getValutPwd());

        } catch (Exception exe) {

            LOGGER.error("Failed to initialized Calendar service Client", exe);
            throw new MBSSystemException("Failed to initialized Calendar service Client",
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
    }

    public EnterpriseCalendarServiceClient() {
    }

    /**
     * Retrieve a particular Calendar Day 'numDaysInFuture' after the startDate.
     * 
     * @param startDate
     *            - any java.util.Date object
     * @param calendarType
     *            - a Calendar Type value, as supported by ECS. Possible choices
     *            are in CalendarTypes enum.
     * @param numDaysInFuture
     *            - an integer designating how many days in the past or future
     *            the desired date should be.
     * @param countBusinessDays
     *            - when true, all business days will be used in calculations
     *            (holidays, weekends, etc.)
     * @param descIndicator
     *            - is a "detailed" description desired?
     * @return a GetCalendarDayResponse object with the requested value
     * @throws Exception
     */
    public GetCalendarDayResponse getCalendarDay(final Date startDate, final String calendarType,
            final int numDaysInFuture, final boolean countBusinessDays, final boolean descIndicator)
            throws MBSBaseException {

        try {

            final ObjectFactory objFactory = new ObjectFactory();
            final EnterpriseCalendarServicePortType port = this.service.getEnterpriseCalendarServicePortTypeEndpoint();

            final GetCalendarDayRequest req = objFactory.createGetCalendarDayRequest();
            final GetCalendarDayRequest.CalendarDays calDays = objFactory.createGetCalendarDayRequestCalendarDays();
            final JAXBElement<BigInteger> numDays = objFactory
                    .createGetCalendarDayRequestCalendarDaysNumOfDaysInPastOrFuture(
                            BigInteger.valueOf(numDaysInFuture));

            calDays.setCountByBusinessDaysInd(countBusinessDays);
            calDays.setDescriptionIndicator(descIndicator);
            calDays.setNumOfDaysInPastOrFuture(numDays);
            calDays.getCalendarType().add(calendarType);

            GregorianCalendar gCal = new GregorianCalendar();
            gCal.setTime(startDate);
            calDays.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
            req.getCalendarDays().add(calDays);

            return port.opGetCalendarDay(req);

        } catch (Exception exe) {

            LOGGER.error("Exception occur for getCalendarDay", exe);
            throw new MBSSystemException("Exception occur for getCalendarDay", MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
    }

    /**
     * Retrieves a list of calendar days between startDate and endDate.
     * 
     * @param startDate
     *            - any java.util.Date object
     * @param endDate
     *            - a java.util.Date object that occurs after startDate
     * @param calendarType
     *            - a supported Calendar Type
     * @return a GetListOfCalendarDaysResponse object with the requested dates
     * @throws Exception
     */
    public GetListOfCalendarDaysResponse getListOfCalendarDays(final Date startDate, final Date endDate,
            final String calendarType) throws MBSBaseException {
        try {

            final ObjectFactory objFactory = new ObjectFactory();
            final EnterpriseCalendarServicePortType port = this.service.getEnterpriseCalendarServicePortTypeEndpoint();
            final GetListOfCalendarDaysRequest req = objFactory.createGetListOfCalendarDaysRequest();

            GregorianCalendar gCal = new GregorianCalendar();
            gCal.setTime(startDate);
            req.setStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
            gCal.setTime(endDate);
            req.getCalendarType().add(calendarType);
            req.setEndDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));

            return port.opGetListOfCalendarDays(req);

        } catch (Exception exe) {

            LOGGER.error("Exception occur for getListOfCalendarDays", exe);
            throw new MBSSystemException("Exception occur for getListOfCalendarDays",
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
    }

    /**
     * Retrieve the last business day of the month represented in the date
     * object.
     * 
     * @param date
     *            - any java.util.Date object.
     * @param calendarType
     *            - any supported Calendar Type.
     * @param numDaysPastFuture
     *            - an int designating how many days in the past or future of
     *            the input date. Use negative numbers for the past, positive
     *            numbers for the future.
     * @return a GetLastBusinessDayOfMonthResponse which contains the requested
     *         value
     * @throws Exception
     */
    public GetLastBusinessDayOfMonthResponse getLastBusinessDayOfMonth(final Date date, final String calendarType,
            final int numDaysPastFuture) throws MBSBaseException {

        try {

            final ObjectFactory objFactory = new ObjectFactory();
            final EnterpriseCalendarServicePortType port = this.service.getEnterpriseCalendarServicePortTypeEndpoint();
            final GetLastBusinessDayOfMonthRequest req = objFactory.createGetLastBusinessDayOfMonthRequest();
            final JAXBElement<BigInteger> numDays = objFactory
                    .createGetCalendarDayRequestCalendarDaysNumOfDaysInPastOrFuture(
                            BigInteger.valueOf(numDaysPastFuture));

            GregorianCalendar gCal = new GregorianCalendar();
            gCal.setTime(date);
            req.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gCal));
            req.getCalendarType().add(calendarType);
            req.setNumOfDaysInPastOrFuture(numDays);

            return port.opGetLastBusinessDayOfMonth(req);

        } catch (Exception exe) {

            LOGGER.error("Exception occur for getLastBusinessDayOfMonth", exe);
            throw new MBSSystemException("Exception occur for getLastBusinessDayOfMonth",
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
    }

    /**
     * Sets up the handlerResolver for the EnterpriseCalendarService with a user
     * ID and password.
     * 
     * @param userId
     *            - a provided user ID
     * @param password
     *            - a provided password
     */
    private void setupHandlerResolver(final String userId, final String password) throws MBSBaseException {
        final HeaderHandlerResolver resolver = new HeaderHandlerResolver(userId, password);
        if (this.service != null) {
            this.service.setHandlerResolver(resolver);
        }
    }

}
