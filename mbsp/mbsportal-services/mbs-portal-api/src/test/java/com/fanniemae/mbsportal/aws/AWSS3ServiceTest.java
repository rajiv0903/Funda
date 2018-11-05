package com.fanniemae.mbsportal.aws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

//import com.fanniemae.mbsportal.api.aws.AWSS3Service;

@Configuration
@TestPropertySource({"classpath:application.properties", "classpath:saml.properties"})
@ActiveProfiles(profiles = "local")
@ComponentScan({"com.fanniemae.mbsportal.test","com.fanniemae.mbsportal.dao","com.fanniemae.mbsportal.util", "com.fanniemae.mbsportal.utils.logging"})
@RunWith(SpringRunner.class)
@Ignore
public class AWSS3ServiceTest {
	
//	AWSS3Service s3Service;
//	
//	@Before
//    public void setUp() { 
//		s3Service = new AWSS3Service();
//	}
//
//	@Test
//	public void testSetS3Local() {
//		try {
//			s3Service.setS3Local();
//			assertNotNull(s3Service.getS3());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			fail("Not able to initialize S3Local");
//		}
//	}
//
//	@Test
//	public void testSetAWSS3Service() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPutObject() {
//		fail("Not yet implemented");
//	}

}
