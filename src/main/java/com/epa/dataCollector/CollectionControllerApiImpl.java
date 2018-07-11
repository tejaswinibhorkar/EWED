package com.epa.dataCollector;

import static com.epa.util.EPAConstants.dbNameMap;
import static com.epa.util.EPAConstants.emissionsIdentifier;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.Arrays;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epa.beans.CountObject;
import com.epa.beans.ObjectList;
import com.epa.beans.EIAGeneration.GenerationSeries;
import com.epa.beans.EIAGeneration.PlantGeneration;
import com.epa.beans.EISEmission.Pollutant;
import com.epa.beans.Facility.Facility;
import com.epa.beans.GHGEmissions.Emissions;
import com.epa.beans.GHGEmissions.EmissionsData;
import com.epa.beans.GHGEmissions.GasInfo;
import com.epa.util.EPAConstants;
import com.epa.util.EnviroFactsUtil;
import com.epa.util.HibernateUtil;

@Service
public class CollectionControllerApiImpl {

	private static RestTemplate restTemplate = EnviroFactsUtil.getRestTemplate();
	private static ObjectList<Facility> facList = new ObjectList<Facility>();
	private static ObjectList<Emissions> emissionsList = new ObjectList<Emissions>();
	private static ObjectList<Pollutant> pollutantsList = new ObjectList<Pollutant>();
	private static ObjectList<GasInfo> gasInfoList = new ObjectList<GasInfo>();
	
	private static Map<String, Emissions> emissionsMap = new HashMap<String, Emissions>();
	private static Map<String, GenerationSeries> generationMap = new HashMap<String, GenerationSeries>();
	
	public String initiateCollectionImpl(String dbName, String returnFormat, String rowStart, String rowEnd,
						String filterField, String filterValue,  boolean clearAndAdd ) {
		
		String rowParam = EPAConstants.enviroFactsRowSpecifier + rowStart + ":" + rowEnd;
			
		StringBuilder urlBuilder = new StringBuilder();
		
		System.out.println("DB Name - " + dbName);
		
		if(!filterField.equals("") && !filterValue.equals("")) {
			urlBuilder.append(EPAConstants.enviroFactsBaseURL).append(dbNameMap.get(dbName.toUpperCase())).append("/").append(filterField)
			.append("/").append(filterValue).append("/").append(EPAConstants.additionalConstant).append(returnFormat).append(rowParam);
		} else {
			urlBuilder.append(EPAConstants.enviroFactsBaseURL).append(dbNameMap.get(dbName.toUpperCase())).append("/")
			.append(EPAConstants.additionalConstant).append(returnFormat).append(rowParam);
		}
		
	//	Gson gson = new GsonBuilder().create();
	
		switch(dbName.toUpperCase()) {
		
			case EPAConstants.facIdIdentifier:
				
				//http://localhost:8080/initiateCollection/facid/json/5/10?filterField=naics_code&filterValue=221111&clearAndAdd=true
				
				urlBuilder.replace(urlBuilder.indexOf(EPAConstants.additionalConstant), urlBuilder.indexOf(EPAConstants.additionalConstant)
						+ EPAConstants.additionalConstant.length(), "");
				System.out.println("Fac id calling " + urlBuilder.toString());
	
			    Facility[] facility = restTemplate.getForObject(urlBuilder.toString(), Facility[].class);
				if(!clearAndAdd)
					facList.objectList.clear();
				
				for(Facility fac : facility) {
					facList.objectList.add(fac);
				}
				
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
			        session.save(facility[0]);
			        tx.commit();
				 } catch (HibernateException e) {
				 	if (tx!=null) tx.rollback();
				 		e.printStackTrace(); 
				 } finally {
					 session.close();
				 }
				
				return facList.toString();
	
				
			case emissionsIdentifier:
//				System.out.println("Entering emissions");
			    
				//http://localhost:8080/initiateCollection/emissions/json/0/0?filterField=year&filterValue=2016
				
				urlBuilder.replace(urlBuilder.indexOf(EPAConstants.additionalConstant), urlBuilder.indexOf(EPAConstants.additionalConstant)
						+ EPAConstants.additionalConstant.length(), "naics_code/BEGINNING/2211/PUB_FACTS_SECTOR_GHG_EMISSION/sector_id/3/");
				
			    System.out.println(urlBuilder.toString());
			    
				Emissions[] emissions = restTemplate.getForObject(urlBuilder.toString(), Emissions[].class);
				
				if(!clearAndAdd)
					emissionsList.objectList.clear();
				
				for(Emissions em : emissions) {
//					System.out.println(em + " ");
					if(em.getEmissions() != null) {
						emissionsList.objectList.add(em);
						emissionsMap.put(em.getFacId(), em);
					}
				}
				
//				return emissions[0].toString();
				return emissionsList.toString();
				/*ResponseEntity<String> responseEntity = restTemplate.getForEntity(urlBuilder.toString(),String.class);
				Emissions[] response = gson.fromJson(responseEntity.toString() , Emissions[].class);
				
				return response.toString();*/
				
		}
			
		return EPAConstants.genericErrorReturn;
	}
	
	public GenerationSeries getGenerationData(String plantCode) {
	
		StringBuilder urlBuilder = new StringBuilder();

		urlBuilder.append(EPAConstants.eiaBaseURL).append(EPAConstants.eiaSeriesHead).append(plantCode).append(EPAConstants.eiaSeriesTail);
	//	System.out.println(urlBuilder.toString());
		GenerationSeries plant = restTemplate.getForObject(urlBuilder.toString() , GenerationSeries.class);
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
	        session.save(plant.getSeries()[0]);
	        tx.commit();
		 } catch (HibernateException e) {
		 	if (tx!=null) tx.rollback();
		 		e.printStackTrace(); 
		 } finally {
			 session.close();
		 }
        
		return plant;
	}
	
	public String getPollutantInfoImpl(String pollutantCode) {
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(EPAConstants.enviroFactsBaseURL).append("pollutant");
		
		if(!pollutantCode.equals(""))
			urlBuilder.append(EPAConstants.enviroFactsBaseURL).append("/pollutant/pollutant_code/").append(pollutantCode);
		
		urlBuilder.append("/json");
		
		System.out.println(urlBuilder.toString());
		
		Pollutant[] pollutant = restTemplate.getForObject(urlBuilder.toString() , Pollutant[].class);
		
		for(Pollutant p : pollutant)
			pollutantsList.objectList.add(p);
		
		return pollutantsList.toString();
	}
	
	public String getGreenhouseGasInfo(String gasId) {
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(EPAConstants.enviroFactsBaseURL).append("PUB_DIM_GHG");
		
		if(!gasId.equals(""))
			urlBuilder.append("/GAS_ID/").append(gasId);
		
		urlBuilder.append("/json");
		
		System.out.println(urlBuilder.toString());
		
		GasInfo[] gases = restTemplate.getForObject(urlBuilder.toString() , GasInfo[].class);
		
		for(GasInfo g : gases)
			gasInfoList.objectList.add(g);
		
		return gasInfoList.toString();
	}
	
	public int getCount(String tableName) {
		
		CountObject[] count = restTemplate.getForObject(EPAConstants.enviroFactsBaseURL
				+ tableName + "/json/count" , CountObject[].class);
		
		return count[0].count;
	}
	
	public String getData() {
			
		StringBuilder finalResult = new StringBuilder();
		
		getGreenhouseGasInfo("");
		
		for(int i=4000 ; i<5000; i+=50) {
			
			try {
				initiateCollectionImpl("facid", "json", ""+i, ""+(i+49), "NAICS_CODE", "BEGINNING/2211", true);
				initiateCollectionImpl("emissions", "json", ""+i, ""+(i+49), "year", "2016", true);
			} catch (Exception e) {
				System.out.println("Exception caught -->  " + e.getStackTrace());
				i -= 50;
				continue;
//				initiateCollectionImpl("facid", "json", ""+i, ""+(i+49), "NAICS_CODE", "BEGINNING/2211", true);
//				initiateCollectionImpl("emissions", "json", ""+i, ""+(i+49), "year", "2016", true);
			}
		}
		//initiateCollectionImpl("facid", "json", "0", "39542", "", "", true);
		
		int emissionsExists = 0, generationExists = 0, both = 0;
		boolean emi = false, gen = false;
		
		for(Facility fac : facList.objectList) {
			if(EnviroFactsUtil.isNumeric(fac.getFacId())) {
				System.out.println("Running fac id - " + fac.getFacId() + " generation -");
				generationMap.put(fac.getFacId(), (getGenerationData(fac.getFacId())));
			}
			
			emi = false; gen = false;
			
			if(emissionsMap.containsKey(fac.getRegistryId())) {
				emi = true;
				emissionsExists++;
			}
			
			if(generationMap.containsKey(fac.getFacId()) && generationMap.get(fac.getFacId()).getSeries() != null) {
				gen = true;
				System.out.println(generationMap.get(fac.getFacId()));
				generationExists++;
			}
			
			if(emi && gen)
				both++;
			
//			initiateCollectionImpl("emissions", "json", "0", "0", "frs_id", fac.getRegistryId(), true);
		}
		
		for(Emissions em : emissionsList.objectList) {
			for(EmissionsData data : em.getEmissions()) {
				if(data.getGasId() != null) {
//					System.out.println("Getting info for " + data.getGasId());
					data.setGasInfo(gasInfoList.objectList.get(Integer.parseInt(data.getGasId())));
				}
			}
			finalResult.append(em).append("  ");
		}
		
		System.out.println("Emissions exists = " + emissionsExists + "  Generation exists = " +  generationExists + "  both = " + both);
		return finalResult.toString();
	}
	
	public boolean clearLists() {
		facList.objectList.clear();
		emissionsList.objectList.clear();
		pollutantsList.objectList.clear();
		gasInfoList.objectList.clear();
	
		return true;
	}

	public String getAllGeneration() {
		
		System.out.println("Started....");
		int gen = 0, fac = 0, both = 0;
		
		for(int i = 0; i < 70000; i++) {
			GenerationSeries temp = getGenerationData(""+i);
			//System.out.println(temp);
			boolean gotFac = false, gotGen = false;
			
			if(temp.getSeries() != null) {
				gen++;
				gotGen = true;
			}
//				generationMap.put(""+i, temp);
			Facility[] facility = null;
		
			while(!gotFac) {
				try {
					facility = restTemplate.getForObject("https://iaspub.epa.gov/enviro/efservice/T_FRS_NAICS_EZ/PGM_SYS_ID/"+i+"/json/rows/0:0", Facility[].class);
					gotFac = true;
				} catch(Exception e) {
					gotFac = false;
				}
			}
				
			if(!Arrays.isNullOrEmpty(facility)) {
				//System.out.println(facility[0].toString());
				
				fac++;
				if(gotGen)
					both++;
			}
			temp = null;
		}
		
		return "Gen = " + gen + " Fac = " + fac + " Both = " + both;
	}
}
