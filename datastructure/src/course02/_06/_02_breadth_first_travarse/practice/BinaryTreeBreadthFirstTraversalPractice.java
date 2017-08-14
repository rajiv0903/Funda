package course02._06._02_breadth_first_travarse.practice;

public class BinaryTreeBreadthFirstTraversalPractice {

	public static void main(String[] args) throws QueuePractice.QueueUnderflowException,
	QueuePractice.QueueOverflowException {

		NodePractice<Character> a = new NodePractice<>('A');
		NodePractice<Character> b = new NodePractice<>('B');
		NodePractice<Character> c = new NodePractice<>('C');
		NodePractice<Character> d = new NodePractice<>('D');
		NodePractice<Character> e = new NodePractice<>('E');
		NodePractice<Character> f = new NodePractice<>('F');
		NodePractice<Character> g = new NodePractice<>('G');
		NodePractice<Character> h = new NodePractice<>('H');
		NodePractice<Character> x = new NodePractice<>('X');

		a.setLeftChild(b);
		a.setRightChild(c);
		c.setLeftChild(d);
		c.setRightChild(e);
		d.setLeftChild(f);
		d.setRightChild(h);
		e.setRightChild(g);
		b.setLeftChild(x);

		breadthFirst(a);
	}

	public static void print(NodePractice<Character> node) {
		System.out.print(node.getData() + "->");
	}

	/*public static void breadthFirst(NodePractice<Character> root)
			throws QueuePractice.QueueUnderflowException,
			QueuePractice.QueueOverflowException {

		if (root == null)
			return;

		QueuePractice<NodePractice> queue = new QueuePractice<NodePractice>(
				NodePractice.class);

		queue.enqueue(root);

		while (!queue.isEmpty()) {
			NodePractice<Character> node = queue.dequeue();
			print(node);

			if (node.getLeftChild() != null)
				queue.enqueue(node.getLeftChild());

			if (node.getRightChild() != null)
				queue.enqueue(node.getRightChild());
		}

	}*/
	
	public static void breadthFirst(NodePractice<Character> root)
			throws QueuePractice.QueueUnderflowException,
			QueuePractice.QueueOverflowException {

		/*if (root == null)
			return;

		QueuePractice<NodePractice> queue = new QueuePractice<NodePractice>(
				NodePractice.class);

		queue.enqueue(root);

		while (!queue.isEmpty()) {
			NodePractice<Character> node = queue.dequeue();
			print(node);

			if (node.getLeftChild() != null)
				queue.enqueue(node.getLeftChild());

			if (node.getRightChild() != null)
				queue.enqueue(node.getRightChild());
		}*/
		
		if(root == null)
			return ;
		QueuePractice<NodePractice> queue = new QueuePractice<NodePractice>(
				NodePractice.class);
		
		queue.enqueue(root);
		
		while(!queue.isEmpty())
		{
			NodePractice<Character> node = queue.dequeue();
			print(node);
			
			if (node.getLeftChild() != null)
				queue.enqueue(node.getLeftChild());

			if (node.getRightChild() != null)
				queue.enqueue(node.getRightChild());
		}

	}

}
