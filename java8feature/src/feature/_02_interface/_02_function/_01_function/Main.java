package feature._02_interface._02_function._01_function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Java Built-in Functional Interfaces
 * 
 * To represent a function that takes an argument of type T and returns a result of type R.
 * Function - public R apply(T t);
 * 
 * To represent a function that takes two arguments of types T and U, and returns a result of type R.
 * BiFunction - public R apply(T t, U u);
 * 
 * To represent a boolean function that returns true or false for the specified argument.
 * Predicate- public boolean test(T  t);
 * 
 * To represent a boolean function that returns true or false for the two specified arguments.
 * BiPredicate - public boolean test(T t, U u);
 * 
 * To represent an operation that takes an argument and returns no result.
 * Consumer- public void accept(T t);
 * 
 * To represent an operation that takes two arguments and returns no result.
 * BiConsumer- public void accept(T t, U  u);
 * 
 * To represent a function that returns a value as of type T.
 * Supplier- public T get();
 * 
 * To represent a function that takes an argument and returns a result of the same type.
 * UnaryOperator- public T  apply(T t);
 * 
 * To represent a function that takes two arguments and returns a result of the same type.
 * BinaryOperator - public T apply(T t1, T t2);
 */
public class Main {

	public static void main(String[] argv) {
		
		//Lambda Expression to by calling the method defined in the functional interface
		Processor stringProcessor1  = (String str) -> str.length();
		
		Processor stringProcessor2  = (str) -> str.length();
		
		String name = "Java Lambda";
	    int length1 = stringProcessor1.getStringLength(name);
	    System.out.println(length1);
	    int length2 = stringProcessor2.getStringLength(name);
	    System.out.println(length2);
	    
	    
	    List<String> list = new ArrayList<String>();
	    list.add("1");
	    
	    Set<String> set = new MyFunction().apply(list);
	    System.out.println(set);
	    
	    Map<String, String> map = new MyBiFunction().apply(list, set);
	    System.out.println(map);
	    
	}
}


class MyFunction implements Function<List<String>, Set<String>>{
	
	public Set<String> apply(List<String> list){
		Set<String> set = new HashSet<String>(list);
		return set;
	}
}

class MyBiFunction implements BiFunction<List<String>, Set<String>, Map<String, String>>
{
	public Map<String, String> apply(List<String> list, Set<String> set)
	{
		Map<String, String> map = new HashMap<>();
		return map;
	}
}

