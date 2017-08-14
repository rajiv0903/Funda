package course02._04._04_minimum_element_stack.practice;


public class StackUsingLinkedListPractice<T> {

	private static int MAX_SIZE = 4;

	private ElementPractice<T> top;
	private int size = 0;
	
	public StackUsingLinkedListPractice(int MAX_SIZE)
	{
		StackUsingLinkedListPractice.MAX_SIZE = MAX_SIZE;
	}

	public void push(T data) throws StackOverflowException {

		if (size == MAX_SIZE) {
			throw new StackOverflowException();
		}
		
		this.top = new ElementPractice<T>(data, top);
		size++;
	}

	public T pop() throws StackUnderflowException {
		
		if (size == 0) {
			throw new StackUnderflowException();
		}
		
		T data = top.getData();
		top = top.getNext();

		size--;
		
		return data;

	}

	public T peek() throws StackUnderflowException {

		if (size == 0) {
			throw new StackUnderflowException();
		}
		
		return top.getData();
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public boolean isFull() {
		return size == MAX_SIZE;
	}

	public int getSize() {
		return size;
	}

	public static class StackOverflowException extends Exception {
	}

	public static class StackUnderflowException extends Exception {
	}

}
