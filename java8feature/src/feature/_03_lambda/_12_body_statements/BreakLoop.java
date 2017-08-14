package feature._03_lambda._12_body_statements;

import java.util.function.Function;

public class BreakLoop {

	public static void main(String[] args) {

		//We can use statements such as break, continue, return, and throw inside the body of a lambda expression.
		Function<String, String> func1 = y -> {
			for (int i = 0; i < 10; i++) {
				System.out.println(i);
				if (i == 4) {
					break; //We can use statements such as break, continue, return, and throw inside the body of a lambda expression.
				}
			}
			return y + " from Rajiv";
		};
		System.out.println(func1.apply("Hi"));

		//We cannot use the jump statements to do non-local jump.
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
			if (i == 4) {

				Function<String, String> func2 = y -> {
					// break; //We cannot use the jump statements to do non-local jump.
					return y + " from Rajiv";
				};
				System.out.println(func2.apply("Hello"));

			}

		}
	}

}
