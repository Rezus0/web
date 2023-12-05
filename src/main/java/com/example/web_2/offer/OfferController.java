package com.example.web_2.offer;

import com.example.web_2.model.ModelService;
import com.example.web_2.offer.dto.OfferPageResDto;
import com.example.web_2.offer.dto.OfferReqDto;
import com.example.web_2.offer.dto.OfferResDto;
import com.example.web_2.offer.dto.UserOffersView;
import com.example.web_2.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/offer")
public class OfferController {
    private OfferService offerService;
    private ModelService modelService;
    private UserService userService;

    @GetMapping
    public ModelAndView getOfferPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            ModelAndView maw
    ) {
        OfferPageResDto offerPage = offerService.getPage(page, pageSize);
        maw.addObject("offerPage", offerPage);
        maw.setViewName("offer-page");
        return maw;
    }

    @GetMapping("/{id}")
    public ModelAndView getOfferById(@PathVariable String id, ModelAndView maw) {
        OfferResDto offer = offerService.getById(id);
        maw.addObject("offer", offer);
        maw.setViewName("offer");
        return maw;
    }

    @GetMapping("/for-user/{userId}")
    public ModelAndView getOffersForUser(@PathVariable String userId, ModelAndView maw) {
        UserOffersView userOffersView = offerService.getOffersForUser(userId);
        maw.addObject("userOffersView", userOffersView);
        maw.setViewName("user-offers");
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createOffer(ModelAndView maw) {
        maw.addObject("offerReqDto", new OfferReqDto());
        maw.addObject("models", modelService.allModels());
        maw.addObject("sellers", userService.allUsers());
        maw.setViewName("offer-add");
        return maw;
    }

    @PostMapping("/add")
    public ModelAndView createOffer(@Valid OfferReqDto offerReqDto, BindingResult bindingResult,
                                    ModelAndView maw) {
        if (bindingResult.hasErrors()) {
            maw.addObject("offerReqDto", offerReqDto);
            maw.addObject("models", modelService.allModels());
            maw.addObject("sellers", userService.allUsers());
            maw.addObject("org.springframework.validation.BindingResult.offerReqDto",
                    bindingResult);
            maw.setViewName("offer-add");
        } else {
            offerService.create(offerReqDto);
            maw.setViewName("redirect:/offer");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateOffer(@PathVariable String id, ModelAndView maw) {
        maw.addObject("offerReqDto", offerService.getForUpdate(id));
        maw.addObject("models", modelService.allModels());
        maw.addObject("sellers", userService.allUsers());
        maw.addObject("offerId", id);
        maw.setViewName("offer-update");
        return maw;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateOffer(@PathVariable String id, @Valid OfferReqDto offerReqDto,
                                    BindingResult bindingResult, ModelAndView maw) {
        if (bindingResult.hasErrors()) {
            maw.addObject("offerReqDto", offerReqDto);
            maw.addObject("models", modelService.allModels());
            maw.addObject("sellers", userService.allUsers());
            maw.addObject("offerId", id);
            maw.addObject("org.springframework.validation.BindingResult.offerReqDto",
                    bindingResult);
            maw.setViewName("offer-update");
        } else {
            offerService.update(id, offerReqDto);
            maw.setViewName("redirect:/offer/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteOffer(@PathVariable String id, ModelAndView maw) {
        offerService.delete(id);
        maw.setViewName("redirect:/offer");
        return maw;
    }

    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }

    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
