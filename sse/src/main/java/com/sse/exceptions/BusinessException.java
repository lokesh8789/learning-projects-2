package com.sse.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class BusinessException extends RuntimeException {
    protected final HttpStatus httpStatus;
    private static final HttpStatus exceptionHttpStatus = HttpStatus.BAD_REQUEST;

    public BusinessException() {
        super("Error Occurred");
        this.httpStatus = exceptionHttpStatus;
    }

    public BusinessException(String message) {
        super(message);
        this.httpStatus = exceptionHttpStatus;
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
        this.httpStatus = exceptionHttpStatus;
    }

    public BusinessException(Throwable throwable, HttpStatus httpStatus) {
        super(throwable);
        this.httpStatus = httpStatus;
    }

    public BusinessException(Throwable throwable, HttpStatusCode httpStatusCode) {
        super(throwable);
        this.httpStatus = HttpStatus.valueOf(httpStatusCode.value());
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatus =  HttpStatus.valueOf(httpStatusCode.value());
    }
}
