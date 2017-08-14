package course01._08._02_shellsort.practice;

public class InsertionSorterPractice <T extends Comparable<T>>{
	
	public void sort(T[] data)
	{
		if (data == null || data.length == 0)
			return;
		
		for (int i = 0; i< data.length ; i++)
		{
			T current = data[i];
			int j = i-1;
			
			while (j>=0 && data[j].compareTo(current) > 0)
			{
				data[j+1] = data[j];
				j--;
			}
			data[j+1] = current;
		}
	}

}
