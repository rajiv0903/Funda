package course01._06._01_recursion._01_factorial.practice;

public class FactorialCalculatorPractice {

	public static void main(String[] args) {
		System.out.println(factorial(5));
	}
	
	
	public static int factorial(int n)
	{
		if(n==0) return 1;
		return n * factorial(n-1);
	}
}
