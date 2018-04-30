package com.epa.beans.GHGEmissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmissionsData {
	
	@JsonProperty("FACILITY_ID") String facId;
	@JsonProperty("YEAR") String year;
	@JsonProperty("SECTOR_ID") String sectorId;
	@JsonProperty("SUBSECTOR_ID") String subsectorId;
	@JsonProperty("GAS_ID") String gasId;
	@JsonProperty("CO2E_EMISSION") String emission;
	GasInfo gasInfo;
	
	public EmissionsData() {
		// TODO Auto-generated constructor stub
	}

	public EmissionsData(String facId, String year, String sectorId, String subsectorId, String gasId, String unit, GasInfo gasInfo) {
		super();
		this.facId = facId;
		this.year = year;
		this.sectorId = sectorId;
		this.subsectorId = subsectorId;
		this.gasId = gasId;
		this.emission = unit;
		this.gasInfo = gasInfo;
	}

	public String getFacId() {
		return facId;
	}

	public void setFacId(String facId) {
		this.facId = facId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getSubsectorId() {
		return subsectorId;
	}

	public void setSubsectorId(String subsectorId) {
		this.subsectorId = subsectorId;
	}

	public String getGasId() {
		return gasId;
	}

	public void setGasId(String gasId) {
		this.gasId = gasId;
	}

	public String getEmission() {
		return emission;
	}

	public void setEmission(String emission) {
		this.emission = emission;
	}

	public GasInfo getGasInfo() {
		return gasInfo;
	}

	public void setGasInfo(GasInfo gasInfo) {
		this.gasInfo = gasInfo;
	}

	@Override
	public String toString() {
		return "EmissionsData [facId=" + facId + ", year=" + year + ", sectorId=" + sectorId + ", subsectorId="
				+ subsectorId + ", gasId=" + gasId + ", emission=" + emission + ", gasInfo=" + gasInfo + "]";
	}
	

}