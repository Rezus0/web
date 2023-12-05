package com.example.web_2.offer;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Transmission {
    MANUAL(0), AUTOMATIC(1);
    private final int value;

    Transmission(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
