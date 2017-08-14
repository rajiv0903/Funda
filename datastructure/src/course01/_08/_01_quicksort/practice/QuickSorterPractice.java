package course01._08._01_quicksort.practice;

public class QuickSorterPractice<T extends Comparable<T>> 
{
	
	public QuickSorterPractice(){}
	
	public T[] sort(T[] data)
	{
		quickSort(data, 0, data.length-1);
		return data;
	}
	
	private void quickSort(T[] data, int start, int end)
	{
		if(start <end)
		{
			int pivotIndex = partition(data, start, end);
			quickSort(data, start, pivotIndex-1);
			quickSort(data, pivotIndex+1, end);
		}
	}
	
	/**
	 * All elements smaller than the pivot will be at left side and greater than the pivot 
	 * will be at right side
	 * @param data
	 * @param start
	 * @param end
	 * @return
	 */
	private int partition(T[] data, int start, int end)
	{
		T pivot = data[end];
		int i = start;
		
		for (int j = start; j < end-1; j++)
		{
			if(data[j].compareTo(pivot)<0)
			{
				T tmp = data[j];
				data[i] = data[j];
				data[j] = tmp;
				i++;
			}
		}
		data[end] = data[i];
		data[i] = pivot;
		return i;
	}

}
