package feature._02_interface._02_function._01_function;


/**
 * An interface with exactly one abstract method is known as Functional Interface.
 * @FunctionalInterface annotation is a facility to avoid accidental addition of abstract methods 
 * in the functional interfaces. It’s optional but good practice to use it.
 * 
 * 
 * Functional interfaces are long awaited and much sought out feature of 
 * Java 8 because it enables us to use lambda expressions to instantiate them. A new package java.util.
 * function with bunch of functional interfaces are added to provide 
 * target types for lambda expressions and method references.
 */
@FunctionalInterface
public interface Processor {

	/**
	 * one abstract method 
	 */
	int getStringLength(String str);
	
	default void print (String str){
		System.out.println(str);
	}
}
