package com.example.web_2.brand;

import com.example.web_2.brand.dto.BrandPageResDto;
import com.example.web_2.brand.dto.BrandReqDto;
import com.example.web_2.brand.dto.BrandResDto;
import com.example.web_2.brand.exception.BrandAlreadyExistsException;
import com.example.web_2.brand.exception.BrandNotFoundException;
import com.example.web_2.util.PaginationValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrandServiceImpl implements BrandService {
    private BrandRepository brandRepository;
    private final ModelMapper mapper;

    public BrandServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<BrandResDto> allBrands() {
        return brandRepository.findAll()
                .stream()
                .map(brand -> mapper.map(brand, BrandResDto.class))
                .toList();
    }

    @Override
    public BrandPageResDto getPage(int pageNumber, int pageSize) {
        long elementsCount = brandRepository.count();
        int totalPages = PaginationValidator
                .validatePagination(pageNumber, pageSize, elementsCount); //pagination validation
        Page<Brand> page = brandRepository.findAll(PageRequest.of(pageNumber - 1, pageSize,
                Sort.by("modified").descending()));
        List<BrandResDto> list = page
                .getContent()
                .stream()
                .map(brand -> mapper.map(brand, BrandResDto.class))
                .toList();
        return new BrandPageResDto(pageNumber, totalPages, pageSize, list);
    }

    @Override
    public BindingResult validateUniqueName(String id, BrandReqDto brandReqDto, BindingResult bindingResult) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fieldError -> !("Brand name already exists!").equals(fieldError.getDefaultMessage()) ||
                        !getById(id).getName().equals(fieldError.getRejectedValue()))
                .toList();
        bindingResult = new BeanPropertyBindingResult(brandReqDto, "userReqDto");
        for (FieldError e:
                errorsToKeep) {
            bindingResult.addError(e);
        }
        return bindingResult;
    }

    @Override
    public BrandResDto getById(String id) {
        Optional<Brand> optionalBrand = brandRepository.findById(UUID.fromString(id));
        if (optionalBrand.isEmpty())
            throw new BrandNotFoundException(String.format("Brand with id \"%s\" not found", id));
        return mapper.map(optionalBrand.get(), BrandResDto.class);
    }

    @Override
    public BrandReqDto getForUpdate(String id) {
        Optional<Brand> optionalBrand = brandRepository.findById(UUID.fromString(id));
        if (optionalBrand.isEmpty())
            throw new BrandNotFoundException(String.format("Brand with id \"%s\" not found", id));
        return mapper.map(optionalBrand.get(), BrandReqDto.class);
    }

    @Override
    public BrandResDto create(String name) {
        BrandReqDto brandReqDto = new BrandReqDto();
        brandReqDto.setName(name);
        if (brandRepository.findByName(name).isPresent())
            throw new BrandAlreadyExistsException(String.format("Brand with name \"%s\" already exists", name));
        Brand brand = mapper.map(brandReqDto, Brand.class);
        LocalDateTime current = LocalDateTime.now();
        brand.setCreated(current);
        brand.setModified(current);
        brandRepository.saveAndFlush(brand);
        return mapper.map(brand, BrandResDto.class);
    }

    public List<BrandResDto> create(List<String> names) {
        List<BrandReqDto> brandReqDtos = names.stream().map(name -> {
                    BrandReqDto dto = new BrandReqDto();
                    dto.setName(name);
                    return dto;
                }).toList();
        List<Brand> brands = brandReqDtos.stream().map(dto -> {
            Brand brand = mapper.map(dto, Brand.class);
            LocalDateTime current = LocalDateTime.now();
            brand.setCreated(current);
            brand.setModified(current);
            return brand;
        }).toList();
        brandRepository.saveAllAndFlush(brands);
        return brands.stream().map(brand -> mapper.map(brand, BrandResDto.class)).toList();
    }


    @Override
    public BrandResDto update(String id, String name) {
        BrandReqDto brandReqDto = new BrandReqDto();
        brandReqDto.setName(name);
        Optional<Brand> optionalBrand = brandRepository.findById(UUID.fromString(id));
        if (optionalBrand.isEmpty())
            throw new BrandNotFoundException(String.format("Brand with id \"%s\" not found", id));
        Brand brand = optionalBrand.get();
        mapper.map(brandReqDto, brand);
        brand.setModified(LocalDateTime.now());
        brandRepository.saveAndFlush(brand);
        return mapper.map(brand, BrandResDto.class);
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (brandRepository.findById(uuid).isEmpty())
            throw new BrandNotFoundException(String.format("Brand with id \"%s\" not found", id));
        brandRepository.deleteById(uuid);
    }

    @Autowired
    public void setBrandRepository(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
}
