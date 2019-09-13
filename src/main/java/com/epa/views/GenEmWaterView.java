package com.epa.views;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 
 * View that holds generation, emission, water usage information per plantCode per month per year with dominant type of the plant code. 
 *
 */
@Entity
@Table(name="facGenEmWaterView")
@Immutable
public class GenEmWaterView {
	
	@Id
	@Column(name="plantCode")
	String plantCode;
	
	@Column(name="genYear")
	int genYear;
	
	@Column(name="genMonth")
	int genMonth;
	
	@Column(name = "primaryName")
	String primaryName;
	
	@Column(name = "naicsCode")
	int naicsCode;
	
	@Column(name = "registryId")
	float registryId;

	@Column(name = "facAddr")
	String facAddr;
	
	@Column(name = "cityName")
	String cityName;
	
	@Column(name = "stateName")
	String stateName;
	
	@Column(name = "postalCode")
	String postalCode;
	
	@Column(name = "latitude")
	float latitude;
	
	@Column(name = "longitude")
	float longitude;
	
	@Column(name = "GEOID")
	int GEOID;
	
	@Column(name = "CountyState1")
	String CountyState1;
	
	@Column(name = "CountyState2")
	String CountyState2;
	
	@Column(name = "HUC8Code")
	int HUC8Code;
	
	@Column(name = "HUC8Name")
	String HUC8Name;
	
	@Column(name = "HUC8Acres")
	float HUC8Acres;
	
	@Column(name="plantType")
	String plantType;
	
	@Column(name="generation")
	String generation;
	
	@Column(name="emissions")
	String emissions;
	
	@Column(name="waterWithdrawal")
	String waterWithdrawal;
	
	@Column(name="waterConsumption")
	String waterConsumption;

	public GenEmWaterView() {
		super();
	}

	

	public GenEmWaterView(String plantCode, int genYear, int genMonth, String primaryName, int naicsCode,
			float registryId, String facAddr, String cityName, String stateName, String postalCode, float latitude,
			float longitude, int gEOID, String countyState1, String countyState2, int hUC8Code, String hUC8Name,
			float hUC8Acres, String plantType, String generation, String emissions, String waterWithdrawal,
			String waterConsumption) {
		super();
		this.plantCode = plantCode;
		this.genYear = genYear;
		this.genMonth = genMonth;
		this.primaryName = primaryName;
		this.naicsCode = naicsCode;
		this.registryId = registryId;
		this.facAddr = facAddr;
		this.cityName = cityName;
		this.stateName = stateName;
		this.postalCode = postalCode;
		this.latitude = latitude;
		this.longitude = longitude;
		GEOID = gEOID;
		CountyState1 = countyState1;
		CountyState2 = countyState2;
		HUC8Code = hUC8Code;
		HUC8Name = hUC8Name;
		HUC8Acres = hUC8Acres;
		this.plantType = plantType;
		this.generation = generation;
		this.emissions = emissions;
		this.waterWithdrawal = waterWithdrawal;
		this.waterConsumption = waterConsumption;
	}

	public GenEmWaterView(String plantCode, int genYear, int genMonth,
			String plantType, String generation, String emissions, String waterWithdrawal, String waterConsumption) {
		super();
		this.plantCode = plantCode;
		this.genYear = genYear;
		this.genMonth = genMonth;
		this.plantType = plantType;
		this.generation = generation;
		this.emissions = emissions;
		this.waterWithdrawal = waterWithdrawal;
		this.waterConsumption = waterConsumption;
	}



//	public GenEmWaterView(GenEmWaterKey genEmWaterKey, String primaryName, String naicsCode, String registryId,
//			String facAddr, String cityName, String stateName, String postalCode, String latitude, String longitude,
//			String gEOID, String countyState1, String countyState2, String hUC8Code, String hUC8Name, String hUC8Acres,
//			String plantType, String generation, String emissions, String waterWithdrawal, String waterConsumption) {
//		super();
//		this.genEmWaterKey = genEmWaterKey;
//		this.primaryName = primaryName;
//		this.naicsCode = naicsCode;
//		this.registryId = registryId;
//		this.facAddr = facAddr;
//		this.cityName = cityName;
//		this.stateName = stateName;
//		this.postalCode = postalCode;
//		this.latitude = latitude;
//		this.longitude = longitude;
//		GEOID = gEOID;
//		CountyState1 = countyState1;
//		CountyState2 = countyState2;
//		HUC8Code = hUC8Code;
//		HUC8Name = hUC8Name;
//		HUC8Acres = hUC8Acres;
//		this.plantType = plantType;
//		this.generation = generation;
//		this.emissions = emissions;
//		this.waterWithdrawal = waterWithdrawal;
//		this.waterConsumption = waterConsumption;
//	}

//	public GenEmWaterKey getGenEmWaterKey() {
//		return genEmWaterKey;
//	}
//
//	public void setGenEmWaterKey(GenEmWaterKey genEmWaterKey) {
//		this.genEmWaterKey = genEmWaterKey;
//	}
	
	

	public String getPrimaryName() {
		return primaryName;
	}

	public int getNaicsCode() {
		return naicsCode;
	}



	public void setNaicsCode(int naicsCode) {
		this.naicsCode = naicsCode;
	}



	public float getRegistryId() {
		return registryId;
	}



	public void setRegistryId(float registryId) {
		this.registryId = registryId;
	}



	public float getLatitude() {
		return latitude;
	}



	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}



	public float getLongitude() {
		return longitude;
	}



	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}



	public int getGEOID() {
		return GEOID;
	}



	public void setGEOID(int gEOID) {
		GEOID = gEOID;
	}



	public int getHUC8Code() {
		return HUC8Code;
	}



	public void setHUC8Code(int hUC8Code) {
		HUC8Code = hUC8Code;
	}



	public float getHUC8Acres() {
		return HUC8Acres;
	}



	public void setHUC8Acres(float hUC8Acres) {
		HUC8Acres = hUC8Acres;
	}



	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public int getGenYear() {
		return genYear;
	}

	public void setGenYear(int genYear) {
		this.genYear = genYear;
	}

	public int getGenMonth() {
		return genMonth;
	}

	public void setGenMonth(int genMonth) {
		this.genMonth = genMonth;
	}

	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
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

	public String getCountyState1() {
		return CountyState1;
	}

	public void setCountyState1(String countyState1) {
		CountyState1 = countyState1;
	}

	public String getCountyState2() {
		return CountyState2;
	}

	public void setCountyState2(String countyState2) {
		CountyState2 = countyState2;
	}

	

	public String getHUC8Name() {
		return HUC8Name;
	}

	public void setHUC8Name(String hUC8Name) {
		HUC8Name = hUC8Name;
	}



	public String getPlantType() {
		return plantType;
	}

	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}

	public String getGeneration() {
		return generation;
	}

	public void setGeneration(String generation) {
		this.generation = generation;
	}

	public String getEmissions() {
		return emissions;
	}

	public void setEmissions(String emissions) {
		this.emissions = emissions;
	}

	public String getWaterWithdrawal() {
		return waterWithdrawal;
	}

	public void setWaterWithdrawal(String waterWithdrawal) {
		this.waterWithdrawal = waterWithdrawal;
	}

	public String getWaterConsumption() {
		return waterConsumption;
	}

	public void setWaterConsumption(String waterConsumption) {
		this.waterConsumption = waterConsumption;
	}

	@Override
	public String toString() {
		return "GenEmWaterView [plantCode=" + plantCode + ", genYear=" + genYear + ", genMonth=" + genMonth
				+ ", primaryName=" + primaryName + ", naicsCode=" + naicsCode + ", registryId=" + registryId
				+ ", facAddr=" + facAddr + ", cityName=" + cityName + ", stateName=" + stateName + ", postalCode="
				+ postalCode + ", latitude=" + latitude + ", longitude=" + longitude + ", GEOID=" + GEOID
				+ ", CountyState1=" + CountyState1 + ", CountyState2=" + CountyState2 + ", HUC8Code=" + HUC8Code
				+ ", HUC8Name=" + HUC8Name + ", HUC8Acres=" + HUC8Acres + ", plantType=" + plantType + ", generation="
				+ generation + ", emissions=" + emissions + ", waterWithdrawal=" + waterWithdrawal
				+ ", waterConsumption=" + waterConsumption + "]";
	}

//	@Override
//	public String toString() {
//		return "GenEmWaterView [genEmWaterKey=" + genEmWaterKey + ", primaryName=" + primaryName + ", naicsCode="
//				+ naicsCode + ", registryId=" + registryId + ", facAddr=" + facAddr + ", cityName=" + cityName
//				+ ", stateName=" + stateName + ", postalCode=" + postalCode + ", latitude=" + latitude + ", longitude="
//				+ longitude + ", GEOID=" + GEOID + ", CountyState1=" + CountyState1 + ", CountyState2=" + CountyState2
//				+ ", HUC8Code=" + HUC8Code + ", HUC8Name=" + HUC8Name + ", HUC8Acres=" + HUC8Acres + ", plantType="
//				+ plantType + ", generation=" + generation + ", emissions=" + emissions + ", waterWithdrawal="
//				+ waterWithdrawal + ", waterConsumption=" + waterConsumption + "]";
//	}

	
}
