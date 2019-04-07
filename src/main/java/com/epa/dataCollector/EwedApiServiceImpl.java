package com.epa.dataCollector;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.epa.util.EPAConstants;
import com.epa.util.HibernateUtil;
import com.epa.views.GenEmWaterPerPlantCodePerMonthPerYearView;
import com.epa.views.MonthlyFacilityData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EwedApiServiceImpl implements EwedApiService {

	//Object mapper is used to convert objects to jsons
	@Autowired
	ObjectMapper mapper;
	
	public Session session;
	
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
		
		/*Query query = session.createQuery("SELECT g.genViewKey.registryId, sum(genSum) from GenerationPerRegistryIdView g"
				+ " where g.genViewKey.registryId in (:ids) and genYear >= :minYear and genYear <= :maxYear group by g.genViewKey.registryId");
		*/
		
		/*Not the use of g.genViewKey.registryId is done to respect the structure of the
		 * class that accomodates the presence of a composite key.
		 */
		
		
		Query query = session.createQuery("SELECT sum(genSum) from GenerationPerRegistryIdView g"
				+ " where g.genViewKey.registryId in (:ids) and genYear between :minYear and :maxYear");
		
		query.setParameterList("ids", registryIds);
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		//List<Object[]> listResult = query.list();  used for queries returning multiple columns that are not mapped to any object
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
			
		session = HibernateUtil.getSessionFactory().openSession();
		StringBuilder facilityQuery = new StringBuilder();
				
		facilityQuery.append("from Facility where ").append(filterField).append(" LIKE :").append(filterField);
		
		Query query = session.createQuery(facilityQuery.toString());
		query.setParameter(filterField, filterValue);
		
		List<Facility> facList = query.list();
		
		System.out.println("Facilities list size = " + facList.size());
		
		List<String> plantCodes = new  ArrayList<String>();
		for(Facility fac: facList) {
			plantCodes.add(fac.getPgmSysId());
		}
		 List<GenEmWaterPerPlantCodePerMonthPerYearView> gewList = queryGEWView(plantCodes,minYear, minMonth, maxYear, maxMonth);
		 
		 String returnJson = returnData(facList,  gewList);
		 
		return returnJson;
	}
	
	public List<GenEmWaterPerPlantCodePerMonthPerYearView> queryGEWView(List<String> plantCodes, int minYear, int minMonth, int maxYear, int maxMonth) {
		
		session = HibernateUtil.getSessionFactory().openSession();
		
		Query query = session.createQuery("from GenEmWaterPerPlantCodePerMonthPerYearView g"
		+ " where g.genEmWaterKey.plantCode in (:ids) and"
		+ " (( genYear = :minYear and genMonth >= :minMonth) OR" 
		+ " (genYear > :minYear and genYear < :maxYear)" 
		+ " OR (genYear = :maxYear and genMonth <= :maxMonth))" 
		+ " order by plantCode, genYear, genMonth");

		query.setParameterList("ids", plantCodes);
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		query.setParameter("minMonth", minMonth);
		query.setParameter("maxMonth", maxMonth);
		
		System.out.println(query);
		List<GenEmWaterPerPlantCodePerMonthPerYearView> gewList = query.list();
		//System.out.println(gewList);
		
		return gewList;
	
	}
	
	public String returnData(List<Facility> facList, List<GenEmWaterPerPlantCodePerMonthPerYearView> gewList) {
		
		ArrayList<String>returnData = new ArrayList<String>();

		for(Facility fac: facList) {
			List<MonthlyFacilityData> facData = new ArrayList<MonthlyFacilityData>();
			
			String plantCode = fac.getPgmSysId();
			
			List<EWEDMonthlyData> monthlyDataList = new ArrayList<EWEDMonthlyData>();
			
			for(int i=0; i<gewList.size(); i++) {
				GenEmWaterPerPlantCodePerMonthPerYearView data = gewList.get(i);
				
				if(data.getGenEmWaterKey().getPlantCode().equals(plantCode)) {
					EWEDMonthlyData monthlyData = new EWEDMonthlyData();
					monthlyData.year = data.getGenEmWaterKey().getGenYear();
					monthlyData.month = data.getGenEmWaterKey().getGenMonth();
					monthlyData.plantType = data.getPlantType();
					monthlyData.generation = data.getGeneration();
					monthlyData.emissions= data.getEmissions();
					monthlyData.waterWithdrawal= data.getWaterWithdrawal();
					monthlyData.waterConsumption= data.getWaterConsumption();
					
					monthlyDataList.add(monthlyData);
					
				}
			}
				
				MonthlyFacilityData mfd = new MonthlyFacilityData();
				mfd.facility = fac;
				mfd.monthlyDataList = monthlyDataList;
				facData.add(mfd);
				
				try {
					returnData.add(mapper.writeValueAsString(facData));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			} 
		
		return returnData.toString();
	}
}
