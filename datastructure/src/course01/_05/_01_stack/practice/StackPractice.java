package course01._05._01_stack.practice;

public class StackPractice<T> {
	
	private int top = -1;
	private T[] elements =  (T[]) new Object[100];
	
	public void push(T data)
	{
		if(top == elements.length-1)
			throw new RuntimeException("Stack is full");
		
		elements[++top] = data;
	}
	public T pop()
	{
		if(isEmpty())
			throw new RuntimeException("Stack is empty");
		return elements[top--];
	}
	
	public T peek()
	{
		if(isEmpty())
			throw new RuntimeException("Stack is empty");
		return elements[top];
	}
	
	public int lenght()
	{
		return top+1;
	}
	
	public boolean isEmpty()
	{
		return top == -1;
	}

}
