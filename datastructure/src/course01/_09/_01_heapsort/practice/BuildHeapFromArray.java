package course01._09._01_heapsort.practice;

import java.util.Arrays;


public class BuildHeapFromArray <T extends Comparable<T>> {

	private T[] heapData;
	private int currentPosition = -1;
	
	public BuildHeapFromArray (T[] data)
	{
		this.heapData = (T[]) new Comparable[data.length];
		heapify(data);
	}
	//This builds a heap from the given data array
	private  void heapify(T[] data) 
	{
		for (int i=0; i < data.length; i++)
		{
			insert(data[i]);
		}
	}
	
	private void insert(T item) 
	{
		if (isFull()) 
			throw new RuntimeException("Heap is full");
		
		this.heapData[++currentPosition] = item;
		fixUp(currentPosition);
		
	}
	
	private boolean isFull() 
	{
		return currentPosition == heapData.length-1;
	}
	
	private void fixUp(int index) {
		
		//find parent index 
		int i = (index -1)/2;
		//continue fixing up by comparing immediate parent 
		while (i >= 0 && heapData[i].compareTo(heapData[index]) < 0)
		{
			T tmp = heapData[i];
			heapData[i] = heapData[index];
			heapData[index] = tmp;
			
			index = i;
			i = (index -1)/2;
		}
	}
	
	/**
	 * Heap Sort could be called in a heap array, so we assume that this heap was
	 * built up by calling insert repeatedly, and then we call heapSort on it.
	 */
	public void heapSort() 
	{
		for (int i=0; i < currentPosition; i++) 
		{
			T tmp = heapData[0]; // max element
			heapData[0] = heapData[currentPosition-i]; // bring last element to the root
			heapData[currentPosition-i] = tmp; // put max at the last of unsorted part
			fixDown(0, currentPosition-i-1);
		}
	}
	
	private void fixDown(int index, int upto) 
	{
		if (upto < 0) upto = currentPosition;
		while (index <= upto) 
		{
			int leftChild = 2 * index + 1;
			int rightChild = 2 * index + 2;
			if (leftChild <= upto) 
			{
				int childToSwap;
				if (rightChild > upto)
					childToSwap = leftChild;
				else
					childToSwap = (heapData[leftChild].compareTo(heapData[rightChild])> 0) ? 
							leftChild : rightChild;
				
				if (heapData[index].compareTo(heapData[childToSwap]) <0) 
				{
					T tmp = heapData[index];
					heapData[index] = heapData[childToSwap];
					heapData[childToSwap] = tmp;
				} else {
					break;
				}
				index = childToSwap;
			} 
			else 
			{
				break;
			}
			
		}
	}
	@Override
	public String toString() {
		return "BuildHeapFromArray [heapData=" + Arrays.deepToString(heapData)
				+ "]";
	}
	
	
}
