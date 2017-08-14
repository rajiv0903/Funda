package course02._10._02_heapsort.practice;
import java.lang.reflect.Array;

public class HeapSortPractice <T extends Comparable<T>>{
	
	private T[] array;
	
	public HeapSortPractice(Class<T> clazz, T[] array) 
	{
		this.array = (T[]) Array.newInstance(clazz, array.length);
		heapify(array);
	}
	
	//This builds a heap from the given data array
	private  void heapify(T[] array) 
	{
		for (int i=0; i < array.length; i++)
		{
			insert(array[i], i);
		}
	}
	
	private void insert(T item, int index) 
	{
		array[index] = item;
		fixUp(index);
	}
	
	private void fixUp(int index) 
	{
		//find parent index
		int parentIndex = (index-1)/2;
		
		while (parentIndex >=0 && array[parentIndex].compareTo(array[index])< 0)
		{
			T tmp = array[parentIndex];
			array[parentIndex] = array[index];
			array[index] = tmp;
			
			//current index becomes parent 
			index = parentIndex;
			//find current's parent index
			parentIndex = (index-1)/2;
		}
	}
	
	 public void printArray() 
	 {
        for (T n : array) 
        {
            System.out.print(n + ", ");
        }
        System.out.println();
	 }
	 
	public void heapSort() 
	{
		for (int i = 0 ; i < array.length; i++)
		{
			//swap with last element 
			int lastIndex = array.length-1-i;
			T tmp = array[0];
			array[0] = array[lastIndex];
			array[lastIndex] = tmp;
			fixDown(0, lastIndex-1);			
		}
	}
	
	private void fixDown(int index, int upto) 
	{		
		while (index <= upto)
		{
			int left = 2*index+1;
			int right = 2*index+2;
			
			if(left <= upto)
			{
				int childToSwap;
				if (right > upto)
					childToSwap = left;
				else
					childToSwap = array[left].compareTo(array[right]) >0 ? left: right;
					
				if(array[index].compareTo(array[childToSwap]) <0)
				{
					T tmp = array[index];
					array[index] = array[childToSwap];
					array[childToSwap] = tmp;
				}
				else
					break;
				
				index = childToSwap;
			}
			else
			{
				break;
			}
		}
	}

}
