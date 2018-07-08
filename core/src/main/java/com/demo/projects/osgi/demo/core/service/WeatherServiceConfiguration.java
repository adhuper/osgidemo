package com.demo.projects.osgi.demo.core.service;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Weather Service Configuration", 
description = "Weahter service configuration to capture city and lat, long mapping")
public @interface WeatherServiceConfiguration {

	@AttributeDefinition(name = "City Lat Long Mapping")
	String[] getCityLatLongMapping();
	
	@AttributeDefinition(name = "OPEN API APP ID")
	String getOpenAPIAppId();
	
}
