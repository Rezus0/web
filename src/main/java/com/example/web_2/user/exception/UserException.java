package com.example.web_2.user.exception;

public abstract class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
