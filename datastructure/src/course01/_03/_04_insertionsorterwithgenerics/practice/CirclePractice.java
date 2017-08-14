package course01._03._04_insertionsorterwithgenerics.practice;

public class CirclePractice implements Comparable<CirclePractice>{

	private double radius;
	
	public CirclePractice(double radius)
	{
		this.radius = radius;
	}
	
	public int compareTo(CirclePractice o)
	{
		if (this.radius > o.radius)
			return 1;
		if (this.radius == o.radius)
			return 0;
		
		return -1;
	}

	@Override
	public String toString() {
		return "CirclePractice [radius=" + radius + "]";
	}
	
	
}
