package course01._05._02_queue.practice;

public class QueuePractice<T> {
	
	private int head = -1;
	private int tail = -1;
	private int numOfItems = 0; 
	
	private T[] elements =  (T[]) new Object[100];

	public QueuePractice (int size)
	{
		elements =  (T[]) new Object[size];
	}
	
	public void enqueue(T data)
	{
		if (isFull())
			throw new RuntimeException("Queue is full");
		if(tail == elements.length-1) 
			tail = -1;
		
		elements[++tail] = data;
		
		if(head == -1 )
		{
			head ++;
		}
		
		numOfItems ++;
	}
	
	public T dequeue ()
	{
		if (isEmpty())
			throw new RuntimeException("Queue is empty");
		if (head == elements.length-1)
			head = -1;
		
		numOfItems --;
		return elements[head++];
	}
	
	public T peek()
	{
		return elements[head];
	}
	
	public boolean isFull()
	{
		/*return Math.abs(tail-head) > 0;*/
		return numOfItems == elements.length;
		
	}
	public boolean isEmpty()
	{
		if(head == -1 && tail == -1)
		{
			return true;
		}
		return false;
	}
}
