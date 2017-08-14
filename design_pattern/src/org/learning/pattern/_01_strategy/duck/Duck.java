/**
 * 
 */
package org.learning.pattern._01_strategy.duck;

import org.learning.pattern._01_strategy.fly.FlyBehavior;
import org.learning.pattern._01_strategy.quark.QuackBehavior;

/**
 * @author Rajiv Chaudhuri
 *
 */
public abstract class Duck {
	
	FlyBehavior flyBehavior;
	QuackBehavior quackBehavior;
	
	//common to all business object
	public void swim()
	{
		System.out.println("Duck: Swimming");
	}
	//must be implemented by specialized class
	public abstract void display();
	//must be implemented by specialized class
	public abstract void performQuack();
	//must be implemented by specialized class
	public abstract void performFly();
	
	public  void setQuackBehavior(QuackBehavior qb)
	{
		quackBehavior = qb;
	}
	public  void setFlyBehavior(FlyBehavior fb)
	{
		flyBehavior = fb;
	}

}
