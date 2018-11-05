package com.fanniemae.mbsportal.utils.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.LoggerFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fanniemae.access.sts.AWSFederationAccess;

import ch.qos.logback.classic.Logger;

public class AWSUtils {

	
	public static Properties loadProps() throws IOException {
		Properties config = new Properties();
		InputStream propInputStream = AWSFederationAccess.class.getClassLoader().getResourceAsStream("application.properties");
		config.load(propInputStream);
		return config;
	}
	
	
	public static AmazonS3 initS3(Properties config) throws Exception {
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
	
	/**
	 * 
	 * @param fileName
	 * @param exportData
	 * @return
	 */
	public static File createFile(String fileName, byte[] exportData) throws Exception {
		File outputFile = new File(fileName); 
		try (FileOutputStream fileOuputStream = new FileOutputStream(fileName)) {
            fileOuputStream.write(exportData);
            return outputFile;
        } catch (IOException e) {
        	e.printStackTrace();
			throw e;
        }
	}
	
}
