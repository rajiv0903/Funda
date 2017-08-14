package course02._04._04_minimum_element_stack.practice;

public class StackMinimumInConstantTimeDemo {

	public static void main(String[] args)
			throws StackUsingLinkedListPractice.StackUnderflowException,
			StackUsingLinkedListPractice.StackOverflowException {

		StackMinimumInConstantTimePractice minimumStack = new StackMinimumInConstantTimePractice();

		minimumStack.push(2);
		minimumStack.push(4);
		minimumStack.push(10);

		System.out.println("Minimum so far is: " + minimumStack.getMinimum());
		minimumStack.push(1);
		System.out.println("Minimum so far is: " + minimumStack.getMinimum());
		minimumStack.push(0);
		System.out.println("Minimum so far is: " + minimumStack.getMinimum());

		minimumStack.pop();
		System.out.println("Minimum so far is: " + minimumStack.getMinimum());
		minimumStack.pop();
		System.out.println("Minimum so far is: " + minimumStack.getMinimum());
		minimumStack.pop();
		System.out.println("Minimum so far is: " + minimumStack.getMinimum());

	}

}
