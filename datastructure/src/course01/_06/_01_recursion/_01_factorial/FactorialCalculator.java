package course01._06._01_recursion._01_factorial;

/**
 * @author rajiv
 *
 */
public class FactorialCalculator {
	
	public long factorial(int n) {
		if (n == 0) return 1;
		return n * factorial(n-1);
	}
		
	public static void main(String[] args) {
		System.out.println(new FactorialCalculator().factorial(5));
	}

}
