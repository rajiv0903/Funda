package feature._03_lambda._11_variable_scope;

import java.util.function.Function;

public class VariableScopeTest {

	static String greeting = ""; 
	
	public static void main(String[] args) {
	
		final String surname = "Chaudhuri"; 
		
		Function<String,String> func1 = firstName -> 
			{	
				greeting ="Hello! "; 
				//surname ="Bose"; //not allowed
				return greeting + " "+firstName+ " "+surname ;
			};
	    System.out.println(func1.apply("Tithi"));

	}

}
