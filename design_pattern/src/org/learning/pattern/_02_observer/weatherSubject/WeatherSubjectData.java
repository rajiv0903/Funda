package org.learning.pattern._02_observer.weatherSubject;

import java.util.ArrayList;

import org.learning.pattern._02_observer.weatherObserver.WeatherObserver;

public class WeatherSubjectData implements WeatherSubjectDataInterface {
	
	private ArrayList<WeatherObserver> weatherObservers;
	private float temperature;
	private float humidity;
	private float pressure;
	
	private boolean isDataChanged;
	
	public WeatherSubjectData() {
		weatherObservers = new ArrayList<WeatherObserver>();
	}
	
	public void registerObserver(WeatherObserver o) {
		weatherObservers.add(o);
	}
	
	public void removeObserver(WeatherObserver o) {
		int i = weatherObservers.indexOf(o);
		if (i >= 0) {
			weatherObservers.remove(i);
		}
	}
	
	public void notifyObservers() {
		if(isDataChanged)
			for (int i = 0; i < weatherObservers.size(); i++) {
				WeatherObserver observer = (WeatherObserver)weatherObservers.get(i);
				observer.update(temperature, humidity, pressure);
			}
		isDataChanged = false;
	}
	
	public void measurementsChanged() {
			notifyObservers();
	}
	
	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		measurementsChanged();
	}
	
	// other WeatherData methods here
	
	public float getTemperature() {
		return temperature;
	}
	
	public float getHumidity() {
		return humidity;
	}
	
	public float getPressure() {
		return pressure;
	}

	public boolean isDataChanged() {
		return isDataChanged;
	}

	public void setDataChanged(boolean isDataChanged) {
		this.isDataChanged = isDataChanged;
	}
	
	
	
	
}
