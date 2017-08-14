package org.learning.pattern._02_observer.weatherObserver;

public interface WeatherObserver {
	
	public void update(float temp, float humidity, float pressure);
}
