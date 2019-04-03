package com.epa.beans.EISEmission;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Deprecated class for old emissions implementation
 */
public class Pollutant {
	
	@JsonProperty("POLLUTANT_CODE") String pollutantCode;
	@JsonProperty("DESCRIPTION")  String description;
	@JsonProperty("POLLUTANT_TYPE") String pollutantType;
	@JsonProperty("GROUP") String group;
	@JsonProperty("GROUP_CODE") String groupCode;
	@JsonProperty("LAST_INVENTORY_YEAR") String lastInventoryYear;
	
	public Pollutant() {
		// TODO Auto-generated constructor stub
	}

	public Pollutant(String pollutantCode, String description, String pollutantType, String group, String groupCode,
			String lastInventoryYear) {
		super();
		this.pollutantCode = pollutantCode;
		this.description = description;
		this.pollutantType = pollutantType;
		this.group = group;
		this.groupCode = groupCode;
		this.lastInventoryYear = lastInventoryYear;
	}

	public String getPollutantCode() {
		return pollutantCode;
	}

	public void setPollutantCode(String pollutantCode) {
		this.pollutantCode = pollutantCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPollutantType() {
		return pollutantType;
	}

	public void setPollutantType(String pollutantType) {
		this.pollutantType = pollutantType;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getLastInventoryYear() {
		return lastInventoryYear;
	}

	public void setLastInventoryYear(String lastInventoryYear) {
		this.lastInventoryYear = lastInventoryYear;
	}

	@Override
	public String toString() {
		return "Pollutant [pollutantCode=" + pollutantCode + ", description=" + description + ", pollutantType="
				+ pollutantType + ", group=" + group + ", groupCode=" + groupCode + ", lastInventoryYear="
				+ lastInventoryYear + "]";
	}

}
