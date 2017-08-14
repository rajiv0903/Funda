package course02._04._02_stack_implementation.practice;

import course02._04._02_stack_implementation.practice.StackUsingLinkedListPractice.StackOverflowException;
import course02._04._02_stack_implementation.practice.StackUsingLinkedListPractice.StackUnderflowException;

public class StackUsingLinkedListDemo {

	public static void main(String[] args) throws StackOverflowException, StackUnderflowException {

		StackUsingLinkedListPractice<Integer> stack = new StackUsingLinkedListPractice<>();

		stack.push(1);
		stack.push(2);
		stack.push(3);

		System.out.println("Stack size: " + stack.getSize());
		System.out.println("Stack full?: " + stack.isFull());
		System.out.println("Stack empty?: " + stack.isEmpty());

		stack.push(4);
		System.out.println("Stack size: " + stack.getSize());
		System.out.println("Stack full?: " + stack.isFull());
		System.out.println("Stack empty?: " + stack.isEmpty());

		System.out.println("Stack peek: " + stack.peek());

		int data = stack.pop();
		System.out.println("Popped element: " + data);

		System.out.println("Peek again: " + stack.peek());

		try 
		{
			stack.push(4);
			stack.push(5);
		} 
		catch (StackOverflowException soe) 
		{
			System.out.println("Stack is full! No room available.");
		}

		try {
			System.out.println(stack.pop());
			System.out.println(stack.pop());
			System.out.println(stack.pop());
			System.out.println(stack.pop());
			System.out.println(stack.pop());
			System.out.println(stack.pop());
		} 
		catch (StackUnderflowException sue) 
		{
			System.out.println("Stack is empty! No elements available.");
		}
	}

}
