package com.example.web_2.user.dto;

import com.example.web_2.util.fieldValidation.UniqueUsername;
import jakarta.validation.constraints.NotBlank;

public abstract class BaseUserDto {
    @UniqueUsername
    private String username;

    @NotBlank(message = "User`s username can`t be blank")
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
