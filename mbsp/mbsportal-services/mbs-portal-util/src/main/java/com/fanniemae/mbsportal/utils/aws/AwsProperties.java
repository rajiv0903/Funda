/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils.aws;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mbsp.aws")
public class AwsProperties {

	private String refId;
	
    private String awsVault;

    /**
     * 
     * awsAccesskey the awsAccesskey to get S3 AWSsecretkey
     */

    private String awsAccesskey;

    /**
     * 
     * awsSecretkey the awsSecretkey to connect to S3
     */
    private String awsSecretkey;

    /**
     * 
     * userName the userName to connect to S3
     */
    private String userName;

    /**
     * 
     * password the password to connect to S3
     */
    private String password;

    /**
     * 
     * isLocal the isLocal to connect to S3 from local env
     */
    private String awsenv;

    /**
     * 
     * bucketName the bucketName to connect to S3
     */
    private String bucketName;

    /**
     * 
     * bucketLocation the bucketLocation to upload file to S3
     */
    private String bucketLocation;

    /**
     * 
     * proxyHost the proxyHost to connect S3
     */
    private String proxyHost;

    /**
     * 
     * proxyPort the proxyPort to connect S3
     */
    private int proxyPort;

    /**
     * 
     * trustStorePassword the trustStorePassword to connect S3
     */
    private String trustStorePassword;

    /**
     * 
     * trustStore the trustStore to connect S3
     */
    private String trustStore;

    /**
     * 
     * env the env to connect AWS Env - S3
     */
    private String env;
    
    

    /**
	 * @return the refId
	 */
	public String getRefId() {
		return refId;
	}

	/**
	 * @param refId the refId to set
	 */
	public void setRefId(String refId) {
		this.refId = refId;
	}

	/**
     * @return the awsVault
     */
    public String getAwsVault() {
        return awsVault;
    }

    /**
     * @param awsVault
     *            the awsVault to set
     */
    public void setAwsVault(String awsVault) {
        this.awsVault = awsVault;
    }

    /**
     * @return the awsAccesskey
     */
    public String getAwsAccesskey() {
        return awsAccesskey;
    }

    /**
     * @param awsAccesskey
     *            the awsAccesskey to set
     */
    public void setAwsAccesskey(String awsAccesskey) {
        this.awsAccesskey = awsAccesskey;
    }

    /**
     * @return the awsSecretkey
     */
    public String getAwsSecretkey() {
        return awsSecretkey;
    }

    /**
     * @param awsSecretkey
     *            the awsSecretkey to set
     */
    public void setAwsSecretkey(String awsSecretkey) {
        this.awsSecretkey = awsSecretkey;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the awsenv
     */
    public String getAwsenv() {
        return awsenv;
    }

    /**
     * @param awsenv
     *            the awsenv to set
     */
    public void setAwsenv(String awsenv) {
        this.awsenv = awsenv;
    }

    /**
     * @return the bucketName
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * @param bucketName
     *            the bucketName to set
     */
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * @return the bucketLocation
     */
    public String getBucketLocation() {
        return bucketLocation;
    }

    /**
     * @param bucketLocation
     *            the bucketLocation to set
     */
    public void setBucketLocation(String bucketLocation) {
        this.bucketLocation = bucketLocation;
    }

    /**
     * @return the proxyHost
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * @param proxyHost
     *            the proxyHost to set
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * @return the proxyPort
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * @param proxyPort
     *            the proxyPort to set
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * @return the trustStorePassword
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * @param trustStorePassword
     *            the trustStorePassword to set
     */
    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    /**
     * @return the trustStore
     */
    public String getTrustStore() {
        return trustStore;
    }

    /**
     * @param trustStore
     *            the trustStore to set
     */
    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    /**
     * @return the env
     */
    public String getEnv() {
        return env;
    }

    /**
     * @param env
     *            the env to set
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AwsProperties [refId=" + refId + ", awsVault=" + awsVault + ", awsAccesskey=" + awsAccesskey
				+ ", awsSecretkey=" + awsSecretkey + ", userName=" + userName + ", password=" + password + ", awsenv="
				+ awsenv + ", bucketName=" + bucketName + ", bucketLocation=" + bucketLocation + ", proxyHost="
				+ proxyHost + ", proxyPort=" + proxyPort + ", trustStorePassword=" + trustStorePassword
				+ ", trustStore=" + trustStore + ", env=" + env + "]";
	}

}
