package com.r2dbc.demo.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Map;

@ReadingConverter
public class JsonToMapReadingConverter implements Converter<Json, Map<String, Object>> {
    private final ObjectMapper objectMapper;
    public JsonToMapReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> convert(Json source) {
        System.out.println("source: " + source);
        if (source == null) {
            return null;
        }
        try {
            return objectMapper.readValue(source.asString(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON to Object mapping failed", e);
        }
    }
}

