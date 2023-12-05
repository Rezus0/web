package com.example.web_2.model;

import com.example.web_2.brand.Brand;
import com.example.web_2.brand.BrandRepository;
import com.example.web_2.brand.exception.BrandNotFoundException;
import com.example.web_2.model.dto.ModelPageResDto;
import com.example.web_2.model.dto.ModelReqDto;
import com.example.web_2.model.dto.ModelResDto;
import com.example.web_2.model.exception.ModelNotFoundException;
import com.example.web_2.util.PaginationValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModelServiceImpl implements ModelService {
    private ModelRepository modelRepository;
    private BrandRepository brandRepository;
    private final ModelMapper mapper;

    public ModelServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<ModelResDto> allModels() {
        return modelRepository.findAll()
                .stream()
                .map(model -> mapper.map(model, ModelResDto.class))
                .toList();
    }

    @Override
    public ModelPageResDto getPage(int pageNumber, int pageSize) {
        long elementsCount = modelRepository.count();
        int totalPages = PaginationValidator.validatePagination(pageNumber, pageSize, elementsCount);
        Page<Model> page = modelRepository.findAll(PageRequest.of(pageNumber - 1, pageSize,
                Sort.by("modified").descending()));
        List<ModelResDto> list = page
                .getContent()
                .stream()
                .map(model -> mapper.map(model, ModelResDto.class))
                .toList();
        return new ModelPageResDto(pageNumber, totalPages, list);
    }

    @Override
    public ModelResDto getById(String id) {
        Optional<Model> optionalModel = modelRepository.findById(UUID.fromString(id));
        if (optionalModel.isEmpty())
            throw new ModelNotFoundException(String.format("Model with id \"%s\" not found", id));
        return mapper.map(optionalModel.get(), ModelResDto.class);
    }

    @Override
    public ModelReqDto getForUpdate(String id) {
        Optional<Model> optionalModel = modelRepository.findById(UUID.fromString(id));
        if (optionalModel.isEmpty())
            throw new ModelNotFoundException(String.format("Model with id \"%s\" not found", id));
        return mapper.map(optionalModel.get(), ModelReqDto.class);
    }

    @Override
    public ModelResDto create(ModelReqDto modelReqDto) {
        Model model = mapper.map(modelReqDto, Model.class);
        validateAndSetBrand(model, modelReqDto.getBrandIdentifier());
        LocalDateTime current = LocalDateTime.now();
        model.setCreated(current);
        model.setModified(current);
        modelRepository.saveAndFlush(model);
        return mapper.map(model, ModelResDto.class);
    }

    public List<ModelResDto> create(List<ModelReqDto> modelReqDtos) {
        List<Model> models = modelReqDtos.stream().map(dto -> {
            Model model = mapper.map(dto, Model.class);
            validateAndSetBrand(model, dto.getBrandIdentifier());
            LocalDateTime current = LocalDateTime.now();
            model.setCreated(current);
            model.setModified(current);
            return model;
        }).toList();
        modelRepository.saveAllAndFlush(models);
        return models.stream().map(model -> mapper.map(model, ModelResDto.class)).toList();
    }

    @Override
    public ModelResDto update(String id, ModelReqDto modelReqDto) {
        Optional<Model> optionalModel = modelRepository.findById(UUID.fromString(id));
        if (optionalModel.isEmpty())
            throw new ModelNotFoundException(String.format("Model with id \"%s\" not found", id));
        Model model = optionalModel.get();
        mapper.map(modelReqDto, model);
        validateAndSetBrand(model, modelReqDto.getBrandIdentifier());
        model.setModified(LocalDateTime.now());
        modelRepository.saveAndFlush(model);
        return mapper.map(model, ModelResDto.class);
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (modelRepository.findById(uuid).isEmpty())
            throw new ModelNotFoundException(String.format("Model with id \"%s\" not found", id));
        modelRepository.deleteById(uuid);
    }

    private void validateAndSetBrand(Model model, String brandId) {
        Optional<Brand> optionalBrand = brandRepository.findById(UUID.fromString(brandId));
        if (optionalBrand.isEmpty())
            throw new BrandNotFoundException(String.format("Brand with id \"%s\" not found", brandId));
        model.setBrand(optionalBrand.get());
    }

    @Autowired
    public void setModelRepository(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Autowired
    public void setBrandRepository(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
}
