package com.hust.smart_Shopping.utils;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter(autoApply = true)
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }

        return jsonNode.toPrettyString();
    }

    @Override
    public JsonNode convertToEntityAttribute(String jsonNodeString) {
        if (!StringUtils.hasLength(jsonNodeString)) {
            log.warn("jsonNodeString input is empty, returning null");
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(jsonNodeString);
        } catch (JsonProcessingException e) {
            log.error("Error parsing jsonNodeString", e);
        }

        return null;
    }

}
