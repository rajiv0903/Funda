package org.learning.pattern.templatemethod.barista.concreteCls;

import org.learning.pattern.templatemethod.barista.abstractCls.CaffeineBeverage;

public class Tea extends CaffeineBeverage {
	public void brew() {
		System.out.println("Steeping the tea");
	}
	public void addCondiments() {
		System.out.println("Adding Lemon");
	}
}
