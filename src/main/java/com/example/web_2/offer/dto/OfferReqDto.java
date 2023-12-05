package com.example.web_2.offer.dto;

import com.example.web_2.offer.Engine;
import com.example.web_2.offer.Transmission;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OfferReqDto {
    private String description;
    private Engine engine;
    private String imageUrl;
    private Integer mileage;
    private Double price;
    private Transmission transmission;
    private Integer year;
    private String modelIdentifier;
    private String sellerIdentifier;

    @NotBlank(message = "Offer`s description can`t be blank")
    public String getDescription() {
        return description;
    }

    @NotNull(message = "Please choose an engine")
    public Engine getEngine() {
        return engine;
    }

    @NotBlank(message = "Offer`s image URL can`t be blank")
    public String getImageUrl() {
        return imageUrl;
    }

    @Positive(message = "Offer`s mileage must be a positive value")
    @NotNull(message = "Mileage must be not null or blank")
    public Integer getMileage() {
        return mileage;
    }

    @Positive(message = "Offer`s price must be a positive value")
    @NotNull(message = "Price must be not null or blank")
    public Double getPrice() {
        return price;
    }

    @NotNull(message = "Please choose a transmission")
    public Transmission getTransmission() {
        return transmission;
    }

    @Positive(message = "Offer's year must be a positive value lower than 10000")
    @Max(9999)
    @NotNull(message = "Year must be not null or blank")
    public Integer getYear() {
        return year;
    }

    @NotBlank(message = "Please choose a model")
    public String getModelIdentifier() {
        return modelIdentifier;
    }

    @NotBlank(message = "Please choose a seller")
    public String getSellerIdentifier() {
        return sellerIdentifier;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setModelIdentifier(String modelIdentifier) {
        this.modelIdentifier = modelIdentifier;
    }

    public void setSellerIdentifier(String sellerIdentifier) {
        this.sellerIdentifier = sellerIdentifier;
    }
}
