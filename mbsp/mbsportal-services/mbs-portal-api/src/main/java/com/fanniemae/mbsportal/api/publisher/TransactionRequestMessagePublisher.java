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

package com.fanniemae.mbsportal.api.publisher;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.service.ProductService;
import com.fanniemae.mbsportal.api.transformation.TransactionRequestPOTransformer;
import com.fanniemae.mbsportal.api.utils.ServiceUrlUtil;
import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.publisher.BaseMessagePublisher;
import com.fanniemae.mbsportal.service.ConfigurationService;
import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.socket.client.StreamingClientAPI;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessage;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessageDetails;
import com.fanniemae.mbsportal.streaming.stomp.MBSStompUtil;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSStompClient;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Rajiv Chaudhuri
 * @Date: Apr 18, 2018
 * @File: com.fanniemae.mbsportal.api.publisher.TransactionRequestPublisher.java
 * @Revision :
 * @Description: TransactionRequestPublisher.java
 */

@SuppressWarnings("rawtypes")
@Component
public class TransactionRequestMessagePublisher extends BaseMessagePublisher {
        
        private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestMessagePublisher.class);
        private static final String LOGGING_METHOD_NAME = "publish";
        private static final String LOGGING_METHOD_SIGNATURE = "com.fanniemae.mbsportal.api.publisher"
                + ".TransactionRequestMessagePublisher.publish(TransformationObject)";
        /**
         * serviceUrlUtil ServiceUrlUtil
         */
        @Autowired
        ServiceUrlUtil serviceUrlUtil;
        /**
         * streamingClientProperties StreamingClientProperties
         */
        @Autowired
        private StreamingClientProperties streamingClientProperties;
        /**
         * configurationService ConfigurationService
         */
        @Autowired
        private ConfigurationService configurationService;
        /**
         * streamingClientAPI StreamingClientAPI
         */
        @Autowired
        private StreamingClientAPI streamingClientAPI;
        /**
         * transactionRequestPOTransformer TransactionRequestPOTransformer
         */
        @Autowired
        private TransactionRequestPOTransformer transactionRequestPOTransformer;
        /**
         * productService ProductService
         */
        @Autowired
        private ProductService productService;
        /**
         * MBSStompClient client
         */
        @Autowired
        private MBSStompClient mbsStompClient;
        @Autowired
        private TokenService tokenService;
        @Autowired
        private CDXClientApi cdxClientApi;
        
        @Override
        public void publish(TransformationObject transformationObject) {
                TransformationObject transformationObjectLocalCp = null;
                MBSTransactionRequest mbsTransactionRequest = null;
                String msgPublishServerTraderUrl;
                String msgPublishServerLenderUrl;
                
                try {
                        transformationObjectLocalCp = (TransformationObject) transformationObject.clone();
                        if(streamingClientProperties.isPublishTransaction()) {
                /*
                 * If Lender Open (Lender Request) then send to Trader Only If
                 * Lender Open (Trader Request) then send to Trader Only If TSP
                 * Open (Lender Request) then send to Trader Only If TSP Open
                 * (Trader Request) then send to Trader Only If Lender Activity
                 * then send to Trader If Trader Activity then send to specific
                 * Lender Only Or TSP Only
                 */
                                LOGGER.debug("Entering publish method in TransactionRequestMessagePublisher");
                                mbsTransactionRequest = (MBSTransactionRequest) transformationObjectLocalCp
                                        .getTargetPojo();
                                if(mbsTransactionRequest == null) {
                                        return;
                                }
                                
                                StreamingMessage streamingMessageLender = new StreamingMessage();
                                
                                LOGGER.debug("In publish message, Role type is {}, transaction details {}",
                                        transformationObjectLocalCp.getMBSRoleType(), mbsTransactionRequest);
                /*
                 * Client will call with role to inform any Lender
                 */
                                if(MBSRoleType.LENDER.equals(transformationObjectLocalCp.getMBSRoleType())
                                        || MBSRoleType.TSP.equals(transformationObjectLocalCp.getMBSRoleType())) {
                                        publishToLender(transformationObjectLocalCp,
                                                mbsTransactionRequest.getCounterpartyTraderIdentifier(),
                                                streamingMessageLender, mbsTransactionRequest.getTransReqNumber(), mbsTransactionRequest.getStateType());
                                }
                                
                /*
                 * Client will call with role to inform any Trader
                 */
                                else if(MBSRoleType.TRADER.equals(transformationObjectLocalCp.getMBSRoleType())) {
                                        
                                        publishToTrader(transformationObjectLocalCp);
                                        
                                }
                /*
                 * Client will call with role to inform Lender and Trader both
                 */
                                else if(MBSRoleType.LENDER_AND_TRADER
                                        .equals(transformationObjectLocalCp.getMBSRoleType())) {
                                        
                    /*
                     * Create Lender and Trader View
                     */
                                        publishToLender(transformationObjectLocalCp,
                                                mbsTransactionRequest.getCounterpartyTraderIdentifier(),
                                                streamingMessageLender, mbsTransactionRequest.getTransReqNumber(), mbsTransactionRequest.getStateType());
                                        publishToTrader(transformationObjectLocalCp);
                                }
                        }
                } catch(Exception exe) {
                        MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                                MBSExceptionConstants.SYSTEM_EXCEPTION.toString(), exe.getMessage(),
                                LOGGING_METHOD_NAME, LOGGING_METHOD_SIGNATURE, transformationObjectLocalCp.toString());
                        LOGGER.error("Error Stack: ", exe);
                        MBSExceptionConstants.resetLogAlert();
                }
                LOGGER.debug("Exiting publish method in TransactionRequestMessagePublisher");
        }
        
        /**
         * @param transformationObjectLocalCp
         * @throws MBSBaseException
         * @throws JsonProcessingException
         * @throws ExecutionException
         * @throws InterruptedException
         */
        public void publishToTrader(TransformationObject transformationObjectLocalCp)
                throws MBSBaseException, JsonProcessingException, ExecutionException, InterruptedException {
                
                // Create Trader View for Lender Activity
                StreamingMessageDetails streamingMessageDetailsTrader = getTransactionMessageDetails(MBSRoleType.TRADER,
                        TradeConstants.TRADER_PUBLISH_USRE_NAME, transformationObjectLocalCp);
                
                // add message
                StreamingMessage streamingMessageTrader = new StreamingMessage();
                streamingMessageTrader.clearMessages();
                streamingMessageTrader.addMessages(streamingMessageDetailsTrader);
                
                // add message identifier
                String msgPublishServerTraderUrl = configurationService
                        .getPropValueAsString(DAOConstants.STREAMING_INTERNAL_URL);
                if(streamingClientProperties.isWsPublishEnabled()) {
            /*
            * Trader: Send it by Template
            */
                        streamingClientAPI.sendMsgByTemplate(streamingMessageTrader);
                } else {
            /*
            * Trader: Send by HTTP 
            */
                        streamingClientAPI.sendMsgByHttp(streamingMessageTrader, msgPublishServerTraderUrl);
                }
        }
        
        /**
         * publishToLender
         *
         * @param transformationObjectLocalCp
         * @param userId
         * @param streamingMessageLender
         * @throws MBSBaseException
         * @throws JsonProcessingException
         * @throws ExecutionException
         * @throws InterruptedException
         */
        void publishToLender(TransformationObject transformationObjectLocalCp, String userId,
                StreamingMessage streamingMessageLender, String messageId, String transStateType)
                throws MBSBaseException, JsonProcessingException, ExecutionException, InterruptedException {
                
                String msgPublishServerLenderUrl = serviceUrlUtil.getUserConfigPO(userId, false).getWebSocketUrl();
                if(StringUtils.isBlank(msgPublishServerLenderUrl)) {
                        //TODO: use latest alert method
                        MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                                MBSExceptionConstants.SYSTEM_EXCEPTION.toString(),
                                "The streaming url is not available in party profile for this user " + userId,
                                LOGGING_METHOD_NAME, LOGGING_METHOD_SIGNATURE, transformationObjectLocalCp.toString());
                        LOGGER.error("The streaming url is not available in party profile for this user " + userId);
                        MBSExceptionConstants.resetLogAlert();
                }
                if(streamingClientProperties.isWsPublishEnabled()) {
                        //Send it by stomp client
                        TransactionRequestPO transactionRequestPOForPublish = prepareTransactionRequestPO(
                                MBSRoleType.LENDER, TradeConstants.TRADER_PUBLISH_USRE_NAME,
                                transformationObjectLocalCp);
                        String msg = prepareJsonForTransReq(transactionRequestPOForPublish);
                        // String topic = getTopic(msgPublishServerTraderUrl);
                        // assumption it is only one lender and tsp also same
                        MBSStompClient stompClient = MBSStompUtil.getClient(msgPublishServerLenderUrl);
                        LOGGER.info("Publishing by WS Client for Lender  to host {} topic {} " + "with "
                                        + "transReqId {} and status {} ", stompClient.getWsUrl(), "transaction/" +
                                        userId,
                                transactionRequestPOForPublish.getTransReqId(),
                                transactionRequestPOForPublish.getStateType());
                        
                        try {
                                if(stompClient.getConnectedSession() == null
                                        || stompClient.getConnectedSession().get() == null || !stompClient
                                        .getConnectedSession().get().isConnected()) {
                                        //connection broken so retry with session again
                                        try {
                                                //TODO: move this code to util after moving the servie to framework
                                                LOGGER.info("WS connection broken. trying to reconnect again for "
                                                        + "publish");
                                                String sessionId = tokenService.getCurrentSessionId();
                                                if(!cdxClientApi.isSessionValid(sessionId)) {
                                                        stompClient.reConnectToWsServer(null);
                                                        LOGGER.info("Ready to publish after re-connect to WS status {}",
                                                                stompClient.getConnectedSession().get().isConnected());
                                                } else {
                                                        stompClient.reConnectToWsServer(sessionId);
                                                        LOGGER.info("Ready to publish after re-connect to WS "
                                                                        + "using existing session id and status {}",
                                                                stompClient.getConnectedSession().get().isConnected());
                                                }
                                        } catch(Exception e) {
                                                LOGGER.error("Need to handle in reconnect? Not going to publish", e);
                                                //no need to go for publish since some error
                                                throw e;
                                        }
                                } else {
                                        LOGGER.info("WS connection still active good to publish");
                                }
                                stompClient.publish(streamingClientProperties.getTransactionDestinationTopic() + userId,
                                        msg, messageId, transStateType);
                        } catch(Exception ex) {
                                LOGGER.error("Not able to publish to WS Server {} and exception: {}",
                                        stompClient.getWsUrl(), ex);
                        }
                } else {
                        //Lender: Send by HTTP
                        StreamingMessageDetails streamingMessageDetailsLender = getTransactionMessageDetails(
                                MBSRoleType.LENDER, userId, transformationObjectLocalCp);
                        streamingMessageLender.clearMessages();
                        streamingMessageLender.addMessages(streamingMessageDetailsLender);
                        streamingClientAPI.sendMsgByHttp(streamingMessageLender, msgPublishServerLenderUrl);
                }
        }
        
        /**
         * This will construct the Transaction Message Details based on Role and
         * topic
         *
         * @param mbsRoleType                 the MBSRoleType
         * @param lenderOrTraderID            the lenderOrTraderID
         * @param transformationObjectLocalCp the TransformationObject
         * @return streamingMessageDetails the StreamingMessageDetails
         * @throws MBSBaseException
         * @throws JsonProcessingException
         */
        private StreamingMessageDetails getTransactionMessageDetails(MBSRoleType mbsRoleType, String lenderOrTraderID,
                TransformationObject transformationObjectLocalCp) throws MBSBaseException, JsonProcessingException {
                TransactionRequestPO transactionRequestPO = prepareTransactionRequestPO(mbsRoleType, lenderOrTraderID,
                        transformationObjectLocalCp);
                // Create Trader Message
                StreamingMessageDetails streamingMessageDetails = new StreamingMessageDetails();
                streamingMessageDetails.setMessage(prepareJsonForTransReq(transactionRequestPO));
                streamingMessageDetails
                        .addTopic(streamingClientProperties.getTransactionDestinationTopic() + lenderOrTraderID);
                
                // set the message identifier map
                Map<String, Object> messageIdentifierMap = new LinkedHashMap<>();
                messageIdentifierMap
                        .put("TOPIC", streamingClientProperties.getTransactionDestinationTopic() + lenderOrTraderID);
                messageIdentifierMap.put("TRANSACTION_ID", transactionRequestPO.getTransReqId());
                messageIdentifierMap.put("PRODUCT_ID", transactionRequestPO.getProduct().getProductId().toString());
                messageIdentifierMap.put("MBS_ROLE", mbsRoleType.name());
                messageIdentifierMap.put("TRANSACTION_STATE", transactionRequestPO.getStateType().name());
                messageIdentifierMap.put("TRADE_AMOUNT", transactionRequestPO.getTradeAmount());
                messageIdentifierMap.put("HANDLE", transactionRequestPO.getPricePercentHandleText());
                messageIdentifierMap.put("TICK", transactionRequestPO.getPricePercentTicksText());
                messageIdentifierMap.put("ACTIVE_VERSION", transactionRequestPO.getActiveVersion());
                messageIdentifierMap.put("PUBLISH_TIME_FROM_MBSP", MBSPortalUtils
                        .getLocalZonedDateTimeFromEpochMilli(MBSPortalUtils.getLocalDateCurrentTimeStamp(),
                                MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS));
                streamingMessageDetails.setMessageIdentifierMap(messageIdentifierMap);
                return streamingMessageDetails;
        }
        
        /**
         * prepare util method to get TransactionRequestPO
         *
         * @param mbsRoleType
         * @param lenderOrTraderID
         * @param transformationObjectLocalCp
         * @return
         * @throws MBSBaseException
         */
        private TransactionRequestPO prepareTransactionRequestPO(MBSRoleType mbsRoleType, String lenderOrTraderID,
                TransformationObject transformationObjectLocalCp) throws MBSBaseException {
                // Create Trader View for Trader Activity
                transformationObjectLocalCp.setMBSRoleType(mbsRoleType);
                transformationObjectLocalCp = transactionRequestPOTransformer.transform(transformationObjectLocalCp);
                TransactionRequestPO transactionRequestPO = (TransactionRequestPO) transformationObjectLocalCp
                        .getSourcePojo();
                // Calling to fetch the Product from Service
                transactionRequestPO
                        .setProduct(productService.getByProductId(transactionRequestPO.getProduct().retrieveKey()));
                return transactionRequestPO;
        }
        
        /**
         * Util method to convert into json
         *
         * @param transactionRequestPO
         * @return
         * @throws JsonProcessingException
         */
        private String prepareJsonForTransReq(TransactionRequestPO transactionRequestPO)
                throws JsonProcessingException {
                ObjectMapper mapper = new ObjectMapper();
                String jsonInString = mapper.writeValueAsString(transactionRequestPO);
                return jsonInString;
        }
        
}
