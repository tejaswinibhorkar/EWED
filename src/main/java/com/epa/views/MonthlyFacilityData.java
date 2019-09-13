package com.epa.views;

import java.util.List;
import java.util.Map;

import com.epa.beans.EWEDMonthlyData;
import com.epa.beans.Facility.Facility;

public class MonthlyFacilityData {
	
	public Facility facility;
	public List<EWEDMonthlyData> monthlyDataList;
	public Map<String, Double> monthlyDataSummary;
	
	@Override
	public String toString() {
		return "MonthlyFacilityData [facility=" + facility + ", monthlyDataList=" + monthlyDataList
				+ ", monthlyDataSummary=" + monthlyDataSummary + "]";
	}
	
	

}
