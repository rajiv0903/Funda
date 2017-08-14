package feature._03_lambda._13_recursive;

import java.util.function.IntFunction;

public class Main {

	/**
	 * The following code creates a recursive function in the normal way then use the recursive function 
	 * as the method reference to create a lambda expression. The final lambda expression becomes recursive.
	 */
	public static void main(String[] args) {
		IntFunction<Long> factorialCalc = Main::factorial;
		System.out.println(factorialCalc.apply(10));
	}

	public static long factorial(int n) {
		if (n == 0) {
			return 1;
		} else {
			return n * factorial(n - 1);
		}
	}
}
