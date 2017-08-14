package org.learning.pattern;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {
	
		public static void main(String args[]) throws MalformedURLException
		{
		URL hp=new URL("http://live.ppe1.ebookplus.pearsoncmg.com");
		System.out.println("Protocol: "+hp.getProtocol());
		System.out.println("Port: "+hp.getPort());
		System.out.println("Host: "+hp.getHost());
		System.out.println("File: "+hp.getFile());
		System.out.println("Ext:"+hp.toExternalForm());
		}
		
}
