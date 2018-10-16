package com.epa.dataCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epa.beans.EWEDataReturn;
import com.epa.beans.Facility.FacilityInfo;
import com.epa.util.EPAConstants;
import com.epa.util.HibernateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EwedApiServiceImpl implements EwedApiService {

	@Autowired
	ObjectMapper mapper;
	
	public Session session;
	
	@Override
	public String getFacility(String filterField, String filterValue, String matchLevel, int minYear, int maxYear) {
		
		session = HibernateUtil.getSessionFactory().openSession();
		StringBuilder facilityQuery = new StringBuilder();
		
		facilityQuery.append("from FacilityInfo where ").append(filterField).append(" LIKE :").append(filterField);
		
		Query query = session.createQuery(facilityQuery.toString());
		
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
		
		Query query = session.createQuery("SELECT sum(genSum) from GenerationPerRegistryIdView g"
				+ " where g.genViewKey.registryId in (:ids) and genYear >= :minYear and genYear <= :maxYear");
		
		query.setParameterList("ids", registryIds);
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		//List<Object[]> listResult = query.list();  used for queries returning multiple columns that are not mapped to any object
		List<String> listResult = query.list();
		
		returnData.generation = listResult.get(0);
		
		query = session.createQuery("SELECT sum(emissionAmount) from EmissionsMonthly e"
					+ " where e.emissionsMonthlyKey.registryId in (:ids) and emYear >= :minYear and emYear <= :maxYear");
		
		query.setParameterList("ids", registryIds);
		query.setParameter("minYear", minYear);
		query.setParameter("maxYear", maxYear);
		listResult.clear();
		listResult = query.list();
		
		returnData.emission = listResult.get(0);
		
		query = session.createQuery("SELECT sum(usageSum) from WaterUsagePerRegView w"
				+ " where w.waterViewKey.registryId in (:ids) and usageYear >= :minYear and usageYear <= :maxYear");
	
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

}
