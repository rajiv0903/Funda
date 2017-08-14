package feature._03_lambda._07_function_interface;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class FunctionIntefaceExample {

	public static void main(String[] args) {
		
		IntFunction<String>  intFunc =  x -> Integer.toString(x);
		
		System.out.println(intFunc.apply(33).length());
		
		ToIntFunction<String> toIntFunc = x -> Integer.parseInt(x);
		
		System.out.println(toIntFunc.applyAsInt("33")+1);

	}

}
