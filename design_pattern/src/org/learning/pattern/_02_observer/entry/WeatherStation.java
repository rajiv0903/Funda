package org.learning.pattern._02_observer.entry;

import org.learning.pattern._02_observer.weatherDisplay.CurrentConditionsDisplay;
import org.learning.pattern._02_observer.weatherDisplay.ForecastDisplay;
import org.learning.pattern._02_observer.weatherDisplay.StatisticsDisplay;
import org.learning.pattern._02_observer.weatherDisplay.WeatherDisplayElement;
import org.learning.pattern._02_observer.weatherSubject.WeatherSubjectData;

public class WeatherStation {

	public static void main(String[] args) {
		WeatherSubjectData weatherSubjectData = new WeatherSubjectData();
	
		WeatherDisplayElement currentDisplay = new CurrentConditionsDisplay(weatherSubjectData);
		WeatherDisplayElement statisticsDisplay = new StatisticsDisplay(weatherSubjectData);
		WeatherDisplayElement forecastDisplay = new ForecastDisplay(weatherSubjectData);

		weatherSubjectData.setDataChanged(true);
		weatherSubjectData.setMeasurements(80, 65, 30.4f);
		weatherSubjectData.setDataChanged(true);
		weatherSubjectData.setMeasurements(82, 70, 29.2f);
		weatherSubjectData.setDataChanged(true);
		weatherSubjectData.setMeasurements(78, 90, 29.2f);
	}
}
