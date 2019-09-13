package com.epa.beans.Facility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object can map to a single facility return
 * object from envirofacts. Directly mapping this table to
 * a SQL table can result in conflicts since pgmSysId and / or
 * registryId may not unique within a set of results.
 * The segregation of facilityInfo and facilityMapping has been
 * done in order to avoid such conflicts. 
 */
@Entity
@Table(name="facility860")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Facility860 {

	@Id
	@Column(name = "pgmSysId")
	@JsonProperty("PGM_SYS_ID") String pgmSysId;
	
	@Column(name = "primaryName")
	@JsonProperty("PRIMARY_NAME") String primaryName;
	
	@Column(name = "naicsCode")
	@JsonProperty("NAICS_CODE") String naicsCode;
	
	@Column(name = "registryId")
	@JsonProperty("REGISTRY_ID") String registryId;

	@Column(name = "facAddr")
	@JsonProperty("LOCATION_ADDRESS") String facAddr;
	
	@Column(name = "cityName")
	@JsonProperty("CITY_NAME") String cityName;
	
	@Column(name = "stateName")
	@JsonProperty("STATE_NAME") String stateName;

	@Column(name = "postalCode")
	@JsonProperty("POSTAL_CODE") String postalCode;
	
	@Column(name = "latitude")
	@JsonProperty("LATITUDE83") String latitude;
	
	@Column(name = "longitude")
	@JsonProperty("LONGITUDE83") String longitude;
	
	@Column(name = "hucCode")
	@JsonProperty("HUCCode") String hucCode;
	
	@Column(name = "fipsCode")
	@JsonProperty("fipsCode") String fipsCode;
	
	
	public Facility860() {
		// TODO Auto-generated constructor stub
	}

	public Facility860(String pgmSysId, String primaryName, String naicsCode, String registryId, String facAddr,
			String cityName, String stateName, String postalCode, String latitude, String longitude, String hUCCode,
			String fipsCode) {
		super();
		this.pgmSysId = pgmSysId;
		this.primaryName = primaryName;
		this.naicsCode = naicsCode;
		this.registryId = registryId;
		this.facAddr = facAddr;
		this.cityName = cityName;
		this.stateName = stateName;
		this.postalCode = postalCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hucCode = hUCCode;
		this.fipsCode = fipsCode;
	}




	public String getPgmSysId() {
		return pgmSysId;
	}


	public void setPgmSysId(String pgmSysId) {
		this.pgmSysId = pgmSysId;
	}


	public String getPrimaryName() {
		return primaryName;
	}


	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
	}


	public String getNaicsCode() {
		return naicsCode;
	}


	public void setNaicsCode(String naicsCode) {
		this.naicsCode = naicsCode;
	}


	public String getRegistryId() {
		return registryId;
	}


	public void setRegistryId(String registryId) {
		this.registryId = registryId;
	}


	public String getFacAddr() {
		return facAddr;
	}


	public void setFacAddr(String facAddr) {
		this.facAddr = facAddr;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public String getStateName() {
		return stateName;
	}


	public void setStateName(String stateName) {
		this.stateName = stateName;
	}


	public String getPostalCode() {
		return postalCode;
	}


	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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


	public String getHUCCode() {
		return this.hucCode;
	}


	public void setHUCCode(String hUCCode) {
		this.hucCode = hUCCode;
	}


	public String getFipsCode() {
		return fipsCode;
	}


	public void setFipsCode(String fipsCode) {
		this.fipsCode = fipsCode;
	}


	@Override
	public String toString() {
		return "Facility860 [pgmSysId=" + pgmSysId + ", primaryName=" + primaryName + ", naicsCode=" + naicsCode
				+ ", registryId=" + registryId + ", facAddr=" + facAddr + ", cityName=" + cityName + ", stateName="
				+ stateName + ", postalCode=" + postalCode + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", HUCCode=" + hucCode + ", fipsCode=" + fipsCode + "]";
	}

}