/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *  
 */

package com.fanniemae.mbsportal.publisher;

import com.fanniemae.mbsportal.pojo.TransformationObject;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Apr 16, 2018
 * @File: com.fanniemae.mbsportal.publisher.BasePublisher.java
 * @Revision : 
 * @Description: BasePublisher.java
 */
public abstract class BaseMessagePublisher<T extends TransformationObject> implements MessagePublisher<T>{

}

