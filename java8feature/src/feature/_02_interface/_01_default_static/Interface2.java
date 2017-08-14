package feature._02_interface._01_default_static;

public interface Interface2 {
	
	void method2();

	/**
	 * Now when a class will implement Interface2, it is not mandatory to provide implementation 
	 * for default methods of interface. This feature will 
	 * help us in extending interfaces with additional methods, 
	 * all we need is to provide a default implementation.
	 */
	default void log(String str) {
		System.out.println("I2 logging::" + str);
	}
	
	

}
