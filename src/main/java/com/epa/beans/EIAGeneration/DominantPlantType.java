package com.epa.beans.EIAGeneration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//Teju
@Entity
@Table(name = "dominantPlantType")
public class DominantPlantType implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 878310634545025282L;

	@EmbeddedId
	CompositeKeyForDominantType compKey;
	
	@Column(name="dominantType")
	String dominantType;
	
	public DominantPlantType() {
		
	}

	public DominantPlantType(CompositeKeyForDominantType compKey, String dominantType) {
		super();
		this.compKey = compKey;
		this.dominantType = dominantType;
	}

	public String getDominantType() {
		return dominantType;
	}

	public void setDominantType(String dominantType) {
		this.dominantType = dominantType;
	}

	@Override
	public String toString() {
		return "DominantPlantType [ dominantType=" + dominantType + "]";
	}
	
}