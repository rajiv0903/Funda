package course02._06._04_depthfirst_in_and_post_order.practice;

public class NodePractice<T> {

	private T data;
	private NodePractice<T> leftChild;
	private NodePractice<T> rightChild;

	public NodePractice(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public NodePractice<T> getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(NodePractice<T> leftChild) {
		this.leftChild = leftChild;
	}

	public NodePractice<T> getRightChild() {
		return rightChild;
	}

	public void setRightChild(NodePractice<T> rightChild) {
		this.rightChild = rightChild;
	}

}
