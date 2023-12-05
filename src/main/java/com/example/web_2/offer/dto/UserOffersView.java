package com.example.web_2.offer.dto;

import java.util.List;

public class UserOffersView {
    private String firstName;
    private String lastName;
    private String username;
    private List<OfferResDto> offers;

    public UserOffersView(String firstName, String lastName, String username, List<OfferResDto> offers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.offers = offers;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OfferResDto> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferResDto> offers) {
        this.offers = offers;
    }
}
