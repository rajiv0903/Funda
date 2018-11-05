/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.socket.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;

import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSProfileRole;
import com.fanniemae.mbsportal.streaming.cdx.CDXStreamingProfileService;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 27, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.interceptor.ChannelInterceptorAdapterImpl.java
 * @Revision:
 * @Description: ChannelInterceptorAdapterImpl.java
 */

@SuppressWarnings("unused")
public class ChannelInterceptorAdapterImpl extends ChannelInterceptorAdapter {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelInterceptorAdapterImpl.class);

    /**
     * streamingClientProperties the StreamingClientProperties
     */

    private final StreamingClientProperties streamingClientProperties;
    /**
     * cDXApiClientConfig the CDXApiClientConfig
     */
    private final CDXApiClientConfig cDXApiClientConfig;
    /**
     * cDXStreamingProfileSessionService the CDXStreamingProfileSessionService
     */
    private final CDXStreamingProfileService cDXStreamingProfileSessionService;

    /**
     * 
     * @param streamingClientProperties
     *            the StreamingClientProperties
     * @param cDXApiClientConfig
     *            the CDXApiClientConfig
     * @param cDXStreamingProfileSessionService
     *            the CDXStreamingProfileSessionService
     */
    public ChannelInterceptorAdapterImpl(final StreamingClientProperties streamingClientProperties,
            final CDXApiClientConfig cDXApiClientConfig,
            final CDXStreamingProfileService cDXStreamingProfileSessionService) {

        this.streamingClientProperties = streamingClientProperties;
        this.cDXApiClientConfig = cDXApiClientConfig;
        this.cDXStreamingProfileSessionService = cDXStreamingProfileSessionService;
    }

    /**
     * 
     * 
     */
    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        Map<String, String> cdxHeaderMap = new HashMap<>();
        List<String> userRoles = new ArrayList<>();
        String cdxChannel = accessor.getFirstNativeHeader(CDXHeaderMap.CHANNEL.getValue());
        String cdxSubChannel = accessor.getFirstNativeHeader(CDXHeaderMap.SUB_CHANNEL.getValue());
        String cdxSessionID = accessor.getFirstNativeHeader(CDXHeaderMap.SESSION_ID.getValue());

        String destination = accessor.getDestination();
        String wsSessionId = accessor.getSessionId();
        StompCommand command = accessor.getCommand();

        LOGGER.debug("Command: {}, wsSessionId: {}, destination: {}", command, wsSessionId, destination);

        /*
         * Perform Authorization at the time of Connect
         */
        if (StompCommand.CONNECT == command) {

            try {
                /*
                 * If Pass through then do not perform the Authorization
                 */
                if (cDXApiClientConfig.isEntitlementpassthrough()) {
                    return message;
                }
                /*
                 * Assign Headers from Connection Header
                 */
                cdxHeaderMap.put(CDXHeaderMap.CHANNEL.getValue(), cdxChannel);
                cdxHeaderMap.put(CDXHeaderMap.SUB_CHANNEL.getValue(), cdxSubChannel);
                cdxHeaderMap.put(CDXHeaderMap.SESSION_ID.getValue(), cdxSessionID);

                /*
                 * Perform Session Validity - With Retry for 429 Error Code- If
                 * not valid then it throws Error
                 */
                cDXStreamingProfileSessionService.authrizeCDXSessionRetry(cdxHeaderMap);

                /*
                 * Fetch Profile and Perform Authorization - Whether allowed to
                 * Direct Host Connection or not
                 */
                MBSProfile mBSProfile = cDXStreamingProfileSessionService.getProfileFromCDXSession(cdxHeaderMap);
                
                /*
                 * Check whether profile exists or not
                 */
                if(Objects.isNull(mBSProfile)){
                    LOGGER.error("Failed to fetch profile for {}", cdxHeaderMap);
                    return null;
                    
                }
                for (MBSProfileRole mBSProfileRole : mBSProfile.getRoles()) {
                    userRoles.add(mBSProfileRole.getName());
                }
                /*
                 * Convert the roles to lower case
                 */
                userRoles = userRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

                /*
                 * Trader Roles - Internal Roles
                 */
                List<String> supportedRoles = Arrays.asList(EntitlementRole.SESSION_ID_SUPPORTED_ROLES);
                supportedRoles = supportedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

                /*
                 * Check if supported role presence at user profile roles - If
                 * not then do not allow to connect
                 */
                if (!CollectionUtils.containsAny(supportedRoles, userRoles)) {
                    LOGGER.error("Supported roles: {}, does not available for user roles {}", supportedRoles,  userRoles);
                    return null;
                }
                
            } catch (Exception exe) {
                LOGGER.error("Exception in  method preSend :", exe);
                return null;

            }
        }

        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.messaging.support.ChannelInterceptorAdapter#postSend(
     * org.springframework.messaging.Message,
     * org.springframework.messaging.MessageChannel, boolean)
     */
    @SuppressWarnings("unused")
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        super.postSend(message, channel, sent);
    }
}
