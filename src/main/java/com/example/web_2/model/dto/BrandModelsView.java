package com.example.web_2.model.dto;

import java.util.List;

public class BrandModelsView {
    private String name;
    private List<ModelResDto> models;

    public BrandModelsView(String name, List<ModelResDto> models) {
        this.name = name;
        this.models = models;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModelResDto> getModels() {
        return models;
    }

    public void setModels(List<ModelResDto> models) {
        this.models = models;
    }
}
