package com.example.web_2.user.user_role;

import com.example.web_2.user.user_role.exception.UserRoleNotFoundException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Role {
    USER(0), ADMIN(1);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public static Role getRoleByValue(int value) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new UserRoleNotFoundException(
                        String.format("There is no role for value: %d", value))
                );
    }
}
