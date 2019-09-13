package com.epa.beans.EIAGeneration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompositeKeyForDominantType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "plantCode")
	String plantCode;
	
	@Column(name="genYear")
	int genYear;

	
	public CompositeKeyForDominantType() {
	}

	public CompositeKeyForDominantType(String plantCode, int genYear) {
		super();
		this.plantCode = plantCode;
		this.genYear = genYear;
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

	@Override
	public String toString() {
		return "CompositeKeyForDominantType [plantCode=" + plantCode + ", genYear=" + genYear + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + genYear;
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeKeyForDominantType other = (CompositeKeyForDominantType) obj;
		if (genYear != other.genYear)
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		return true;
	}
	
}