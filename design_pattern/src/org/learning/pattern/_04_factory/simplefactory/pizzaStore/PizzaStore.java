package org.learning.pattern._04_factory.simplefactory.pizzaStore;

import org.learning.pattern._04_factory.simplefactory.pizzafactory.SimplePizzaFactory;
import org.learning.pattern._04_factory.simplefactory.pizzas.Pizza;

public class PizzaStore {
	SimplePizzaFactory factory;
 
	public PizzaStore(SimplePizzaFactory factory) { 
		this.factory = factory;
	}
 
	public Pizza orderPizza(String type) {
		Pizza pizza;
 
		pizza = factory.createPizza(type);
 
		pizza.prepare();
		pizza.bake();
		pizza.cut();
		pizza.box();

		return pizza;
	}

}
