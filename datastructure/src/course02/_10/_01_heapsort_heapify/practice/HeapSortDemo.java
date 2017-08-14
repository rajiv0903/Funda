package course02._10._01_heapsort_heapify.practice;

public class HeapSortDemo {
	
	public static void main(String[] args) 
	{
	
		Integer[] array = {4, 6, 9, 2, 10, 56, 12, 5, 1, 17, 14};
		
		HeapSortPractice<Integer> hs = new HeapSortPractice<Integer>(Integer.class, array);
		hs.printArray();
		hs.heapSort();
		hs.printArray();
	}

}
