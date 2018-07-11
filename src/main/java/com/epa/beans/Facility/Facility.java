package com.epa.beans.Facility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="Facility")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Facility {

	@Id
	@Column(name = "facId")
	@JsonProperty("PGM_SYS_ID") String facId;
	
	@Column(name = "naicsCode")
	@JsonProperty("NAICS_CODE") String naicsCode;
	
	@Column(name = "codeDescription")
	@JsonProperty("CODE_DESCRIPTION") String codeDescription;
	
	@Column(name = "registryId")
	@JsonProperty("REGISTRY_ID") String registryId;
	
	@Column(name = "primaryName")
	@JsonProperty("PRIMARY_NAME") String primaryName;

	public Facility() {
		// TODO Auto-generated constructor stub
	}

	public Facility(String naics, String codeDescription, String pgmSysId, String registryId, String primaryName) {
		super();
		this.naicsCode = naics;
		this.codeDescription = codeDescription;
		this.facId = pgmSysId;
		this.registryId = registryId;
		this.primaryName = primaryName;
	}


	public String getNaics() {
		return naicsCode;
	}

	public void setNaics(String naicsCode) {
		this.naicsCode = naicsCode;
	}

	public String getCodeDescription() {
		return codeDescription;
	}

	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}

	public String getFacId() {
		return facId;
	}

	public void setPgmSysId(String facId) {
		this.facId = facId;
	}

	public String getRegistryId() {
		return registryId;
	}

	public void setRegistryId(String registryId) {
		this.registryId = registryId;
	}

	public String getPrimaryName() {
		return primaryName;
	}

	public void setPrimaryId(String primaryName) {
		this.primaryName = primaryName;
	}


	@Override
	public String toString() {
		return "Facility [naicsCode=" + naicsCode + ", codeDescription=" + codeDescription + ", facId=" + facId
				+ ", registryId=" + registryId + ", primaryName=" + primaryName + "]";
	}
	
}
