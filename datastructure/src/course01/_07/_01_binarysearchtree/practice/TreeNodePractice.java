package course01._07._01_binarysearchtree.practice;


public class TreeNodePractice<T extends Comparable<T>>
{
	private T data;
	private TreeNodePractice<T> leftChild;
	private TreeNodePractice<T> rightChild;

	public TreeNodePractice(T data) {
		this.data = data;
	}
	
	public  TreeNodePractice<T> addSorted(T[] data, int start, int end) 
	{		
		if(end >= start)
		{
			int mid = (start+end)/2;
			TreeNodePractice<T> newNode = new TreeNodePractice<T>(data[mid]);
			newNode.setLeftChild(addSorted(data, start, mid-1));
			newNode.setRightChild(addSorted(data, mid+1, end));
			return newNode;
		}
		return null;
	}
	public int height() 
	{
		if(isLeaf()) return 1;
		int left =0;
		int right =0;
		if(this.leftChild != null)
			left = this.leftChild.height();
		if(this.rightChild != null)
			right = this.rightChild.height();
		return (left>right)? left+1 : right+1;
	}
	public int numOfLeafNodes() 
	{
		if(isLeaf())
			return 1;
		int leftLeaves = 0;
		int rightLeaves = 0;
		if(this.leftChild != null)
			leftLeaves = this.leftChild.numOfLeafNodes();
		if(this.rightChild != null)
			rightLeaves = this.rightChild.numOfLeafNodes();
		return leftLeaves+rightLeaves;
	}
	public boolean isLeaf() 
	{
		return (this.leftChild == null && this.rightChild == null);
	}
	public void traverseInOrder() 
	{
		if(this.leftChild != null)
			this.leftChild.traverseInOrder();
		System.out.print(this + " ");
		if(this.rightChild != null)
			this.rightChild.traverseInOrder();
	}
	public TreeNodePractice<T> find(T data) 
	{
		if(this.data.compareTo(data) ==0)
			return this;
		if(this.data.compareTo(data) <0 && this.rightChild != null)
			return this.rightChild.find(data);
		if(this.data.compareTo(data) >0 && this.leftChild != null)
			return this.leftChild.find(data);
		return null;
	}
	public void insert(T data) 
	{		
		if(this.data.compareTo(data) <0)
		{
			if(this.rightChild == null)
				this.rightChild = new TreeNodePractice<T>(data);
			else
				this.rightChild.insert(data);
		}
		else
		{
			if(this.leftChild == null)
				this.leftChild = new TreeNodePractice<T>(data);
			else
				this.leftChild.insert(data);
		}
	}
	public T largest() 
	{
		if (this.rightChild == null)
			return this.data;
		else
			return this.rightChild.largest();
	}
	public T smallest() 
	{
		if(this.leftChild == null)
			return this.data;
		else
			return this.leftChild.smallest();
	}
	public T getData() 
	{
		return data;
	}
	public TreeNodePractice<T> getLeftChild() 
	{
		return leftChild;
	}
	public void setLeftChild(TreeNodePractice<T> left) 
	{
		this.leftChild = left;
	}
	public TreeNodePractice<T> getRightChild() 
	{
		return rightChild;
	}
	public void setRightChild(TreeNodePractice<T> right) 
	{
		this.rightChild = right;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.data);
	}

}
