package feature._03_lambda._04_lambda_context;

public class ContextAssignment {

	public static void main(String[] argv) 
	{
		Calculator iCal = (x, y) -> x + y;
		System.out.println(iCal.calculate(1, 2));
	}
}
