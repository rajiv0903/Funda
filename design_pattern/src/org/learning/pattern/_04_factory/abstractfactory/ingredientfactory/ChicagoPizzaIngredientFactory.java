package org.learning.pattern._04_factory.abstractfactory.ingredientfactory;

import org.learning.pattern._04_factory.abstractfactory.ingredients.cheese.Cheese;
import org.learning.pattern._04_factory.abstractfactory.ingredients.cheese.MozzarellaCheese;
import org.learning.pattern._04_factory.abstractfactory.ingredients.clams.Clams;
import org.learning.pattern._04_factory.abstractfactory.ingredients.clams.FrozenClams;
import org.learning.pattern._04_factory.abstractfactory.ingredients.dough.Dough;
import org.learning.pattern._04_factory.abstractfactory.ingredients.dough.ThickCrustDough;
import org.learning.pattern._04_factory.abstractfactory.ingredients.pepperoni.Pepperoni;
import org.learning.pattern._04_factory.abstractfactory.ingredients.pepperoni.SlicedPepperoni;
import org.learning.pattern._04_factory.abstractfactory.ingredients.sauce.PlumTomatoSauce;
import org.learning.pattern._04_factory.abstractfactory.ingredients.sauce.Sauce;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.BlackOlives;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.Eggplant;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.Spinach;
import org.learning.pattern._04_factory.abstractfactory.ingredients.veggies.Veggies;

public class ChicagoPizzaIngredientFactory 
	implements PizzaIngredientFactory 
{

	public Dough createDough() {
		return new ThickCrustDough();
	}

	public Sauce createSauce() {
		return new PlumTomatoSauce();
	}

	public Cheese createCheese() {
		return new MozzarellaCheese();
	}

	public Veggies[] createVeggies() {
		Veggies veggies[] = { new BlackOlives(), 
		                      new Spinach(), 
		                      new Eggplant() };
		return veggies;
	}

	public Pepperoni createPepperoni() {
		return new SlicedPepperoni();
	}

	public Clams createClam() {
		return new FrozenClams();
	}
}
