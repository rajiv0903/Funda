package com.fanniemae.mbsportal.service;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.utils.cdx.token.CDXTokenRefresher;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

/**
 * Created by g9usid on 8/6/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {
        
       @InjectMocks
        private TokenService tokenService;
       
       @Mock
        private CDXTokenRefresher cdxTokenRefresher;
       
       @Mock
        private ConfigService configService;
        
        @Mock
        private Logger LOGGER;
        
        private String sessionId = "testingId";
        
        @Before
        public void setUp() throws Exception {
        }
        
        @Test
        @Ignore
        public void getValidSessionId_Success() throws MBSBaseException {
                when(cdxTokenRefresher.decrypt(configService.getPropValueAsString(DAOConstants.SESSION_ID))).thenReturn
                        (null);
                tokenService.getValidSessionId();
        }
        
        @Test
        public void getValidSessionId_NonNullSessionId() throws MBSBaseException {
                when(cdxTokenRefresher.decrypt(configService.getPropValueAsString(DAOConstants.SESSION_ID))).thenReturn
                        (sessionId);
                tokenService.getValidSessionId();
        }
        
        @Test
        public void resetSessionAndToken_Success() throws MBSBaseException {
                tokenService.resetSessionAndToken();
        }
        
        @Test (expected = Exception.class)
        public void getValidSessionId_CatchException() throws MBSBaseException {
                when(cdxTokenRefresher.refreshLogin()).thenThrow(new RuntimeException());
                tokenService.getValidSessionId();
                
        }
}
