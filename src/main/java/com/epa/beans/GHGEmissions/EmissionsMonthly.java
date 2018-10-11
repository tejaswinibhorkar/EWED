package com.epa.beans.GHGEmissions;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="emissionsMonthly")
public class EmissionsMonthly {

	@EmbeddedId
	EmissionsMonthlyKey emissionsMonthlyKey;
	
	@Column(name="emissionAmount")
	String emissionAmount;
	
	@Column(name="derived")
	int derived;
}

@Embeddable
class EmissionsMonthlyKey implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7446397996024636150L;

	@Column(name="registryId")
	String registryId;
	
	@Column(name="emYear")
	int emYear;
	
	@Column(name="emMonth")
	int emMonth;
}