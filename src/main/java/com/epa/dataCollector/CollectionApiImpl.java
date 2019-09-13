package com.epa.dataCollector;

import static com.epa.util.EPAConstants.dbNameMap;
import static com.epa.util.EPAConstants.emissionsIdentifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.epa.beans.EIAGeneration.CompositeKeyForDominantType;
import com.epa.beans.EIAGeneration.DominantPlantType;
import com.epa.beans.EIAGeneration.GenerationRow;
import com.epa.beans.EIAGeneration.GenerationSeries;
import com.epa.beans.EIAGeneration.KeyItems;
import com.epa.beans.EIAGeneration.PlantGeneration;
import com.epa.beans.EISEmission.Pollutant;
import com.epa.beans.Facility.Facility860;
import com.epa.beans.GHGEmissions.Emissions;
import com.epa.beans.GHGEmissions.EmissionsData;
import com.epa.beans.GHGEmissions.EmissionsKey;
import com.epa.beans.GHGEmissions.EmissionsRow;
import com.epa.beans.GHGEmissions.GasInfo;
import com.epa.util.EPAConstants;
import com.epa.util.EnviroFactsUtil;
import com.epa.util.ExcelOperations;
import com.epa.util.HibernateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CollectionApiImpl implements CollectionApiService{

	private static RestTemplate restTemplate = EnviroFactsUtil.getRestTemplate();
	private static ObjectList<Facility860> facList = new ObjectList<Facility860>();
	private static ObjectList<Emissions> emissionsList = new ObjectList<Emissions>();
	private static ObjectList<Pollutant> pollutantsList = new ObjectList<Pollutant>();
	private static ObjectList<GasInfo> gasInfoList = new ObjectList<GasInfo>();
	
	private static Map<String, Emissions> emissionsMap = new HashMap<String, Emissions>();
	private static Map<String, GenerationSeries> generationMap = new HashMap<String, GenerationSeries>();
	
	private static Map<String, Map<Integer, String>> PlantdominantTypeMap = new HashMap<String, Map<Integer, String>>();
	private static String [] choices = new String[] 
							{"AB-ST", "BFG-OT", "BFG-ST", "BIT-GT", "BIT-ST", "BLQ-ST", "DFO-CA", "DFO-CT", "DFO-GT", "DFO-HY", 
							 "DFO-IC", "DFO-ST", "GEO-BT", "GEO-ST", "JF-GT", "JF-IC", "KER-GT", "LFG-CT", "LFG-FC", "LFG-GT", 
							 "LFG-IC", "LFG-ST", "LIG-ST", "MSB-ST" , "MWH-BA", "MWH-FW", "NG-CA", "NG-CS"," NG-CT","NG-FC",
							 "NG-GT","NG-IC","NG-ST","No-Gen-No-Gen","No-Gen-No-Gen","NUC-ST","OBG-CT", "OBG-FC", "OBG-GT",
							 "OBG-IC","OBG-ST","OBL-GT","OBS-ST","OG-GT","OG-IC","OG-ST","OTH-GT","OTH-OT","OTH-ST","PC-OT",
							 "PC-ST","PUR-ST","RC-ST","RFO-CT","RFO-ST","SGC-CA","SGC-CT","SGP-CT","SLW-ST","SUB-ST","SUN-CP","SUN-PV","SUN-ST",
							 "TDF-ST", "WAT-HY","WAT-PS","WC-ST","WDL-ST","WDS-OT","WDS-ST","WH-OT","WH-ST","WND-WS","WND-WT","WO-CT","WO-CT","WO-GT", "WO-GT"
							 };
	private static ArrayList<String> allPlantTypes = new ArrayList<String>(Arrays.asList(choices));
	
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
		
		switch(dbName.toUpperCase()) {
		
		case EPAConstants.facIdIdentifier:
			
			//http://localhost:8080/initiateCollection/facid/json/5/10?filterField=naics_code&filterValue=221111&clearAndAdd=true
			
			urlBuilder.replace(urlBuilder.indexOf(EPAConstants.additionalConstant), urlBuilder.indexOf(EPAConstants.additionalConstant)
					+ EPAConstants.additionalConstant.length(), "");
			System.out.println("Fac id calling " + urlBuilder.toString());

		    Facility860[] facility = restTemplate.getForObject(urlBuilder.toString(), Facility860[].class);
		    
			if(!clearAndAdd)
				facList.objectList.clear();
			
			for(Facility860 fac : facility) {
				if(!facList.objectList.contains(fac))
					facList.objectList.add(fac);
			}
			
					
			return facList.toString();
	
			case emissionsIdentifier:
				//http://localhost:8080/initiateCollection/emissions/json/0/0?filterField=year&filterValue=2016
				urlBuilder.replace(urlBuilder.indexOf(EPAConstants.additionalConstant), urlBuilder.indexOf(EPAConstants.additionalConstant)
						+ EPAConstants.additionalConstant.length(), EPAConstants.emissionJoinURL);
				
			    System.out.println(urlBuilder.toString());
			    
				Emissions[] emissions = restTemplate.getForObject(urlBuilder.toString(), Emissions[].class);
				
				
				if(!clearAndAdd)
					emissionsList.objectList.clear();
				
				for(Emissions em : emissions) {
					if(em.getEmissions() != null) {
						emissionsList.objectList.add(em);
						emissionsMap.put(em.getLocalFacId(), em);
					} else {
						System.out.println("Emissions is null, moving on");
					}
				}
				
				return emissionsList.toString();
		}
			
		return EPAConstants.genericErrorReturn;
	}
	
	public GenerationSeries getGenerationData(String plantCode) {
	
		StringBuilder urlBuilder = new StringBuilder();

		urlBuilder.append(EPAConstants.eiaBaseURL).append(EPAConstants.eiaSeriesHead).append(plantCode).append(EPAConstants.eiaSeriesTail);
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
		ArrayList<Facility860> newFacilityList = new ArrayList<Facility860>();
		
		for(int i=0 ; i<50850; i+=50) {
			
			try {
				initiateCollectionImpl("facid", "json", ""+i, ""+(i+49), "", "", true);
			} catch (Exception e) {
				System.out.println("Exception caught -->  " + e.getStackTrace());
				i -= 50;
			}
		}
		
		for(Facility860 fac : facList.getObjectList()) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				if(session.get(Facility860.class, fac.getPgmSysId()) == null) {
					System.out.print("NEW FACILITY = " + fac.getPgmSysId());
					newFacilityList.add(fac);
				
					System.out.println("Inserting ---- " + fac.toString());
					//session.saveOrUpdate(fac);
					session.save(fac);
					session.flush();
					session.refresh(fac);
					
			        tx.commit();
				}
				else {
					System.out.println("NO new fac");
				}
			 } catch (HibernateException e) {
			 	if (tx!=null) tx.rollback();
			 		e.printStackTrace(); 
			 } finally {
				 session.close();
			 }
			
		}
		
		if(newFacilityList.size()>0) {
			// Create an excel file of newly added facilities
			ExcelOperations excelOp = new ExcelOperations();
			excelOp.writeToExcel(newFacilityList);
		}
		
		return "Done";
	}
	
	public boolean clearLists() {
		facList.objectList.clear();
		emissionsList.objectList.clear();
		pollutantsList.objectList.clear();
		gasInfoList.objectList.clear();
	
		return true;
	}
	
	public String getAllGeneration() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		int genFound = 0, genNotFound = 0;
		
		try {
			Query query = session.createQuery("select distinct pgmSysId from Facility");
			List<String> list = query.list();
			
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
				} else {
					genNotFound++;
				}
				
				
				
			}
			
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
				
			    Facility860[] facility = restTemplate.getForObject(url, Facility860[].class);
			    
			    for(Facility860 fac : facility) {
					if(!facList.objectList.contains(fac))
						facList.objectList.add(fac);
				}
			    
			} catch (Exception e) {
				System.out.println("Exception caught -->  " + e.getStackTrace());
				i -= 50;
				continue;
			}
		}
	    
	    for (Facility860 fac : facList.objectList) {
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
		Query query = session.createQuery("select localFacId, ORISCode from LocalFacIdToORISCodeView");
		
		 List<Object[]> ids= (List<Object[]>)query.list();
		 
		 System.out.println("localFac Id list size:  " + ids.size());
		 
		 int breaker=0, ctr=0, objectCtr=0, errorCnt=0;
		 
	     for(Object[] id: ids){
	    	 String localFacId = (String) id[0];
	    	 String ORISCode = (String) id[1];
	    	 System.out.println(localFacId + " " + ORISCode);
	    	 
	    	 breaker++;
				try {
					initiateCollectionImpl("emissions", "json", null, null, "FACILITY_ID", localFacId, true);

					ctr++;
					System.out.println(ctr + " / " + ids.size() + " done.. Error count = " + errorCnt);
				} catch (Exception e) {
					initiateCollectionImpl("emissions", "json", null, null, "FACILITY_ID", localFacId, true);
					ctr++;
				}
				
				while(objectCtr < emissionsList.objectList.size()) {
					
					Emissions emission = emissionsList.objectList.get(objectCtr);
						if(emission != null) {
						System.out.println(emission.toString());
						EmissionsRow row = new EmissionsRow();
						EmissionsKey key = new EmissionsKey();
						key.setORISCode(ORISCode);
						key.setEmYear(emission.getYear());
						
						row.setLocalFacId(localFacId);
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
	
	//Get Dominant Plant Type of the Facility
	
		
			


	public String getAllDominantType() {
				
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("select distinct pgmSysId from Facility");
		List<String> plantCodes = query.list();
		
		int size = plantCodes.size();
		System.out.println("Total plantcodes= " + plantCodes.size());
		
		//	System.out.println("plantcode at index 1957 " + plantCodes.get(1957));
		// int count = 0;
		
	   // for(String plantCode: plantCodes) {
		for(int i=4000;i< plantCodes.size(); i++) {
			String plantCode = plantCodes.get(i);
			System.out.println( "Plancode = " + plantCode);
			//System.out.println(size--);
			
			Map<Integer, String> yearTypeMap = new HashMap<Integer, String>();
			Map<Integer, Double>maxYearWiseEnergyMap = new HashMap<Integer, Double>();
	
			
			for(String plantType: allPlantTypes) {
				PlantGeneration[] plantGenerations = getPlantGenerationPlantTypeWise(plantCode, plantType);
				if(plantGenerations != null) {
					for(PlantGeneration plantGen: plantGenerations) {
		
						String [][] data = plantGen.getData();
						for(String[] s: data) {
							int year = Integer.parseInt(s[0]);
							double energy = Double.parseDouble(s[1]);
							
							if((maxYearWiseEnergyMap.containsKey(year) && maxYearWiseEnergyMap.get(year) < energy) || !maxYearWiseEnergyMap.containsKey(year)) {
						
								maxYearWiseEnergyMap.put(year, energy);
								yearTypeMap.put(year, plantType);
							}	
						}
					}
				}
			}
			
			saveDominantTypeToDB(plantCode, yearTypeMap);
			PlantdominantTypeMap.put(plantCode, yearTypeMap);
			/*count++;
			
			if(count == 2000) {
				System.out.println("*********************************************************************************");
				System.out.println("2000 RECORDS PROCESSED!!!!!! breaking the looopppp");
				System.out.println("Last plant code processed:  "  + plantCode);
				System.out.println("*********************************************************************************");
				break;
			} 
			*/
		}
		
		System.out.println("Final Map: " + PlantdominantTypeMap);
		return PlantdominantTypeMap.toString(); 
	}
			
	public PlantGeneration[] getPlantGenerationPlantTypeWise(String plantCode, String plantType){
				
		PlantGeneration[] plantGeneration = null;
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(EPAConstants.eiaBaseURL).append(EPAConstants.eiaSeriesHead).append(plantCode).append("-" + plantType + ".A");
		System.out.println(urlBuilder.toString());
		
		RestTemplate rst = new RestTemplate();
		String result = rst.getForObject(urlBuilder.toString(), String.class);

		ObjectMapper mapper = new ObjectMapper();
		GenerationSeries plant=null;
		try {
			 plant = mapper.readValue(result, GenerationSeries.class);
			 if(plant.getSeries() != null) {
				 System.out.println(result);
				 plantGeneration = plant.getSeries();
			 }
		} catch (IOException e) {
			}
		
		return plantGeneration;
	}
			
	public String saveDominantTypeToDB(String plantCode, Map<Integer, String> yearTypeMap) {
				
		//populate data
		//getAllDominantType();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			for(int year: yearTypeMap.keySet()) {
				String dType = yearTypeMap.get(year);
				DominantPlantType dominantType = new DominantPlantType(new CompositeKeyForDominantType(plantCode, year), dType);
				
				session.saveOrUpdate(dominantType);
			}
			
	        tx.commit();
		 } catch (HibernateException e) {
		 	if (tx!=null) tx.rollback();
		 		e.printStackTrace(); 
		 } finally {
			 session.close();
		 }
		return "Done";
		
	}
}
