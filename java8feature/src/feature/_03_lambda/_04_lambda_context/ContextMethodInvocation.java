package feature._03_lambda._04_lambda_context;

public class ContextMethodInvocation {

	public static void main(String[] argv) 
	{
		engine((x, y) -> x / y);
	}

	private static void engine(LongCalculator calculator) {
		long x = 2, y = 4;
		long result = calculator.calculate(x, y);
		System.out.println(result);
	}
}
