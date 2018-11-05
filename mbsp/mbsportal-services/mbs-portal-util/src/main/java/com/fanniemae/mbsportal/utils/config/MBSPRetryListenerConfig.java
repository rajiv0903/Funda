package com.fanniemae.mbsportal.utils.config;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 23, 2018
 * @File: com.fanniemae.mbsportal.utils.config.MBSPRetryListenerConfig.java 
 * @Revision: 
 * @Description: MBSPRetryListenerConfig.java
 */
@Configuration
public class MBSPRetryListenerConfig {

    @Value("${mbs.messaging.client.retryMaxAttempts}")
    private String streamingClientRetryMaxAttempts;

    private static final String STREAMING_LOGGING_METHOD_NAME = "sendMsgByHttp";
    private static final String STREAMING_LOGGING_METHOD_SIGNATURE = "com.fanniemae.mbsportal.streaming.socket.client.StreamingClientAPI.sendMsgByHttp(com.fanniemae.mbsportal.streaming.socket.model.StreamingMessage,java.lang.String)";

    /**
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MBSPRetryListenerConfig.class);

    @Bean
    public List<RetryListener> retryListeners() {

        return Collections.singletonList(new RetryListener() {

            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                LOGGER.debug("Entering open method in retryListeners");
                LOGGER.debug("Retryable Context: {}", context.getAttribute("context.name"));
                LOGGER.debug("Exiting open method in retryListeners");
                return true;
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
                    Throwable throwable) {

                LOGGER.debug("Entering onError method in retryListeners");
                LOGGER.debug("Retryable Context: {}, Retry Count: {}", context.getAttribute("context.name"),
                        context.getRetryCount());

                String contextName = (String) context.getAttribute("context.name");
                int contextRetryCount = context.getRetryCount();

                if (contextName.contains(STREAMING_LOGGING_METHOD_SIGNATURE)) {

                    if (contextRetryCount == Integer.parseInt(streamingClientRetryMaxAttempts)) {

                        MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                                MBSExceptionConstants.SYSTEM_EXCEPTION.toString(), throwable.getMessage(),
                                STREAMING_LOGGING_METHOD_NAME, STREAMING_LOGGING_METHOD_SIGNATURE,
                                throwable.toString());
                        LOGGER.error("Throwable: {}", throwable);
                        MBSExceptionConstants.resetLogAlert();
                    }
                }
                LOGGER.debug("Exiting onError method in retryListeners");
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
                    Throwable throwable) {
                LOGGER.debug("Entering close method in retryListeners");
                LOGGER.debug("Retryable Context: {}", context.getAttribute("context.name"));
                LOGGER.debug("Exiting close method in retryListeners");
            }

        });
    }
}
