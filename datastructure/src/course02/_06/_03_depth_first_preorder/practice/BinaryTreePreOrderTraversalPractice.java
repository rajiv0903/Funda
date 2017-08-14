package course02._06._03_depth_first_preorder.practice;


public class BinaryTreePreOrderTraversalPractice {

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
		
		preOrder(a);
	}
	
	public static void preOrder(NodePractice<Character>root) {
		if(root == null)
			return;
		
		print(root);
		preOrder(root.getLeftChild());
		preOrder(root.getRightChild());
	}

	public static void print(NodePractice<Character> node) {
		System.out.print(node.getData() + "->");
	}
}
