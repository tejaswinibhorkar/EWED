package com.epa.dataCollector;

import com.epa.beans.Facility.Facility;

public interface EwedApiService {

	Facility getFacility(int pgmSysId);
	Facility getFacility(String registryId);
	
}
