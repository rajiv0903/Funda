package course02._03._01_linkedlist.practice;


public class NodePractice<T extends Comparable<T>> {

	private T data;
	private NodePractice<T> next;

	public NodePractice(T data) {
		this.data = data;
		setNext(null);
	}

	public T getData() {
		return data;
	}

	public NodePractice<T> getNext() {
		return next;
	}

	public void setNext(NodePractice<T> next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return String.valueOf(data);
	}

}
