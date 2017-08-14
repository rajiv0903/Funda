package course01._06._01_recursion._02_mergesort.practice;

import java.util.Arrays;

public class MergeSortDemo {
	
	public static void main(String[] args) {
		
		Integer[] data = {25,5,7,2,18,23,12,18};
		long startTime = System.nanoTime();
		MergeSorterPractice<Integer> sorter = new MergeSorterPractice<Integer>();
		sorter.sort(data);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("Time Taken:"+duration/1000 +" ms");
		System.out.println(Arrays.toString(data));
	}

}
