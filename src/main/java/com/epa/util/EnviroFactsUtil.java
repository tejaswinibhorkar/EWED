package com.epa.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EnviroFactsUtil {
	
	/**
	 * This method is the base setup for a future implementation of logging.
	 * 
	 * @param LOG
	 * @param level
	 * @param message
	 */
	public static void logMessage(Logger LOG, String level, String message) {
		
		message = new Date() + " " + message;

		switch (level) {
		case "ERROR":
			LOG.error(message);
		case "INFO":
			LOG.info(message);
		case "DEBUG":
			LOG.debug(message);
		}
	
	}
	
	/**
	 * IMP - This is one of the main functions of the application.
	 * This function creates the rest template that is used to call and map jsons
	 * to the objects created in the packages. 
	 * 
	 * @return Rest Template Instantiation with correct configurations
	 */
	public static RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();        
        //Add the Jackson Message converter
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    ObjectMapper mapper = new ObjectMapper();
	    
	    //This feature allows all values that are single objects when only one 
	    //object is returned and an array when multiple are returned. This 
	    //feature ensures all single objects are also treated as arrays.
	    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	    converter.setObjectMapper(mapper);
	    
	    // Note: here we are making this converter to process any kind of response, 
	    // not only application/*json, which is the default behaviour
	    converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
   
	    messageConverters.add(converter);  
	    restTemplate.setMessageConverters(messageConverters);  
	    return restTemplate;
	}
	
	/**
	 * Generic utility function to check whether a given string is a
	 * number i.e. contains only numeric digits
	 * 
	 * @param s
	 * @return boolean answer for if s is a number or not
	 */
	public static boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}  
	
}
