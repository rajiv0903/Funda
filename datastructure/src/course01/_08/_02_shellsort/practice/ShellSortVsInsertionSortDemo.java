package course01._08._02_shellsort.practice;

import java.util.Arrays;
import java.util.Random;

public class ShellSortVsInsertionSortDemo {
	
	public static void main(String[] args) {
		
		int lengthOfArray = 100000;
		Integer[] data1 = new Integer[lengthOfArray];
		
		Random rand = new Random(System.currentTimeMillis());
		
		for (int i=0; i < lengthOfArray; i++) 
		{
			data1[i] = rand.nextInt(100001);
		}
		Integer[] data2 = new Integer[lengthOfArray];
		System.arraycopy(data1, 0, data2, 0, lengthOfArray);
		
		long start = System.currentTimeMillis();
		new ShellSorterPractice<Integer>().<Integer>sort(data1);
		long end = System.currentTimeMillis();
		System.out.println("Takes " + (end-start) + " millis for Shell Sort");

		long start2 = System.currentTimeMillis();
		new InsertionSorterPractice<Integer>().<Integer>sort(data2); // InsertionSort was implemented earlier, calling it to compare with Shell Sort
		long end2 = System.currentTimeMillis();
		System.out.println("Takes " + (end2-start2) + " millis for Insertion Sort");
		
		System.out.println(Arrays.equals(data1, data2));
	}

}
