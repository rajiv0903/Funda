package org.learning.pattern._04_factory.factorymethod.pizzafactrory;

import org.learning.pattern._04_factory.factorymethod.pizzas.*;

import org.learning.pattern._04_factory.factorymethod.pizzastore.PizzaStore;



public class NYPizzaStore extends PizzaStore {

	public Pizza createPizza(String item) {
		if (item.equals("cheese")) {
			return new NYStyleCheesePizza();
		} else if (item.equals("veggie")) {
			return new NYStyleVeggiePizza();
		} else if (item.equals("clam")) {
			return new NYStyleClamPizza();
		} else if (item.equals("pepperoni")) {
			return new NYStylePepperoniPizza();
		} else return null;
	}
}
