package course01._04._05_assignment._01;


public class Node<T> {
	private T data;
	private Node<T> nextNode;
	
	public Node(T data) {
		this.data = data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
	
	public Node<T> getNextNode() {
		return nextNode;
	}
	
	public void setNextNode(Node<T> nextNode) {
		this.nextNode = nextNode;
	}
	
	@Override
	public String toString() {
		return this.data.toString();
	}
	
}
