package com.example.web_2.brand;

import com.example.web_2.brand.dto.BrandPageResDto;
import com.example.web_2.brand.dto.BrandReqDto;
import com.example.web_2.brand.dto.BrandResDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/brand")
public class BrandController {
    private BrandService brandService;

    @GetMapping
    public ModelAndView getBrandPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            ModelAndView maw
    ) {
        BrandPageResDto brandPage = brandService.getPage(page, pageSize);
        maw.addObject("brandPage", brandPage);
        maw.setViewName("brand-page");
        return maw;
    }

    @GetMapping("/{id}")
    public ModelAndView getBrandById(@PathVariable String id, ModelAndView maw) {
        BrandResDto brand = brandService.getById(id);
        maw.addObject("brand", brand);
        maw.setViewName("brand");
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createBrand(ModelAndView maw) {
        maw.setViewName("brand-add");
        maw.addObject("brandReqDto", new BrandReqDto());
        return maw;
    }

    @PostMapping("/add")
    public ModelAndView createBrand(@Valid BrandReqDto brandReqDto, BindingResult bindingResult,
                                    ModelAndView maw) {
        if (bindingResult.hasErrors()) {
            maw.addObject("brandReqDto", brandReqDto);
            maw.addObject("org.springframework.validation.BindingResult.brandReqDto",
                    bindingResult);
            maw.setViewName("brand-add");
        } else {
            brandService.create(brandReqDto.getName());
            maw.setViewName("redirect:/brand");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateBrand(@PathVariable String id, ModelAndView maw) {
        maw.addObject("brandReqDto", brandService.getForUpdate(id));
        maw.addObject("brandId", id);
        maw.setViewName("brand-update");
        return maw;
    }

    @PutMapping("/{id}")
    public ModelAndView updateBrand(@PathVariable String id, @Valid BrandReqDto brandReqDto,
                                    BindingResult bindingResult, ModelAndView maw) {
        if (bindingResult.hasFieldErrors("name")) {
            List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> !("Brand name already exists!").equals(fieldError.getDefaultMessage()) ||
                            !brandService.getById(id).getName().equals(fieldError.getRejectedValue()))
                    .toList();
            bindingResult = new BeanPropertyBindingResult(brandReqDto, "userReqDto");
            for (FieldError e:
                    errorsToKeep) {
                bindingResult.addError(e);
            }
        }
        if (bindingResult.hasErrors()) {
            maw.addObject("brandReqDto", brandReqDto);
            maw.addObject("brandId", id);
            maw.addObject("org.springframework.validation.BindingResult.brandReqDto", bindingResult);
            maw.setViewName("brand-update");
        } else {
            brandService.update(id, brandReqDto.getName());
            maw.setViewName("redirect:/brand/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteBrand(@PathVariable String id, ModelAndView maw) {
        brandService.delete(id);
        maw.setViewName("redirect:/brand");
        return maw;
    }


    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }
}
