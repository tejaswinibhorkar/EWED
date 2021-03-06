package com.epa.beans;

public class EWEDMonthlyData {
	
	public int year;
	public int month;
	public String plantType;
	public String coolingSystemType;
	public String waterType;
	public String waterSource;
	public String waterSourceName;
	public String generation;
	public String emissions;
	public String waterWithdrawal;
	public String waterConsumption;
	@Override
	public String toString() {
		return "EWEDMonthlyData [year=" + year + ", month=" + month + ", plantType=" + plantType
				+ ", coolingSystemType=" + coolingSystemType + ", waterType=" + waterType + ", waterSource="
				+ waterSource + ", waterSourceName=" + waterSourceName + ", generation=" + generation + ", emissions="
				+ emissions + ", waterWithdrawal=" + waterWithdrawal + ", waterConsumption=" + waterConsumption + "]";
	}
}
