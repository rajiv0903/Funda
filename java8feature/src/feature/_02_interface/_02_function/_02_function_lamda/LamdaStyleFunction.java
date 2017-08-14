package feature._02_interface._02_function._02_function_lamda;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class LamdaStyleFunction {

	public static void main(String[] args) {
		
		//Define function apply method
		//Function<T, R>
		Function<Integer, String> iToHex = i -> Integer.toHexString(i) + " hello!";
		
		//chain a string transformation
		//Function<T, V>
		Function<Integer,String> itoUpperHex = iToHex.andThen(String::toUpperCase);
		
		
		Function<Integer, String> increment = i -> Integer.toHexString(i) + "5";
		Function<Integer,Integer> incrementThen = 
				increment.andThen(String::toUpperCase) // 55 for input 5
				.andThen(Integer::parseInt); //Return Int value of String 55
		
		
		//On the other hand, if your already existing function is
		UnaryOperator<String> quote=s -> "'"+s+"'";
		Function<Integer,String> iuToHex = quote.compose(Integer::toHexString);
		
		int i =5;
		System.out.println( 	itoUpperHex.apply(i));
		
		System.out.println( incrementThen.apply(i));
	}

}
