package com.maira.livrosapi.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ObjectMapperConfig {
	
	@Bean
	public ObjectMapper objectMapper() {		
		var objectMapper = JsonMapper.builder()
				.addModule(new JavaTimeModule())
				.build();
		
		return objectMapper;
	}

}
