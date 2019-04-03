package com.epa.beans.WaterUsage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * 
 * View that holds water usage information per registryId. 
 * All pgmSysId water usages are summed up per registryId.
 *
 */
@Entity
@Table(name="waterUsagePerReg")
@Immutable
public class WaterUsagePerRegView {

	@EmbeddedId
	WaterViewKey waterViewKey;
	
	@Column(name="usageSum")
	String usageSum;
}

/**
 * 
 * The composite key for the above view consists of the 
 * following fields
 *
 */
@Embeddable
class WaterViewKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5236884226264528824L;

	@Column(name="registryId")
	String registryId;
	
	@Column(name="usageYear")
	int usageYear;
	
	@Column(name="usageMonth")
	int usageMonth;
}