package com.example.web_2.offer.dto;

import com.example.web_2.baseEntity.BaseResDto;
import com.example.web_2.model.dto.ModelResDto;
import com.example.web_2.offer.Engine;
import com.example.web_2.offer.Transmission;
import com.example.web_2.user.dto.UserResDto;

public class OfferResDto extends BaseResDto {
    private String id;
    private String description;
    private Engine engine;
    private String imageUrl;
    private int mileage;
    private double price;
    private Transmission transmission;
    private int year;
    private ModelResDto model;
    private UserResDto seller;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ModelResDto getModel() {
        return model;
    }

    public void setModel(ModelResDto model) {
        this.model = model;
    }

    public UserResDto getSeller() {
        return seller;
    }

    public void setSeller(UserResDto seller) {
        this.seller = seller;
    }
}
