package feature._03_lambda._01_implicit_explicit;

public class ImplicitLambda {

	public static void main(String[] args) {
		
		MyIntegerCalculator implicit = s -> s*2;
		System.out.println("1- Result x2 : " + implicit.calcIt(5));

	}
}
