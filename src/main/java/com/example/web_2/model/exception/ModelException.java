package com.example.web_2.model.exception;

public abstract class ModelException extends RuntimeException {
    public ModelException(String message) {
        super(message);
    }
}
