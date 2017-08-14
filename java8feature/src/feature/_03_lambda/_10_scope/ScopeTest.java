package feature._03_lambda._10_scope;

import java.util.function.Function;

public class ScopeTest {

	public static void main(String[] args) 
	{
		new ScopeTest();

	}
	
	/**
	 * If we remove the comment we would get compile time error since it 
	 * is conflicting with the variable definition of the lambda expression.
	 * 
	 * This is another demo showing that the lambda expression has the same scope with its outside method. 
	 * And the lambda expression doesn't create its own scope.
	 */
	public ScopeTest() 
	{
		//int x= 0;
		Function<String, String> func1 = x ->
		{
			System.out.println(this);
			return x;
		};
		
		System.out.println(func1.apply(""));
	}

	@Override
	public String toString() {
		return "ScopeTest";
	}
	

}
