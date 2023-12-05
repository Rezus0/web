package com.example.web_2.offer;

import com.example.web_2.offer.dto.OfferPageResDto;
import com.example.web_2.offer.dto.OfferReqDto;
import com.example.web_2.offer.dto.OfferResDto;
import com.example.web_2.offer.dto.UserOffersView;

import java.util.List;

public interface OfferService {
    List<OfferResDto> allOffers();
    OfferPageResDto getPage(int pageNumber, int pageSize);
    OfferResDto getById(String id);
    OfferReqDto getForUpdate(String id);
    UserOffersView getOffersForUser(String userId);
    OfferResDto create(OfferReqDto offerReqDto);
    List<OfferResDto> create(List<OfferReqDto> offerReqDtos);
    OfferResDto update(String id, OfferReqDto offerReqDto);
    void delete(String id);
}
