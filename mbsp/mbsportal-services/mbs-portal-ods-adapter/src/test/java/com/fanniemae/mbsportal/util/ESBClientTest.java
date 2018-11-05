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

package com.fanniemae.mbsportal.util;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.api.ESBClientException;
import com.fanniemae.all.messaging.api.ESBClientFactory;
import com.fanniemae.all.messaging.api.ESBMessageProcessor;
import com.fanniemae.all.messaging.message.ESBMessage;
import com.fanniemae.all.messaging.message.PayloadIdentifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

/**
 * @author g8uaxt Created on 11/20/2017.
 */

public class ESBClientTest implements ESBMessageProcessor {
        private ESBClient client = null;
        //private String small_file = "c:\\test\\test-ample.txt";
        
        
        public ESBClientTest() throws IOException{
                
                String EventNameOrder = "TestEvent100";
                String EntityNameOrder = "TestRTEntity100";
                
                String EventName = "DataSetReady";
                String EntityName = "Activity";
                String small_string_Content = "Hello world ";
                
                //        try {
                //            small_string_Content = getFileContentAsString(small_file);
                //        } catch (IOException e1) {
                //            e1.printStackTrace();
                //        }
                
                try {
                        client = ESBClientFactory.createESBClient();
                } catch (ESBClientException e) {
                        e.printStackTrace();
                }
                
                System.out.println(" client"+client);
                PayloadIdentifier pid = new PayloadIdentifier(EventNameOrder, EntityNameOrder, "1");
                ESBMessage small_message = null;
                try {
                        small_message = client.createMessage(pid, small_string_Content);
                        client.send(small_message);
                        System.out.println(" published:"+small_message);
                        
                        client.startMessageConsumer(this);
                        System.out.println(" Started consumer");
                        System.in.read();
                } catch (ESBClientException e) {
                        e.printStackTrace();
                }
                finally{
                        client.terminate();
                }
        }
        
        public static void main(String args[]) throws IOException {
                ESBClientTest tester = new ESBClientTest();
                
        }
        
        private static String getFileContentAsString(String fileName)
                throws IOException {
                InputStream bais = new FileInputStream(new File(fileName));
                
                Reader r = new InputStreamReader(bais);
                StringWriter sw = new StringWriter();
                char[] buffer = new char[1024];
                for (int n; (n = r.read(buffer)) != -1;)
                        sw.write(buffer, 0, n);
                String str = sw.toString();
                return str;
        }
        
        @Override
        public boolean processMessage(ESBMessage esbMessage) {
                // TODO Auto-generated method stub
                
                try {
                        // TODO Auto-generated catch block
                        System.out.println("Received message Exception"+esbMessage.getMessageId());
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println("Received message Exception"+esbMessage.getMessageId());
                        e.printStackTrace();
                }
                return true;
        }
        
}
