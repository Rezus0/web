package com.example.web_2.brand.dto;

import com.example.web_2.baseEntity.BaseResDto;

public class BrandResDto extends BaseResDto {
    private String id;
    private String name;

    public BrandResDto() {
    }

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
}
