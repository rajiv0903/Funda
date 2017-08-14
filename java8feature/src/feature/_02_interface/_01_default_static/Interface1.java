package feature._02_interface._01_default_static;

public interface Interface1 {

	void method1(String str);
	
	/**
	 * Now when a class will implement Interface1, it is not mandatory to provide implementation 
	 * for default methods of interface. This feature will 
	 * help us in extending interfaces with additional methods, 
	 * all we need is to provide a default implementation.
	 */
	default void log(String str)
	{
		System.out.println("I1 logging::"+str);
	}
	
	/**
	 * we can’t override them
	 * @param str
	 */
	static void print(String str)
	{
		System.out.println("Printing "+str);
	}
}
