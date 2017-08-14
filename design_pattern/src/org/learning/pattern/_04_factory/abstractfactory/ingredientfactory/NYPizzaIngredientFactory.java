package org.learning.pattern._04_factory.abstractfactory.ingredientfactory;

import org.learning.pattern._04_factory.abstractfactory.ingredients.cheese.Cheese;
import org.learning.pattern._04_factory.abstractfactory.ingredients.cheese.ReggianoCheese;
import org.learning.pattern._04_factory.abstractfactory.ingredients.clams.Clams;
import org.learning.pattern._04_factory.abstractfactory.ingredients.clams.FreshClams;
import org.learning.pattern._04_factory.abstractfactory.ingredients.dough.Dough;
import org.learning.pattern._04_factory.abstractfactory.ingredients.dough.ThinCrustDough;
import org.learning.pattern._04_factory.abstractfactory.ingredients.pepperoni.Pepperoni;
import org.learning.pattern._04_factory.abstractfactory.ingredients.pepperoni.SlicedPepperoni;
import org.learning.pattern._04_factory.abstractfactory.ingredients.sauce.MarinaraSauce;
import org.learning.pattern._04_factory.abstractfactory.ingredients.sauce.Sauce;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.Garlic;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.Mushroom;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.Onion;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.RedPepper;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.Veggies;

public class NYPizzaIngredientFactory implements PizzaIngredientFactory {
 
	public Dough createDough() {
		return new ThinCrustDough();
	}
 
	public Sauce createSauce() {
		return new MarinaraSauce();
	}
 
	public Cheese createCheese() {
		return new ReggianoCheese();
	}
 
	public Veggies[] createVeggies() {
		Veggies veggies[] = { new Garlic(), new Onion(), new Mushroom(), new RedPepper() };
		return veggies;
	}
 
	public Pepperoni createPepperoni() {
		return new SlicedPepperoni();
	}

	public Clams createClam() {
		return new FreshClams();
	}
}
