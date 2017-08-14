package org.learning.pattern.adapter.entry;

import org.learning.pattern.adapter.adaptee.WildTurkey;
import org.learning.pattern.adapter.adapter.TurkeyAdapter;
import org.learning.pattern.adapter.target.Duck;
import org.learning.pattern.adapter.target.MallardDuck;

public class DuckTestDrive {
	public static void main(String[] args) {
		MallardDuck duck = new MallardDuck();
 
		WildTurkey turkey = new WildTurkey();
		Duck turkeyAdapter = new TurkeyAdapter(turkey);
   
		System.out.println("The Turkey says...");
		turkey.gobble();
		turkey.fly();
 
		System.out.println("\nThe Duck says...");
		testDuck(duck);
  
		System.out.println("\nThe TurkeyAdapter says...");
		testDuck(turkeyAdapter);
	}
 
	static void testDuck(Duck duck) {
		duck.quack();
		duck.fly();
	}
}
