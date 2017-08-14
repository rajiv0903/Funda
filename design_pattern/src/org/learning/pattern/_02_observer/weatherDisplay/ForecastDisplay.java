package org.learning.pattern._02_observer.weatherDisplay;

import org.learning.pattern._02_observer.weatherObserver.WeatherObserver;
import org.learning.pattern._02_observer.weatherSubject.WeatherSubjectDataInterface;

public class ForecastDisplay implements WeatherObserver, WeatherDisplayElement {
	private float currentPressure = 29.92f;  
	private float lastPressure;
	
	private WeatherSubjectDataInterface weatherSubjectData;

	public ForecastDisplay(WeatherSubjectDataInterface weatherSubjectData) {
		this.weatherSubjectData = weatherSubjectData;
		weatherSubjectData.registerObserver(this);
	}

	public void update(float temp, float humidity, float pressure) {
        lastPressure = currentPressure;
		currentPressure = pressure;

		display();
	}

	public void display() {
		System.out.print("Forecast: ");
		if (currentPressure > lastPressure) {
			System.out.println("Improving weather on the way!");
		} else if (currentPressure == lastPressure) {
			System.out.println("More of the same");
		} else if (currentPressure < lastPressure) {
			System.out.println("Watch out for cooler, rainy weather");
		}
	}
}
