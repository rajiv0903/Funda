package org.learning.pattern._03_decorator.condiments;

import org.learning.pattern._03_decorator.beverage.Beverage;
import org.learning.pattern._03_decorator.condiment.CondimentDecorator;

public class Milk extends CondimentDecorator {
	Beverage beverage;

	public Milk(Beverage beverage) {
		this.beverage = beverage;
	}

	public String getDescription() {
		return beverage.getDescription() + ", Milk";
	}

	public double cost() {
		return .10 + beverage.cost();
	}
}
