package com.epa.beans.EIAGeneration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The composite key for the table Generation consists 
 * of the following elements
 *
 */
@Embeddable
public class KeyItems implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3324231350230064568L;

	@Column(name = "plantCode")
	String plantCode;
	
	@Column(name = "genYear")
	String genYear;
	
	@Column(name = "genMonth")
	String genMonth;
	
	public KeyItems() {
		// TODO Auto-generated constructor stub
	}
	
	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	
	public String getGenYear() {
		return genYear;
	}

	public void setGenYear(String genYear) {
		this.genYear = genYear;
	}

	public String getGenMonth() {
		return genMonth;
	}

	public void setGenMonth(String genMonth) {
		this.genMonth = genMonth;
	}

	@Override
	public String toString() {
		return "KeyItems [plantCode=" + plantCode + ", genYear=" + genYear + ", genMonth=" + genMonth + "]";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		KeyItems key = (KeyItems) obj;
		return (this.getPlantCode().equals(key.getPlantCode()) &&
				this.getGenMonth().equals(key.getGenMonth()) &&
				this.getGenYear().equals(key.getGenYear())) ;
	}
	
	
	
}
