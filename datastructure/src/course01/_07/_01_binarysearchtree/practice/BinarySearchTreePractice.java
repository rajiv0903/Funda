package course01._07._01_binarysearchtree.practice;



public class BinarySearchTreePractice<T extends Comparable<T>> {

	private TreeNodePractice<T> root;

	public TreeNodePractice<T> find(T data) 
	{		
		if(this.root != null)
			return this.root.find(data);
		return null;
	}
	public T largest() 
	{
		if(this.root != null)
			return this.root.largest();
		return null;
	}
	public T smallest() 
	{
		if(this.root != null)
			this.root.smallest();
		return null;
	}
	public void insert(T data) 
	{
		if(this.root == null)
			this.root = new TreeNodePractice<T>(data);
		else
			this.root.insert(data);
	}
	public int height() 
	{
		if(this.root == null) 
			return 0;
		return this.root.height();
	}

	public void delete(T data) {}

	private TreeNodePractice<T> getSuccessor(TreeNodePractice<T> node) 
	{
		return null;
	}
	public void traverseInOrder() 
	{
		if (this.root != null)
			this.root.traverseInOrder();
		System.out.println();
	}
	public int numOfLeafNodes() 
	{
		if(this.root != null)
			return this.root.numOfLeafNodes();
		return 0;
	}
	public  BinarySearchTreePractice<T> createFromSortedArray(T[] data) 
	{
		BinarySearchTreePractice<T> bst = new BinarySearchTreePractice<T>();		
		if (data != null && data.length > 0) {
			bst.root = root.addSorted(data, 0, data.length-1);
		}
		return bst;
	}
	public boolean isBinarySearchTree()
	{
		return isBinarySearchTree(this.root, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	private boolean isBinarySearchTree(TreeNodePractice<T> node , Integer min, Integer max)
	{
		if(node == null)
			return true;
		if((Integer)node.getData() <= min ||(Integer) node.getData() >= max)
		{
			return false;
		}
		return isBinarySearchTree(node.getLeftChild(), min, (Integer)node.getData()) && isBinarySearchTree(node.getRightChild(), (Integer)node.getData(), max);
	}
	
}
