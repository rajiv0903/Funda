package course01._04._04_sortedlinkedlist.practice;


public class SortedLinkedListPractice<T extends Comparable<T>> {

	private NodePractice<T> head;
	
	public void insert(T data)
	{
		NodePractice<T> newNode = new NodePractice<T>(data);
		//Empty
		if(this.head == null)
		{
			newNode.setNextNode(this.head);
			this.head = newNode;
			return;
		}
		//Data is smaller than head
		if(this.head.getData().compareTo(data) >0)
		{
			newNode.setNextNode(this.head);
			this.head = newNode;
			return;
		}
		//Find the node for which next node data is greater than new node
		NodePractice<T> curr = this.head;
		while (curr!= null && curr.getNextNode()!= null
				&& curr.getNextNode().getData().compareTo(data) < 0)
		{
			curr = curr.getNextNode();
		}
		newNode.setNextNode(curr.getNextNode());
		curr.setNextNode(newNode);
	}
	
	@Override
	public String toString() 
	{
		String result = "[";
		NodePractice<T> current = this.head;
		while (current != null) {
			result += current.toString() + ((current.getNextNode()!=null)? ", ":"");
			current = current.getNextNode();
		}
		result += "]";
		return result;
	}
}
