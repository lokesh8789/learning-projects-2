package com.micro.hotel.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Resource Not Found");
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
