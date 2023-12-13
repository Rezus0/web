package com.example.web_2.offer.dto;

import java.util.List;

public class OfferPageResDto {
    private int page;
    private int totalPages;
    private int pageSize;
    private List<OfferResDto> offers;

    public OfferPageResDto() {
    }

    public OfferPageResDto(int page, int totalPages, int pageSize, List<OfferResDto> offers) {
        this.page = page;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.offers = offers;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<OfferResDto> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferResDto> offers) {
        this.offers = offers;
    }
}
