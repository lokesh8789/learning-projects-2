package com.r2dbc.demo.converters;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@WritingConverter
public class MapToJsonWritingConverter implements Converter<Map<String, Object>, Json> {
    private final ObjectMapper objectMapper;

    public MapToJsonWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Json convert(Map<String, Object> source) {
        System.out.println("source Write: " + source);
        if (source == null) {
            return null;
        }
        try {
            String json = objectMapper.writeValueAsString(source);
            System.out.println("json: " + json);
            return Json.of(json);
        } catch (Exception e) {
            throw new RuntimeException("Object to JSON mapping failed", e);
        }
    }
}

