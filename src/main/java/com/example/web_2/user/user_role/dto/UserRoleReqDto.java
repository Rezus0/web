package com.example.web_2.user.user_role.dto;

import com.example.web_2.user.user_role.Role;
import jakarta.validation.constraints.NotNull;

public class UserRoleReqDto {

    private Role name;

    @NotNull(message = "Please choose a role name")
    public Role getName() {
        return name;
    }

    public void setName(Role name) {
        this.name = name;
    }
}
