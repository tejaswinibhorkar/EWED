package com.epa.beans;

/**
 * This transient object only materializes to ensure that 
 * the form and type of json returned for facility summary
 * remains consistent. This ensures that the front end does
 * not have to parse same types of jsons in different ways.
 */
public class EWEDataReturn {

	public int participatingFacilities;
	public String generation;
	public String emission;
	public String waterUsage;
}
