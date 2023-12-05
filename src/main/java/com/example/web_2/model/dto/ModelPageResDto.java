package com.example.web_2.model.dto;

import java.util.List;

public class ModelPageResDto {
    private int page;
    private int totalPages;
    private List<ModelResDto> models;

    public ModelPageResDto(int page, int totalPages, List<ModelResDto> models) {
        this.page = page;
        this.totalPages = totalPages;
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

    public List<ModelResDto> getModels() {
        return models;
    }

    public void setModels(List<ModelResDto> models) {
        this.models = models;
    }
}
