package com.example.web_2.model;

import com.example.web_2.brand.BrandService;
import com.example.web_2.model.dto.ModelPageResDto;
import com.example.web_2.model.dto.ModelReqDto;
import com.example.web_2.model.dto.ModelResDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/model")
public class ModelController {
    private ModelService modelService;
    private BrandService brandService;

    @GetMapping
    public ModelAndView getModelPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            ModelAndView maw
    ) {
        ModelPageResDto modelPage = modelService.getPage(page, pageSize);
        maw.addObject("modelPage", modelPage);
        maw.setViewName("model-page");
        return maw;
    }

    @GetMapping("/{id}")
    public ModelAndView getModelById(@PathVariable String id, ModelAndView maw) {
        ModelResDto model = modelService.getById(id);
        maw.addObject("model", model);
        maw.setViewName("model");
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createModel(ModelAndView maw) {
       maw.addObject("modelReqDto", new ModelReqDto());
       maw.addObject("brands", brandService.allBrands());
       maw.setViewName("model-add");
       return maw;
    }

    @PostMapping("/add")
    public ModelAndView createModel(@Valid ModelReqDto modelReqDto, BindingResult bindingResult,
                                                   ModelAndView maw) {
        if (bindingResult.hasErrors()) {
            maw.addObject("modelReqDto", modelReqDto);
            maw.addObject("brands", brandService.allBrands());
            maw.addObject("org.springframework.validation.BindingResult.modelReqDto",
                    bindingResult);
            maw.setViewName("model-add");
        } else {
            modelService.create(modelReqDto);
            maw.setViewName("redirect:/model");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateModel(@PathVariable String id, ModelAndView maw) {
        maw.addObject("modelReqDto", modelService.getForUpdate(id));
        maw.addObject("modelId", id);
        maw.addObject("brands", brandService.allBrands());
        maw.setViewName("model-update");
        return maw;
    }

    @PutMapping("/{id}")
    public ModelAndView updateModel(@PathVariable String id, @Valid ModelReqDto modelReqDto,
                                    BindingResult bindingResult, ModelAndView maw) {
        if (bindingResult.hasErrors()) {
            maw.addObject("modelReqDto", modelReqDto);
            maw.addObject("modelId", id);
            maw.addObject("brands", brandService.allBrands());
            maw.addObject("org.springframework.validation.BindingResult.modelReqDto",
                    bindingResult);
            maw.setViewName("model-update");
        } else {
            modelService.update(id, modelReqDto);
            maw.setViewName("redirect:/model/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteModel(@PathVariable String id, ModelAndView maw) {
        modelService.delete(id);
        maw.setViewName("redirect:/model");
        return maw;
    }

    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }
}
