package com.example.web_2.offer;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Engine {
    GASOLINE(0), DIESEL(1), ELECTRIC(2), HYBRID(3);
    private final int value;

    Engine(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
