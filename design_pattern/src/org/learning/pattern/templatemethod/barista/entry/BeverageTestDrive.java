package org.learning.pattern.templatemethod.barista.entry;

import org.learning.pattern.templatemethod.barista.concreteCls.Coffee;
import org.learning.pattern.templatemethod.barista.concreteCls.CoffeeWithHook;
import org.learning.pattern.templatemethod.barista.concreteCls.Tea;
import org.learning.pattern.templatemethod.barista.concreteCls.TeaWithHook;

public class BeverageTestDrive {
	public static void main(String[] args) {
 
		Tea tea = new Tea();
		Coffee coffee = new Coffee();
 
		System.out.println("\nMaking tea...");
		tea.prepareRecipe();
 
		System.out.println("\nMaking coffee...");
		coffee.prepareRecipe();

 
		TeaWithHook teaHook = new TeaWithHook();
		CoffeeWithHook coffeeHook = new CoffeeWithHook();
 
		System.out.println("\nMaking tea...");
		teaHook.prepareRecipe();
 
		System.out.println("\nMaking coffee...");
		coffeeHook.prepareRecipe();
	}
}
