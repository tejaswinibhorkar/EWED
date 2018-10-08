package com.epa.dataCollector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ewedService/")
@RestController
public class EwedApiController {

	@Autowired
	EwedApiService apiService;
	
	@RequestMapping("/test")
	public String test() {
		System.out.println("Reached ewed api test");
		return "Reached ewed api test";
	}
	
	
}
