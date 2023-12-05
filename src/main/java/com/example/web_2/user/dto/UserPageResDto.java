package com.example.web_2.user.dto;

import java.util.List;

public class UserPageResDto {
    private int page;
    private int totalPages;
    private List<UserResDto> users;

    public UserPageResDto(int page, int totalPages, List<UserResDto> users) {
        this.page = page;
        this.totalPages = totalPages;
        this.users = users;
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

    public List<UserResDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserResDto> users) {
        this.users = users;
    }
}
