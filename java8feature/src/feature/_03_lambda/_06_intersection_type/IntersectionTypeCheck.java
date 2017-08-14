package feature._03_lambda._06_intersection_type;

import java.io.Serializable;

public class IntersectionTypeCheck {

	public static void main(String[] args) {

		/**
		 * In order to create a lambda expression and assign to NonFunction we
		 * use the & to create a new subtype.
		 */
		Calculator nonfunction = (NonFunction & Calculator) (x, y) -> x + y;
		System.out.println(engine(nonfunction));

		/**
		 * In this way we make a lambda expression serializable.
		 */
		Calculator calSer = (java.io.Serializable & Calculator) (x, y) -> x
				+ y;
		
		if(Serializable.class.isInstance(calSer)){
			System.out.println("Yes!");
		}

	}

	private static long engine(Calculator nonfunction) {
		long x = 1;
		long y = 2;
		return nonfunction.calculate(x, y);
	}

}
