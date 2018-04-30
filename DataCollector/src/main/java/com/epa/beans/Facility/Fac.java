package com.epa.beans.Facility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fac {

	@JsonProperty("NAICS_CODE")
	String NAICS_CODE;
	
	public Fac() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Fac(String nAICS_CODE) {
		super();
		NAICS_CODE = nAICS_CODE;
	}

	public String getNAICS_CODE() {
		return NAICS_CODE;
	}

	public void setNAICS_CODE(String nAICS_CODE) {
		NAICS_CODE = nAICS_CODE;
	}

	@Override
	public String toString() {
		return "Fac [NAICS_CODE=" + NAICS_CODE + "]";
	}
	
	
}
