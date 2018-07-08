package com.demo.projects.osgi.demo.core.service;

import java.util.List;

public interface WeatherService {

	public double getWeather(String cityName);
	public List<String> getCities();
}
