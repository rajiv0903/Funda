package course01._01.gcdfinder.practice;

public class GCDFinderPractice {
	
	public static void main(String[] args) {
		
		System.out.println(gcd(25,5));
	}
	
	public static int gcd(int a, int b)
	{
		if(b==0) return a;
		return gcd (b, a%b);
	}

}
