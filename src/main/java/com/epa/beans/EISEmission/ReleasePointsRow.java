package com.epa.beans.EISEmission;

import com.epa.beans.GHGEmissions.EmissionsData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Deprecated class for old emissions implementation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleasePointsRow {
	
	@JsonProperty("EIS_RELEASE_POINT_ID") String eisReleasePointId;
	@JsonProperty("EMISSIONS") EmissionsData[] emissions;
	
	public ReleasePointsRow() {
		// TODO Auto-generated constructor stub
	}

	public ReleasePointsRow(String eisReleasePointId, EmissionsData[] emissions) {
		super();
		this.eisReleasePointId = eisReleasePointId;
		this.emissions = emissions;
	}

	public String getEisReleasePointId() {
		return eisReleasePointId;
	}

	public void setEisReleasePointId(String eisReleasePointId) {
		this.eisReleasePointId = eisReleasePointId;
	}

	public EmissionsData[] getEmissions() {
		return emissions;
	}

	public void setEmissions(EmissionsData[] emissions) {
		this.emissions = emissions;
	}

	public String printEmissions(EmissionsData[] emissions) {
	
		StringBuilder emissionsString = new StringBuilder();
		
		for (EmissionsData emission: emissions) {
			emissionsString.append(emission.toString() + " , ");
		}
		
		return emissionsString.toString();
	}
	
	@Override
	public String toString() {
		return "ReleasePoints [eisReleasePointId=" + eisReleasePointId + ", emissions=" + printEmissions(emissions) + "]";
	}
	
	
}
