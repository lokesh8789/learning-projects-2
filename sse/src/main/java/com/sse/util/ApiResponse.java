package com.sse.util;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ApiResponse<T> {
    private int statusCode = 200;
    private String message = "Success";
    private T data;

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}