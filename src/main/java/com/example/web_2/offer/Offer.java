package com.example.web_2.offer;

import com.example.web_2.model.Model;
import com.example.web_2.user.User;
import com.example.web_2.baseEntity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {
    private String description;
    private Engine engine;
    private String imageUrl;
    private int mileage;
    private double price;
    private Transmission transmission;
    private int year;
    private Model model;
    private User seller;

    @NotBlank(message = "Offer's description can't be blank")
    public String getDescription() {
        return description;
    }

    @Enumerated
    @NotNull(message = "Offer's engine can't be null")
    public Engine getEngine() {
        return engine;
    }

    @NotBlank(message = "Offer's image URL can't be blank")
    public String getImageUrl() {
        return imageUrl;
    }

    @Positive(message = "Offer's mileage must be a positive value")
    public int getMileage() {
        return mileage;
    }

    @Positive(message = "Offer's price must be a positive value")
    public double getPrice() {
        return price;
    }

    @Enumerated
    @NotNull(message = "Offer's transmission can't be null")
    public Transmission getTransmission() {
        return transmission;
    }

    @Positive(message = "Offer's year must be a positive value lower than 10000")
    @Max(9999)
    public int getYear() {
        return year;
    }

    @ManyToOne
    @JoinColumn(name = "model_id")
    @NotNull(message = "Offer's model can't be null")
    public Model getModel() {
        return model;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "Offer's seller can't be null")
    public User getSeller() {
        return seller;
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

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
}
