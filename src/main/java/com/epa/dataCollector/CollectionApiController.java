package com.epa.dataCollector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epa.util.EnviroFactsUtil;

/**
 * 
 * The rest mapping of the server ends up on this controller.
 * This is the default controller. 
 *
 */
@RestController
public class CollectionApiController {

	@Autowired
	CollectionApiService apiService;
	
   private static final Logger LOG = LoggerFactory.getLogger(EnviroFactsUtil.class);
//   private static CollectionControllerApiImpl apiImpl = new CollectionControllerApiImpl();
   
	@RequestMapping("/test")
	public String test() {
		System.out.println("Reached collection test");
		return "Reached collection test!";
	}
	
	/**
	 * REQUIRED PARAMS -
	 * @param dbName
	 * @param returnFormat
	 * @param rowStart
	 * @param rowEnd
	 * 
	 * OPTIONAL PARAMS - 
	 * @param filterField
	 * @param filterValue
	 * @param clearAndAdd - This field, when set to true, clears the existing list object and from the subsequent calls
	 * keeps adding data fetched to the previous data. This is helpful in collecting and adding up data that cannot
	 * be fetched all at once due to API limitations.
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,  value = "/initiateCollection/{dbName}/"
			+ "/{returnFormat}/{rowStart}/{rowEnd}")
	public String initiateCollection(@PathVariable(value="dbName") String dbName,
									 @PathVariable(value="returnFormat") String returnFormat,
									 @PathVariable(value="rowStart") String rowStart,
									 @PathVariable(value="rowEnd") String rowEnd,
							 @RequestParam(value="filterField", defaultValue = "") String filterField,
							 @RequestParam(value="filterValue", defaultValue = "") String filterValue,
							 @RequestParam(value="clearAndAdd" , defaultValue = "false") boolean clearAndAdd ) {
		
		System.out.println("Reached here");
//		return "Reached";
		return apiService.initiateCollectionImpl(dbName, returnFormat, rowStart, rowEnd, filterField, filterValue, clearAndAdd);
	}

	@RequestMapping(method = RequestMethod.GET,  value = "/getGenerationData/{plantCode}")
	public String getGenerationData( @PathVariable(value="plantCode") String plantCode) {
		
		return apiService.getGenerationData(plantCode).toString();
	}

	
	/**
	 * This method is used to get the information of all the pollutants from the pollutant table in the frs database. 
	 * If a pollutant code is mentioned then the function will return information about just one pollutant.
	 * @param pollutantCode
	 * @return String of pollutants
	 */
	@RequestMapping(method = RequestMethod.GET,  value = "/getPollutantInfo")
	public String getPollutantInfo( @RequestParam(value="pollutantCode", defaultValue = "") String pollutantCode) {
		return apiService.getPollutantInfoImpl(pollutantCode);
	}
	
	/**
	 * This method is used to get the information of all the greenhouse gases from the GHG database. 
	 * If a gas id is mentioned then the function will return information about just one gas.
	 * @param gasId
	 * @return String of gases
	 */
	@RequestMapping(method = RequestMethod.GET,  value = "/getGreenhouseGasInfo")
	public String getGreenhouseGasInfo( @RequestParam(value="gasId", defaultValue = "") String gasId) {
		return apiService.getGreenhouseGasInfo(gasId);
	}

	/**
	 * This functions gets all the facility information 
	 * from the currently set base facility table
	 * and saves or updates facility information.
	 * 
	 * @return Confirmation msg string
	 */
	@RequestMapping(method = RequestMethod.GET,  value = "/getData")
	public String getData() {
		return apiService.getData();
	}
	
	/**
	 * – This function has been made in case there are multiple
	 *  functions called in one session of the server.
	 *  It should be called before trying to grab multiple 
	 *  forms of the same data or from different sources.
	 *  
	 *  @return Confirmation boolean
	 */
	@RequestMapping(method = RequestMethod.GET,  value = "/clearLists")
	public boolean clearLists() {
		return apiService.clearLists();
	}
	
	/**
	 * This function is used to get generation data on all the facilities 
	 * currently present in the mapped facility table based upon their plant codes. 
	 * They call the afore mentioned get generation data function to get information on each plant.
	 * 
	 * @return String delineating number of facilities whose generation were found
	 * and not found.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getAllGeneration")
	public String getAllGeneration() {
		return apiService.getAllGeneration();
	}
	
	/**
	 * This function reads plantCodes from a file called genIds in the project structure.
	 * This is useful if there are plantCodes to be read from a flat file.
	 * @return String delineating number of facilities whose generation were found
	 * and not found.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getGenerationFromFile")
	public String getGenerationFromFile() {
		return apiService.getGenerationFromFile();
	}
	
	/**
	 * This is a specialized version of the getData function where in you can
	 *  get Facility from any url. In the current implementation, 
	 *  the function gets all the data from frs_program_facility, 
	 *  the base table of facility in envirofacts, for all facilities
	 *   which have the acronym as EIA-860.
	 * @return Confirmation string
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getFacilityfromGen")
	public String getFacilityfromGen() {
		return apiService.getFacilityfromGen();
	}
	
	/**
	 * This function gets emissions data for facilities that exists in the
	 *  mapped facility table. This retrieves data on the basis of registryIds. 
	 * @return Confirmation with msg with a count of conflicts (if any)
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getEmissions")
	public String getEmissions() {
		return apiService.getEmissions();
	}
	
	/**
	 * This function gets the dominant plant type for all facilities that exists in the
	 *  mapped facility table. 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getAllDominantType")
	public String getAllDominantType() {
		return apiService.getAllDominantType();
	}
}
