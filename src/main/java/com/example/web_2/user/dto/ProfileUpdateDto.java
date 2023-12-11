package com.example.web_2.user.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfileUpdateDto extends UserRegDto {
    private String oldPassword;

    @NotBlank(message = "Old password can`t be blank")
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
