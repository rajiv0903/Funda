package course01._03._04_insertionsorterwithgenerics.practice;


public class InsertionSorterWithGenericsPractice<T>  {
	
	public static void main(String[] args) {
		
		CirclePractice[] circles = new CirclePractice[] 
						{
							new CirclePractice(10),
							new CirclePractice(8),
							new CirclePractice(9),
							new CirclePractice(6),
							new CirclePractice(5),
							new CirclePractice(3),
							new CirclePractice(2)
							
						};
		
		long startTime = System.nanoTime();
		InsertionSorterWithGenericsPractice<CirclePractice> sorter = new InsertionSorterWithGenericsPractice<CirclePractice>();
		sorter.sort(circles);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("Time Taken:"+duration/1000 +" ms");
		for (int i=0; i < circles.length; i++) 
		{
			System.out.println(circles[i]);
		}
	}
	
	public  void sort(Comparable<T>[] objects) 
	{
		for (int i=0; i<objects.length ;i++)
		{
			Comparable<T> current = objects[i];
			
			int j = i-1;
			
			while (j>=0 && objects[j].compareTo((T)current) >0)
			{
				objects[j+1] = objects[j];
				j--;
			}
			objects[j+1] = current;
		}
	}

}
