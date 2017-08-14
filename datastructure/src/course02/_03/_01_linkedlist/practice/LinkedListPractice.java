package course02._03._01_linkedlist.practice;

import java.util.ArrayList;
import java.util.List;



public class LinkedListPractice<T extends Comparable<T>> implements Cloneable {
	
	private NodePractice<T> head = null;
	
	public LinkedListPractice(){
		
	}
	
	/**
     * Append a new node to the end of the linked list.
     */
    public void insert(T data) 
    {
    	NodePractice<T> newNode = new NodePractice<T>(data);
		//Empty
		if(this.head == null)
		{
			newNode.setNext(this.head);
			this.head = newNode;
			return;
		}
		//Data is smaller than head
		if(this.head.getData().compareTo(data) >0)
		{
			newNode.setNext(this.head);
			this.head = newNode;
			return;
		}
		//Find the node for which next node data is greater than new node
		NodePractice<T> curr = this.head;
		while (curr!= null && curr.getNext()!= null
				&& curr.getNext().getData().compareTo(data) < 0)
		{
			curr = curr.getNext();
		}
		newNode.setNext(curr.getNext());
		curr.setNext(newNode);
    }
    

	 /**
     * Append a new node to the end of the linked list.
     */
    public void addNode(T data) {
    	if(this.head == null){
    		this.head = new NodePractice<T>(data);
    	}
    	else
    	{
    		NodePractice<T> curr = this.head;
    		while (curr.getNext() != null){
    			curr = curr.getNext();
    		}
    		curr.setNext(new NodePractice<T>(data));
    	}
    }
    
    /**
     * Print all the nodes in the linked list.
     */
    public void printNodes() {
    	if(this.head == null)
    		System.out.println("There is no node at linked list");
    	else
    	{
    		NodePractice<T> curr = this.head;
    		int counter = 0;
    		while (curr != null)
    		{
    			System.out.println("Node at ["+counter+"] "+curr.toString());
    			curr = curr.getNext();
    			counter++;
    		}
    	}
    }
    
    /**
     * Count the number of nodes in the linked list.
     */
    public int countNodes() {
    	if(this.head == null)
    		return  0;
    	else
    	{
    		int count = 0;
    		NodePractice<T> curr= this.head;
    		while (curr != null){
    			curr = curr.getNext();
    			count++;
    		}
    		return count;
    	}
    }
    
    /**
     * Return the first element in the linked list.
     */
    public T popElement() {
    	if(this.head == null)
    		return null;
    	else
    	{
    		T curr= this.head.getData();
    		this.head = this.head.getNext();
    		return curr;
    	}
    }
    
    /**
     * Delete all the elements in the linked list.
     */
    public void deleteLinkedList() {
    	this.head = null;
    }
    /**
     * Insert at the nth position in the list. Return if the number of nodes is
     * less than n.
     */
    public void insertNth(int n, T data) {
    	if(n > countNodes() )
    		return;
    	
    	//insert at head
    	if(n==0)
    	{
    		NodePractice<T> tmp = this.head;
    		head = new NodePractice<T>(data);
    		head.setNext(tmp);
    	}
    	else
    	{
    		NodePractice<T> curr = this.head;
    		int i = 0;
    		while ( i < n-1)
    		{
    			curr = curr.getNext();
    			i++;
    		}
    		NodePractice<T> next = curr.getNext();
            curr.setNext(new NodePractice<T>(data));
            curr.getNext().setNext(next);
    	}
    }
    
    /**
     * Insert the data into the correct position in a sorted list. Assume
     * that the list is sorted in ascending order.
     */
    public void insertSorted(T data) {
    	if(countNodes() == 0 || this.head.getData().compareTo(data) >0)
    	{
    		NodePractice<T> next = this.head;
    		this.head = new NodePractice<T>(data);
    		this.head.setNext(next);
    	}
    	else
    	{
    		NodePractice<T> curr = this.head;
    		
    		if(curr.getNext() != null && curr.getNext().getData().compareTo(data) <0)
    		{
    			curr = curr.getNext();
    		}
    		
    		NodePractice<T> next =  curr.getNext();
    		curr.setNext(new NodePractice<T>(data));
    		curr.getNext().setNext(next);
    	}
    }
    
    /**
     * Append the nodes of the other list to the end of the curren list.
     */
    public void appendList(LinkedListPractice<T> ll) {
    	if(ll.head == null)
    		return;
    	
    	NodePractice<T> curr =  ll.head;
    	while(curr != null)
    	{
    		addNode((T)curr.getData());
    		curr = curr.getNext();
    	}
    	
    	//this.head.setNext(ll.head);
    }
    
    /**
     * Split a linked list into 2 equal parts. If there are an odd number of
     * elements, the extra element should be in the first list.
     */
    public List<NodePractice<T>> frontBackSplit() {
    	NodePractice<T> front = null;
    	NodePractice<T> back = null;

          // A 0 element list means both the front list and back
          // list will both be null.
          if (head == null) {
              front = null;
              back = null;
          } else if (head.getNext() == null) {
              // For a one element list, include the first element in the
              // front list.
              front = head;
              back = null;
          } else {
              // Move one pointer twice as fast as another.
        	  NodePractice<T> slow = head;
        	  NodePractice<T> fast = head;

              while (fast != null) {
                  fast = fast.getNext();
                  if (fast == null) {
                      break;
                  }
                  fast = fast.getNext();
                  if (fast != null) {
                      slow = slow.getNext();
                  }
              }
              front = head;
              back = slow.getNext();
              slow.setNext(null);
          }

          List<NodePractice<T>> list = new ArrayList<>();
          list.add(front);
          list.add(back);

          return list;
    }
    /**
     * Remove duplicates in a sorted list.
     */
    public void removeDuplicates() {
    	
    	int count = countNodes();
    	
    	if(count == 0 || count ==1)
    		return;
    	
    	NodePractice<T> curr = this.head;
    	
    	while (curr.getNext()!= null)
    	{
    		if(curr.getData().compareTo(curr.getNext().getData()) == 0)
    		{
    			curr.setNext(curr.getNext().getNext());
    			curr = curr.getNext().getNext();
    		}
    		else
    		{
    			curr = curr.getNext();
    		}
    	}
    	
    }
    
    /**
     * Move the head element or the first element from this list to
     * the destination linked list as the destination's new head node.
     */
    public void changeHead(LinkedListPractice<T> destinationList) {
    	T currHeadData = popElement();
    	
    	if(destinationList.head == null)
    	{
    		destinationList.head = new NodePractice<T>(currHeadData);
    	}
    	else
    	{
    		NodePractice<T> next = destinationList.head;
    		destinationList.head= new NodePractice<T>(currHeadData);
    		destinationList.head.setNext(next);
    	}
    }
    
    /**
     * Create a new sorted list which is the merged from two original sorted lists.
     * Assume the lists are sorted in ascending order.
     */
    public LinkedListPractice<T> sortedMergeList(LinkedListPractice<T> otherList) 
    {
    	 if (otherList == null) 
    	 {
             return this;
         } 
    	 else if (head == null) 
         {
             return otherList;
         } 
    	 else 
         {
    		 LinkedListPractice<T> sortedList = new LinkedListPractice<T>();
    		 
    		 NodePractice<T> curr1 = this.head;
    		 NodePractice<T> curr2 = otherList.head;
    		 
    		 while (curr1 != null || curr2 != null)
    		 {
    			 if(curr2 == null || (curr1 != null && curr1.getData().compareTo(curr2.getData()) <0) )
    			 {
    				 sortedList.addNode(curr1.getData());
    				 curr1 = curr1.getNext();
    			 }
    			 else
    			 {
    				 sortedList.addNode(curr2.getData());
    				 curr2 = curr2.getNext();
    			 }
    		 }
    		 return sortedList;
         }
    }
    
    /**
     * Reverse all the nodes in the linked list so that the last node
     * becomes the first node.
     */
    public void reverseList() {
    	  if(head == null || head.getNext() == null) {
              return;
          }

    	  NodePractice<T> prev = null;
    	  NodePractice<T> curr = head;
          while (curr != null) {
        	  NodePractice<T> next = curr.getNext();
              curr.setNext(prev);
              prev = curr;
              curr = next;
          }

          head = prev;
    }
}
