package feature._03_lambda._03_lambda_behavior;

public class CalculatorEngine {

	public static void main(String[] argv) {

		engine((int x, int y) -> x + y);
		engine((long x, long y) -> x * y);
		engine((int x, int y) -> x / y);
		engine((long x, long y) -> x % y);
	}

	/*
	 * private static void engine(Calculator calculator) { int x = 2, y = 4; int
	 * result = calculator.calculate(x, y); System.out.println("Calculator:" +
	 * result); }
	 */

	private static void engine(IntCalculator calculator) {
		int x = 2, y = 4;
		int result = calculator.calculate(x, y);
		System.out.println("IntCalculator:" + result);
	}

	private static void engine(LongCalculator calculator) {
		long x = 2, y = 4;
		long result = calculator.calculate(x, y);
		System.out.println("LongCalculator:" + result);
	}
}
