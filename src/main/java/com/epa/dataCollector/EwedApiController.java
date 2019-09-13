package com.epa.dataCollector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epa.views.Top4;

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
	
	@RequestMapping("/getAllFacilities/{filterField}/{filterValue}")
	public String getAllFacilities(@PathVariable(value="filterField") String filterField, @PathVariable(value="filterValue") String filterValue) {
		return apiService.getAllFacilities(filterField ,filterValue);
	}
	
	@RequestMapping("/getFacilityData/{filterField}/{filterValue}/{minYear}/{minMonth}/{maxYear}/{maxMonth}")
	public String getFacilityData(
			@PathVariable(value="filterField") String filterField, 
			@PathVariable(value="filterValue") String filterValue, 
			@PathVariable(value="minYear") int minYear, @PathVariable(value="minMonth") int minMonth, 
			@PathVariable(value="maxYear") int maxYear, @PathVariable(value="maxMonth") int maxMonth) {
		return apiService.getFacilityData(filterField ,filterValue, minYear, minMonth, maxYear, maxMonth);
	}
	
	@RequestMapping("/defaultViewData/{filterField}/{minYear}/{minMonth}/{maxYear}/{maxMonth}")
	public String defaultViewData(
			@PathVariable(value="filterField") String filterField,  
			@PathVariable(value="minYear") int minYear,
			@PathVariable(value="minMonth") int minMonth,
			@PathVariable(value="maxYear") int maxYear,
			@PathVariable(value="maxMonth") int maxMonth){
		return apiService.defaultGEWData(filterField, minYear,minMonth, maxYear, maxMonth);
	}
	
	@RequestMapping("/getWaterAvailabilityData/{filterField}/{filterValue}/{minYear}/{minMonth}/{maxYear}/{maxMonth}")
	public String getWaterAvailabilityData(
			@PathVariable(value="filterField") String filterField, 
			@PathVariable(value="filterValue") String filterValue, 
			@PathVariable(value="minYear") int minYear, @PathVariable(value="minMonth") int minMonth, 
			@PathVariable(value="maxYear") int maxYear, @PathVariable(value="maxMonth") int maxMonth) {
		return apiService.returnWaterAvailabilityFromHUCs(filterField ,filterValue, minYear, minMonth, maxYear, maxMonth);
	}
	
	@RequestMapping("/getSummaryWithin/{filterField1}/{filterValue1}/{filterField2}/{minYear}/{minMonth}/{maxYear}/{maxMonth}")
	public String getHUCSummaryWithinState(
			@PathVariable(value="filterField1") String filterField1, 
			@PathVariable(value="filterValue1") String filterValue1, 
			@PathVariable(value="filterField2") String filterField2, 
			@PathVariable(value="minYear") int minYear, @PathVariable(value="minMonth") int minMonth, 
			@PathVariable(value="maxYear") int maxYear, @PathVariable(value="maxMonth") int maxMonth) {
		return apiService.getSummaryWithin(filterField1, filterValue1, filterField2, minYear, minMonth, maxYear, maxMonth);
		}
	
	@RequestMapping("/processWaterAvailabilityFile/{fileName}/{startYear}/{endYear}")
	public String processWaterAvailabilityFile(
			@PathVariable(value="fileName") String fileName, 
			@PathVariable(value="startYear") int startYear, 
			@PathVariable(value="endYear") int endYear){
		return apiService.processWaterAvailabilityFile(fileName, startYear, endYear);
		}
	
//	@RequestMapping("/getTopRecords/{filterField1}/{filterValue1}/{filterField2}/{minYear}/{minMonth}/{maxYear}/{maxMonth}")
//	public List<Top4> getTopRecords(
//			@PathVariable(value="filterField1") String filterField1, 
//			@PathVariable(value="filterValue1") String filterValue1, 
//			@PathVariable(value="filterField2") String filterField2, 
//			@PathVariable(value="minYear") int minYear, @PathVariable(value="minMonth") int minMonth, 
//			@PathVariable(value="maxYear") int maxYear, @PathVariable(value="maxMonth") int maxMonth) {
//		return apiService.getTop4Records(filterField1, filterValue1, filterField2, minYear, minMonth, maxYear, maxMonth);
//		}
}
