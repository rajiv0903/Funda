package org.learning.pattern._03_decorator.condiments;

import org.learning.pattern._03_decorator.beverage.Beverage;
import org.learning.pattern._03_decorator.condiment.CondimentDecorator;

public class Soy extends CondimentDecorator {
	Beverage beverage;

	public Soy(Beverage beverage) {
		this.beverage = beverage;
	}

	public String getDescription() {
		return beverage.getDescription() + ", Soy";
	}
	
	public int getSize() {
		return beverage.getSize();
	}

	public double cost() {
		double cost = beverage.cost();
		if(getSize() == Beverage.TALL){
			cost +=.15d;
		}
		else if(getSize() == Beverage.GRANDE){
			cost +=1.0d;
		}
		else if(getSize() == Beverage.VENTI){
			cost +=.25d;
		}
		else{
			cost +=.10;
		}
		return cost;

	}
}
