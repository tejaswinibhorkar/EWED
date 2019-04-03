package com.epa.dataCollector;

import static com.epa.util.EPAConstants.dbNameMap;
import static com.epa.util.EPAConstants.emissionsIdentifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.epa.beans.CountObject;
import com.epa.beans.ObjectList;
import com.epa.beans.EIAGeneration.GenerationRow;
import com.epa.beans.EIAGeneration.GenerationSeries;
import com.epa.beans.EIAGeneration.KeyItems;
import com.epa.beans.EIAGeneration.PlantGeneration;
import com.epa.beans.EISEmission.Pollutant;
import com.epa.beans.Facility.Facility;
import com.epa.beans.GHGEmissions.Emissions;
import com.epa.beans.GHGEmissions.EmissionsData;
import com.epa.beans.GHGEmissions.EmissionsKey;
import com.epa.beans.GHGEmissions.EmissionsRow;
import com.epa.beans.GHGEmissions.GasInfo;
import com.epa.util.EPAConstants;
import com.epa.util.EnviroFactsUtil;
import com.epa.util.HibernateUtil;

@Service
public class CollectionApiImpl implements CollectionApiService{

	private static RestTemplate restTemplate = EnviroFactsUtil.getRestTemplate();
	private static ObjectList<Facility> facList = new ObjectList<Facility>();
	private static ObjectList<Emissions> emissionsList = new ObjectList<Emissions>();
	private static ObjectList<Pollutant> pollutantsList = new ObjectList<Pollutant>();
	private static ObjectList<GasInfo> gasInfoList = new ObjectList<GasInfo>();
	
	private static Map<String, Emissions> emissionsMap = new HashMap<String, Emissions>();
	private static Map<String, GenerationSeries> generationMap = new HashMap<String, GenerationSeries>();
	
	public String initiateCollectionImpl(String dbName, String returnFormat, String rowStart, String rowEnd,
						String filterField, String filterValue,  boolean clearAndAdd ) {
		
		String rowParam = "";
		if(rowStart != null && rowEnd != null)
			rowParam = EPAConstants.enviroFactsRowSpecifier + rowStart + ":" + rowEnd;
			
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
					if(!facList.objectList.contains(fac))
						facList.objectList.add(fac);
				}
				
				/*Session session = HibernateUtil.getSessionFactory().openSession();
				
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					for(int i = 0; i <= Integer.parseInt(rowEnd) - Integer.parseInt(rowStart); i++)
						session.save(facility[i]);
					
			        tx.commit();
				 } catch (HibernateException e) {
				 	if (tx!=null) tx.rollback();
				 		e.printStackTrace(); 
				 } finally {
					 session.close();
				 }*/
				
				return facList.toString();
	
			case emissionsIdentifier:
//				System.out.println("Entering emissions");
			    
				//http://localhost:8080/initiateCollection/emissions/json/0/0?filterField=year&filterValue=2016
				urlBuilder.replace(urlBuilder.indexOf(EPAConstants.additionalConstant), urlBuilder.indexOf(EPAConstants.additionalConstant)
						+ EPAConstants.additionalConstant.length(), EPAConstants.emissionJoinURL);
				
			    System.out.println(urlBuilder.toString());
			    
				Emissions[] emissions = restTemplate.getForObject(urlBuilder.toString(), Emissions[].class);
				
				
				if(!clearAndAdd)
					emissionsList.objectList.clear();
				
				for(Emissions em : emissions) {
//					System.out.println(em.getFrsId());
					if(em.getEmissions() != null) {
						emissionsList.objectList.add(em);
						emissionsMap.put(em.getFrsId(), em);
					} else {
						System.out.println("Emissions is null, moving on");
					}
				}
				
//				return emissions[0].toString();
//				System.out.println("returning - "+emissionsList.toString());
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
		plant.setPlantCode(plantCode);
		return plant;
	}
	
	@Deprecated
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
		
		for(GasInfo gasInfoObject : gases) {
			gasInfoList.objectList.add(gasInfoObject);
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				System.out.println("Inserting ---- " + gasInfoObject.toString());
				session.save(gasInfoObject);
				
		        tx.commit();
			 } catch (HibernateException e) {
			 	if (tx!=null) tx.rollback();
			 		e.printStackTrace(); 
			 } finally {
				 session.close();
			 }
		}
		
		return gasInfoList.toString() + "Stored";
	}
	
	public int getCount(String tableName) {
		
		CountObject[] count = restTemplate.getForObject(EPAConstants.enviroFactsBaseURL
				+ tableName + "/json/count" , CountObject[].class);
		
		return count[0].count;
	}
	
	public String getData() {
			
		StringBuilder finalResult = new StringBuilder();
		
//		getGreenhouseGasInfo("");
		
		for(int i=0 ; i<50850; i+=50) {
			
			try {
				initiateCollectionImpl("facid", "json", ""+i, ""+(i+49), "", "", true);
				//initiateCollectionImpl("emissions", "json", ""+i, ""+(i+49), "year", "2016", true);
			} catch (Exception e) {
				System.out.println("Exception caught -->  " + e.getStackTrace());
				i -= 50;
				continue;
//				initiateCollectionImpl("facid", "json", ""+i, ""+(i+49), "NAICS_CODE", "BEGINNING/2211", true);
//				initiateCollectionImpl("emissions", "json", ""+i, ""+(i+49), "year", "2016", true);
			}
		}
		
		for(Facility fac : facList.getObjectList()) {
			
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				System.out.println("Inserting ---- " + fac.toString());
				session.saveOrUpdate(fac);
				
		        tx.commit();
			 } catch (HibernateException e) {
			 	if (tx!=null) tx.rollback();
			 		e.printStackTrace(); 
			 } finally {
				 session.close();
			 }
			
		}
		return "Done";
		//initiateCollectionImpl("facid", "json", "0", "39542", "", "", true);
		/*
		int emissionsExists = 0, generationExists = 0, both = 0;
		boolean emi = false, gen = false;
		
		for(Facility fac : facList.objectList) {
			if(EnviroFactsUtil.isNumeric(fac.getPgmSysId())) {
				System.out.println("Running fac id - " + fac.getPgmSysId() + " generation -");
				generationMap.put(fac.getPgmSysId(), (getGenerationData(fac.getPgmSysId())));
			}
			
			emi = false; gen = false;
			
			if(emissionsMap.containsKey(fac.getRegistryId())) {
				emi = true;
				emissionsExists++;
			}
			
			if(generationMap.containsKey(fac.getPgmSysId()) && generationMap.get(fac.getPgmSysId()).getSeries() != null) {
				gen = true;
				System.out.println(generationMap.get(fac.getPgmSysId()));
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
		return finalResult.toString();  */
	}
	
	public boolean clearLists() {
		facList.objectList.clear();
		emissionsList.objectList.clear();
		pollutantsList.objectList.clear();
		gasInfoList.objectList.clear();
	
		return true;
	}

	/* public String getAllGeneration() {
		
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
	} */
	
	public String getAllGeneration() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		int genFound = 0, genNotFound = 0;
		
		try {
			Query query = session.createQuery("select distinct pgmSysId from Facility");
			List<String> list = query.list();
//			System.out.println(list);
			
			for(String plantCode : list) {
				GenerationSeries plant = getGenerationData(plantCode);  //9255 index gives plant code 2512
				System.out.println(plant);
				
				if(plant.getSeries() != null) {
					
					Transaction tx = null;
					tx = session.beginTransaction();
					PlantGeneration plantGen = ((PlantGeneration) (plant.getSeries()[0]));
					
					for (String[] dataRow : plantGen.getData()) {
						GenerationRow row = new GenerationRow();
						KeyItems keyItems = new KeyItems();
						keyItems.setPlantCode(plant.getPlantCode());
						keyItems.setGenYear(dataRow[0].substring(0, 4));
						keyItems.setGenMonth(dataRow[0].substring(4, 6));
						row.setPlantName(plantGen.getName().split(":")[1].trim());
						// If required to convert month in int to word use - new DateFormatSymbols().getMonths()[Integer.parseInt( dataRow[0].substring(4, 6))-1] 
						row.setKeyTimes(keyItems);
						row.setGenData(dataRow[1]);
						row.setUnits(plantGen.getUnits());
						row.setLatitude(plantGen.getLatitude());
						row.setLongitude(plantGen.getLongitude());
						System.out.println(row);
						
						session.saveOrUpdate(row);
						genFound++;
					}
					
					
	//					session.save(plant.getSeries()[0]);
						tx.commit();
				} else {
					genNotFound++;
					System.out.println("No generation for - " + plant.getPlantCode());
				}
			}
			
		} finally {
		 session.close();
		} 
		
		return "genFound = " + genFound + " genNotFound = " + genNotFound;
	}

	public String getGenerationFromFile() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		int genFound = 0, genNotFound = 0;
		Set<String> foundIds = new HashSet<String>();
		
		try {
			List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\epa\\git\\EWED\\src\\main\\resources\\genIds"));
//			System.out.println(lines);
			System.out.println("Total = " + lines.size());
			
			for(String plantCode : lines) {
				GenerationSeries plant = getGenerationData(plantCode);  //9255 index gives plant code 2512
				
				if(plant.getSeries() != null) {
//					System.out.println("Found - " + plantCode);
					foundIds.add(plantCode);
					
					Transaction tx = null;
					tx = session.beginTransaction();
					PlantGeneration plantGen = ((PlantGeneration) (plant.getSeries()[0]));
					
					for (String[] dataRow : plantGen.getData()) {
						GenerationRow row = new GenerationRow();
						KeyItems keyItems = new KeyItems();
						keyItems.setPlantCode(plant.getPlantCode());
						keyItems.setGenYear(dataRow[0].substring(0, 4));
						keyItems.setGenMonth(dataRow[0].substring(4, 6));
						row.setPlantName(plantGen.getName().split(":")[1].trim());
						// If required to convert month in int to word use - new DateFormatSymbols().getMonths()[Integer.parseInt( dataRow[0].substring(4, 6))-1] 
						row.setKeyTimes(keyItems);
						row.setGenData(dataRow[1]);
						row.setUnits(plantGen.getUnits());
						row.setLatitude(plantGen.getLatitude());
						row.setLongitude(plantGen.getLongitude());
						
						System.out.println(row);
						
						session.save(row);
						genFound++;
					}
					
					tx.commit();
//					genFound++;
				} else {
					genNotFound++;
				}
				
				
				
			}
			
			/*Query query = session.createQuery("select distinct g.keyItems.plantCode from GenerationRow g"); //CreateQuery maps tables as the beans that it corresponds to.
																							//Thus, we mention the mapped bean instead
																								//of the table name that we want to query
			List<String> facList = query.list();
			System.out.println(facList);*/
			
			/*for(int i=0, j=0; i <= facList.size(); i++, j++) {
				
			}*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return "genFound = " + foundIds.size() + " genNotFound = " + genNotFound;
	}

	public String getFacilityfromGen() {
		
		for(int i=0 ; i<7300; i+=50) {
			
			try {
				String url = "https://iaspub.epa.gov/enviro/efservice/frs_program_facility/PGM_SYS_ACRNM/EIA-860/json/rows/" + i + ":" + (i+49);
				System.out.println("Fac id calling " + url);
				
			    Facility[] facility = restTemplate.getForObject(url, Facility[].class);
			    
			    for(Facility fac : facility) {
					if(!facList.objectList.contains(fac))
						facList.objectList.add(fac);
				}
			    
			} catch (Exception e) {
				System.out.println("Exception caught -->  " + e.getStackTrace());
				i -= 50;
				continue;
			}
		}
	    
	    for (Facility fac : facList.objectList) {
	    	Session session = HibernateUtil.getSessionFactory().openSession();
			
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				System.out.println("Inserting ---- " + fac.toString());
				session.save(fac);
				
		        tx.commit();
			 } catch (HibernateException e) {
			 	if (tx!=null) tx.rollback();
			 		e.printStackTrace(); 
			 } finally {
				 session.close();
			 }
	    }

		return "Done";
	}

	public String getEmissions() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("select distinct registryId from Facility");
		List<String> list = query.list();
		
		int breaker=0, ctr=0, objectCtr=0, errorCnt=0;
		for(String registryId : list) {
//			breaker++;
			try {
				initiateCollectionImpl("emissions", "json", null, null, "frs_id", registryId, true);
				ctr++;
				System.out.println(ctr + " / " + list.size() + " done.. Error count = " + errorCnt);
			} catch (Exception e) {
				initiateCollectionImpl("emissions", "json", null, null, "frs_id", registryId, true);
				ctr++;
			}
//			if(breaker==40) break;
		
			
			
//			System.out.println("List now - " + emissionsList.objectList.toString());
	//		for(Emissions emission : emissionsList.objectList) {
			while(objectCtr < emissionsList.objectList.size()) {
				
				Emissions emission = emissionsList.objectList.get(objectCtr);
					if(emission != null) {
					System.out.println(emission.toString());
					EmissionsRow row = new EmissionsRow();
					EmissionsKey key = new EmissionsKey();
					key.setRegistryId(emission.getFrsId());
					key.setEmYear(emission.getYear());
					row.setLatitude(emission.getLatitude());
					row.setLongitude(emission.getLongitude());

					for(EmissionsData emissionData : emission.getEmissions()) {
						if(emission.getEmissions()[0].getFacId() == null) {
							emissionData = emission.getEmissions()[0].getSectorEmissionsRow();  	//Some emissions are reported as a single row called PUB_FACTS_SECTOR_GHG_EMISSION_ROW
						}
						Session session2 = HibernateUtil.getSessionFactory().openSession();
						key.setGasId(emissionData.getGasId());
						row.setEmissionsKey(key);
						row.setSector(Integer.parseInt(emissionData.getSectorId()));
						row.setEmissionAmount(emissionData.getEmission());
						Transaction tx = null;
						try {
							
							if(row.getEmissionAmount() != null) { 	//There are some emission rows with null emissions and gasIds
								tx = session2.beginTransaction();
								System.out.println("Inserting ---- " + row.toString());
								session2.saveOrUpdate(row);
								
						        tx.commit();
							}	
						 } catch (HibernateException e) {
						 	if (tx!=null) tx.rollback();
						 		e.printStackTrace(); 
						 		errorCnt++;
						 } finally {
							 session2.close();
						 }
					}
					objectCtr++;
				}
			}
		}  
		session.close();
		return "Done with errorCnt = " + errorCnt;
	}
	
}
