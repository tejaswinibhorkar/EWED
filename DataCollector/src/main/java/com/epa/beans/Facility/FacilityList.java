package com.epa.beans.Facility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FacilityList {

	public List<Facility> facilities = new ArrayList<Facility>();
	
	public FacilityList() {
		// TODO Auto-generated constructor stub
	}

	public FacilityList(List<Facility> facilities) {
		super();
		this.facilities = facilities;
	}

	public List<Facility> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<Facility> facilities) {
		this.facilities = facilities;
	}

	@Override
	public String toString() {
		return "FacilityList [facilities=" + facilities + "]";
	}
	
}
