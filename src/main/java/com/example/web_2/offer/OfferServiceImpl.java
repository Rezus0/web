package com.example.web_2.offer;

import com.example.web_2.model.Model;
import com.example.web_2.model.ModelRepository;
import com.example.web_2.model.exception.ModelNotFoundException;
import com.example.web_2.offer.dto.OfferPageResDto;
import com.example.web_2.offer.dto.OfferReqDto;
import com.example.web_2.offer.dto.OfferResDto;
import com.example.web_2.offer.dto.UserOffersView;
import com.example.web_2.offer.exception.OfferNotFoundException;
import com.example.web_2.user.User;
import com.example.web_2.user.UserRepository;
import com.example.web_2.user.exception.UserNotFoundException;
import com.example.web_2.util.PaginationValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("offerService")
@EnableCaching
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    private UserRepository userRepository;
    private ModelRepository modelRepository;
    private final ModelMapper mapper;

    public OfferServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Cacheable(cacheNames = "offers")
    public List<OfferResDto> allOffers() {
        return offerRepository.findAll()
                .stream()
                .map(offer -> mapper.map(offer, OfferResDto.class))
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "offerPage", key = "#pageNumber + '-' + #pageSize")
    public OfferPageResDto getPage(int pageNumber, int pageSize) {
        long elementsCount = offerRepository.count();
        int totalPages = PaginationValidator.validatePagination(pageNumber, pageSize, elementsCount);
        Page<Offer> page = offerRepository.findAll(PageRequest.of(pageNumber - 1, pageSize,
                Sort.by("modified").descending()));
        List<OfferResDto> list = page
                .getContent()
                .stream()
                .map(offer -> mapper.map(offer, OfferResDto.class))
                .toList();
        return new OfferPageResDto(pageNumber, totalPages, pageSize, list);
    }

    @Override
    @Cacheable(cacheNames = "offers", key = "#id")
    public OfferResDto getById(String id) {
        Optional<Offer> optionalOffer = offerRepository.findById(UUID.fromString(id));
        if (optionalOffer.isEmpty()) {
            throw new OfferNotFoundException(String.format("Offer with id \"%s\" not found", id));
        }
        return mapper.map(optionalOffer.get(), OfferResDto.class);
    }

    @Override
    public OfferReqDto getForUpdate(String id) {
        Optional<Offer> optionalOffer = offerRepository.findById(UUID.fromString(id));
        if (optionalOffer.isEmpty()) {
            throw new OfferNotFoundException(String.format("Offer with id \"%s\" not found", id));
        }
        Offer offer = optionalOffer.get();
        OfferReqDto offerReqDto = mapper.map(optionalOffer.get(), OfferReqDto.class);
        offerReqDto.setModelIdentifier(offer.getModel().getId().toString());
        offerReqDto.setSellerIdentifier(offer.getSeller().getId().toString());
        return offerReqDto;
    }

    @Override
    @Cacheable(cacheNames = "userOffers", key = "#userId")
    public UserOffersView getOffersForUser(String userId) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(userId));
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with id \"%s\" not found", userId));
        User user = optionalUser.get();
        List<OfferResDto> offers = user.getOffers().stream()
                .map(offer -> mapper.map(offer, OfferResDto.class))
                .toList();
        return new UserOffersView(user.getFirstName(), user.getLastName(), user.getUsername(), offers);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "users", allEntries = true)
    })
    public String signUpOffer(OfferReqDto offerReqDto, String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with username \"%s\" not found", username));
        String userId = optionalUser.get().getId().toString();
        Offer offer = mapper.map(offerReqDto, Offer.class);
        validateAndSetUserAndModel(offer, userId, offerReqDto.getModelIdentifier());
        LocalDateTime current = LocalDateTime.now();
        offer.setCreated(current);
        offer.setModified(current);
        offerRepository.saveAndFlush(offer);
        return userId;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "users", allEntries = true)
    })
    public OfferResDto create(OfferReqDto offerReqDto) {
        Offer offer = mapper.map(offerReqDto, Offer.class);
        validateAndSetUserAndModel(offer, offerReqDto.getSellerIdentifier(), offerReqDto.getModelIdentifier());
        LocalDateTime current = LocalDateTime.now();
        offer.setCreated(current);
        offer.setModified(current);
        offerRepository.saveAndFlush(offer);
        return mapper.map(offer, OfferResDto.class);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "users", allEntries = true)
    })
    public List<OfferResDto> create(List<OfferReqDto> offerReqDtos) {
        List<Offer> offers = offerReqDtos.stream().map(dto -> {
            Offer offer = mapper.map(dto, Offer.class);
            validateAndSetUserAndModel(offer, dto.getSellerIdentifier(), dto.getModelIdentifier());
            LocalDateTime current = LocalDateTime.now();
            offer.setCreated(current);
            offer.setModified(current);
            return offer;
        }).toList();
        offerRepository.saveAllAndFlush(offers);
        return offers.stream().map(offer -> mapper.map(offer, OfferResDto.class)).toList();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "users", allEntries = true)
    })
    public OfferResDto update(String id, OfferReqDto offerReqDto) {
        Optional<Offer> optionalOffer = offerRepository.findById(UUID.fromString(id));
        if (optionalOffer.isEmpty())
            throw new OfferNotFoundException(String.format("Offer with id \"%s\" not found", id));
        Offer offer = optionalOffer.get();
        mapper.map(offerReqDto, offer);
        validateAndSetUserAndModel(offer, offerReqDto.getSellerIdentifier(), offerReqDto.getModelIdentifier());
        offer.setModified(LocalDateTime.now());
        offerRepository.saveAndFlush(offer);
        return mapper.map(offer, OfferResDto.class);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "users", allEntries = true)
    })
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (offerRepository.findById(uuid).isEmpty())
            throw new OfferNotFoundException(String.format("Offer with id \"%s\" not found", id));
        offerRepository.deleteById(uuid);
    }

    private void validateAndSetUserAndModel(Offer offer, String userId, String modelId) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(userId));
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with id \"%s\" not found", userId));
        Optional<Model> optionalModel = modelRepository.findById(UUID.fromString(modelId));
        if (optionalModel.isEmpty())
            throw new ModelNotFoundException(String.format("Model with id \"%s\" not found", modelId));
        offer.setSeller(optionalUser.get());
        offer.setModel(optionalModel.get());
    }

    @Autowired
    public void setOfferRepository(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelRepository(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

}

