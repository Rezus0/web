package com.example.web_2.model;

import com.example.web_2.brand.BrandService;
import com.example.web_2.model.dto.BrandModelsView;
import com.example.web_2.model.dto.ModelPageResDto;
import com.example.web_2.model.dto.ModelReqDto;
import com.example.web_2.model.dto.ModelResDto;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/model")
public class ModelController {
    private ModelService modelService;
    private BrandService brandService;
    private final static Logger LOG = LogManager.getLogger(Controller.class);

    @GetMapping
    public ModelAndView getModelPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            ModelAndView maw
    ) {
        LOG.log(Level.INFO, String.format("Try to show model page %d of size %d", page, pageSize));
        ModelPageResDto modelPage = modelService.getPage(page, pageSize);
        LOG.log(Level.INFO, String.format("Show model page %d of %d", modelPage.getPage(), modelPage.getTotalPages()));
        maw.addObject("modelPage", modelPage);
        maw.setViewName("model-page");
        return maw;
    }

    @GetMapping("/{id}")
    public ModelAndView getModelById(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show model by ID: %s for: %s", id, principal.getName()));
        ModelResDto model = modelService.getById(id);
        maw.addObject("model", model);
        maw.setViewName("model");
        return maw;
    }

    @GetMapping("/for-brand/{brandId}")
    public ModelAndView getModelsForBrand(@PathVariable String brandId, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show brand: %s models for: %s", brandId, principal.getName()));
        BrandModelsView brandModelsView = modelService.getModelsForBrand(brandId);
        maw.addObject("brandModelsView", brandModelsView);
        maw.setViewName("brand-models");
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createModel(ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show model add-form for: %s", principal.getName()));
        maw.addObject("modelReqDto", new ModelReqDto());
        maw.addObject("brands", brandService.allBrands());
        maw.setViewName("model-add");
        return maw;
    }

    @PostMapping("/add")
    public ModelAndView createModel(@Valid ModelReqDto modelReqDto, BindingResult bindingResult,
                                    ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Try to add model for: %s", principal.getName()));
        if (bindingResult.hasErrors()) {
            maw.addObject("modelReqDto", modelReqDto);
            maw.addObject("brands", brandService.allBrands());
            maw.addObject("org.springframework.validation.BindingResult.modelReqDto",
                    bindingResult);
            maw.setViewName("model-add");
        } else {
            LOG.log(Level.INFO, String.format("Add model by: %s", principal.getName()));
            modelService.create(modelReqDto);
            maw.setViewName("redirect:/model");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateModel(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show model update-form for: %s", principal.getName()));
        maw.addObject("modelReqDto", modelService.getForUpdate(id));
        maw.addObject("modelId", id);
        maw.addObject("brands", brandService.allBrands());
        maw.setViewName("model-update");
        return maw;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateModel(@PathVariable String id, @Valid ModelReqDto modelReqDto,
                                    BindingResult bindingResult, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Try to update model %s for: %s", id, principal.getName()));
        if (bindingResult.hasErrors()) {
            maw.addObject("modelReqDto", modelReqDto);
            maw.addObject("modelId", id);
            maw.addObject("brands", brandService.allBrands());
            maw.addObject("org.springframework.validation.BindingResult.modelReqDto",
                    bindingResult);
            maw.setViewName("model-update");
        } else {
            LOG.log(Level.INFO, String.format("Update model %s by: %s", id, principal.getName()));
            modelService.update(id, modelReqDto);
            maw.setViewName("redirect:/model/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteModel(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Delete brand %s by: %s", id, principal.getName()));
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
