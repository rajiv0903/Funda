package course01._03._01_selectionsort.practice;

public class SelectionSorterPractice <T extends Comparable<T>>{
	
	
	public void sort(T[] data)
	{
		for (int i = 0 ; i < data.length-1; i++)
		{
			int minIdex = i;
			
			for (int j = i+1 ; j < data.length; j++)
			{
				if( data[j].compareTo(data[minIdex]) <0)
				{
					minIdex = j;
				}
				
				T tmp = data [minIdex];
				data[minIdex] = data[i];
				data[i] = tmp;
			}
		}
		
	}

}
