package com.epa.dataCollector;

public interface EwedApiService {

	public String getFacility(String filterField, String filterValue, String matchLevel, int minYear, int maxYear);
	//Facility getFacility(String registryId);
	
}
