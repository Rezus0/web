package com.example.web_2.model;

import com.example.web_2.brand.Brand;
import com.example.web_2.offer.Offer;
import com.example.web_2.baseEntity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;


@Entity
@Table(name = "models")
public class Model extends BaseEntity {
    private String name;
    private Category category;
    private String imageUrl;
    private int startYear;
    private int endYear;
    private Brand brand;
    private List<Offer> offers;

    @NotBlank(message = "Model's name can't be blank")
    public String getName() {
        return name;
    }

    @Enumerated
    @NotNull(message = "Model's category can't be null")
    public Category getCategory() {
        return category;
    }

    @NotBlank(message = "Model's image URL can't be blank")
    public String getImageUrl() {
        return imageUrl;
    }

    @Positive(message = "Model's start year must be a positive value lower than 10000")
    @Max(9999)
    public int getStartYear() {
        return startYear;
    }

    @Positive(message = "Model's end year must be a positive value lower than 10000")
    @Max(9999)
    public int getEndYear() {
        return endYear;
    }

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @NotNull(message = "Model's brand can't be null")
    public Brand getBrand() {
        return brand;
    }

    @BatchSize(size = 10)
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "model")
    public List<Offer> getOffers() {
        return offers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
