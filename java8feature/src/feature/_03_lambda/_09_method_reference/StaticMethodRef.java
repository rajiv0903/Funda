package feature._03_lambda._09_method_reference;

import java.util.function.BiFunction;
import java.util.function.Function;

public class StaticMethodRef {

	public static void main(String[] args) {

		// Using a lambda expression
		Function<Integer, String> func1 = x -> Integer.toBinaryString(x);
		System.out.println(func1.apply(10));

		// Using a method reference
		Function<Integer, String> func2 = Integer::toBinaryString;
		System.out.println(func2.apply(10));

		// Uses a lambda expression
		BiFunction<Integer, Integer, Integer> func3 = (x, y) -> Integer.sum(x,y);
		System.out.println(func3.apply(2, 3));

		// Uses a method reference
		BiFunction<Integer, Integer, Integer> func4 = Integer::sum;
		System.out.println(func4.apply(2, 3));
		
		 // Uses  Integer.valueOf(int)
	    Function<Integer, Integer> func5  = Integer::valueOf;

	    // Uses  Integer.valueOf(String)
	    Function<String, Integer> func6  = Integer::valueOf;

	    // Uses  Integer.valueOf(String, int)
	    BiFunction<String, Integer,  Integer> func7  = Integer::valueOf;

	    System.out.println(func5.apply(7)); 
	    System.out.println(func6.apply("7")); 
	    System.out.println(func7.apply("101010101010", 2));
	}

}
