package com.epa.beans.EIAGeneration;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="generationEIA")
public class GenerationRow {
	
	@Column(name = "plantName")
	String plantName;
	
	@Column(name = "genData")
	String genData;
	
	@Column(name = "units")
	String units;
	
	@Column(name = "latitude")
	String latitude;
	
	@Column(name = "longitude")
	String longitude;
	
	@EmbeddedId
	KeyItems keyTimes; 
	
	public GenerationRow() {
		// TODO Auto-generated constructor stub
	}

	public GenerationRow(String plantName, String genData, String units, KeyItems keyItems) {
		super();
		this.plantName = plantName;
		this.genData = genData;
		this.units = units;
		this.keyTimes = keyItems;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getGenData() {
		return genData;
	}

	public void setGenData(String genData) {
		this.genData = genData;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public KeyItems getKeyTimes() {
		return keyTimes;
	}

	public void setKeyTimes(KeyItems keyTimes) {
		this.keyTimes = keyTimes;
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

	@Override
	public String toString() {
		return "GenerationRow [plantName=" + plantName + ", genData=" + genData + ", units=" + units + ", latitude="
				+ latitude + ", longitude=" + longitude + ", keyTimes=" + keyTimes + "]";
	}

}
