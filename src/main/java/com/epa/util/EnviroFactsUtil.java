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
	
	public static RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();        
        //Add the Jackson Message converter
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	    converter.setObjectMapper(mapper);
	    // Note: here we are making this converter to process any kind of response, 
	    // not only application/*json, which is the default behaviour
	    converter.setSupportedMediaTypes(Arrays.asList(MediaType.ALL));
   
	    messageConverters.add(converter);  
	    restTemplate.setMessageConverters(messageConverters);  
	    return restTemplate;
	}
	
	public static boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}  
}
