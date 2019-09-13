package com.epa.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.epa.beans.EIAGeneration.DominantPlantType;
import com.epa.beans.EIAGeneration.GenerationPerRegistryIdView;
import com.epa.beans.EIAGeneration.GenerationRow;
import com.epa.beans.Facility.Facility;
import com.epa.beans.Facility.Facility860;
import com.epa.beans.Facility.FacilityInfo;
import com.epa.beans.GHGEmissions.EmissionsMonthly;
import com.epa.beans.GHGEmissions.EmissionsRow;
import com.epa.beans.GHGEmissions.GasInfo;
import com.epa.beans.GHGEmissions.LocalFacIdToORISCodeView;
import com.epa.beans.WaterUsage.WaterAvailability;
import com.epa.beans.WaterUsage.WaterUsage;
import com.epa.beans.WaterUsage.WaterUsagePerRegView;
import com.epa.views.DefaultOutputJson;
import com.epa.views.GenEmWaterView;
 
public class HibernateUtil {
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	
	/**
	 * IMP - ALWAYS ADD A CLASS WHEN MAPPING NEW TABLE OR VIEW
	 * 
	 * @return SessionFactory object which is required to do all 
	 * operations using hibernate.
	 */
    private static SessionFactory buildSessionFactory() {

		 try {
			 sessionFactory = new Configuration().
	                  configure("/hibernate.cfg.xml").
	                  //addPackage("com.xyz") //add package if used.
	                  addAnnotatedClass(GenerationRow.class).
	                  addAnnotatedClass(Facility.class).
	                  addAnnotatedClass(GasInfo.class).
	                  addAnnotatedClass(EmissionsRow.class).
	                  addAnnotatedClass(FacilityInfo.class).
	                  addAnnotatedClass(GenerationPerRegistryIdView.class).
	                  addAnnotatedClass(EmissionsMonthly.class).
	                  addAnnotatedClass(WaterUsage.class).
	                  addAnnotatedClass(WaterUsagePerRegView.class).
	                  addAnnotatedClass(DominantPlantType.class).
	                  addAnnotatedClass(LocalFacIdToORISCodeView.class).
	                  addAnnotatedClass(GenEmWaterView.class). 
	                  addAnnotatedClass(DefaultOutputJson.class).
	                  addAnnotatedClass(WaterAvailability.class).
	                  addAnnotatedClass(Facility860.class).
	                  buildSessionFactory();
	     } catch (Throwable ex) { 
	        System.err.println("Failed to create sessionFactory object." + ex);
	        throw new ExceptionInInitializerError(ex); 
	     }
	 
	 return sessionFactory;
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
    
}