package com.epa.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.epa.beans.EIAGeneration.GenerationPerRegistryIdView;
import com.epa.beans.EIAGeneration.GenerationRow;
import com.epa.beans.Facility.Facility;
import com.epa.beans.Facility.FacilityInfo;
import com.epa.beans.GHGEmissions.EmissionsMonthly;
import com.epa.beans.GHGEmissions.EmissionsRow;
import com.epa.beans.GHGEmissions.GasInfo;
import com.epa.beans.WaterUsage.WaterUsage;
import com.epa.beans.WaterUsage.WaterUsagePerRegView;
 
public class HibernateUtil {
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	
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
    
 /*   
 
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure("C:\\Users\\epa\\git\\EWED\\hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
   */
 
}