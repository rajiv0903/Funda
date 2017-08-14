package course01._03._03_orderedarray.practice;


public class OrderedArrayPracticeDemo {

	public static void main(String[] args) {
		
		OrderedArrayPractice<Integer> oa = new OrderedArrayPractice<Integer>(10);
		oa.insert(5);
		oa.insert(4);
		oa.insert(10);
		oa.insert(7);
		oa.insert(3);
		oa.insert(6);
		System.out.println(oa);
		System.out.println(oa.find(7));
		oa.delete(5);
		System.out.println(oa);
	}
}
