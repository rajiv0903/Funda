package course01._07._01_binarysearchtree.practice;


public class BinarySearchTreeGenericsDemo {
	
	public static void main(String[] args) {
		
		int[] sample = { 212, 580, 6, 7, 28, 84, 112, 434};
		BinarySearchTreePractice<Integer> bst = new BinarySearchTreePractice<Integer>();
		for (int x : sample) {
			bst.insert(x);
		}
		System.out.println(bst.isBinarySearchTree());
		bst.traverseInOrder();
		System.out.println(bst.find(17));
		System.out.println(bst.smallest());
		System.out.println(bst.largest());
		System.out.println(bst.numOfLeafNodes());
		System.out.println(bst.height());
		
		/*Integer[] objectArray = new Integer[sample.length];
		for(int ctr = 0; ctr < sample.length; ctr++) {
		    objectArray[ctr] = Integer.valueOf(sample[ctr]); // returns Integer value
		}*/
		Integer[] objectArray = {6, 7, 100, 200, 400, 500, 600, 712, 900};
		bst = bst.createFromSortedArray(objectArray);
		bst.traverseInOrder();
		System.out.println(bst.height());
		System.out.println(bst.isBinarySearchTree());
	}

}
