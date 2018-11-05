package com.fanniemae.mbsportal.service;

import com.fanniemae.mbsportal.api.publisher.TransactionRequestMessagePublisher;
import com.fanniemae.mbsportal.api.service.TransactionEventService;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSProductTransactionXrefDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.model.MBSProductTransactionXref;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by g9usid on 8/9/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionEventServiceTest {

        @InjectMocks
        private TransactionEventService transactionEventService;
        
        @Mock
        private ConfigService configService;
        
        @Mock
        private MBSTransactionRequest mbsTransactionRequest;
        
        @Mock
        private org.slf4j.Logger LOGGER;
        
        @Mock
        private Thread thread;
        
        @Mock
        private MBSTransactionRequestDao mbsTransactionRequestDao;
        
        @Mock
        private MBSProductTransactionXrefDao mbsProductTransactionXrefDao;
        
        @Mock
        private MBSObjectCreator mbsObjectCreator;
        
        @Mock
        private TransactionRequestMessagePublisher transactionRequestMessagePublisher;
        
        private boolean isActive = true;
        
        @Value("${mbsp.timeToTimeoutinMilli}")
        private long timeToTimeoutinMilli;
        
        @Value("${mbsp.timeoutSleep}")
        long sleepTime;
        
        @Mock
        private Map<String, MBSTransactionRequest> tradesToScan = new ConcurrentHashMap<String, MBSTransactionRequest>();
        
        @Before
        public void setUp() throws Exception {
        
        }
        
        //Need to review scanStatusAndTimedOut() method, infinite loop
       
        @Test
        public void processEvent_Success() throws MBSBaseException {
                transactionEventService.processEvent(mbsTransactionRequest);
        }
        
        @Test
        public void removeTransToCrossRef_Success() throws MBSBaseException {
                transactionEventService.removeTransToCrossRef(mbsTransactionRequest);
        }
        
        @Test
        public void publishOnStartUp_SkipsWhileLoop() throws MBSBaseException {
                transactionEventService.publishOnStartup();
        }
        
        @Test
        public void remove_Success() throws MBSBaseException {
                transactionEventService.add("test", mbsTransactionRequest);
                transactionEventService.remove("test");
        }
        
        @Test
        public void getActiveTransactions_Success() {
                transactionEventService.getActiveTransactions("Source");
        
        }
        
        @Test
        public void isItNonTrackingStatus_Success() {
                transactionEventService.isItNonTrackingStatus("StateType");
        }
        
}
