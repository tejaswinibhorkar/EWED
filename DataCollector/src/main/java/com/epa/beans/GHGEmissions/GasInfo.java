package com.epa.beans.GHGEmissions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GasInfo {

	@JsonProperty("GAS_ID") String gasId;
	@JsonProperty("GAS_CODE") String gasCode;
	@JsonProperty("GAS_NAME") String gasName;
	@JsonProperty("GAS_LABEL") String gasLabel;
	
	public GasInfo() {
		// TODO Auto-generated constructor stub
	}

	public GasInfo(String gasId, String gasCode, String gasName, String gasLabel) {
		super();
		this.gasId = gasId;
		this.gasCode = gasCode;
		this.gasName = gasName;
		this.gasLabel = gasLabel;
	}

	@Override
	public String toString() {
		return "GasInfo [gasId=" + gasId + ", gasCode=" + gasCode + ", gasName=" + gasName + ", gasLabel=" + gasLabel
				+ "]";
	}
	
}
