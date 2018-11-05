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

package com.fanniemae.mbsportal.service;

import static com.fanniemae.mbsportal.constants.MBSAdapterConstants.ALLOWED_TRADE_EVENTS;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.ecf.ods.Event;
import com.fanniemae.mbsportal.ecf.ods.EventMessage;
import com.fanniemae.mbsportal.ecf.ods.TradeEventMessage;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.transformer.ECFTransformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.google.common.base.Objects;

/**
 * @author g8uaxt Created on 11/16/2017.
 */
@Service
public class TradeMessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeMessageProcessor.class);

    protected static JAXBContext ecfJaxbContext;
    private static XMLInputFactory xif;

    private DocumentBuilderFactory factory;
    @SuppressWarnings("unused")
    private DocumentBuilder builder;
    private Unmarshaller unmarshaller;

    @Autowired
    private ECFTransformer ecfTransformer;

    @Autowired
    private ESBTransactionRequestService esbTransactionRequestService;

    /**
     * prepareTradeEvent
     * 
     * @param xmlMessage
     * @return
     * @throws XMLStreamException
     * @throws JAXBException
     */
    public TradeEventMessage prepareTradeEvent(String xmlMessage) throws XMLStreamException, JAXBException {
        InputStream is = new ByteArrayInputStream(xmlMessage.getBytes());
        StreamSource xml = new StreamSource(is);
        XMLStreamReader xsr = xif.createXMLStreamReader(xml);
        xsr.nextTag();
        while (xsr.hasNext()) {
            if (xsr.isStartElement() && "TradeEventMessage".equals(xsr.getLocalName())) {
                break;
            }
            xsr.next();
        }
        TradeEventMessage tradeEventMessage = unmarshaller.unmarshal(xsr, TradeEventMessage.class).getValue();
        return tradeEventMessage;
    }

    /**
     * This method process the message
     * @param tradeEventMsg
     * @return
     * @throws MBSBaseException
     */
    boolean processMessage(TradeEventMessage tradeEventMsg) throws MBSBaseException {
        boolean handleMsgReturn = false;
        String messageId = null;
        MBSTransactionRequest mbsTransactionRequests;
        MBSTrade mbsTrade;
        LOGGER.debug("TradeEvent Message is: " + tradeEventMsg);
        // Check if event code is valid
        try {
            Event event = tradeEventMsg.getEvent();
            if (!Objects.equal(event, null)) {
                if (!event.getTypeCode().equalsIgnoreCase(ALLOWED_TRADE_EVENTS)) {
                    LOGGER.debug("Event type code is not valid. " + event.getTypeCode());
                    handleMsgReturn = true;
                    return handleMsgReturn;
                }
            } else {
                LOGGER.debug("Event context is empty in the message");
                handleMsgReturn = true;
                return handleMsgReturn;
            }
            mbsTrade = ecfTransformer.transform(tradeEventMsg);
            // If the UDF value for Trans Req Number is empty, skip the message
            if (StringUtils.isEmpty(mbsTrade.getTransReqNumber())) {
                LOGGER.debug("Trans Req Number is not present in the message and hence skipping the message for trade details : "+mbsTrade.getId());
                handleMsgReturn = true;
                return handleMsgReturn;
            }
            LOGGER.debug("MBSTrade details is: " + mbsTrade);
            esbTransactionRequestService.updateMBSTrade(mbsTrade);
            // Retrieve the corresponding transaction request object
            mbsTransactionRequests = deriveTradeRequest(mbsTrade.getTransReqNumber());
            if (!Objects.equal(null, mbsTransactionRequests)) {
                mbsTransactionRequests.setStateType(StateType.EXECUTED.toString());
                esbTransactionRequestService.updateMBSTransReq(mbsTransactionRequests);
                LOGGER.debug("ESBMessagelistener processMessage After Update");
            } else {
                LOGGER.warn("No matching transaction request found");
                handleMsgReturn = true;
            }
            handleMsgReturn = true;
            LOGGER.debug("ESBMessagelistener processMessage exit ");
        } catch (MBSBaseException ex) {
            LOGGER.error("Error while processing message in the Listener ", ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Error while processing message in the Listener ", ex);
            throw new MBSSystemException("Error while processing message in the Listener",
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        return handleMsgReturn;
    }

    /**
     * This method filters the transaction request records based on the lenderid
     * and state type
     * 
     * @param transReqId
     * @return
     * @throws MBSBaseException
     */
    private MBSTransactionRequest deriveTradeRequest(String transReqId) throws MBSBaseException {
        // String transReqId = deriveTransReqId(tradeEventMsg,
        // MBS_TRANS_REQ_ID);
        LOGGER.debug("Trans req id in deriveTradeRequest method " + transReqId);
        MBSTransactionRequest mbsTransReq = null;
        if (StringUtils.isNotEmpty(transReqId)) {
            mbsTransReq = esbTransactionRequestService.getMBSTransReq(transReqId);
        } else {
            LOGGER.error("Trans id is invalid");
            throw new MBSDataAccessException("Trans id is invalid");
        }
        return mbsTransReq;
    }

    @PostConstruct
    public void initialize() {
        xif = XMLInputFactory.newFactory();
        factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            ecfJaxbContext = JAXBContext.newInstance(EventMessage.class);
            unmarshaller = ecfJaxbContext.createUnmarshaller();
        } catch (ParserConfigurationException | JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * prepareAndProcess
     * 
     * @param xmlMessage
     */
    public void prepareAndProcess(String xmlMessage) throws MBSBaseException {
        try {
            TradeEventMessage tradeEventMessage = prepareTradeEvent(xmlMessage);
            processMessage(tradeEventMessage);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
