package com.example.web_2.user.dto;

import com.example.web_2.util.fieldValidation.UniqueUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegDto {
    @UniqueUsername
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String imageUrl;

    @NotBlank(message = "User`s username can`t be blank")
    public String getUsername() {
        return username;
    }

    @NotBlank(message = "User`s password can`t be blank")
    @Size(min = 6, message = "User`s password must be at least 6 characters long")
    public String getPassword() {
        return password;
    }

    @NotBlank(message = "User`s first name can`t be blank")
    public String getFirstName() {
        return firstName;
    }

    @NotBlank(message = "User`s last name can't be blank")
    public String getLastName() {
        return lastName;
    }

    @NotBlank(message = "Confirm your password")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @NotBlank(message = "User`s image URL can`t be blank")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
