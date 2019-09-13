package com.epa.dataCollector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epa.beans.EWEDMonthlyData;
import com.epa.beans.EWEDataReturn;
import com.epa.beans.Facility.Facility;
import com.epa.beans.Facility.FacilityInfo;
import com.epa.beans.WaterUsage.WaterAvailability;
import com.epa.util.EPAConstants;
import com.epa.util.HibernateUtil;
import com.epa.views.DefaultOutputJson;
import com.epa.views.DefaultOutputJson_custom;
import com.epa.views.GenEmWaterView;
import com.epa.views.Top4;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;


@Service
public class EwedApiServiceImpl implements EwedApiService {

	//Object mapper is used to convert objects to jsons
	@Autowired
	ObjectMapper mapper;
	public Session session;
	NumberFormat formatter = new DecimalFormat("########.#########");  
	
	@Override
	public String getFacility(String filterField, String filterValue, String matchLevel, int minYear, int maxYear) {
		
		session = HibernateUtil.getSessionFactory().openSession();
		StringBuilder facilityQuery = new StringBuilder();
		
		
		//In hibernate if Select statement is not give, it assumes select *
		
		facilityQuery.append("from FacilityInfo where ").append(filterField).append(" LIKE :").append(filterField);
		
		Query query = session.createQuery(facilityQuery.toString());
		
		// Match level usage
		switch(matchLevel.toUpperCase()) {
			case "BEGINNING" :  query.setParameter(filterField,  filterValue + "%"); break;
			case "CONTAINING" : query.setParameter(filterField, "%" + filterValue + "%"); break; 
			case "ENDING" : query.setParameter(filterField, "%" + filterValue ); break;
			default : query.setParameter(filterField, filterValue); break;
		}
		
		System.out.println("Querying - " + query.getQueryString());
		List<FacilityInfo> list = query.list();
		
		Map<String, String> returnData = new HashMap<String, String>();
		
		try {
			returnData.put(EPAConstants.facilityDataKey, mapper.writeValueAsString(list)) ; //Return result as json
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> registryIds = new ArrayList<String>();
		
		for(FacilityInfo fac : list) {
			registryIds.add(fac.getRegistryId());
		}
		
		if(!registryIds.isEmpty())
			returnData.put(EPAConstants.eweDataKey, getEWEData(registryIds, minYear, maxYear));

		try {
			return mapper.writeValueAsString(returnData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return EPAConstants.genericErrorReturn;
	}
	
	public String getEWEData(List<String> registryIds, int minYear, int maxYear) {
		EWEDataReturn returnData = new EWEDataReturn();
		
		returnData.participatingFacilities = registryIds.size();
		
		Query query = session.createQuery("SELECT sum(genSum) from GenerationPerRegistryIdView g"
				+ " where g.genViewKey.registryId in (:ids) and genYear between :minYear and :maxYear");
		
		query.setParameterList("ids", registryIds);
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		List<String> listResult = query.list();
		
		returnData.generation = listResult.get(0);
		
		query = session.createQuery("SELECT sum(emissionAmount) from EmissionsMonthly e"
					+ " where e.emissionsMonthlyKey.registryId in (:ids) and emYear between :minYear and :maxYear");
		
		query.setParameterList("ids", registryIds);
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		listResult.clear();
		listResult = query.list();
		
		returnData.emission = listResult.get(0);
		
		query = session.createQuery("SELECT sum(usageSum) from WaterUsagePerRegView w"
				+ " where w.waterViewKey.registryId in (:ids) and usageYear between :minYear and :maxYear");
	
		query.setParameterList("ids", registryIds);
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		listResult.clear();
		listResult = query.list();
		
		returnData.waterUsage = listResult.get(0);
		
		try {
			return mapper.writeValueAsString(returnData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return EPAConstants.genericErrorReturn;
	}
	
	@Override
	public String getFacilityData(String filterField, String filterValue, int minYear, int minMonth, int maxYear, int maxMonth) {
		
		List<Facility> facList = listOfFacilitiesWithinFilter(filterField, filterValue);
		
		List<String> plantCodes = new  ArrayList<String>();
		for(Facility fac: facList) {
			plantCodes.add(fac.getPgmSysId());
		}
		
		List<GenEmWaterView> gewList = queryGEWView(plantCodes,minYear, minMonth, maxYear, maxMonth);
		
		//Add the water availability part 
		List<String> hucCodes = getAllHUCCodes(filterField, filterValue, minYear, minMonth, maxYear, maxMonth);
		List<WaterAvailability> waterAvailabilityList = getWaterAvailabilityDataList(hucCodes, minYear, minMonth, maxYear, maxMonth);
 
		String returnJson = returnData(facList,  gewList, waterAvailabilityList);
		 
		return returnJson;
	}
	
	@Override
	public String getAllFacilities(String filterField, String filterValue) {
		
		try {
			return mapper.writeValueAsString(listOfFacilitiesWithinFilter(filterField, filterValue));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{\"Result\": \"Data not found\"}";
		
	}
	public List<Facility> listOfFacilitiesWithinFilter(String filterField, String filterValue){
		
		session = HibernateUtil.getSessionFactory().openSession();
		StringBuilder facilityQuery = new StringBuilder();
		Query query =null;
		
		if(filterValue.equalsIgnoreCase("all")) {
			facilityQuery.append("from Facility");
			query = session.createQuery(facilityQuery.toString());
		}
		else {
			facilityQuery.append("from Facility where ").append(filterField).append(" LIKE :").append(filterField);
			query = session.createQuery(facilityQuery.toString());
			query.setParameter(filterField, filterValue);
		}
		System.out.println("Facility query = " + query);
		List<Facility> facList = query.list();
		
		System.out.println("Facilities list size = " + facList.size());
		
		return facList;
	}
	public List<GenEmWaterView> queryGEWView(List<String> plantCodes, int minYear, int minMonth, int maxYear, int maxMonth) {
		
		session = HibernateUtil.getSessionFactory().openSession();
		
		List<GenEmWaterView> gewList =  new ArrayList<GenEmWaterView>();
		
		int partitionSize = 2000;
		for (int i = 0; i < plantCodes.size(); i += partitionSize) {
			List<String> partPlantCodes = new ArrayList<String>();
			if(plantCodes.size() < i+partitionSize)
				partPlantCodes = plantCodes.subList(i, plantCodes.size());
			else
				partPlantCodes = plantCodes.subList(i, i+partitionSize);
			
			StringBuilder gewQueryBuilder = new StringBuilder();
			gewQueryBuilder.append("SELECT new com.epa.views.GenEmWaterView (g.plantCode, g.genYear, g.genMonth, g.plantType, g.generation, g.emissions, g.waterWithdrawal, g.waterConsumption) ")
			.append("from GenEmWaterView g where (g.plantCode in (:ids) and ")
			.append("((( genYear = :minYear and genMonth >= :minMonth) OR (genYear > :minYear)) and ((genYear = :maxYear and genMonth <= :maxMonth) or (genYear < :maxYear)))) ")
			.append("order by plantCode, genYear, genMonth");
			
			Query query = session.createQuery(gewQueryBuilder.toString());

			query.setParameterList("ids", partPlantCodes);
			query.setParameter("minYear", minYear);
			query.setParameter("maxYear", maxYear);
			query.setParameter("minMonth", minMonth);
			query.setParameter("maxMonth", maxMonth);
			
			System.out.println(query);
			List<GenEmWaterView> temp_gewList = query.list();
			gewList.addAll(temp_gewList); 
		}
		System.out.println("all gew list size = " + gewList.size());
		return gewList;
	
	}
	
	public String returnData(List<Facility> facList, List<GenEmWaterView> gewList, List<WaterAvailability> waterAvailabilityList) {
		
		// Stores the complete structure
		Map<String, Object> completeGenEmWaterOutput = new HashMap<String, Object>();
		
		// Stores the month wise summary of all facilities in the given range of year
		HashMap<Integer, HashMap<Integer, DefaultOutputJson_custom>> facMonthWiseData = new HashMap<Integer, HashMap<Integer,DefaultOutputJson_custom>>();
		
		// Stores the detailed list of facility with monthly summary
		List<Object> finalFacList = new ArrayList<Object>();
		
		// Stores the total summary data
		Map<String, Object> totalSummaryData = new HashMap<String, Object>();
		double totalGen = 0;
		double totalEm = 0;
		double totalWaterConsumption = 0;
		double totalWaterWithdrawal = 0;
		
		for(Facility fac: facList) {
			
			LinkedHashMap<Object, Object> facilityReturnMap = new LinkedHashMap<Object, Object>();
			//List<MonthlyFacilityData> facData = new ArrayList<MonthlyFacilityData>();
			String plantCode = fac.getPgmSysId();
			
			List<EWEDMonthlyData> monthlyDataList = new ArrayList<EWEDMonthlyData>();
			Map<String, Object> monthlyDataSummary = new HashMap<String, Object>();
			double monthlyGen = 0;
			double monthlyEm = 0;
			double monthlyWaterConsumption = 0;
			double monthlyWaterWithdrawal = 0;
			
			for(int i=0; i<gewList.size(); i++) {
				
				GenEmWaterView data = gewList.get(i);
				if(data.getPlantCode().equals(plantCode)) {
					EWEDMonthlyData monthlyData = new EWEDMonthlyData();
					monthlyData.year = data.getGenYear();
					monthlyData.month = data.getGenMonth();
					monthlyData.plantType = data.getPlantType();
					monthlyData.generation = data.getGeneration() != null ? formatter.format(Double.parseDouble(data.getGeneration().trim())) : "null";
					monthlyData.emissions= data.getEmissions() != null ? formatter.format(Double.parseDouble(data.getEmissions().trim())) : "null";
					monthlyData.waterWithdrawal= data.getWaterWithdrawal() != null ?  formatter.format(Double.parseDouble(data.getWaterWithdrawal().trim())) : "null";
					monthlyData.waterConsumption= data.getWaterConsumption() != null ?  formatter.format(Double.parseDouble(data.getWaterConsumption().trim())) : "null";
					
					monthlyDataList.add(monthlyData);
					
					double gen = data.getGeneration() != null ? Double.parseDouble(data.getGeneration().trim()) : 0;
					double em = data.getEmissions() != null ? Double.parseDouble(data.getEmissions().trim()) : 0;
					double wc = data.getWaterConsumption() != null ? Double.parseDouble(data.getWaterConsumption().trim()) : 0;
					double ww = data.getWaterWithdrawal() != null ? Double.parseDouble(data.getWaterWithdrawal().trim()) : 0;
					
					monthlyGen += gen;
					monthlyEm += em;
					monthlyWaterConsumption += wc;
					monthlyWaterWithdrawal += ww;
					
					// Year-wise Data
					HashMap<Integer, DefaultOutputJson_custom> genEmWaterPerMonth =  new HashMap<Integer, DefaultOutputJson_custom>();
					
					if(facMonthWiseData.containsKey(monthlyData.year)) {
						
						genEmWaterPerMonth = facMonthWiseData.get(monthlyData.year);
						DefaultOutputJson_custom customObj ;
						if(genEmWaterPerMonth.containsKey(monthlyData.month)) {
							customObj = genEmWaterPerMonth.get(monthlyData.month);
							customObj.generation = (formatter.format(Double.parseDouble(customObj.generation) + gen));
							customObj.emission = formatter.format(Double.parseDouble(customObj.emission) + em);
							customObj.waterConsumption = formatter.format(Double.parseDouble(customObj.waterConsumption)+ wc);
							customObj.waterWithdrawal = formatter.format(Double.parseDouble(customObj.waterWithdrawal) + ww);
							
						} else {
							customObj = new DefaultOutputJson_custom();
							customObj.generation = formatter.format(gen);
							customObj.emission = formatter.format(em);
							customObj.waterConsumption = formatter.format(wc);
							customObj.waterWithdrawal = formatter.format(ww);
						}
						genEmWaterPerMonth.put(monthlyData.month, customObj);
						facMonthWiseData.put(monthlyData.year, genEmWaterPerMonth);
						
					}else {
						DefaultOutputJson_custom customObj ;
						customObj = new DefaultOutputJson_custom();
						customObj.generation = formatter.format(gen);
						customObj.emission = formatter.format(em);
						customObj.waterConsumption = formatter.format(wc);
						customObj.waterWithdrawal = formatter.format(ww);
						genEmWaterPerMonth.put(monthlyData.month, customObj);
						facMonthWiseData.put(monthlyData.year, genEmWaterPerMonth);
						
					}
				}
			}
			
			//Loop on waterAvailabilityList to add WA data
			for(WaterAvailability waData: waterAvailabilityList) {
				
				HashMap<Integer, DefaultOutputJson_custom> genEmWaterPerMonth =  new HashMap<Integer, DefaultOutputJson_custom>();
				
				if(facMonthWiseData.containsKey(waData.getYear())) {
					
					genEmWaterPerMonth = facMonthWiseData.get(waData.getYear());
					DefaultOutputJson_custom customObj ;
					if(genEmWaterPerMonth.containsKey(waData.getMonth())) {
						customObj = genEmWaterPerMonth.get(waData.getMonth());
						double tempWA = customObj.waterAvailability != null ? Double.parseDouble(customObj.waterAvailability) : 0;
						double newWA = waData.getWaterAvailable() != null ? Double.parseDouble(waData.getWaterAvailable()) : 0;
						customObj.waterAvailability = (formatter.format(tempWA + newWA)) ;
						genEmWaterPerMonth.put(waData.getMonth(), customObj);
						facMonthWiseData.put(waData.getYear(), genEmWaterPerMonth);
					}
				}
			}
			
			monthlyDataSummary.put("MonthlyGeneration", formatter.format(monthlyGen));
			monthlyDataSummary.put("MonthlyEmission", formatter.format(monthlyEm));
			monthlyDataSummary.put("MonthlyWaterConsumption", formatter.format(monthlyWaterConsumption));
			monthlyDataSummary.put("MonthlyWaterWithdrawal", formatter.format(monthlyWaterWithdrawal));
			
			totalGen += monthlyGen;
			totalEm += monthlyEm;
			totalWaterConsumption += monthlyWaterConsumption;
			totalWaterWithdrawal += monthlyWaterWithdrawal;

			facilityReturnMap.put("Facility",fac);
			facilityReturnMap.put("MonthlyData",monthlyDataList);
			facilityReturnMap.put("MonthlyDataSummary", monthlyDataSummary);
			finalFacList.add(facilityReturnMap);

		} 
		
		totalSummaryData.put("TotalGeneration", formatter.format(totalGen));
		totalSummaryData.put("TotalEmission", formatter.format(totalEm));
		totalSummaryData.put("TotalWaterConsumption", formatter.format(totalWaterConsumption));
		totalSummaryData.put("TotalWaterWithdrawal", formatter.format(totalWaterWithdrawal));
		
		completeGenEmWaterOutput.put("Summary",totalSummaryData);
		completeGenEmWaterOutput.put("All Facilities", finalFacList);
		completeGenEmWaterOutput.put("MonthWiseSummary", facMonthWiseData);
		
		// Convert Map to JSON
		try {
			return mapper.writeValueAsString(completeGenEmWaterOutput);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return "{\"Result\": \"Data not found\"}";
	}
	
	
	public String defaultGEWData(String filterName, int minYear, int minMonth, int maxYear, int maxMonth) {
		
		Map<String, Object> completeData = new HashMap<String, Object>();
		session = HibernateUtil.getSessionFactory().openSession();
		
		Map<String, String> totalSummaryData = new HashMap<String, String>();
		double totalGen = 0;
		double totalEm = 0;
		double totalWaterConsumption = 0;
		double totalWaterWithdrawal = 0;
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT new com.epa.views.DefaultOutputJson(cast(g.").append(filterName).append( " as string)").append(" as filterName,")
		.append( " sum(g.generation) as generation, ")
		.append( "sum(g.emissions) as emission, sum(g.waterWithdrawal) as waterWithdrawal, sum(g.waterConsumption) as waterConsumption)")
		.append( " from com.epa.views.GenEmWaterView g")
		.append( " where ((( genYear = :minYear and genMonth >= :minMonth) OR (genYear > :minYear)) and ((genYear = :maxYear and genMonth <= :maxMonth) or (genYear < :maxYear)))")
		.append( " Group by " ).append( filterName);
		
		Query query = session.createQuery(queryBuilder.toString());
		
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		query.setParameter("minMonth", minMonth);
		query.setParameter("maxMonth", maxMonth);
		
		System.out.println(query);
		
		List<DefaultOutputJson> results = query.list();
		
		Map<String, Object>returnData = new HashMap<String, Object>();
		
		for(DefaultOutputJson output: results) {
			DefaultOutputJson_custom obj = new DefaultOutputJson_custom();
			if(output.getFilterName() != null) {
				double em = output.getEmission() != null ? Double.parseDouble(output.getEmission().trim()) : 0;
				double gen = output.getGeneration() != null ? Double.parseDouble(output.getGeneration().trim()) : 0;
				double wc = output.getWaterConsumption() != null ? Double.parseDouble(output.getWaterConsumption().trim()) : 0;
				double ww = output.getWaterWithdrawal() != null ? Double.parseDouble(output.getWaterWithdrawal().trim()) : 0;
				
				obj.setEmission(formatter.format(em));
				obj.setGeneration(formatter.format(gen));
				obj.setWaterConsumption(formatter.format(wc));
				obj.setWaterWithdrawal(formatter.format(ww));
				
				returnData.put(output.getFilterName(),obj);
				
				totalGen += gen;
				totalEm += em;
				totalWaterConsumption += wc;
				totalWaterWithdrawal += ww;
			}
		}
		
		completeData.put("Summary", returnData);
		
		String genTotal = (formatter.format(totalGen));
		String emTotal = (formatter.format(totalEm));
		String wcTotal = (formatter.format(totalWaterConsumption));
		String wwTotal = (formatter.format(totalWaterWithdrawal));
		
		totalSummaryData.put("TotalGeneration", (genTotal));
		totalSummaryData.put("TotalEmission", (emTotal));
		totalSummaryData.put("TotalWaterConsumption",(wcTotal));
		totalSummaryData.put("TotalWaterWithdrawal",(wwTotal));
		completeData.put("Total Summary", (totalSummaryData));
		
		
		Map<String, Object> topRecords = new HashMap<String, Object>();
		if(filterName.equals("plantType")) {
			List<Top4> genList = getTop4Records("", "", filterName, "generation", minYear, minMonth, maxYear, maxMonth);
			System.out.println("genList= " + genList.size());
			List<Top4> emList = getTop4Records("", "", filterName, "emissions", minYear, minMonth, maxYear, maxMonth);
			System.out.println("emList = " + emList.size());
			List<Top4> wwList = getTop4Records("", "", filterName, "waterWithdrawal", minYear, minMonth, maxYear, maxMonth);
			System.out.println("wwList = " + wwList.size());
			List<Top4> wcList = getTop4Records("", "", filterName, "waterConsumption", minYear, minMonth, maxYear, maxMonth);
			System.out.println("wcList = " + wcList.size());
			
			topRecords.put("topGen", genList);
			topRecords.put("topEm", emList);
			topRecords.put("topWW", wwList);
			topRecords.put("topWC", wcList);
			
			completeData.put("Top_Records", topRecords);
		}
		
		try {
			return mapper.writeValueAsString(completeData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"Result\": \"Data not found\"}";
	
	}
	
	//end-point to get water availability json output
	public String returnWaterAvailabilityFromHUCs(String filterField, String filterValue, int minYear, int minMonth, int maxYear, int maxMonth) {
		List<String> hucCodes = new  ArrayList<String>();
		hucCodes = getAllHUCCodes(filterField, filterValue, minYear, minMonth, maxYear, maxMonth);
		if(hucCodes.size() == 0) {
			return "{\"Result\": \"Data not found\"}";
		}
		
		List<WaterAvailability> waterAvailibilityList =  new ArrayList<WaterAvailability>();
		double totalWaterAvailable = 0;
		
		waterAvailibilityList = getWaterAvailabilityDataList(hucCodes, minYear, minMonth, maxYear, maxMonth);
		
		for(WaterAvailability wa: waterAvailibilityList) {
			totalWaterAvailable += wa.getWaterAvailable() != null ? Double.parseDouble(wa.getWaterAvailable().trim()) : 0;
		}
		Map<String, Object> returnWaterAvaiData = new HashMap<String, Object>();
		returnWaterAvaiData.put("Water Availability Summary", totalWaterAvailable);
		returnWaterAvaiData.put("MonthlyData", waterAvailibilityList);
		
		try {
			return mapper.writeValueAsString(returnWaterAvaiData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"Result\": \"Data not found\"}";
		
	}
	
	public List<String> getAllHUCCodes(String filterField, String filterValue, int minYear, int minMonth, int maxYear, int maxMonth) {
		// allowed filterField - state and Huc	
		session = HibernateUtil.getSessionFactory().openSession();
		StringBuilder facilityQuery = new StringBuilder();
		Query query =null;
		
		if(filterValue.equalsIgnoreCase("all")) {
			facilityQuery.append("from Facility");
			query = session.createQuery(facilityQuery.toString());
		}
		else {
			facilityQuery.append("from Facility where ").append(filterField).append(" LIKE :").append(filterField);
			query = session.createQuery(facilityQuery.toString());
			query.setParameter(filterField, filterValue);
		}
		System.out.println("Facility query = " + query);
		List<Facility> facList = query.list();
		
		List<String> hucCodes = new  ArrayList<String>();
		for(Facility fac: facList) {
			String huc = fac.getHUC8Code();
			if(!hucCodes.contains(huc))
				hucCodes.add(fac.getHUC8Code());
		}
		System.out.println("huccodes list size = " + hucCodes.size());
		return hucCodes;
	}
	
	
	public List<WaterAvailability> getWaterAvailabilityDataList(List<String> hucCodes, int minYear, int minMonth, int maxYear, int maxMonth){
		
		session = HibernateUtil.getSessionFactory().openSession();
		List<WaterAvailability> waterAvailibilityList =  new ArrayList<WaterAvailability>();
		
		int partitionSize = 2000;
		for (int i = 0; i < hucCodes.size(); i += partitionSize) {
			List<String> parthucCodes = new ArrayList<String>();
			if(hucCodes.size() < i+partitionSize)
				parthucCodes = hucCodes.subList(i, hucCodes.size());
			else
				parthucCodes = hucCodes.subList(i, i+partitionSize);
			System.out.println("parthucCodes list size = " + parthucCodes.size());
			
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder.append("SELECT new com.epa.beans.WaterUsage.WaterAvailability (HUCCode, year, month, waterAvailable)")
			.append("  from WaterAvailability")
			.append(" where (HUCCode in (:ids) and").append(" ((( year = :minYear and month >= :minMonth) OR" )
			.append(" (year > :minYear))" ).append(" and ((year = :maxYear and month <= :maxMonth) or (year < :maxYear))))" )
			.append(" order by HUCCode, year, month");
			Query query = session.createQuery(queryBuilder.toString());

			query.setParameterList("ids", parthucCodes);
			query.setParameter("minYear", minYear);
			query.setParameter("maxYear", maxYear);
			query.setParameter("minMonth", minMonth);
			query.setParameter("maxMonth",maxMonth);
			
			System.out.println(query);
			List<WaterAvailability> temp_waList = query.list();
			waterAvailibilityList.addAll(temp_waList); 
		}
		System.out.println("all gew list size = " + waterAvailibilityList.size());
		
		return waterAvailibilityList;
		
	}

	public String getSummaryWithin(String filterField1, String filterValue1, String filterField2, int minYear, int minMonth, int maxYear, int maxMonth) {
				
		List<Facility> facList = listOfFacilitiesWithinFilter(filterField1, filterValue1);
		
		List<String> plantCodes = new  ArrayList<String>();
		for(Facility fac: facList) {
			plantCodes.add(fac.getPgmSysId());
		}
		if(plantCodes.size() == 0) {
			return "{\"Result\": \"Data not found\"}";
		}
		
		session = HibernateUtil.getSessionFactory().openSession();
		StringBuilder viewQuery = new StringBuilder();
		
		if(filterField2.equals("fuelType")) {
			StringBuilder caseStatement = new StringBuilder();
			caseStatement.append("CASE WHEN g.plantType LIKE 'WAT-%' THEN 'WAT'")
			.append(" WHEN g.plantType LIKE 'BIT-%' THEN 'BIT' ")
			.append("END");
			
			viewQuery.append("SELECT new com.epa.views.DefaultOutputJson( ").append(caseStatement)	
			.append(" AS filterName, sum(g.generation) as generation, sum(g.emissions) as emission, sum(g.waterWithdrawal) as waterWithdrawal, sum(g.waterConsumption) as waterConsumption) from com.epa.views.GenEmWaterView g where (g.plantCode in ")
			.append(":ids").append(" and ((( genYear = :minYear and genMonth >= :minMonth) OR (genYear > :minYear)) and ((genYear = :maxYear and genMonth <= :maxMonth) or (genYear < :maxYear)))) Group by ")
			.append(caseStatement);
							
		}
		else {
			viewQuery.append("SELECT new com.epa.views.DefaultOutputJson(cast(g.").append(filterField2).
			append(" as string) as filterName, sum(g.generation) as generation, sum(g.emissions) as emission, sum(g.waterWithdrawal) as waterWithdrawal, sum(g.waterConsumption) as waterConsumption) from com.epa.views.GenEmWaterView g where (g.plantCode in ").
			append(":ids").append(" and ((( genYear = :minYear and genMonth >= :minMonth) OR (genYear > :minYear)) and ((genYear = :maxYear and genMonth <= :maxMonth) or (genYear < :maxYear)))) Group by ").append(filterField2);
		}
		Query query = session.createQuery(viewQuery.toString());
		
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		query.setParameter("minMonth", minMonth);
		query.setParameter("maxMonth", maxMonth);
		query.setParameterList("ids", plantCodes);
		
		System.out.println(query);
		List<DefaultOutputJson> results = query.list();
		System.out.println("output list summarry = " + results.size());
		
		Map<String, Object>returnData = new HashMap<String, Object>();
		
		for(DefaultOutputJson output: results) {
			DefaultOutputJson_custom obj = new DefaultOutputJson_custom();
			if(output.getFilterName() != null) {
				
				double em = output.getEmission() != null ? Double.parseDouble(output.getEmission().trim()) : 0;
				double gen = output.getGeneration() != null ? Double.parseDouble(output.getGeneration().trim()) : 0;
				double wc = output.getWaterConsumption() != null ? Double.parseDouble(output.getWaterConsumption().trim()) : 0;
				double ww = output.getWaterWithdrawal() != null ? Double.parseDouble(output.getWaterWithdrawal().trim()) : 0;
				
				obj.setEmission(formatter.format(em));
				obj.setGeneration(formatter.format(gen));
				obj.setWaterConsumption(formatter.format(wc));
				obj.setWaterWithdrawal(formatter.format(ww));
				
				returnData.put(output.getFilterName(),obj);
			}
		}
		
		Map<String, Object> topRecords = new HashMap<String, Object>();
		if(filterField2.equals("plantType")) {
			List<Top4> genList = getTop4Records(filterField1, filterValue1, filterField2, "generation", minYear, minMonth, maxYear, maxMonth);
			System.out.println("genList= " + genList.size());
			List<Top4> emList = getTop4Records(filterField1, filterValue1, filterField2, "emissions", minYear, minMonth, maxYear, maxMonth);
			System.out.println("emList = " + emList.size());
			List<Top4> wwList = getTop4Records(filterField1, filterValue1, filterField2, "waterWithdrawal", minYear, minMonth, maxYear, maxMonth);
			System.out.println("wwList = " + wwList.size());
			List<Top4> wcList = getTop4Records(filterField1, filterValue1, filterField2, "waterConsumption", minYear, minMonth, maxYear, maxMonth);
			System.out.println("wcList = " + wcList.size());
			
			topRecords.put("topGen", genList);
			topRecords.put("topEm", emList);
			topRecords.put("topWW", wwList);
			topRecords.put("topWC", wcList);
			
			returnData.put("Top_Records", topRecords);
		}
		
		try {
			return mapper.writeValueAsString(returnData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{\"Result\": \"Data not found\"}";
	}
	
	public List<Top4> getTop4Records(String filterField1, String filterValue1, String filterField2, String totalOf, int minYear, int minMonth, int maxYear, int maxMonth) {

		session = HibernateUtil.getSessionFactory().openSession();
		StringBuilder genQuery = new StringBuilder();
		StringBuilder whereClause = new StringBuilder();
		genQuery.append("WITH orderByPlantType AS (select plantType, sum(").append(totalOf).append(") as total, ROW_NUMBER() OVER (ORDER BY sum(").append(totalOf).append(") desc) AS RowNumber ")
				.append("from dbo.facGenEmWaterView ")
				.append(" where (");
		whereClause.append(" (( genYear = :minYear and genMonth >= :minMonth) OR") 
			.append(" (genYear > :minYear and genYear < :maxYear)" )
			.append(" OR (genYear = :maxYear and genMonth <= :maxMonth))) group by plantType)" )
			.append("(SELECT plantType, total, RowNumber as ranking FROM orderByPlantType where RowNumber <5) ")
			.append("union (SELECT 'allOthers', Sum (total) as total, 5 as ranking FROM orderByPlantType WHERE RowNumber >4)");
		
		if(!filterField1.equals("")) {
			genQuery.append(filterField1).append(" LIKE :value and").append(whereClause);
		}
		else {
			genQuery.append(whereClause);
		}
		
		Query query = session.createSQLQuery(genQuery.toString());
		if(!filterField1.equals("")) {
			query.setParameter("value", filterValue1);
		}
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		query.setParameter("minMonth", minMonth);
		query.setParameter("maxMonth", maxMonth);
	
		List<Object[]> results = (List<Object[]>)query.list();
		System.out.println("output list size = " + results.size());
		
		List<Top4> listOfTop4 = new ArrayList<Top4>();
		for(Object[] arr : results) {
			Top4 obj = new Top4(); 
		    obj.setPlantType(String.valueOf((arr[0])));
		    if(arr[1] instanceof Number) {
		    	 obj.setTotal(formatter.format((Double)arr[1]));
		    }
		    else {
		    	obj.setTotal("0");
		    }
		    if(arr[2] instanceof Number) {
		    	obj.setRank(((BigInteger)arr[2]).intValue());
		    }
		    else {
		    	obj.setRank(0);
		    }
		    listOfTop4.add(obj);
		}
		return listOfTop4;
	}

	@Override
	public String processWaterAvailabilityFile(String fileName, int startYear, int endYear) {

		BufferedReader reader;
		FileWriter outputfile;
		String inputFileName = "C:\\Users\\epa\\Documents\\historic_monthly\\" + fileName + ".TXT";
		
		try {
			reader = new BufferedReader(new FileReader(inputFileName));
			outputfile = new FileWriter("out.csv");
			CSVWriter writer = new CSVWriter(outputfile); 
			
			String line = reader.readLine();
			String[] header = line.split("\\s*,\\s*");
	        writer.writeNext(header);
	        
	        line = reader.readLine();
	        
			while (line != null) {
				String [] data = line.split("\\s*,\\s*");
				int year  = Integer.parseInt(data[1]);
				if((year >= startYear) && (year <= endYear)) {
					writer.writeNext(data); 
				}
				// read next line
				line = reader.readLine();
			}
			reader.close();
			writer.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "{\"Result\": \"Done\"}";
	}
}
