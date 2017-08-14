package feature._02_interface._01_default_static;


/**
 * A simple class that is implementing both Interface1 and Interface2 will be
 * Interface1 and Interface2 both have default log method -
 *  Hence Deadly Diamond Problem occurrs- 
 *  To Overcome this the if it is implementing both the class then it should 
 *  provide implementation of log method
 *  
 *  
 *  One of the major reason for introducing default methods in interfaces is to 
 *  enhance the Collections API in Java 8 to support lambda expressions.
 *  Like - forEach
 */
public class MyClass implements Interface1 , Interface2{

	@Override
	public void method1(String str) {
	}
	
	@Override
	public void method2() {
	}
	
	@Override
	public void log(String str)
	{
		System.out.println("MyClass logging::"+str);
		//calling Interface static method 
		Interface1.print("abc");
	}
	
}
