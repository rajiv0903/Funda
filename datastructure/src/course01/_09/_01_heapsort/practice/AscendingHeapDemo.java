package course01._09._01_heapsort.practice;


public class AscendingHeapDemo {
	
	public static void main(String[] args) 
	{
		AscendingHeapPractice<Integer> heap = new AscendingHeapPractice<Integer>(10);
		heap.insert(10);
		heap.insert(15);
		heap.insert(27);
		heap.insert(5);
		heap.insert(2);
		heap.insert(21);
		System.out.println(heap);
		System.out.println(heap.deleteRoot());
		System.out.println(heap);
	}

}
