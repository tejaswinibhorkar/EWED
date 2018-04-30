package com.epa.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountObject {
	@JsonProperty("TOTALQUERYRESULTS") public int count;

	@Override
	public String toString() {
		return "CountObject [count=" + count + "]";
	} 
	
}
