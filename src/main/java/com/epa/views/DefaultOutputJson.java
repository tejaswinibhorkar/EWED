package com.epa.views;

public class DefaultOutputJson {
	
	public String filterName;
	public String generation;
	public String emission;
	public String waterWithdrawal;
	public String waterConsumption;
	
	
	
	public DefaultOutputJson() {
		super();
	}


	public DefaultOutputJson(String filterName, String generation, String emission,
			String waterWithdrawal,  String waterConsumption) {
		super();
		this.filterName = filterName;
		this.generation = generation;
		this.emission = emission;
		this.waterWithdrawal = waterWithdrawal;
		this.waterConsumption = waterConsumption;
		
	}


	public String getFilterName() {
		return filterName;
	}


	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}


	public String getGeneration() {
		return generation;
	}


	public void setGeneration(String generation) {
		this.generation = generation;
	}


	public String getEmission() {
		return emission;
	}


	public void setEmission(String emission) {
		this.emission = emission;
	}


	public String getWaterConsumption() {
		return waterConsumption;
	}


	public void setWaterConsumption(String waterConsumption) {
		this.waterConsumption = waterConsumption;
	}


	public String getWaterWithdrawal() {
		return waterWithdrawal;
	}


	public void setWaterWithdrawal(String waterWithdrawal) {
		this.waterWithdrawal = waterWithdrawal;
	}


	@Override
	public String toString() {
		return "DefaultOutputJson [filterName=" + filterName + ", generation=" + generation + ", emission=" + emission
				+ ", waterConsumption=" + waterConsumption + ", waterWithdrawal=" + waterWithdrawal + "]";
	}

}
