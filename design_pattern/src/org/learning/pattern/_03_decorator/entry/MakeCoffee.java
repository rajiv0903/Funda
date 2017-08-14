package org.learning.pattern._03_decorator.entry;

import org.learning.pattern._03_decorator.beverage.Beverage;
import org.learning.pattern._03_decorator.beverages.DarkRoast;
import org.learning.pattern._03_decorator.beverages.Espresso;
import org.learning.pattern._03_decorator.beverages.HouseBlend;
import org.learning.pattern._03_decorator.condiments.Mocha;
import org.learning.pattern._03_decorator.condiments.Soy;
import org.learning.pattern._03_decorator.condiments.Whip;

public class MakeCoffee {
 
	public static void main(String args[]) {
		Beverage beverage = new Espresso();
		System.out.println(beverage.getDescription() 
				+ " $" + beverage.cost());
 
		Beverage beverage2 = new DarkRoast();
		beverage2 = new Mocha(beverage2);
		beverage2 = new Mocha(beverage2);
		beverage2 = new Whip(beverage2);
		System.out.println(beverage2.getDescription() 
				+ " $" + beverage2.cost());
 
		Beverage beverage3 = new HouseBlend();
		beverage3.setSize(Beverage.GRANDE);
		beverage3 = new Soy(beverage3);
		beverage3 = new Mocha(beverage3);
		beverage3 = new Whip(beverage3);
		System.out.println(beverage3.getDescription() 
				+ " $" + beverage3.cost());
	}
}
