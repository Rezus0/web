package com.example.web_2.offer;

import com.example.web_2.model.ModelService;
import com.example.web_2.offer.dto.OfferPageResDto;
import com.example.web_2.offer.dto.OfferReqDto;
import com.example.web_2.offer.dto.OfferResDto;
import com.example.web_2.offer.dto.UserOffersView;
import com.example.web_2.user.UserService;
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
@RequestMapping("/offer")
public class OfferController {
    private OfferService offerService;
    private ModelService modelService;
    private UserService userService;
    private final static Logger LOG = LogManager.getLogger(Controller.class);

    @GetMapping
    public ModelAndView getOfferPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(required = false) String sortByPrice,
            ModelAndView maw
    ) {
        LOG.log(Level.INFO, String.format("Try to show offer page %d of size %d", page, pageSize));
        OfferPageResDto offerPage = offerService.getPage(page, pageSize, sortByPrice);
        LOG.log(Level.INFO, String.format("Show offer page %d of %d", offerPage.getPage(), offerPage.getTotalPages()));
        maw.addObject("offerPage", offerPage);
        maw.addObject("sortByPrice", sortByPrice);
        maw.setViewName("offer-page");
        return maw;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@offerService.getById(#id).seller.id == authentication.principal.id.toString() or hasRole('ADMIN')")
    public ModelAndView getOfferById(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show offer by ID: %s for: %s", id, principal.getName()));
        OfferResDto offer = offerService.getById(id);
        maw.addObject("offer", offer);
        maw.setViewName("offer");
        return maw;
    }

    @GetMapping("/for-user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id.toString() or hasRole('ADMIN')")
    public ModelAndView getOffersForUser(@PathVariable String userId, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show user: %s offers for: %s", userId, principal.getName()));
        UserOffersView userOffersView = offerService.getOffersForUser(userId);
        maw.addObject("userOffersView", userOffersView);
        maw.setViewName("user-offers");
        return maw;
    }

    @GetMapping("/sign-up")
    public ModelAndView signUpOffer(ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show offer sign-up-form for: %s", principal.getName()));
        maw.addObject("offerReqDto", new OfferReqDto());
        maw.addObject("models", modelService.allModels());
        maw.setViewName("offer-sign-up");
        return maw;
    }

    @PostMapping("/sign-up")
    public ModelAndView signUpOffer(@Valid OfferReqDto offerReqDto, BindingResult bindingResult,
                                    Principal principal, ModelAndView maw) {
        LOG.log(Level.INFO, String.format("Try to sing up new offer for: %s", principal.getName()));
        if (bindingResult.hasErrors() &&
                !(bindingResult.getFieldErrors().size() == 1 && bindingResult.hasFieldErrors("sellerIdentifier"))) {
            maw.addObject("offerReqDto", offerReqDto);
            maw.addObject("models", modelService.allModels());
            maw.addObject("org.springframework.validation.BindingResult.offerReqDto",
                    bindingResult);
            maw.setViewName("offer-sign-up");
        } else {
            LOG.log(Level.INFO, String.format("Sing up new offer for: %s", principal.getName()));
            String userId = offerService.signUpOffer(offerReqDto, principal.getName());
            maw.setViewName(String.format("redirect:/offer/for-user/%s", userId));
        }
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createOffer(ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show offer add-form for: %s", principal.getName()));
        maw.addObject("offerReqDto", new OfferReqDto());
        maw.addObject("models", modelService.allModels());
        maw.addObject("sellers", userService.allUsers());
        maw.setViewName("offer-add");
        return maw;
    }

    @PostMapping("/add")
    public ModelAndView createOffer(@Valid OfferReqDto offerReqDto, BindingResult bindingResult,
                                    ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Try to add offer for: %s", principal.getName()));
        if (bindingResult.hasErrors()) {
            maw.addObject("offerReqDto", offerReqDto);
            maw.addObject("models", modelService.allModels());
            maw.addObject("sellers", userService.allUsers());
            maw.addObject("org.springframework.validation.BindingResult.offerReqDto",
                    bindingResult);
            maw.setViewName("offer-add");
        } else {
            LOG.log(Level.INFO, String.format("Add offer by: %s", principal.getName()));
            offerService.create(offerReqDto);
            maw.setViewName("redirect:/offer");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateOffer(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show offer update-form for: %s", principal.getName()));
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
                                    BindingResult bindingResult, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Try to update offer %s for: %s", id, principal.getName()));
        if (bindingResult.hasErrors()) {
            maw.addObject("offerReqDto", offerReqDto);
            maw.addObject("models", modelService.allModels());
            maw.addObject("sellers", userService.allUsers());
            maw.addObject("offerId", id);
            maw.addObject("org.springframework.validation.BindingResult.offerReqDto",
                    bindingResult);
            maw.setViewName("offer-update");
        } else {
            LOG.log(Level.INFO, String.format("Update offer %s by: %s", id, principal.getName()));
            offerService.update(id, offerReqDto);
            maw.setViewName("redirect:/offer/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteOffer(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Delete offer %s by: %s", id, principal.getName()));
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
