package org.learning.pattern._01_strategy.quark;

public class MuteQuack implements QuackBehavior{
	
	public void quark()
	{
		System.out.println("Can't Quack");
	}

}
