package course01._02.bubblesort.practice;

public class BubbleSorterPractice<T extends Comparable<T>> 
{
	
	public void sort (T[] data)
	{
		if(data== null || data.length ==0)
			return ;
		
		for (int i = 0 ; i < data.length -1 ; i++)
		{
			for (int j = 0 ; j < data.length -1 - i; j++)
			{
				if (data[j].compareTo(data[j+1]) > 0)
				{
					T tmp = data[j+1];
					data[j+1] = data[j];
					data[j] = tmp;
				}
			}
		}
	}

}
