package course01._08._02_shellsort.practice;

public class ShellSorterPractice <T extends Comparable<T>>{
	
	
	public void sort(T[] data)
	{
		if(data == null || data.length ==0)
			return;
		int knuthNum = maxKnuthSequenceNumber(data.length);
		while (knuthNum >= 1)
		{
			for (int i=0; i < knuthNum ; i++)
			{
				insertionSortWithGap(data, i, knuthNum);
			}
			knuthNum = (knuthNum-1)/3;
		}
	}
	
	private void insertionSortWithGap(T[] data, int startIndex, int gap)
	{
		int i = startIndex;
		while (i <data.length)
		{
			T current = data[i];
			int j = i - gap;

			while (j >= 0 && data[j].compareTo(current) >= 0) {
				data[j + gap] = data[j];
				j = j - gap;
			}
			data[j + gap] = current;
			i = i + gap;
		}
	}
	
	private int maxKnuthSequenceNumber(int size)
	{		
		int h = 1;
		while (h < size/3)
		{
			h = 3*h+1;
		}
		return h;
	}

}
