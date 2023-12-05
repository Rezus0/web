package com.example.web_2.util.fieldValidation;

import com.example.web_2.brand.BrandRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueBrandNameValidator implements ConstraintValidator<UniqueBrandName, String> {
    private BrandRepository brandRepository;

    public UniqueBrandNameValidator() {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return brandRepository.findByName(value).isEmpty();
    }

    @Autowired
    public void setBrandRepository(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
}
