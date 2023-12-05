package com.example.web_2.user.user_role.dto;

import java.util.List;

public class UserRolePageResDto {
    private int page;
    private int totalPages;
    private List<UserRoleResDto> userRoles;

    public UserRolePageResDto(int page, int totalPages, List<UserRoleResDto> userRoles) {
        this.page = page;
        this.totalPages = totalPages;
        this.userRoles = userRoles;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<UserRoleResDto> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleResDto> userRoles) {
        this.userRoles = userRoles;
    }
}
