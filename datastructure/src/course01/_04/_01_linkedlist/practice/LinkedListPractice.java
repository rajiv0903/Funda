package course01._04._01_linkedlist.practice;


public class LinkedListPractice<T> {
	
	NodePractice<T> head;
	
	public void addAtStart(T data) {
		NodePractice<T> newNode = new NodePractice<T>(data);
		newNode.setNextNode(this.head);
		this.head = newNode;
	}

	public NodePractice<T> deleteAtStart() 
	{
		NodePractice<T> toDel = this.head;
		if(!isEmpty())
				this.head = this.head.getNextNode();
		return toDel;
	}
	
	public NodePractice<T> find(T data) {
		if(isEmpty())
			return null;
		
		NodePractice<T> current = this.head;
		
		while(current != null && !current.getData().equals(data))
		{
			current = current.getNextNode();
		}
		return current;
	}
	
	public int length() {
		if(isEmpty()) return 0;
		int len = 0;
		NodePractice<T> current = this.head;
		while(current != null){
			len++;
			current = current.getNextNode();
		}
		return len;
	}
	
	public boolean isEmpty() {
		return (this.head==null? true: false);
	}
}
