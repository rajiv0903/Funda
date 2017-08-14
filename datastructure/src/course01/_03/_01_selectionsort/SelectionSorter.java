package course01._03._01_selectionsort;

import java.util.Arrays;

public class SelectionSorter {
	
	public static void main(String[] args) {
		int[] data = {7,3,6,8,2};
		new SelectionSorter().sort(data);
		System.out.println(Arrays.toString(data));
	}
	
	public void sort(int[] data) 
	{
		//Search the smallest element and swap it with the item in first bin- 1st Pass
		//Search the smallest element and swap it with the item in second bin- 2nd Pass
		for (int i = 0; i < data.length-1; i++) 
		{
			int minIndex = i;
			for (int j = i+1; j < data.length; j++) {
				if (data[j] < data[minIndex]) {
					minIndex = j;
				}
			}
			int tmp = data[minIndex];
			data[minIndex] = data[i];
			data[i] = tmp;
		}
	}

}
