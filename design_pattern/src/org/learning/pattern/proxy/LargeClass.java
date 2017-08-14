package org.learning.pattern.proxy;

public class LargeClass implements ILargeClass{
	private String title;
	public LargeClass(String title)throws Exception {
		this.title = title;
		Thread.sleep(10000);
	}
	public void method1() 
	{// Do method1 stuff.
		System.out.println("LargeClass: method1");
	}
	public void method2() 
	{// Do method2 stuff.
		System.out.println("LargeClass: method2");
	}
}
