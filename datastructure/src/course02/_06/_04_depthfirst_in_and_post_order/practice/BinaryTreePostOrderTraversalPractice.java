package course02._06._04_depthfirst_in_and_post_order.practice;

public class BinaryTreePostOrderTraversalPractice {
	
	public static void main(String[] args) {
		
		NodePractice<Character> a = new NodePractice<Character>('A');
		NodePractice<Character> b = new NodePractice<Character>('B');
		NodePractice<Character> c = new NodePractice<Character>('C');
		NodePractice<Character> d = new NodePractice<Character>('D');
		NodePractice<Character> e = new NodePractice<Character>('E');
		NodePractice<Character> f = new NodePractice<Character>('F');
		NodePractice<Character> g = new NodePractice<Character>('G');
		NodePractice<Character> h = new NodePractice<Character>('H');
		NodePractice<Character> x = new NodePractice<Character>('X');

		a.setLeftChild(b);
		a.setRightChild(c);
		c.setLeftChild(d);
		c.setRightChild(e);
		d.setLeftChild(f);
		d.setRightChild(h);
		e.setRightChild(g);
		b.setLeftChild(x);
		
		postOrder(a);
		
	}
	
	public static void postOrder(NodePractice<Character> root) {
		if (root == null) {
			return;
		}

		postOrder(root.getLeftChild());
		postOrder(root.getRightChild());
		print(root);
	}

	public static void print(NodePractice<Character> node) {
		System.out.print(node.getData() + "->");
	}
}
