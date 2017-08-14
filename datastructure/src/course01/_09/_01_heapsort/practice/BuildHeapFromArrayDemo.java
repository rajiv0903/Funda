package course01._09._01_heapsort.practice;


public class BuildHeapFromArrayDemo 
{

	public static void main(String[] args) {
		BuildHeapFromArray another = new BuildHeapFromArray(
				new Integer[]{73,16,40,1,46,28,12,21,22,44,66,90,7});

		System.out.println(another);
		another.heapSort();
		System.out.println(another);
	}
}
