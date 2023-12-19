package com.example.web_2.brand;

import com.example.web_2.brand.dto.BrandPageResDto;
import com.example.web_2.brand.dto.BrandReqDto;
import com.example.web_2.brand.dto.BrandResDto;
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
@RequestMapping("/brand")
public class BrandController {
    private BrandService brandService;
    private final static Logger LOG = LogManager.getLogger(Controller.class);

    @GetMapping
    public ModelAndView getBrandPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            ModelAndView maw
    ) {
        LOG.log(Level.INFO, String.format("Try to show brand page %d of size %d", page, pageSize));
        BrandPageResDto brandPage = brandService.getPage(page, pageSize);
        LOG.log(Level.INFO, String.format("Show brand page %d of %d", brandPage.getPage(), brandPage.getTotalPages()));
        maw.addObject("brandPage", brandPage);
        maw.setViewName("brand-page");
        return maw;
    }

    @GetMapping("/{id}")
    public ModelAndView getBrandById(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show brand by ID: %s for: %s", id, principal.getName()));
        BrandResDto brand = brandService.getById(id);
        maw.addObject("brand", brand);
        maw.setViewName("brand");
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createBrand(ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show brand add-form for: %s", principal.getName()));
        maw.setViewName("brand-add");
        maw.addObject("brandReqDto", new BrandReqDto());
        return maw;
    }

    @PostMapping("/add")
    public ModelAndView createBrand(@Valid BrandReqDto brandReqDto, BindingResult bindingResult,
                                    ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Try to add brand for: %s", principal.getName()));
        if (bindingResult.hasErrors()) {
            maw.addObject("brandReqDto", brandReqDto);
            maw.addObject("org.springframework.validation.BindingResult.brandReqDto",
                    bindingResult);
            maw.setViewName("brand-add");
        } else {
            LOG.log(Level.INFO, String.format("Add brand by: %s", principal.getName()));
            brandService.create(brandReqDto.getName());
            maw.setViewName("redirect:/brand");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateBrand(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show brand update-form for: %s", principal.getName()));
        maw.addObject("brandReqDto", brandService.getForUpdate(id));
        maw.addObject("brandId", id);
        maw.setViewName("brand-update");
        return maw;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateBrand(@PathVariable String id, @Valid BrandReqDto brandReqDto,
                                    BindingResult bindingResult, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Try to update brand %s for: %s", id, principal.getName()));
        if (bindingResult.hasFieldErrors("name")) {
            bindingResult = brandService.validateUniqueName(id, brandReqDto, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            maw.addObject("brandReqDto", brandReqDto);
            maw.addObject("brandId", id);
            maw.addObject("org.springframework.validation.BindingResult.brandReqDto", bindingResult);
            maw.setViewName("brand-update");
        } else {
            LOG.log(Level.INFO, String.format("Update brand %s by: %s", id, principal.getName()));
            brandService.update(id, brandReqDto.getName());
            maw.setViewName("redirect:/brand/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteBrand(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Delete brand %s by: %s", id, principal.getName()));
        brandService.delete(id);
        maw.setViewName("redirect:/brand");
        return maw;
    }


    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }
}
