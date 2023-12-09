package com.example.web_2.user.dto;

import com.example.web_2.user.user_role.dto.UserRoleResDto;

public class UserProfileView {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private UserRoleResDto role;
    private String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRoleResDto getRole() {
        return role;
    }

    public void setRole(UserRoleResDto role) {
        this.role = role;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
