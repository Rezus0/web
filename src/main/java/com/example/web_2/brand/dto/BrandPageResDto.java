package com.example.web_2.brand.dto;

import java.util.List;
public class BrandPageResDto {

    private int page;
    private int totalPages;
    private List<BrandResDto> brands;

    public BrandPageResDto(int page, int totalPages, List<BrandResDto> brands) {
        this.page = page;
        this.totalPages = totalPages;
        this.brands = brands;
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

    public List<BrandResDto> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandResDto> brands) {
        this.brands = brands;
    }
}
