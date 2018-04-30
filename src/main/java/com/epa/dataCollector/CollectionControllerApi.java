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


@RestController
public class CollectionControllerApi {

	@Autowired
	CollectionControllerApiImpl apiImpl;
	
   private static final Logger LOG = LoggerFactory.getLogger(EnviroFactsUtil.class);
//   private static CollectionControllerApiImpl apiImpl = new CollectionControllerApiImpl();
   
	@RequestMapping("/test")
	public String test() {
		System.out.println("Reached test");
		return "Got it!";
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
		return apiImpl.initiateCollectionImpl(dbName, returnFormat, rowStart, rowEnd, filterField, filterValue, clearAndAdd);
	}

	@RequestMapping(method = RequestMethod.GET,  value = "/getGenerationData/{plantCode}")
	public String getGenerationData( @PathVariable(value="plantCode") String plantCode) {
		
		return apiImpl.getGenerationData(plantCode).toString();
	}

	
	/**
	 * This method is used to get the information of all the pollutants from the pollutant table in the frs database. 
	 * If a pollutant code is mentioned then the function will return information about just one pollutant.
	 * @param pollutantCode
	 * @return String of pollutants
	 */
	@RequestMapping(method = RequestMethod.GET,  value = "/getPollutantInfo")
	public String getPollutantInfo( @RequestParam(value="pollutantCode", defaultValue = "") String pollutantCode) {
		return apiImpl.getPollutantInfoImpl(pollutantCode);
	}
	
	/**
	 * This method is used to get the information of all the greenhouse gases from the GHG database. 
	 * If a gas id is mentioned then the function will return information about just one gas.
	 * @param gasId
	 * @return String of gases
	 */
	@RequestMapping(method = RequestMethod.GET,  value = "/getGreenhouseGasInfo")
	public String getGreenhouseGasInfo( @RequestParam(value="gasId", defaultValue = "") String gasId) {
		return apiImpl.getGreenhouseGasInfo(gasId);
	}

	@RequestMapping(method = RequestMethod.GET,  value = "/getData")
	public String getData() {
		return apiImpl.getData();
	}
	
	@RequestMapping(method = RequestMethod.GET,  value = "/clearLists")
	public boolean clearLists() {
		return apiImpl.clearLists();
	}
}
