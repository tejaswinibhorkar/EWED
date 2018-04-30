package com.epa.beans.EISEmission;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReleasePoints {

	@JsonProperty("RELEASE_POINTS_ROW") ReleasePointsRow releasePointsRow;
	
	public ReleasePoints() {
		// TODO Auto-generated constructor stub
	}

	public ReleasePoints(ReleasePointsRow releasePointsRow) {
		super();
		this.releasePointsRow = releasePointsRow;
	}

	public ReleasePointsRow getReleasePointsRow() {
		return releasePointsRow;
	}

	public void setReleasePointsRow(ReleasePointsRow releasePointsRow) {
		this.releasePointsRow = releasePointsRow;
	}

	@Override
	public String toString() {
		return "ReleasePoints [releasePointsRow=" + releasePointsRow + "]";
	}
	
	
}
