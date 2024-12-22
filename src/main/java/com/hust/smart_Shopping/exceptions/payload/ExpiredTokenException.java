package com.hust.smart_Shopping.exceptions.payload;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException(String message) {
        super(message);
    }

}