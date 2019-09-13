package com.epa.views;

public class Top4 {
	
	private String plantType;
	private String total;
	private int rank;
	
	public Top4() {
		super();
	}

	public Top4(String plantType, String total, int rank) {
		super();
		this.plantType = plantType;
		this.total = total;
		this.rank = rank;
	}

	public String getPlantType() {
		return plantType;
	}

	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "Top4 [plantType=" + plantType + ", total=" + total + ", rank=" + rank + "]";
	}
	
	

}
