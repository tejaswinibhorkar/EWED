package com.epa.beans.GHGEmissions;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * The composite key for emissions monthly consists of the following
 * fields.
 *
 */
@Embeddable
public class EmissionsKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -979102299038558768L;

	@Column(name = "registryId")
	String registryId;
	
	@Column(name = "emYear")
	String emYear;
	
	@Column(name = "gasId")
	String gasId;
	
	public EmissionsKey() {
		// TODO Auto-generated constructor stub
	}

	public EmissionsKey(String registryId, String emYear, String gasId) {
		super();
		this.registryId = registryId;
		this.emYear = emYear;
		this.gasId = gasId;
	}

	public String getRegsitryId() {
		return registryId;
	}

	public void setRegistryId(String registryId) {
		this.registryId = registryId;
	}

	public String getEmYear() {
		return emYear;
	}

	public void setEmYear(String emYear) {
		this.emYear = emYear;
	}

	public String getGasId() {
		return gasId;
	}

	public void setGasId(String gasId) {
		this.gasId = gasId;
	}

	@Override
	public String toString() {
		return "EmissionsKey [registryId=" + registryId + ", emYear=" + emYear + ", gasId=" + gasId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emYear == null) ? 0 : emYear.hashCode());
		result = prime * result + ((registryId == null) ? 0 : registryId.hashCode());
		result = prime * result + ((gasId == null) ? 0 : gasId.hashCode());
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
		EmissionsKey other = (EmissionsKey) obj;
		if (emYear == null) {
			if (other.emYear != null)
				return false;
		} else if (!emYear.equals(other.emYear))
			return false;
		if (registryId == null) {
			if (other.registryId != null)
				return false;
		} else if (!registryId.equals(other.registryId))
			return false;
		if (gasId == null) {
			if (other.gasId != null)
				return false;
		} else if (!gasId.equals(other.gasId))
			return false;
		return true;
	}

}
