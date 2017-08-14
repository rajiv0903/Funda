package org.learning.pattern._04_factory.abstractfactory.pizzastore;

import org.learning.pattern._04_factory.abstractfactory.ingredientfactory.NYPizzaIngredientFactory;
import org.learning.pattern._04_factory.abstractfactory.ingredientfactory.PizzaIngredientFactory;
import org.learning.pattern._04_factory.abstractfactory.pizzas.CheesePizza;
import org.learning.pattern._04_factory.abstractfactory.pizzas.ClamPizza;
import org.learning.pattern._04_factory.abstractfactory.pizzas.PepperoniPizza;
import org.learning.pattern._04_factory.abstractfactory.pizzas.Pizza;
import org.learning.pattern._04_factory.abstractfactory.pizzas.VeggiePizza;

public class NYPizzaStore extends PizzaStore {
 
	protected Pizza createPizza(String item) {
		Pizza pizza = null;
		PizzaIngredientFactory ingredientFactory = 
			new NYPizzaIngredientFactory();
 
		if (item.equals("cheese")) {
  
			pizza = new CheesePizza(ingredientFactory);
			pizza.setName("New York Style Cheese Pizza");
  
		} else if (item.equals("veggie")) {
 
			pizza = new VeggiePizza(ingredientFactory);
			pizza.setName("New York Style Veggie Pizza");
 
		} else if (item.equals("clam")) {
 
			pizza = new ClamPizza(ingredientFactory);
			pizza.setName("New York Style Clam Pizza");
 
		} else if (item.equals("pepperoni")) {

			pizza = new PepperoniPizza(ingredientFactory);
			pizza.setName("New York Style Pepperoni Pizza");
 
		} 
		return pizza;
	}
}
