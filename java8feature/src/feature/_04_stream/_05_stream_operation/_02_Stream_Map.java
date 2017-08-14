package feature._04_stream._05_stream_operation;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class _02_Stream_Map {

	public static void main(String[] args) {
		
		IntStream.rangeClosed(1, 5)
        .map(n -> n * n)
        .forEach(System.out::println);
		
		Stream.of(1, 2, 3)
	    .flatMap(n -> Stream.of(n, n+1))
	    .forEach(System.out::println);

	}

}
