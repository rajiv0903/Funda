package com.fanniemae.mbsportal.utils.aws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fanniemae.mbsportal.utils.config.AWSServicesConfig;



@Configuration
@TestPropertySource({"classpath:application.properties", "classpath:saml.properties"})
@ActiveProfiles(profiles = "local")
@ComponentScan({"com.fanniemae.mbsportal.test","com.fanniemae.mbsportal.utils.aws","com.fanniemae.mbsportal.dao","com.fanniemae.mbsportal.util", "com.fanniemae.mbsportal.utils.logging"})
@ContextConfiguration(classes = {AWSServicesConfig.class})
//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AWSS3ServiceTest {
	
//	@Autowired
//    Environment environment; 
	
//	@Autowired
//	AWSS3Service s3Service;
	
	@InjectMocks
    AWSS3Service s3Service;
	
	private static final String awsAccessKey = "AKIAI3UOEDG6KUYAKYHA";
    private static final String awsSecretKey = "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";
    private String env = "LOCAL";
    private static final String BUCKET_NAME = "fnma-mbsp01-devl-us-east-1-runtime-private"; 
	
	@Before
    public void setUp() { 
	}
	


	@Test	
	public void testSetS3Local() {
		try {
			s3Service.setS3Local();
			assertNotNull(s3Service.getS3());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not able to initialize S3Local");
		}
	}

	@Test
	@Ignore
	public void testSetAWSS3Service() {
		try {		
//	        Mockito.when(s3Service.setAWSS3Service(Mockito.any(String.class), Mockito.anyString()));

			s3Service.setAWSS3Service(awsAccessKey, awsSecretKey);			
			assertNotNull(s3Service.getS3());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Not yet implemented");
		}
		
	}
	
	@Test
	@Ignore
	public void testGetUnSignedURL() {
		try {
			byte[] exportData = "Lender export Data".getBytes();
			s3Service.getUnSignedURL("TEST1111.csv", exportData);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not able to initialize S3Local");
		}
	}

}