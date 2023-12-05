package com.example.web_2.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    CAR(0), BUS(1), TRACK(2), MOTORCYCLE(3);
    private final int value;

    Category(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
