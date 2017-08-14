package org.learning.pattern._01_strategy.fly;

public class FlyNoWay implements FlyBehavior{
	
	public void fly()
	{
		System.out.println("Can't fly");
	}

}
