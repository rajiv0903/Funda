package course02._11._02_heap_max_elements_min_heap.practice;

public class GetMaxElementFromMinHeaps <T extends Comparable<T>>{
	
	public static Integer[] randomNumberArray = {2, 5, 6, 100, 67, 88, 4, 1, 3, 9, 99};
	
	public static void main(String[] args) throws MinHeapPractice.HeapFullException,
	MinHeapPractice.HeapEmptyException
	{
		
		MinHeapPractice<Integer> heap = new MinHeapPractice<Integer>(Integer.class, randomNumberArray.length);
		
		for (Integer value: randomNumberArray){
			heap.insert(value);
		}
		
		System.out.println("************Heap Data*********");
		heap.printHeapArray();
		System.out.println("******************************");
		
		
		System.out.println("************Max Data from MIN Heap*********");
		System.out.println(
				(Integer)
					(new GetMaxElementFromMinHeaps<Integer>().getMaximum(heap))
				);
	}
	
	private T getMaximum(MinHeapPractice<T> heap)
	{
				
		if(heap.isEmpty())
			return null;
		int lastIndex = heap.getCount()-1;
		int lastParentIndex = heap.getParentIndex(lastIndex);
		int firstChildIndex = lastParentIndex +1;
		T maxData = heap.getElementAtIndex(firstChildIndex);
		for (int i = firstChildIndex; i <= lastIndex; i++)
		{
			if(maxData.compareTo(heap.getElementAtIndex(i))<0)
			{
				maxData = heap.getElementAtIndex(i);
			}
		}
		return maxData;
		
	}

}
