package com.system.checkout.util;

import javax.persistence.AttributeConverter;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.checkout.model.BasketDTO;

public class CustomAttributeConverter implements AttributeConverter<BasketDTO, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(BasketDTO value) {
		try {
			return objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error while converting to string");
		}
	}

	@Override
	public BasketDTO convertToEntityAttribute(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		try {
			return objectMapper.readValue(value, BasketDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Error while reading value to string");
		}

	}
}
