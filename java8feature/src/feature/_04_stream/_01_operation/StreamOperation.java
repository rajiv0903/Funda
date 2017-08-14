package feature._04_stream._01_operation;

import java.util.Arrays;
import java.util.List;

public class StreamOperation {

	public static void main(String[] args) {
		
		
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
	    int sum = 0;
	    for (int n : numbers) {
	      if (n % 2 == 1) {
	        int square = n * n;
	        sum = sum + square;
	      }
	    }
	    System.out.println(sum);
	    
	    //With Internal Iteration
	    List<Integer> numbers2 = Arrays.asList(1, 2, 3, 4, 5);
	    int sum2 = numbers2.stream()
	        .filter(n -> n % 2  == 1)
	        .map(n  -> n * n)
	        .reduce(0, Integer::sum);

	    System.out.println(sum2);
	    
	    //Sequential and parallel
	    //An intermediate operation on a stream produces another stream.
	    //A lazy operation does not process the elements until an eager operation is called on the stream
	    List<Integer> numbers3 = Arrays.asList(1, 2, 3, 4, 5);
	    int sum3 = numbers3.parallelStream() //parallel
	        .filter(n -> n % 2  == 1) //filter() and map() are all lazy operations
	        .map(n  -> n * n)
	        .reduce(0, Integer::sum); //reduce() is eager operation

	    System.out.println(sum3);

	}

}
