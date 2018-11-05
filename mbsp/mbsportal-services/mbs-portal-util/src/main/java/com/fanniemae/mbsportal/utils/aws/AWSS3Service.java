package com.fanniemae.mbsportal.utils.aws;

import java.io.ByteArrayInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

@Configuration
public class AWSS3Service {
	
	/**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3Service.class);

	@Autowired
	private AwsProperties awsProperties;
	
	@Autowired
	private Environment environment;
	
	@Autowired
    private EpvConnector epvConnector;
	
	private AmazonS3 s3; 

	/**
	 * Default constructor for LOCAL 
	 */
	public AWSS3Service()  {
		
	}
	
	public void setS3Local() throws Exception {
		this.s3 = AWSUtils.initS3(AWSUtils.loadProps());
	}
	
	public void  setAWSS3Service(String awsAccessKey, String awsSecretKey ) throws Exception {
		LOGGER.debug("starting [setAWSS3Service] with args : [awsAccessKey]: " + awsAccessKey + " :: [awsSecretKey]: " + awsSecretKey);
//		AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
//		this.s3 = AmazonS3ClientBuilder.standard().
//										withCredentials(new AWSStaticCredentialsProvider(credentials)).
//										withRegion(Regions.US_EAST_1).
//										build();
		
		this.s3 = AmazonS3ClientBuilder.defaultClient();
		LOGGER.debug("end [setAWSS3Service] with args : [awsAccessKey]: " + awsAccessKey + " :: [awsSecretKey]: " + awsSecretKey);

	}
	
	private void putObject(String bucketName, String  fileOutputPath, String fileName, byte[] outputData) throws Exception {
		try {
			LOGGER.info("Start [putObject] fileOutputPath :" + fileOutputPath + " :: file.length: " + outputData.length);
			ObjectMetadata meta = new ObjectMetadata();
	        meta.setContentLength(outputData.length);
			meta.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
			meta.setContentType("binary/octet-stream");
			s3.putObject(bucketName, fileOutputPath + "/" + fileName, new ByteArrayInputStream(outputData), meta);
			LOGGER.info("Ends [putObject] fileOutputPath :" + fileOutputPath + " :: file.length: " + outputData.length);
		} catch (Exception e ) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param fileName
	 * @param exportData
	 * @return
	 * @throws MBSBaseException
	 */
	public String  getUnSignedURL(String fileName, byte[] exportData) throws MBSBaseException {
		LOGGER.info("start of getUnSignedURL[] method");
		String env = "LOCAL"; //Default to set to PROD
		String awsSecretKey = null;
		String refId = awsProperties.getRefId();
		String awsAccessKey = awsProperties.getAwsAccesskey();
		String bucketName = awsProperties.getBucketName();
		String[] profiles = environment.getActiveProfiles();
		if (null != profiles && profiles.length > 0) {
			env = profiles[0];
		}
		String bucketLocation = awsProperties.getBucketLocation();			
		
		// Persists file(CSV/EXCEL) to S3-bucket and return S3 URL
        try {
        	//Check is it local 
        	if (env.equalsIgnoreCase("LOCAL") || env.equalsIgnoreCase("DEFAULT")) {        		
        		this.setS3Local();        		
        	} else {
//        		String awsSecretKey = epvConnector.getPassword(refId);
        		LOGGER.debug("[getUnSignedURL] awsSecretKey : " + awsSecretKey);
        		this.setAWSS3Service(awsAccessKey, awsSecretKey);
        	}
        	LOGGER.debug("awsAccessKey:" + awsAccessKey 
    				+  ":: env: " + env + ":: bucketLocation :" + bucketLocation + ":: bucketName :" + bucketName + " : refId : " + refId  );
        	this.putObject(bucketName, bucketLocation+"/" + "mbspdownload" , fileName, exportData);
        	LOGGER.debug("Published to S3 successfully");
//        	String unsignedUrl = bucketLocation + "/" + fileName;
        	String unsignedUrl = fileName;
        	LOGGER.info("End of getUnSignedURL[] method with URL : " + unsignedUrl);
        	return unsignedUrl;
		} catch (Exception e) {
			e.printStackTrace();
			//TODO:: Needs to correct error code and desc.
			throw new MBSSystemException("General Problem",
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
		}	
        
	}

	public AmazonS3 getS3() {
		return s3;
	}

	public void setS3(AmazonS3 s3) {
		this.s3 = s3;
	}

	public AwsProperties getAwsProperties() {
		return awsProperties;
	}

	public void setAwsProperties(AwsProperties awsProperties) {
		this.awsProperties = awsProperties;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public EpvConnector getEpvConnector() {
		return epvConnector;
	}

	public void setEpvConnector(EpvConnector epvConnector) {
		this.epvConnector = epvConnector;
	}
	
}
