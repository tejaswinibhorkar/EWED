package com.epa.views;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

public /**
 * 
 * The composite key for the above view consists of the 
 * following fields
 *
 */
@Embeddable
class GenEmWaterKey implements Serializable {
	
	private static final long serialVersionUID = -3899384498497000845L;

	@Column(name="plantCode")
	String plantCode;
	
	@Column(name="genYear")
	String genYear;
	
	@Column(name="genMonth")
	String genMonth;

	
	public GenEmWaterKey() {
		super();
	}


	public GenEmWaterKey(String plantCode, String genYear, String genMonth) {
		super();
		this.plantCode = plantCode;
		this.genYear = genYear;
		this.genMonth = genMonth;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getPlantCode() {
		return plantCode;
	}


	public String getGenYear() {
		return genYear;
	}


	public String getGenMonth() {
		return genMonth;
	}


	@Override
	public String toString() {
		return "GenEmWaterKey [plantCode=" + plantCode + ", genYear=" + genYear + ", genMonth=" + genMonth + "]";
	}
	
	
	
}