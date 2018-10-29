package com.epa.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This is a template class which ensures all types of such lists
 * remain in the same format and with the same methods. For any type
 * of facility characteristic, such lists can store all objects in this 
 * type of list. This is a generic class, its specific forms are 
 * instantiated and used in the Collection API.
 *
 * @param <T>
 */
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
