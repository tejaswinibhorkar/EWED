package com.epa.beans.EISEmission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EISEmissions {

	@JsonProperty("EIS_FACILITY_ID") String facId;
	@JsonProperty("NAICS_CODE") String naicsCode;
	@JsonProperty("NAME") String facilityName;
	@JsonProperty("ADDRESS") String facilityAddress;
	@JsonProperty("ZIP_CODE") String facilityZip;
	@JsonProperty("LATITUDE") String latitude;
	@JsonProperty("LONGITUDE") String longitude;
	@JsonProperty("RELEASE_POINTS") ReleasePoints releasePoints;
	
	public EISEmissions() {
	}

	public EISEmissions(String facId, String naicsCode, String facilityName, String facilityAddress, String facilityZip,
			String latitude, String longitude, ReleasePoints releasePoints) {
		super();
		this.facId = facId;
		this.naicsCode = naicsCode;
		this.facilityName = facilityName;
		this.facilityAddress = facilityAddress;
		this.facilityZip = facilityZip;
		this.latitude = latitude;
		this.longitude = longitude;
		this.releasePoints = releasePoints;
	}

	public String getFacId() {
		return facId;
	}

	public void setFacId(String facId) {
		this.facId = facId;
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

	public ReleasePoints getReleasePoints() {
		return releasePoints;
	}

	public void setReleasePoints(ReleasePoints releasePoints) {
		this.releasePoints = releasePoints;
	}

	@Override
	public String toString() {
		return "Emissions [facId=" + facId + ", naicsCode=" + naicsCode + ", facilityName=" + facilityName
				+ ", facilityAddress=" + facilityAddress + ", facilityZip=" + facilityZip + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", releasePoints=" + releasePoints + "]";
	}

	
	
}
