package com.example.web_2.model.dto;

import java.util.List;

public class ModelPageResDto {
    private int page;
    private int totalPages;
    private int pageSize;
    private List<ModelResDto> models;

    public ModelPageResDto(int page, int totalPages, int pageSize, List<ModelResDto> models) {
        this.page = page;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.models = models;
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

    public List<ModelResDto> getModels() {
        return models;
    }

    public void setModels(List<ModelResDto> models) {
        this.models = models;
    }
}
