
package com.fanniemae.mbsportal.utils.aws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fanniemae.access.sts.AWSFederationAccess;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;

public class AWSConnector {
	/*
	
	@Autowired
    EpvConnector epvConnector;
	
	
	public static AmazonS3 s3Client(String awsAccessKey, String awsSecretKey, String env) throws Exception {
		AmazonS3 s3Client = null;		
		if (!env.equalsIgnoreCase("LOCAL")) {		
			AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
			s3Client = AmazonS3ClientBuilder.standard().
											withCredentials(new AWSStaticCredentialsProvider(credentials)).
											withRegion(Regions.US_EAST_1).
											build();
		} else {
			Properties config = loadProps();
			s3Client = initS3(config);
		}
		return s3Client;
	}
	
	public static void writeFileToS3 (AmazonS3 s3Client, File outputFile, String bucketName, String  fileOutputPath) throws Exception {
		ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(outputFile.length());
		meta.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
		meta.setContentType("binary/octet-stream");
//		System.out.println("bucketName:" + bucketName + " ::uploading file:" + outputFile.getName() + " To location: " + fileOutputPath + ": with key:" + fileOutputPath + "/" + outputFile.getName());
		s3Client.putObject(bucketName, fileOutputPath + "/" + outputFile.getName(), new FileInputStream(outputFile), meta);

	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		Properties config = loadProps();
		for(int i=0; i<2; i++) {
			AmazonS3 s3 = initS3(config);
			String bucketName = config.getProperty("mbsp.aws.bucketName");
			String fileOutputPath = config.getProperty("mbsp.aws.bucketLocation");
			writeFileToS3(s3,createFile(i),bucketName,fileOutputPath);
		}        
	}
	
	
	public static File createFile(int i) {
		String fileName = "T"+ i +".txt";
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			String content = "This is Test Content11111"  + " :: " + i + "\n";
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outputFile = new File(fileName);
		return outputFile;
	}
	
	
	public static File createFile() {
		String fileName = "Test11.txt";
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
			String content = "This is Test Content11111\n";
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outputFile = new File(fileName);
		return outputFile;
	}
	private static Properties loadProps() throws IOException {
		Properties config = new Properties();
		InputStream propInputStream = AWSFederationAccess.class.getClassLoader().getResourceAsStream("application.properties");
		config.load(propInputStream);
		return config;
	}
	
	private static AmazonS3 initS3(Properties config) throws Exception {
		AWSFederationAccess fedAccess = new AWSFederationAccess();		
		String pwd = config.getProperty("mbsp.aws.trustStorePassword");
		String trustStoreAPassword = new String(org.apache.commons.codec.binary.Base64.decodeBase64(pwd));
		String store = config.getProperty("mbsp.aws.trustStore");
		KeyStore trustKeyStore = KeyStore.getInstance("JKS");
        String userName = config.getProperty("mbsp.aws.userName");
        String passwd = config.getProperty("mbsp.aws.password");
        
		trustKeyStore.load(				
				AWSFederationAccess.class.getClassLoader().getResourceAsStream(store),
				trustStoreAPassword.toCharArray());
		SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustKeyStore, new TrustSelfSignedStrategy())
				.build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" }, null,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		BasicSessionCredentials sessionCredentials = fedAccess.getBasicSessionCredentials(userName, passwd, "DEVL-MBSP01-DEVELOPER");
		
		ClientConfiguration clientConf = new ClientConfiguration();
		clientConf.getApacheHttpClientConfig().setSslSocketFactory(sslsf);
		clientConf.setProtocol(Protocol.HTTPS);
		clientConf.setProxyHost("zsproxy.fanniemae.com");
		clientConf.setProxyPort(10479);
		BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
				sessionCredentials.getAWSAccessKeyId(), sessionCredentials.getAWSSecretKey(),
				sessionCredentials.getSessionToken());
		
		
		AmazonS3 s3Client = 	AmazonS3ClientBuilder.standard().
				withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials)).
				withClientConfiguration(clientConf).
				withRegion(Regions.US_EAST_1).
				build();
//		List<Bucket> buckets = s3Client.listBuckets();
//		System.out.println("size:" + buckets.size());
//		for (Bucket bucket : buckets) {
//			System.out.println(bucket.getName());
//		}	
		return s3Client;
	}
	*/

}

