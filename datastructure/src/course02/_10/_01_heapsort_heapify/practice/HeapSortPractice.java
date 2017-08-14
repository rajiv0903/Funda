package course02._10._01_heapsort_heapify.practice;
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
		//find the parent 
		int i = (index-1)/2;
		
		while (i >=0 && array[i].compareTo(array[index]) <0) //if parent is small then swap
		{
			T tmp = array[i];
			array[i] = array[index];
			array[index] = tmp;
			index = i; //index becomes parent 
			i = (index-1)/2; //i becomes parent 
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
		
		for (int i=0; i < array.length; i++)
		{
			int lastElementToSwap = array.length - 1 - i;
			//swapping 
			T tmp = array[0];
			array[0] = array[lastElementToSwap];
			array[lastElementToSwap] = tmp;
			
			//fix Down till last element to swap 
			fixDown(0, lastElementToSwap-1);
		}
	}
	
	private void fixDown(int index, int upto) 
	{		
		while (index <= upto)
		{
			int leftChild = 2* index+1;
			int rightChild = 2*index+2;
			
			if(leftChild <= upto)
			{
				int childToSwap;
				if(rightChild > upto)
					childToSwap = leftChild;
				else
					childToSwap= array[leftChild].compareTo(array[rightChild]) >0 ? leftChild: rightChild;
					
				if(array[index].compareTo(array[childToSwap]) <0 )
				{
					T tmp = array[index];
					array[index] = array[childToSwap];
					array[childToSwap] = tmp;
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

}
