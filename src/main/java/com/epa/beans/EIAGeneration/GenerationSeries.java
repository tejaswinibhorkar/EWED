package com.epa.beans.EIAGeneration;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerationSeries {

	String plantCode;
	@JsonProperty("series") PlantGeneration[] series;

	public GenerationSeries() {
		// TODO Auto-generated constructor stub
	}
	
	
	public GenerationSeries(PlantGeneration[] series) {
		super();
		this.series = series;
	}


	public PlantGeneration[] getSeries() {
		return series;
	}


	public void setSeries(PlantGeneration[] series) {
		this.series = series;
	}


	public String getPlantCode() {
		return plantCode;
	}


	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}


	@Override
	public String toString() {
		return "GenerationSeries [series=" + Arrays.toString(series) + "]";
	}
	
}
