package feature._04_stream._04_create;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CreateTest {

	public static void main(String[] args) {
		
		
		System.out.println("Stream Of--------");
		Stream<String> stream1  = Stream.of("java2s.com");
		stream1.forEach(System.out::println);
	    
		System.out.println("Stream Of--------");
	    Stream<String> stream2  = Stream.of("XML", "Java",  "CSS", "SQL");
	    stream2.forEach(System.out::println);
	    
	    System.out.println("Stream Of from Array--------");
	    String[] names = { "XML", "Java", "SQL", "CSS" };
	    Stream<String> stream3 = Stream.of(names);
	    stream3.forEach(System.out::println);
	    
	    System.out.println("Stream Of from Builder--------");
	    Stream<String> stream4  = Stream.<String>builder()
	            .add("XML")
	            .add("Java")
	            .add("CSS")
	            .add("SQL")
	            .build();
	    stream4.forEach(System.out::println);

	    System.out.println("IntStream Range--------");
	    IntStream oneToFive  = IntStream.range(1, 6);
	    //IntStream oneToFive  = IntStream.rangeClosed(1, 5);
	    oneToFive.forEach(System.out::println);
	    
	    System.out.println("Stream Empty--------");
	    Stream<String> stream5  = Stream.empty();
	    stream5.forEach(System.out::println);
	    
	    System.out.println("Stream iterate--------");
	    Stream<Long> tenNaturalNumbers = Stream.iterate(1L, n  ->  n  + 1)
	            .limit(10);
	    tenNaturalNumbers.forEach(System.out::println);
	    
	    System.out.println("Stream iterate- isOdd-------");
	    Stream.iterate(2L, n  ->  n  + 1)
	    .filter(CreateTest::isOdd)
	    .limit(5)
	    .forEach(System.out::println);
	    
	    System.out.println("Stream iterate- skip-------");
	    //The following code uses skip to skip the first 100 odd number
	    Stream.iterate(2L, n  ->  n  + 1)
	    .filter(CreateTest::isOdd)
	    .skip(100)
	    .limit(5)
	    .forEach(System.out::println);
	    
	    System.out.println("Stream generate--------");
	    Stream.generate(Math::random)
	    .limit(5)
	    .forEach(System.out::println);
	    
	    System.out.println("Stream generate--------");
	    Stream.generate(CreateTest::next)
	    .limit(5)
	    .forEach(System.out::println);
	    
	    System.out.println("Stream Random--------");
	    new Random().ints()
	    .limit(5)
	    .forEach(System.out::println);
	    
	    System.out.println("Stream Random nextInt--------");
	    Stream.generate(new Random()::nextInt)
	    .limit(5)
	    .forEach(System.out::println);
	    
	    System.out.println("IntStream generate--------");
	    IntStream.generate(() ->  0)
	    .limit(5)
	    .forEach(System.out::println);
	    
	    
	    //Stream from collection 
	    Set<String> names2 = new HashSet<>(); 
	    names2.add("XML");
	    names2.add("Java");

	    Stream<String> sequentialStream  = names2.stream();
	    sequentialStream.forEach(System.out::println);

	    Stream<String> parallelStream = names2.parallelStream();
	    parallelStream.forEach(System.out::println);
	    
	    System.out.println("Stream of characters from str..............");
	    String str = "5 123,123,qwe,1,123, 25";
	    str.chars()
	    .filter(n ->  !Character.isDigit((char)n) &&   !Character.isWhitespace((char)n))
	    .forEach(n ->  System.out.print((char)n));
	    
	    System.out.println("");
	    System.out.println("Stream from Regex..............");
	    String str2 = "XML,CSS,HTML"; 
	    Pattern.compile(",")
	    .splitAsStream(str2)
	    .forEach(System.out::println);
	    
	    
	    System.out.println("Stream from file..........");
	    Path path = Paths.get("C:/Rajiv/eworkspace/myaccount_wfm/springfarm/java8feature/src/feature/_04_stream/_04_create/CreateTest.java");
	    try (Stream<String> lines = Files.lines(path)) {
	      lines.forEach(System.out::println);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    
	}
	
	public static boolean isOdd(long number) {
	    if (number % 2 == 0) {
	      return false;
	    }
	    return true;
	  }
	
	static int i=0;
	  private static int next(){
	    i++;
	    return i;
	  }

}
