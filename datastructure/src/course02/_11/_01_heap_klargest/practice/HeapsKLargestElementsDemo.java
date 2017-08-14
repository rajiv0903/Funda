package course02._11._01_heap_klargest.practice;

public class HeapsKLargestElementsDemo 
{
	public static Integer[] randomNumberArray = {2, 5, 6, 21, 67, 88, 4, 1, 3, 9, 99};
	
	public static void main(String[] args) 
	throws MinHeapPractice.HeapEmptyException, 
	MinHeapPractice.HeapFullException 
	{
		
		HeapsKLargestElementsPractice<Integer> hkep = new HeapsKLargestElementsPractice<Integer>(randomNumberArray);
		hkep.printMaximumKElements(Integer.class, 3);
		hkep.printMaximumKElements(Integer.class,5);
		hkep.printMaximumKElements(Integer.class,6);
	        
	}

}
