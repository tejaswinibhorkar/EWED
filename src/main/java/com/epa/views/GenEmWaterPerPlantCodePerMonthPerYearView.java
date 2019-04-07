package com.epa.views;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 
 * View that holds generation, emission, water usage information per plantCode per month per year with dominant type of the plant code. 
 *
 */
@Entity
@Table(name="genEmWaterPerPlantCodePerMonthPerYearView_New")
@Immutable
public class GenEmWaterPerPlantCodePerMonthPerYearView {
	
	@EmbeddedId
	GenEmWaterKey genEmWaterKey;
	
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
	
	

	public GenEmWaterKey getGenEmWaterKey() {
		return genEmWaterKey;
	}



	public String getPlantType() {
		return plantType;
	}



	public String getGeneration() {
		return generation;
	}



	public String getEmissions() {
		return emissions;
	}



	public String getWaterWithdrawal() {
		return waterWithdrawal;
	}



	public String getWaterConsumption() {
		return waterConsumption;
	}



	@Override
	public String toString() {
		return "GenEmWaterPerPlantCodePerMonthPerYearView [genEmWaterKey=" + genEmWaterKey + ", plantType=" + plantType
				+ ", generation=" + generation + ", emissions=" + emissions + ", waterWithdrawal=" + waterWithdrawal
				+ ", waterConsumption=" + waterConsumption + "]";
	}
	
	
}
