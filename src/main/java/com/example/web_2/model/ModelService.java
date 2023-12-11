package com.example.web_2.model;

import com.example.web_2.model.dto.BrandModelsView;
import com.example.web_2.model.dto.ModelPageResDto;
import com.example.web_2.model.dto.ModelReqDto;
import com.example.web_2.model.dto.ModelResDto;

import java.util.List;

public interface ModelService {
    List<ModelResDto> allModels();
    ModelPageResDto getPage(int pageNumber, int pageSize);
    ModelResDto getById(String id);
    BrandModelsView getModelsForBrand(String brandId);
    ModelReqDto getForUpdate(String id);
    ModelResDto create(ModelReqDto modelReqDto);
    List<ModelResDto> create(List<ModelReqDto> modelReqDtos);
    ModelResDto update(String id, ModelReqDto modelReqDto);
    void delete(String id);
}

