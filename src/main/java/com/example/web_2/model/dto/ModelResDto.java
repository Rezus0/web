package com.example.web_2.model.dto;

import com.example.web_2.baseEntity.BaseResDto;
import com.example.web_2.brand.dto.BrandResDto;
import com.example.web_2.model.Category;

public class ModelResDto extends BaseResDto {
    private String id;
    private String name;
    private Category category;
    private String imageUrl;
    private int startYear;
    private int endYear;
    private BrandResDto brand;
    private int viewCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public BrandResDto getBrand() {
        return brand;
    }

    public void setBrand(BrandResDto brand) {
        this.brand = brand;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
