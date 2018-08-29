package com.epa.util;

import java.util.HashMap;
import java.util.Map;

public class EPAConstants {

	public static Map<String, String> dbNameMap = new HashMap<String, String>();

	//Table Names
	public static final String facIdIdentifier = "FACID";
	public static final String facIdTable = "T_FRS_NAICS_EZ2";
	
	public static final String emissionsIdentifier = "EMISSIONS";
	public static final String emissionsGHGTable = "PUB_DIM_FACILITY";
	public static final String emissionsEISTable = "FACILITIES/RELEASE_POINTS/EMISSIONS";
	public static final String gasInfoTable = "PUB_DIM_GHG";
	
	public static final String eiaSeriesHead = "ELEC.PLANT.GEN.";
	public static final String eiaSeriesTail = "-ALL-ALL.M";
	
	//URL Constants
	public static final String enviroFactsBaseURL = "https://iaspub.epa.gov/enviro/efservice/";
	public static final String enviroFactsRowSpecifier = "/rows/";
	public static final String eiaAPIKey = "d46693c2b9f4716d140635bcab2db96c";
	public static final String eiaBaseURL = "http://api.eia.gov/series/?api_key="+eiaAPIKey+"&series_id=";
	public static final String additionalConstant = "%#additional#%/";
	public static final String emissionJoinURL = "PUB_FACTS_SECTOR_GHG_EMISSION/";
	
	//Error Messages
	public static final String genericErrorReturn = "Please recheck the url and try again";
	
	static {
		dbNameMap.put(facIdIdentifier, facIdTable);
		dbNameMap.put(emissionsIdentifier, emissionsGHGTable);
	}
	
}
