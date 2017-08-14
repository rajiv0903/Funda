package course01._05._02_queue.practice;

public class QueuePracticeDemo {

	public static void main(String[] args) {
		
		QueuePractice<Integer> queue = new QueuePractice<Integer>(3);
		
		
		queue.enqueue(10);
		queue.dequeue();
		queue.enqueue(3);
		queue.enqueue(2);
		
		
		System.out.println(queue.peek());
	}
}
