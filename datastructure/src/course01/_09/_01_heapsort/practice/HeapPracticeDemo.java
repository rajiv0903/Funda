package course01._09._01_heapsort.practice;


public class HeapPracticeDemo {

	public static void main(String[] args) {
		HeapPractice<Integer> heap = new HeapPractice<Integer>(10);
		heap.insert(10);
		heap.insert(15);
		heap.insert(27);
		heap.insert(5);
		heap.insert(2);
		heap.insert(21);
		System.out.println(heap);
		System.out.println(heap.deleteRoot());
		heap.changePriority(5, 28);
		heap.changePriority(15, 1);
		System.out.println(heap);
		heap.heapSort();
		System.out.println(heap);
	}
}
