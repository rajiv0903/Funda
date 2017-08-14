/**
 * 
 */
package org.learning.pattern._01_strategy.duck;

import org.learning.pattern._01_strategy.fly.FlyWithWings;
import org.learning.pattern._01_strategy.quark.Quack;

/**
 * @author Rajiv Chaudhuri
 *
 */
public class MallardDuck extends Duck{

	/**
	 * 
	 */
	public MallardDuck() {
		quackBehavior = new  Quack();
		flyBehavior = new FlyWithWings();
	}
	
	public void display()
	{
		System.out.println("MallarDuck: Display");
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
