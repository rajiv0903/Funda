package course01._05._01_stack.practice;

public class StackPracticeDemo {
	
	public static void main(String[] args) {
		
		StackPractice<Integer> stack = new StackPractice<Integer>();
		System.out.println(stack.isEmpty());
		
		stack.push(10);
		stack.push(12);
		
		System.out.println(stack.peek());
		
		stack.pop();
		
		System.out.println(stack.peek());
	}

}
