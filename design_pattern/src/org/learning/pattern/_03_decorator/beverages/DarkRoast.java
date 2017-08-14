package org.learning.pattern._03_decorator.beverages;

import org.learning.pattern._03_decorator.beverage.Beverage;

public class DarkRoast extends Beverage {
	public DarkRoast() {
		description = "Dark Roast Coffee";
	}
 
	public double cost() {
		return .99;
	}
}

