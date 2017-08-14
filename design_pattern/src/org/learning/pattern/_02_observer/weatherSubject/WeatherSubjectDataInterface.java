package org.learning.pattern._02_observer.weatherSubject;

import org.learning.pattern._02_observer.weatherObserver.WeatherObserver;

public interface WeatherSubjectDataInterface {
	public void registerObserver(WeatherObserver o);
	public void removeObserver(WeatherObserver o);
	public void notifyObservers();
}
