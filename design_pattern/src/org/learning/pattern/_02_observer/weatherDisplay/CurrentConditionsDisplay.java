package org.learning.pattern._02_observer.weatherDisplay;

import org.learning.pattern._02_observer.weatherObserver.WeatherObserver;
import org.learning.pattern._02_observer.weatherSubject.WeatherSubjectDataInterface;
	
public class CurrentConditionsDisplay implements WeatherObserver, WeatherDisplayElement {
	private float temperature;
	private float humidity;
	private WeatherSubjectDataInterface weatherSubjectData;
	
	public CurrentConditionsDisplay(WeatherSubjectDataInterface weatherSubjectData) {
		this.weatherSubjectData = weatherSubjectData;
		weatherSubjectData.registerObserver(this);
	}
	
	public void update(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		display();
	}
	
	public void display() {
		System.out.println("Current conditions: " + temperature 
			+ "F degrees and " + humidity + "% humidity");
	}
}
