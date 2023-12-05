package com.example.web_2.model.dto;

import com.example.web_2.model.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ModelReqDto {
    private String name;
    private Category category;
    private String imageUrl;
    private Integer startYear;
    private Integer endYear;
    private String brandIdentifier;

    @NotBlank(message = "Model`s name can`t be blank")
    public String getName() {
        return name;
    }

    @NotNull(message = "Please choose a model category")
    public Category getCategory() {
        return category;
    }

    @NotBlank(message = "Model`s image URL can`t be blank")
    public String getImageUrl() {
        return imageUrl;
    }

    @Positive(message = "Model`s start year must be a positive value lower than 10000")
    @Max(9999)
    @NotNull(message = "Start year must not be null or empty")
    public Integer getStartYear() {
        return startYear;
    }

    @Positive(message = "Model`s end year must be a positive value lower than 10000")
    @Max(9999)
    @NotNull(message = "End year must not be null or empty")
    public Integer getEndYear() {
        return endYear;
    }

    @NotBlank(message = "Please choose a brand")
    public String getBrandIdentifier() {
        return brandIdentifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public void setBrandIdentifier(String brandIdentifier) {
        this.brandIdentifier = brandIdentifier;
    }
}
