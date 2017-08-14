package course01._02.bubblesort.practice;

import java.util.Arrays;

public class BubbleSorterDemo {
	
	public static void main(String[] args) {
		
		Integer[] data = {25,5,7,2,18,23,12,18};
		long startTime = System.nanoTime();
		BubbleSorterPractice<Integer> sorter = new BubbleSorterPractice<Integer>();
		sorter.sort(data);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("Time Taken:"+duration/1000 +" ms");
		System.out.println(Arrays.toString(data));
	}

}
