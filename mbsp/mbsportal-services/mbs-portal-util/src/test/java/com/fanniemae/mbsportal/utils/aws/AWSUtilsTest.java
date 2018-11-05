package com.fanniemae.mbsportal.utils.aws;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.services.s3.AmazonS3;

@Configuration
@TestPropertySource({"classpath:application.properties", "classpath:saml.properties"})
//@ActiveProfiles(profiles = "local")
@ComponentScan({"com.fanniemae.mbsportal.test","com.fanniemae.mbsportal.dao","com.fanniemae.mbsportal.util", "com.fanniemae.mbsportal.utils.logging"})
@RunWith(SpringRunner.class)
public class AWSUtilsTest {

	AmazonS3 s3; 
    private AWSConnector awsConnector; 
    private static final String awsAccessKey = "AKIAI3UOEDG6KUYAKYHA";
    private static final String awsSecretKey = "test";
    private String env = "LOCAL";
    private static final String BUCKET_NAME = "fnma-mbsp01-devl-us-east-1-runtime-private"; 
    
    private File outputFile ;
    private String  fileOutputPath = "reports/transactions/mbspdownload";
    
     
    @Test (expected = IOException.class)
	public void createFile_Failure() throws Exception {
    	byte[] byt = new byte[4];
    	AWSUtils.createFile("", byt);
	}
	
	@Test
	public void testLoadProps() {
		String actualStore = "cacerts_mbsp";
		try {
			Properties props = AWSUtils.loadProps();
			String trustStore = props.getProperty("mbsp.aws.trustStore");
			System.out.println("trustStore=" + trustStore);
			assertEquals(trustStore, actualStore);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInitS3() {
		String actualStore = "cacerts_mbsp";
		try {
			Properties props = AWSUtils.loadProps();
			String trustStore = props.getProperty("mbsp.aws.trustStore");
			System.out.println("trustStore=" + trustStore);
			assertEquals(trustStore, actualStore);
			AmazonS3 s3 = AWSUtils.initS3(props);
			assertThat(s3).isNotNull(); 
			boolean isBucketExists = s3.doesBucketExist(BUCKET_NAME);
			assertTrue(isBucketExists);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateFile() {
		try {
			byte[] exportData = "Lender export Data".getBytes();
			outputFile = AWSUtils.createFile("Test11.csv", exportData);
			assertNotNull(outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}