package course01._04._04_sortedlinkedlist.practice;

public class NodePractice<T> {
	
	private T data;
	private NodePractice<T> nextNode;
	
	
	public NodePractice(T data)
	{
		this.data = data;
	}
	
	public T getData() {
		return data;
	}
	/*public void setData(T data) {
		this.data = data;
	}*/
	public NodePractice<T> getNextNode() {
		return nextNode;
	}
	public void setNextNode(NodePractice<T> nextNode) {
		this.nextNode = nextNode;
	}
	
	@Override
	public String toString() {
		return this.data.toString();
	}
	
	

}
