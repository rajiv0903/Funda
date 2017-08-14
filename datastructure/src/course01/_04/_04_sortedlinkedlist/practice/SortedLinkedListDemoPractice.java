package course01._04._04_sortedlinkedlist.practice;

public class SortedLinkedListDemoPractice {

	public static void main(String[] args) {
		
		SortedLinkedListPractice<Integer> list = new SortedLinkedListPractice<Integer>();
		list.insert(10);
		list.insert(8);
		list.insert(4);
		list.insert(5);
		list.insert(12);
		
		System.out.println(list);
	}
}
