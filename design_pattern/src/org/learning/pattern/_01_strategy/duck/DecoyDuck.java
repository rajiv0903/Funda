package org.learning.pattern._01_strategy.duck;

import org.learning.pattern._01_strategy.fly.FlyWithWings;
import org.learning.pattern._01_strategy.quark.Quack;

public class DecoyDuck extends Duck{

	public DecoyDuck() {
		quackBehavior = new  Quack();
		flyBehavior = new FlyWithWings();
	}
	
	public void display()
	{
		System.out.println("DecoyDuck: Display");
	}
	public void performQuack()
	{
		quackBehavior.quark();
	}
	public void  performFly()
	{
		flyBehavior.fly();
	}
}
