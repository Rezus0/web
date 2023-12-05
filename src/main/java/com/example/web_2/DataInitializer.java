package com.example.web_2;

import com.example.web_2.brand.BrandService;
import com.example.web_2.brand.dto.BrandPageResDto;
import com.example.web_2.brand.dto.BrandResDto;
import com.example.web_2.model.Category;
import com.example.web_2.model.ModelService;
import com.example.web_2.model.dto.ModelPageResDto;
import com.example.web_2.model.dto.ModelReqDto;
import com.example.web_2.model.dto.ModelResDto;
import com.example.web_2.offer.Engine;
import com.example.web_2.offer.OfferService;
import com.example.web_2.offer.Transmission;
import com.example.web_2.offer.dto.OfferReqDto;
import com.example.web_2.offer.dto.OfferResDto;
import com.example.web_2.user.UserService;
import com.example.web_2.user.dto.UserReqDto;
import com.example.web_2.user.dto.UserResDto;
import com.example.web_2.user.user_role.UserRoleService;
import com.example.web_2.user.user_role.dto.UserRoleResDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataInitializer implements CommandLineRunner {
    private BrandService brandService;
    private ObjectMapper objectMapper;
    private ModelService modelService;
    private UserService userService;
    private UserRoleService userRoleService;
    private OfferService offerService;

    @Override
    public void run(String... args) throws Exception {
        List<String> brandNames = new ArrayList<>();
        List<ModelReqDto> modelReqDtos = new ArrayList<>();
        List<Integer> userRoleValues = new ArrayList<>();
        List<UserReqDto> userReqDtos = new ArrayList<>();
        List<OfferReqDto> offerReqDtos = new ArrayList<>();
        
        Faker faker = new Faker(Locale.US);
        
        for (int i = 0; i < 100; i++) {
            brandNames.add(faker.company().name());
        }

        List<BrandResDto> brandResDtos = brandService.create(brandNames);

        for (int i = 0; i < 100; i++) {
            ModelReqDto modelReqDto = new ModelReqDto();
            modelReqDto.setName(faker.name().title());
            modelReqDto.setCategory(Category.values()[faker.number().numberBetween(0, 4)]);
            modelReqDto.setImageUrl(faker.company().logo());
            int startYear = faker.number().numberBetween(1000, 9999);
            modelReqDto.setStartYear(startYear);
            modelReqDto.setEndYear(faker.number().numberBetween(startYear, 9999));
            modelReqDto.setBrandIdentifier(brandResDtos.get(faker.number().numberBetween(0, 100)).getId());
            modelReqDtos.add(modelReqDto);
        }

        List<ModelResDto> modelResDtos = modelService.create(modelReqDtos);

        for (int i = 0; i < 100; i++) {
            userRoleValues.add(faker.number().numberBetween(0, 2));
        }

        List<UserRoleResDto> userRoleResDtos = userRoleService.create(userRoleValues);

        for (int i = 0; i < 100; i++) {
            UserReqDto userReqDto = new UserReqDto();
            userReqDto.setUsername(faker.name().username());
            userReqDto.setPassword(faker.internet().password(true));
            userReqDto.setFirstName(faker.name().firstName());
            userReqDto.setLastName(faker.name().lastName());
            userReqDto.setRoleIdentifier(userRoleResDtos.get(faker.number().numberBetween(0, 100)).getId());
            userReqDto.setImageUrl(faker.internet().image());
            userReqDtos.add(userReqDto);
        }

        List<UserResDto> userResDtos = userService.create(userReqDtos);

        for (int i = 0; i < 100; i++) {
            OfferReqDto offerReqDto = new OfferReqDto();
            offerReqDto.setDescription(faker.company().catchPhrase());
            offerReqDto.setEngine(Engine.values()[faker.number().numberBetween(0, 4)]);
            offerReqDto.setImageUrl(faker.internet().image());
            offerReqDto.setMileage(faker.number().numberBetween(1, 99999999));
            offerReqDto.setPrice(faker.number().randomDouble(2, 1, 999999));
            offerReqDto.setTransmission(Transmission.values()[faker.number().numberBetween(0, 2)]);
            offerReqDto.setYear(faker.number().numberBetween(0, 9999));
            offerReqDto.setModelIdentifier(modelResDtos.get(faker.number().numberBetween(0, 100)).getId());
            offerReqDto.setSellerIdentifier(userResDtos.get(faker.number().numberBetween(0, 100)).getId());
            offerReqDtos.add(offerReqDto);
        }

        List<OfferResDto> offerResDtos = offerService.create(offerReqDtos);

        BrandPageResDto brandPageResDto = brandService.getPage(1, 10);
        String jsonBrand = objectMapper.writeValueAsString(brandPageResDto);
        System.out.println(jsonBrand);

        brandService.delete(brandResDtos.get(17).getId());
        userRoleService.delete(userRoleResDtos.get(22).getId());
        userService.delete(userResDtos.get(33).getId());
        offerService.delete(offerResDtos.get(54).getId());

        brandPageResDto = brandService.getPage(2, 10);
        jsonBrand = objectMapper.writeValueAsString(brandPageResDto);
        System.out.println(jsonBrand);

        ModelPageResDto modelPageResDto = modelService.getPage(3, 20);
        String jsonModel = objectMapper.writeValueAsString(modelPageResDto);
        System.out.println(jsonModel);

        ModelResDto modelResDto = modelService.getById(modelResDtos.get(7).getId());
        jsonModel = objectMapper.writeValueAsString(modelResDto);
        System.out.println(jsonModel);

        modelReqDtos.get(0).setName("AAAAAAAAAA");
        offerReqDtos.get(98).setDescription("AAAAAAAAAA");
        userReqDtos.get(96).setUsername("AAAAAAAAAA");
        modelResDto = modelService.update(modelResDtos.get(0).getId(), modelReqDtos.get(0));
        var offer = offerService.update(offerResDtos.get(98).getId(), offerReqDtos.get(98));
        var userRole = userRoleService.update(userRoleResDtos.get(97).getId(), 1);
        var user = userService.update(userResDtos.get(96).getId(), userReqDtos.get(96));
        var brand = brandService.update(brandResDtos.get(95).getId(), "AAAAAAAA");
        jsonModel = objectMapper.writeValueAsString(modelResDto);
        String jsonModel2 = objectMapper.writeValueAsString(offer);
        String jsonModel3 = objectMapper.writeValueAsString(userRole);
        String jsonModel4 = objectMapper.writeValueAsString(user);
        String jsonModel5 = objectMapper.writeValueAsString(brand);
        System.out.println("-------------------------------------");
        System.out.println(jsonModel);
        System.out.println(jsonModel2);
        System.out.println(jsonModel3);
        System.out.println(jsonModel4);
        System.out.println(jsonModel5);
        System.out.println("-------------------------------------");

        modelService.delete(modelResDtos.get(0).getId());
        modelPageResDto = modelService.getPage(1, 7);
        jsonModel = objectMapper.writeValueAsString(modelPageResDto);
        System.out.println(jsonModel);

        brandPageResDto = brandService.getPage(1, 3);
        jsonBrand = objectMapper.writeValueAsString(brandPageResDto);
        System.out.println(jsonBrand);
    }

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }
}
