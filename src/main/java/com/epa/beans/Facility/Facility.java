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
@Table(name="facility860C")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Facility {

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
	
	@Column(name = "geoid")
	@JsonProperty("GEOID") String GEOID;
	
	@Column(name = "countystate1")
	@JsonProperty("CountyState1") String CountyState1;
	
	@Column(name = "countystate2")
	@JsonProperty("CountyState2") String CountyState2;
	
	@Column(name = "huc8code")
	@JsonProperty("HUC8Code") String HUC8Code;
	
	@Column(name = "huc8name")
	@JsonProperty("HUC8Name") String HUC8Name;
	
	@Column(name = "huc8acres")
	@JsonProperty("HUC8Acres") String HUC8Acres;
	
	
	public Facility() {
		// TODO Auto-generated constructor stub
	}


	public Facility(String pgmSysId, String primaryName, String naicsCode, String registryId, String facAddr,
			String cityName, String stateName, String postalCode, String latitude, String longitude, String gEOID,
			String countyState1, String countyState2, String hUC8Code, String hUC8Name, String hUC8Acres) {
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
		this.GEOID = gEOID;
		this.CountyState1 = countyState1;
		this.CountyState2 = countyState2;
		this.HUC8Code = hUC8Code;
		this.HUC8Name = hUC8Name;
		this.HUC8Acres = hUC8Acres;
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


	public String getGEOID() {
		return GEOID;
	}


	public void setGEOID(String gEOID) {
		this.GEOID = gEOID;
	}


	public String getCountyState1() {
		return CountyState1;
	}


	public void setCountyState1(String countyState1) {
		this.CountyState1 = countyState1;
	}


	public String getCountyState2() {
		return CountyState2;
	}


	public void setCountyState2(String countyState2) {
		this.CountyState2 = countyState2;
	}


	public String getHUC8Code() {
		return HUC8Code;
	}


	public void setHUC8Code(String hUC8Code) {
		this.HUC8Code = hUC8Code;
	}


	public String getHUC8Name() {
		return HUC8Name;
	}


	public void setHUC8Name(String hUC8Name) {
		this.HUC8Name = hUC8Name;
	}


	public String getHUC8Acres() {
		return HUC8Acres;
	}


	public void setHUC8Acres(String hUC8Acres) {
		this.HUC8Acres = hUC8Acres;
	}


	@Override
	public String toString() {
		return "Facility [pgmSysId=" + pgmSysId + ", primaryName=" + primaryName + ", naicsCode=" + naicsCode
				+ ", registryId=" + registryId + ", facAddr=" + facAddr + ", cityName=" + cityName + ", stateName="
				+ stateName + ", postalCode=" + postalCode + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", GEOID=" + GEOID + ", CountyState1=" + CountyState1 + ", CountyState2=" + CountyState2
				+ ", HUC8Code=" + HUC8Code + ", HUC8Name=" + HUC8Name + ", HUC8Acres=" + HUC8Acres + "]";
	}


}