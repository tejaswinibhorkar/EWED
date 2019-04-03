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
	
	/**
	 * REQUIRED PARAMS - 
	 * @param filterField
	 * @param filterValue
	 * @param minYear
	 * @param maxYear
	 * 
	 * This function uses the filter field to match the filter value in the 
	 * facility info table and returns the facility information json along
	 * with a summary as a json of the EWEDataReturn class.
	 * 
	 * @return Facility Information and Summary JSON
	 */
	@RequestMapping("/getFacility/{filterField}/{filterValue}/{minYear}/{maxYear}")
	public String getFacility(@PathVariable(value="filterField") String filterField, @PathVariable(value="filterValue") String filterValue,
			@PathVariable(value="minYear") int minYear, @PathVariable(value="maxYear") int maxYear) {
		return apiService.getFacility(filterField ,filterValue,"", minYear, maxYear);
	}
	
	/**
	 * REQUIRED PARAMS - 
	 * @param filterField
	 * @param filterValue
	 * @param matchLevel (Extra from above)
	 * @param minYear
	 * @param maxYear
	 * 
	 * This works exactly like the above function but having this method ensures
	 * that a match level is an optional parameter without having to use request
	 * parameters. This function ensures that a match level is used.
	 * 
	 * @return Facility Information and Summary JSON
	 */
	@RequestMapping("/getFacility/{filterField}/{filterValue}/{matchLevel}/{minYear}/{maxYear}")
	public String getFacilityWithMatchLevel(@PathVariable(value="filterField") String filterField, @PathVariable(value="filterValue") String filterValue, 
			@PathVariable(value="matchLevel") String matchLevel,
			@PathVariable(value="minYear") int minYear, @PathVariable(value="maxYear") int maxYear) {
		return apiService.getFacility(filterField ,filterValue, matchLevel, minYear, maxYear);
	}
}
