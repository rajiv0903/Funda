package course01._04._01_linkedlist.practice;

public class LinkedListDemoPractice {

	public static void main(String[] args) {
		
		LinkedListPractice<Integer> list = new LinkedListPractice<Integer>();
		
		System.out.println(list.length());
		
		list.addAtStart(10);
		list.addAtStart(5);
		list.addAtStart(4);
		
		
		System.out.println(list.length());
		System.out.println(list.find(10));
		System.out.println(list.find(5));
		list.deleteAtStart();
		System.out.println(list.length());
		
	}
	

}
