package org.learning.pattern._03_decorator.beverage;

public abstract class Beverage {
	
	private int size;
	public static final int TALL =1;
	public static final int GRANDE =2;
	public static final int VENTI =3;
	
	protected String description = "Unknown Beverage";
	
  
	public String getDescription() {
		return description;
	}
	
	
 
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}



	public abstract double cost();
}
