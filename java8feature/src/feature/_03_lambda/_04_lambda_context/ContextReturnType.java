package feature._03_lambda._04_lambda_context;

public class ContextReturnType {

	public static void main(String[] args) {
		
		System.out.println(create().calculate(2, 2));

	}
	
	private static IntCalculator create(){
		return (x, y) ->  x / y;
	}

}
