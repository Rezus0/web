package com.example.web_2.user.user_role.dto;

import java.util.List;

public class UserRolePageResDto {
    private int page;
    private int totalPages;
    private int pageSize;
    private List<UserRoleResDto> userRoles;

    public UserRolePageResDto(int page, int totalPages, int pageSize, List<UserRoleResDto> userRoles) {
        this.page = page;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<UserRoleResDto> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleResDto> userRoles) {
        this.userRoles = userRoles;
    }
}
