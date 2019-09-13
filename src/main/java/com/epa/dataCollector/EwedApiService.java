package com.epa.dataCollector;

public interface EwedApiService {

	public String getFacility(String filterField, String filterValue, String matchLevel, int minYear, int maxYear);
	
	public String getFacilityData(String filterField, String filterValue, int minYear, int minMonth, int maxYear, int maxMonth);

	public String defaultGEWData(String filterName, int minYear, int minMonth, int maxYear, int maxMonth);

	public String returnWaterAvailabilityFromHUCs(String filterField, String filterValue, int minYear, int minMonth, int maxYear,
			int maxMonth);

	public String getSummaryWithin(String filterField1, String filterValue1, String filterField2, int minYear,
			int minMonth, int maxYear, int maxMonth);

	public String getAllFacilities(String filterField, String filterValue);

	public String processWaterAvailabilityFile(String fileName, int startYear, int endYear);

}
