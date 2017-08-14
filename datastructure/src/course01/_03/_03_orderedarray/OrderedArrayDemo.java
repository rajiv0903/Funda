package course01._03._03_orderedarray;

public class OrderedArrayDemo {

	public static void main(String[] args) {
		OrderedArray oa = new OrderedArray(10);
		oa.insert(5);
		oa.insert(4);
		oa.insert(10);
		oa.insert(7);
		oa.insert(3);
		oa.insert(6);
		System.out.println(oa);
		System.out.println(oa.binarySearch(7));
		oa.delete(5);
		System.out.println(oa);
	}
	
}
