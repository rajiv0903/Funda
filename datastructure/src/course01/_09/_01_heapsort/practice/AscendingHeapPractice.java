package course01._09._01_heapsort.practice;

import java.util.Arrays;

public class AscendingHeapPractice <T extends Comparable<T>>{

	private T[] heapData;
	private int currentPosition = -1;
	
	public AscendingHeapPractice(int size) {
		this.heapData = (T []) new Comparable [size];
	}
	
	public void insert(T item) {
		if (isFull()) 
			throw new RuntimeException("Heap is full");
		
		this.heapData[++currentPosition] = item;
		fixUp(currentPosition);
		
	}
	public T deleteRoot() 
	{
		if (currentPosition < 0) 
			throw new RuntimeException("Heap is empty");
		
		T result = heapData[0];
		heapData[0] = heapData[currentPosition--];
		heapData[currentPosition+1] = null;
		fixDown(0);
		
		return result;
	}
	private void fixDown(int index) 
	{
		while (index <= currentPosition) 
		{
			int leftChild = 2 * index + 1;
			int rightChild = 2 * index + 2;
			
			if (leftChild <= currentPosition) 
			{
				int childToSwap;
				if (rightChild > currentPosition)
					childToSwap = leftChild;
				else
					childToSwap = (heapData[leftChild].compareTo(heapData[rightChild]) <0) ? leftChild : rightChild;
				 
				if (heapData[index].compareTo(heapData[childToSwap]) >0) 
				{
					T tmp = heapData[index];
					heapData[index] = heapData[childToSwap];
					heapData[childToSwap] = tmp;
				} 
				else 
				{
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
	
	private void fixUp(int index) {
		
		int i = (index-1)/2;
		while (i >= 0 && heapData[i].compareTo(heapData[index]) >0)
		{
			T tmp = heapData[i];
			heapData[i] = heapData[index];
			heapData[index] = tmp;
			
			index = i;
			i = (index-1)/2;
		}
	}
	
	private boolean isFull() {
		return currentPosition == heapData.length-1;
	}

	@Override
	public String toString() {
		return "AscendingHeapPractice [heapData=" + Arrays.deepToString(heapData)
				+ "]";
	}
	
}
