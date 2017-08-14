package org.learning.pattern.templatemethod.barista.concreteCls;

import org.learning.pattern.templatemethod.barista.abstractCls.CaffeineBeverage;

public class Coffee extends CaffeineBeverage {
	public void brew() {
		System.out.println("Dripping Coffee through filter");
	}
	public void addCondiments() {
		System.out.println("Adding Sugar and Milk");
	}
}
