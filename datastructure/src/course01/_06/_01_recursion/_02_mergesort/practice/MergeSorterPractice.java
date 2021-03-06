package course01._06._01_recursion._02_mergesort.practice;


public class MergeSorterPractice<T extends Comparable<T>> {
	
	public void sort(T[] data)
	{
		mergeSort(data, 0, data.length-1);
	}
	
	private void mergeSort(T[] data, int start, int end)
	{
		if(start <end)
		{
			int mid = (int) Math.floor((start+end)/2);
			mergeSort(data, start, mid);
			mergeSort(data, mid+1, end);
			merge(data, start, mid, end);
		}
	}
	
	private void merge(T[] data, int start, int mid, int end)
	{
		int sizeOfLeft = mid - start +1 ;
		int sizeOfRight = end - mid ;
		T[] left = (T[]) new Comparable[sizeOfLeft];
		T[] right  = (T[]) new Comparable[sizeOfRight];
		for (int i = 0 ; i <sizeOfLeft; i++){
			left[i] = data[start+i];
		}
		for (int j= 0; j< sizeOfRight; j++){
			right[j] = data[mid+1+j];
		}
		int i=0, j=0;
		for (int k = start; k <= end ; k++)
		{
			if( (j >= sizeOfRight)  || (i < sizeOfLeft &&  left[i].compareTo(right[j]) <= 0))
			{
				data[k] = left[i];
				i++;
			}
			else{
				data[k] = right[j];
				j++;
			}
		}
	}
}
