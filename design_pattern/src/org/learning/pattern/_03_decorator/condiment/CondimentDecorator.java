package org.learning.pattern._03_decorator.condiment;

import org.learning.pattern._03_decorator.beverage.Beverage;

public abstract class CondimentDecorator extends Beverage {
	public abstract String getDescription();
}
