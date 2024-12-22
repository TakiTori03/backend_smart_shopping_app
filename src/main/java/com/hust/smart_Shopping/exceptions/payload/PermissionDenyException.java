package com.hust.smart_Shopping.exceptions.payload;

public class PermissionDenyException extends RuntimeException {
    public PermissionDenyException(String message) {
        super(message);
    }
}