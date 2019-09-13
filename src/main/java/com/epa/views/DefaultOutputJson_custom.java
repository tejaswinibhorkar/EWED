package com.epa.views;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DefaultOutputJson_custom {
	
	public String generation;
	public String emission;
	public String waterConsumption;
	public String waterWithdrawal;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Transient
	public String waterAvailability;
	
	public DefaultOutputJson_custom(String generation, String emission, String waterConsumption,
			String waterWithdrawal) {
		super();
		this.generation = generation;
		this.emission = emission;
		this.waterConsumption = waterConsumption;
		this.waterWithdrawal = waterWithdrawal;
	}
	
	public DefaultOutputJson_custom(String generation, String emission, String waterConsumption,
			String waterWithdrawal, String waterAvailability ) {
		super();
		this.generation = generation;
		this.emission = emission;
		this.waterConsumption = waterConsumption;
		this.waterWithdrawal = waterWithdrawal;
		this.waterAvailability = waterAvailability;
	}
	
	public DefaultOutputJson_custom() {
		super();
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
	
	public String getWaterAvailability() {
		return waterAvailability;
	}

	public void setWaterAvailability(String waterAvailability) {
		this.waterAvailability = waterAvailability;
	}

	@Override
	public String toString() {
		return "DefaultOutputJson_custom [generation=" + generation + ", emission=" + emission + ", waterConsumption="
				+ waterConsumption + ", waterWithdrawal=" + waterWithdrawal + "]";
	}
	
	

}
