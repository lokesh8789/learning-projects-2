package com.r2dbc.demo.dto;

import java.util.Map;

public record PDto(Map<String, Object> details, String id) {
}
