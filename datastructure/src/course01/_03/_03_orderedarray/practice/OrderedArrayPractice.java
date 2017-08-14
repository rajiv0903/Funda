package course01._03._03_orderedarray.practice;

import java.util.Arrays;

public class OrderedArrayPractice  <T extends Comparable<T>>{

	private T[] data;
	
	public OrderedArrayPractice(int size)
	{
		data = (T[]) new Comparable[size]; 
	}
	
	public OrderedArrayPractice() 
	{
		this(100); // default size of the array is 100.
	}
	
	public int insert(T item) 
	{
		if(size() >= data.length)
			throw new RuntimeException("Capacity is full!");
		
		int index = 0;
		while ( (data[index] != null ) && data[index].compareTo(item) < 0 )
		{
			index ++;
		}
		shiftElementsToRight(index);
		data[index] = item;
		return index;
	}
	
	public int delete(T item) 
	{
		if(size() <=0)
			throw new RuntimeException("Empty array!");
		
		int i = binarySearch(item);
		if (i >= 0) 
		{
			shiftElementsToLeft(i+1);
		}
		return i;
	}
	public int find(T item) 
	{
		/*return binarySearch(item);*/
		return binarySearch(item, 0, size()-1);
	}
	
	private int size() 
	{
		int i=0;
		while((i <data.length) && (data[i]!= null)  )
		{
			i++;
		}
		return i;
	}
	
	private void shiftElementsToRight(int startIndex) 
	{
		int maxIndex = size()-1;
		for (int i = maxIndex ; i >=startIndex; i--)
		{
			data[i+1] = data[i];
		}
	}
	
	private void shiftElementsToLeft(int startIndex) 
	{
		int maxIndex = size()-1;
		for (int i = startIndex; i <= maxIndex; i++) 
		{
			data[i - 1] = data[i];
		}
		data[maxIndex] = null;
	}
	
	
	private int binarySearch(T item, int minIndex, int maxIndex) 
	{
		if(minIndex == maxIndex)
		{
			if(data[minIndex].compareTo(item) == 0)
				return minIndex;
			return -1;
		}
		int indexToLook = (int) Math.floor((minIndex + maxIndex) / 2);
		
		if(data[indexToLook].compareTo(item) == 0)
		{
			return indexToLook;
		}
		if(data[indexToLook].compareTo(item) > 0)
		{
			return binarySearch(item, minIndex, indexToLook -1);
		}
		return binarySearch(item, indexToLook +1 , maxIndex);
	}
	/**
	 * This method implements the binary search algorithm
	 * in an iterative way
	 * @param item
	 * @return the index of the item if found, -1 otherwise
	 */
	public int binarySearch(T item) 
	{
		int maxIndex = size()-1;
		int minIndex = 0; 
		int indexToLook = (int) Math.floor((minIndex+maxIndex)/2);
		
		while (data[indexToLook].compareTo(item) != 0 && minIndex <maxIndex)
		{
			//search at left side
			if(data[indexToLook].compareTo(item) > 0)
			{
				maxIndex = indexToLook -1;
			}
			else
			{
				minIndex = indexToLook +1;
			}
			indexToLook = (int) Math.floor((minIndex+maxIndex)/2);
		}
		if (data[indexToLook].compareTo(item) == 0) 
		{
			return indexToLook;
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return Arrays.deepToString(this.data);
	}
}
