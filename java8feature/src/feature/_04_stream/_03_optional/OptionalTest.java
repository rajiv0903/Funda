package feature._04_stream._03_optional;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class OptionalTest {

	
	public static void main(String[] args) {
		
		
		Optional<String> empty  = Optional.empty();
	    System.out.println(empty);

	    Optional<String> str = Optional.of("Rajiv Optional-1");
	    System.out.println(str);

	    String nullableString = ""; 
	    Optional<String> str2  = Optional.of(nullableString);
	    System.out.println(str2);
	    
	    
	    Optional<String> str3 = Optional.of(nullableString);
	    if (str3.isPresent()) {
	      String value = str3.get();
	      System.out.println("Optional contains " + value);
	    } else {
	      System.out.println("Optional is  empty.");
	    }
	    
	    
	    Optional<String> str4 = Optional.of("Rajiv Optional-3");
	    str4.ifPresent(value -> System.out.println("Optional contains " + value));
	    
	    
	    OptionalInt number = OptionalInt.of(2);

	    if (number.isPresent()) {
	      int value = number.getAsInt();
	      System.out.println("Number is " + value);
	    } else {
	      System.out.println("Number is absent.");
	    }
	    
	    
	    OptionalInt maxOdd = IntStream.of(10, 20, 30).filter(n -> n % 2 == 1).max();
	    if (maxOdd.isPresent()) {
	      int value = maxOdd.getAsInt();
	      System.out.println("Maximum odd  integer is " + value);
	    } else {
	      System.out.println("Stream is  empty.");
	    }
	    
	}
}
