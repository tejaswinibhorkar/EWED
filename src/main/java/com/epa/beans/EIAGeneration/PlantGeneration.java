package com.epa.beans.EIAGeneration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the base class that can map for generation. 
 * This is a deprecated form of mapping since
 * GenerationRow has superceeded it. This can be used as a 
 * reference for mapping similar jsons.
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantGeneration {

	@JsonProperty("series_id") String series_id;
	
	@JsonProperty("name") String name;
	
	@JsonProperty("units") String units;
	
	@JsonProperty("start") String startDate;
	
	@JsonProperty("end") String endDate;
	
	@JsonProperty("lat") String latitude;
	
	@JsonProperty("lon") String longitude;

	String genData;
	
	@JsonProperty("data") String data[][];
	
	public PlantGeneration() {
		// TODO Auto-generated constructor stub
	}

	public PlantGeneration(String series_id, String name, String units, String startDate, String endDate,
			String latitude, String longitude, String genData, String[][] data) {
		super();
		this.series_id = series_id;
		this.name = name;
		this.units = units;
		this.startDate = startDate;
		this.endDate = endDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.genData = genData;
		this.data = data;
	}

	public String getSeries_id() {
		return series_id;
	}

	public void setSeries_id(String series_id) {
		this.series_id = series_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getGenData() {
		return genData;
	}

	public void setGenData(String genData) {
		this.genData = genData;
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public String printData(String[][] data) {
		StringBuilder dataString = new StringBuilder();
//		dataString.append("Year").append("\t").append("Value").append("\n");
		for (String[] part : data) {
			dataString.append(part[0]).append("  ").append(part[1]).append("  ");
		}
		return dataString.toString();
	}


	@Override
	public String toString() {
		return "PlantGeneration [series_id=" + series_id + ", name=" + name + ", units=" + units + ", startDate="
				+ startDate + ", endDate=" + endDate + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", genData=" + genData + ", data=" + printData(data) + "]";
	}
	
}
