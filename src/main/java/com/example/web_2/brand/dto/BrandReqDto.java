package com.example.web_2.brand.dto;


import com.example.web_2.util.fieldValidation.UniqueBrandName;
import jakarta.validation.constraints.NotBlank;

public class BrandReqDto {
    @UniqueBrandName
    private String name;

    @NotBlank(message = "Brand`s name can`t be blank")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
