package org.learning.pattern._03_decorator.beverages;

import org.learning.pattern._03_decorator.beverage.Beverage;

public class HouseBlend extends Beverage {
	public HouseBlend() {
		description = "House Blend Coffee";
	}
 
	public double cost() {
		return .89;
	}
}

