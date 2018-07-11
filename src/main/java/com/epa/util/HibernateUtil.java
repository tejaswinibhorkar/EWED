package com.epa.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.epa.beans.EIAGeneration.PlantGeneration;
 
public class HibernateUtil {
	
	private static SessionFactory sessionFactory = buildSessionFactory();
	
    private static SessionFactory buildSessionFactory() {

		 try {
			 sessionFactory = new Configuration().
	                  configure("/hibernate.cfg.xml").
	                  //addPackage("com.xyz") //add package if used.
	                  addAnnotatedClass(PlantGeneration.class).
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
    
 /*   p
 
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