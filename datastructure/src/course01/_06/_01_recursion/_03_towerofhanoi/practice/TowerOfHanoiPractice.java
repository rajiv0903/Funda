package course01._06._01_recursion._03_towerofhanoi.practice;


public class TowerOfHanoiPractice {

	private static int  numOfMoves = 0;
	
	public static void main(String[] args) {
		
		TowerOfHanoiPractice toh = new TowerOfHanoiPractice();
		toh.move(3, 'A', 'C', 'B');
		System.out.println("Number of moves: " + numOfMoves);
	}
	
	public void move(int numberOfDiscs, char from, char to, char inter)
	{
		if (numberOfDiscs == 1) {
			System.out.println("Moving disc 1 from " + from + " to " + to);
			numOfMoves++;
		}
		else
		{
			move(numberOfDiscs-1, from, inter, to);
			System.out.println("Moving disc " + numberOfDiscs + " from " + from + " to " + to);
			numOfMoves++;
			move(numberOfDiscs - 1, inter, to, from);
		}
	}
}
