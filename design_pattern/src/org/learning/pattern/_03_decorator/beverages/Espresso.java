package org.learning.pattern._03_decorator.beverages;

import org.learning.pattern._03_decorator.beverage.Beverage;

public class Espresso extends Beverage {
  
	public Espresso() {
		description = "Espresso";
	}
  
	public double cost() {
		return 1.99;
	}
}

