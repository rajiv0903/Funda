package course02._04._04_minimum_element_stack.practice;

public class StackMinimumInConstantTimePractice {

	private StackUsingLinkedListPractice<Integer> stack = new StackUsingLinkedListPractice<Integer>(40);
	private StackUsingLinkedListPractice<Integer> minimumStack = new StackUsingLinkedListPractice<Integer>(40);

	public void push(int data)
			throws StackUsingLinkedListPractice.StackOverflowException,
			StackUsingLinkedListPractice.StackUnderflowException 
	{
		int min = data;
		
		if(!minimumStack.isEmpty())
		{
			if(min > minimumStack.peek())
			{
				min = minimumStack.peek();
			}
		}
		stack.push(data);
		minimumStack.push(min);
	}

	public int pop() throws StackUsingLinkedListPractice.StackUnderflowException 
	{
		minimumStack.pop();
		return stack.pop();
	}

	public int getMinimum()
			throws StackUsingLinkedListPractice.StackUnderflowException 
	{
		return minimumStack.peek();
	}

}
