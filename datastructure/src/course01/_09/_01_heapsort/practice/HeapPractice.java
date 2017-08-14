package course01._09._01_heapsort.practice;

import java.util.Arrays;

public class HeapPractice <T extends Comparable<T>> {

	private T[] heapData;
	private int currentPosition = -1;
	
	public HeapPractice (int size)
	{
		this.heapData = (T[]) new Comparable[size];
	}
	
	public void insert(T item) 
	{		
		if(isFull())
			throw new RuntimeException("Heap is full");
		this.heapData[++currentPosition] = item;
		fixUp(currentPosition);
	}
	
	public void changePriority(T fromItem, T toItem)
	{		
		if(currentPosition <0 )
			throw new RuntimeException("Heap is empty");
		
		int index = -1; 
		for (int i= 0; i< currentPosition; i++)
		{
			if(heapData[i].compareTo(fromItem) == 0)
			{
				index = i;
				break;
			}
		}
		if(index < 0 )
			throw new RuntimeException("Item to change priority not found");
		
		heapData[index] = toItem;
		if(toItem.compareTo(fromItem) > 0 )
			fixUp(index);
		else
			fixDown(index, -1);
	}
	
	public T deleteRoot() 
	{
		T result = heapData[0];
		heapData[0] = heapData[currentPosition--]; //make root the last child
		heapData[currentPosition+1] = null; //nullify the last element
		fixDown(0, -1);
		return result;
	}
	
	private void fixDown(int index, int upto) 
	{
		
		if(upto < 0)
			upto = currentPosition;
		
		while (index <= upto)
		{
			int leftChild = 2*index +1;
			int rightChild = 2*index+2;
			
			if(leftChild <= upto)
			{
				int childToSwap;
				if(rightChild > upto)
				{
					childToSwap = leftChild;
				}
				else
				{
					childToSwap = (heapData[leftChild].compareTo(heapData[rightChild])> 0) ? 
							leftChild : rightChild;
				}
				
				if (heapData[index].compareTo(heapData[childToSwap]) <0) 
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
	private void fixUp(int index) 
	{
		int parent = (index-1)/2;
		while (parent >=0 && heapData[parent].compareTo(heapData[index]) <0) //parent is less than current
		{
			//shift up 
			T tmp = heapData[parent];
			heapData[parent] = heapData[index];
			heapData[index] = tmp;
			
			//make current index as parent 
			index = parent;
			parent = (index-1)/2; //make parent based on current index
		}
	}
	
	private boolean isFull() 
	{
		return currentPosition == heapData.length -1;
	}
	
	/**
	 * Heap Sort could be called in a heap array, so we assume that this heap was
	 * built up by calling insert repeatedly, and then we call heapSort on it.
	 */
	public void heapSort() 
	{
		/*for (int i=0; i < currentPosition; i++) 
		{
			T tmp = heapData[0]; // max element
			heapData[0] = heapData[currentPosition-i]; // bring last element to the root
			heapData[currentPosition-i] = tmp; // put max at the last of unsorted part
			fixDown(0, currentPosition-i-1);
		}*/
		
		for (int i=0; i < currentPosition; i++)
		{
			T tmp = heapData[0];
			heapData[0] = heapData[currentPosition-i]; //bring the last element to the root
			heapData[currentPosition-i] = tmp; //bring root i.e. max element to last position 
			fixDown(0, currentPosition -i -1);
		}
	}
	
	@Override
	public String toString() {
		return Arrays.deepToString(this.heapData);
	}
}
