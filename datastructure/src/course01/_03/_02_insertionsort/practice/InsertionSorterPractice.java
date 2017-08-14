package course01._03._02_insertionsort.practice;

public class InsertionSorterPractice <T extends Comparable<T>>{
	
	public void sort(T[] data)
	{
		for (int i = 0 ; i < data.length ; i++)
		{
			T current = data[i];
			
			int j = i-1;
			
			while (j >=0 && data[j].compareTo(current) > 0)
			{
				data[j+1] = data[j]; //make space 
				j --;
			}
			
			data[j+1] = current;
			
		}
	}

}
