package course01._07._01_binarysearchtree;

public class BinarySearchTreeDemo {

	public static void main(String[] args) {
		int[] sample = { 212, 580, 6, 7, 28, 84, 112, 434};
		BinarySearchTree bst = new BinarySearchTree();
		for (int x : sample) {
			bst.insert(x);
		}
		System.out.println(bst.find(65));
		System.out.println(bst.smallest());
		System.out.println(bst.largest());
//		bst.delete(84);
		System.out.println(bst.numOfLeafNodes());
		System.out.println(bst.height());
		bst.traverseInOrder();
	}

}
