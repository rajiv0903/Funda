package org.learning.pattern.state.gumballstatewinner.state;

public interface State {
 
	public void insertQuarter();
	public void ejectQuarter();
	public void turnCrank();
	public void dispense();
}
