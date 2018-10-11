package com.epa.beans.WaterUsage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="waterUsage")
public class WaterUsage {

	@EmbeddedId
	WaterUsageKey waterUsageKey;
	
	@Column(name="usageData")
	String usageData;
	
	@Column(name="dervied")
	int dervied;
}

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