package course02._04._06_circularqueue.practice;

import java.lang.reflect.Array;


public class QueuePractice<T> {

	private static final int SPECIAL_EMPTY_VALUE = -1;
	private static int MAX_SIZE = 40;
	private T[] elements;

	// The head index is initialized to a special value which
	// indicate that the queue is empty.
	private int headIndex = SPECIAL_EMPTY_VALUE;
	private int tailIndex = SPECIAL_EMPTY_VALUE;

	public QueuePractice(Class<T> clazz, int size) {
		QueuePractice.MAX_SIZE = size;
		elements = (T[]) Array.newInstance(clazz, MAX_SIZE);
	}

	public QueuePractice(Class<T> clazz) {
		this(clazz, MAX_SIZE);
	}

	public void enqueue(T data) throws QueueOverflowException {
		if (isFull()) {
			throw new QueueOverflowException();
		}

		tailIndex = (tailIndex + 1) % elements.length;
		elements[tailIndex] = data;

		if (headIndex == SPECIAL_EMPTY_VALUE) {
			headIndex = tailIndex;
		}
	}

	public boolean offer(T data) {
		if (isFull()) {
			return false;
		}
		try {
			enqueue(data);
		} catch (QueueOverflowException qoe) {
			// Ignore, this should never happen, we've checked
			// for a full queue already.
		}

		return true;
	}

	public T dequeue() throws QueueUnderflowException {
		if (isEmpty()) {
			throw new QueueUnderflowException();
		}
		
		T data = elements[headIndex];
		elements[headIndex] = null;
		
		if(headIndex == tailIndex)
			headIndex = SPECIAL_EMPTY_VALUE;
		else
		{
			headIndex = (headIndex+1) % elements.length;
		}
		
		return data;
	}

	public T peek() throws QueueUnderflowException {
		if (isEmpty()) {
			throw new QueueUnderflowException();
		}

		return elements[headIndex];
	}

	public boolean isEmpty() {
		return headIndex == SPECIAL_EMPTY_VALUE;
	}

	public boolean isFull() {
		int nextIndex = (tailIndex + 1) % elements.length;
		return nextIndex == headIndex;
	}

	public static class QueueOverflowException extends Exception {
	}

	public static class QueueUnderflowException extends Exception {
	}
}
