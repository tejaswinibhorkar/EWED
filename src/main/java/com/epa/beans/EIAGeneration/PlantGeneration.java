package com.epa.beans.EIAGeneration;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="genTest")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantGeneration {

	@Id
	@Column(name = "series_id")
	@JsonProperty("series_id") String series_id;	
	@Column(name = "name", length=150)
	@JsonProperty("name") String name;
	
	@Column(name = "units", length=20)
	@JsonProperty("units") String units;
	
	@Column(name = "latitude")
	@JsonProperty("lat") String latitude;
	
	@Column(name = "longitude")
	@JsonProperty("lon") String longitude;
	
	@Column(name = "startDate")
	@JsonProperty("start") String startDate;
	
	@Column(name = "endDate")
	@JsonProperty("end") String endDate;
	
	@Transient
	@JsonProperty("data") String data[][];
	
	public PlantGeneration() {
		// TODO Auto-generated constructor stub
	}
	
	
	public PlantGeneration(String series_id, String name, String units, String latitude, String longitude, String start,
			String end, Map<String, String>[] data) {
		super();
		this.series_id = series_id;
		this.name = name;
		this.units = units;
		this.latitude = latitude;
		this.longitude = longitude;
		this.startDate = start;
		this.endDate = end;
		//this.data = data;
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
	public String getStart() {
		return startDate;
	}
	public void setStart(String start) {
		this.startDate = start;
	}
	public String getEnd() {
		return endDate;
	}
	public void setEnd(String end) {
		this.endDate = end;
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
		return "PlantGeneration [series_id=" + series_id + ", name=" + name + ", units=" + units + ", latitude="
				+ latitude + ", longitude=" + longitude + ", start=" + startDate + ", end=" + endDate + ", data="
				+ printData(data) + "]";
	}

	
}
