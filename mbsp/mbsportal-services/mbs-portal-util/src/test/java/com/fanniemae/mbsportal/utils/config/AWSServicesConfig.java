/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fanniemae.mbsportal.utils.aws.AWSS3Service;
import com.fanniemae.mbsportal.utils.aws.AwsProperties;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;

@Configuration
public class AWSServicesConfig {

	
	@Bean
	public AWSS3Service awsS3Service() {
		return new AWSS3Service();
	}
	
	@Bean
	public AwsProperties awsProperties() {
		return new AwsProperties();
	}
	
	@Bean
	public EpvConnector epvConnector() {
		return new EpvConnector();
	}
}
