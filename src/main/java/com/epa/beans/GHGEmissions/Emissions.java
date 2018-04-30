package com.epa.beans.GHGEmissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Emissions {

	@JsonProperty("FRS_ID") String facId;  //Linked to Registry ID in  Facility
	@JsonProperty("FACILITY_ID") String localFacId; 
	@JsonProperty("NAICS_CODE") String naicsCode;
	@JsonProperty("FACILITY_NAME") String facilityName;
	@JsonProperty("ADDRESS1") String facilityAddress;
	@JsonProperty("ZIP") String facilityZip;
	@JsonProperty("LATITUDE") String latitude;
	@JsonProperty("LONGITUDE") String longitude;
//	@JsonProperty("PUB_FACTS_SECTOR_GHG_EMISSION") EmissionsData emission;
	@JsonProperty("PUB_FACTS_SECTOR_GHG_EMISSION") EmissionsData[] emissions;  
	//@JsonProperty("RELEASE_POINTS") ReleasePoints releasePoints;
	
	public Emissions() {
	}

	public Emissions(String facId, String localFacId, String naicsCode, String facilityName, String facilityAddress,
			String facilityZip, String latitude, String longitude, EmissionsData[] emissions) {
		super();
		this.facId = facId;
		this.localFacId = localFacId;
		this.naicsCode = naicsCode;
		this.facilityName = facilityName;
		this.facilityAddress = facilityAddress;
		this.facilityZip = facilityZip;
		this.latitude = latitude;
		this.longitude = longitude;
//		this.emission = emission;
		this.emissions = emissions;
	}

	public String getFacId() {
		return facId;
	}

	public void setFacId(String facId) {
		this.facId = facId;
	}

	public String getLocalFacId() {
		return localFacId;
	}

	public void setLocalFacId(String localFacId) {
		this.localFacId = localFacId;
	}

	public String getNaicsCode() {
		return naicsCode;
	}

	public void setNaicsCode(String naicsCode) {
		this.naicsCode = naicsCode;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getFacilityAddress() {
		return facilityAddress;
	}

	public void setFacilityAddress(String facilityAddress) {
		this.facilityAddress = facilityAddress;
	}

	public String getFacilityZip() {
		return facilityZip;
	}

	public void setFacilityZip(String facilityZip) {
		this.facilityZip = facilityZip;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public EmissionsData[] getEmissions() {
		return emissions;
	}

//	public EmissionsData getEmission() {
//		return emission;
//	}

	/*public void setEmission(EmissionsData emission) {
		this.emissions[0] = emission;
	}
*/
	public void setEmission(EmissionsData[] emissions) {
		this.emissions = emissions;
	}
	
	public String printEmissions(EmissionsData[] emissions) {
		
		StringBuilder emissionsString = new StringBuilder();
		
		for (EmissionsData emission: emissions) {
			emissionsString.append(emission.toString() + " , ");
		}
		
		return emissionsString.toString();
	}

	@Override
	public String toString() {
		return "Emissions [facId=" + facId + ", localFacId=" + localFacId + ", naicsCode=" + naicsCode
				+ ", facilityName=" + facilityName + ", facilityAddress=" + facilityAddress + ", facilityZip="
				+ facilityZip + ", latitude=" + latitude + ", longitude=" + longitude
				+ "emissions=" + printEmissions(emissions) + "]";
	}

}