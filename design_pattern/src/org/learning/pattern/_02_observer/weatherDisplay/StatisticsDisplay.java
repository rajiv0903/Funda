package org.learning.pattern._02_observer.weatherDisplay;

import org.learning.pattern._02_observer.weatherObserver.WeatherObserver;
import org.learning.pattern._02_observer.weatherSubject.WeatherSubjectDataInterface;

public class StatisticsDisplay implements WeatherObserver, WeatherDisplayElement {
	private float maxTemp = 0.0f;
	private float minTemp = 200;
	private float tempSum= 0.0f;
	
	private int numReadings;
	
	private WeatherSubjectDataInterface weatherSubjectData;

	public StatisticsDisplay(WeatherSubjectDataInterface weatherSubjectData) {
		this.weatherSubjectData = weatherSubjectData;
		weatherSubjectData.registerObserver(this);
	}

	public void update(float temp, float humidity, float pressure) {
		tempSum += temp;
		numReadings++;

		if (temp > maxTemp) {
			maxTemp = temp;
		}
 
		if (temp < minTemp) {
			minTemp = temp;
		}

		display();
	}

	public void display() {
		System.out.println("Avg/Max/Min temperature = " + (tempSum / numReadings)
			+ "/" + maxTemp + "/" + minTemp);
	}
}
