package com.example.web_2.brand;

import com.example.web_2.brand.dto.BrandPageResDto;
import com.example.web_2.brand.dto.BrandReqDto;
import com.example.web_2.brand.dto.BrandResDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface BrandService {
    List<BrandResDto> allBrands();
    BrandPageResDto getPage(int pageNumber, int pageSize);
    BrandResDto getById(String id);
    BrandReqDto getForUpdate(String id);
    BrandResDto create(String name);
    List<BrandResDto> create(List<String> names);
    BrandResDto update(String id, String name);
    void delete(String id);
    BindingResult validateUniqueName(String id, BrandReqDto brandReqDto, BindingResult bindingResult);
}
