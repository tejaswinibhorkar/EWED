package com.epa.beans;

import java.util.ArrayList;
import java.util.List;

public class ObjectList<T> {

	public List<T> objectList = new ArrayList<T>();
	
	public ObjectList() {
		// TODO Auto-generated constructor stub
	}

	public ObjectList(List<T> objectList) {
		super();
		this.objectList = objectList;
	}

	public List<T> getObjectList() {
		return objectList;
	}

	public void setFacilities(List<T> objectList) {
		this.objectList = objectList;
	}

	@Override
	public String toString() {
		return "ObjectList [facilities=" + objectList + "]";
	}
	
}
