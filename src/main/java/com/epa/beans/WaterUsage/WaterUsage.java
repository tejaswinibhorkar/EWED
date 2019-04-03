package com.epa.beans.WaterUsage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * Base table that holds water usage information per plantCode
 *
 */
@Entity
@Table(name="waterUsage")
public class WaterUsage {

	@EmbeddedId
	WaterUsageKey waterUsageKey;
	
	@Column(name="usageData")
	String usageData;
	
	@Column(name="dervied")
	boolean dervied;
}

/**
 * 
 * The composite key for the base table consists of the 
 * following fields
 *
 */
@Embeddable
class WaterUsageKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3739678775733410121L;

	@Column(name="plantCode")
	String plantCode;
	
	@Column(name="usageYear")
	int usageYear;
	
	@Column(name="usageMonth")
	int usageMonth;
}