package org.learning.pattern.templatemethod.barista.abstractCls;

public abstract class CaffeineBeverage {
  
	public final void prepareRecipe() {
		boilWater();
		brew();
		pourInCup();
		addCondiments();
	}
 
	public abstract void brew();
  
	public abstract void addCondiments();
 
	void boilWater() {
		System.out.println("Boiling water");
	}
  
	void pourInCup() {
		System.out.println("Pouring into cup");
	}
}
