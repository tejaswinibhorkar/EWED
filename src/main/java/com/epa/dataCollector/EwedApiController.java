package com.epa.dataCollector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ewedService/")
@RestController
public class EwedApiController {

	@Autowired
	EwedApiService apiService;
	
	@RequestMapping("/test")
	public String test() {
		System.out.println("Reached ewed api test");
		return "Reached ewed api test";
	}
	
	@RequestMapping("/getFacility/{filterField}/{filterValue}/{minYear}/{maxYear}")
	public String getFacility(@PathVariable(value="filterField") String filterField, @PathVariable(value="filterValue") String filterValue,
			@PathVariable(value="minYear") int minYear, @PathVariable(value="maxYear") int maxYear) {
		return apiService.getFacility(filterField ,filterValue,"", minYear, maxYear);
	}
	
	@RequestMapping("/getFacility/{filterField}/{filterValue}/{matchLevel}/{minYear}/{maxYear}")
	public String getFacilityWithMatchLevel(@PathVariable(value="filterField") String filterField, @PathVariable(value="filterValue") String filterValue, 
			@PathVariable(value="matchLevel") String matchLevel,
			@PathVariable(value="minYear") int minYear, @PathVariable(value="maxYear") int maxYear) {
		return apiService.getFacility(filterField ,filterValue, matchLevel, minYear, maxYear);
	}
}
