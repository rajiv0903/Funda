package course02._11._01_heap_klargest.practice;


public class HeapsKLargestElementsPractice <T extends Comparable<T>> 
{
	
	public T[] randomNumberArray;
	
	public HeapsKLargestElementsPractice(T[] randomNumberArray)
	{
		this.randomNumberArray = randomNumberArray;
	}
	
	public void printMaximumKElements(Class<T> clazz, int k) throws MinHeapPractice.HeapEmptyException, 
	MinHeapPractice.HeapFullException 
	{
		if(randomNumberArray == null || randomNumberArray.length <=0)
			return;
		
		MinHeapPractice minHeap = new MinHeapPractice(clazz, k);
		
		for (T data: randomNumberArray)
		{
			if(minHeap.isEmpty()){
				minHeap.insert(data);
			}
			else
			{
				if(!minHeap.isFull() || minHeap.getHighestPriority().compareTo(data) <0)
				{
					if(minHeap.isFull())
						minHeap.removeHighestPriority();
					
					minHeap.insert(data);
				}
			}
		}
		
		minHeap.printHeapArray();
	}

}
