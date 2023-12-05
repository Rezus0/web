package com.example.web_2.user.user_role;

import com.example.web_2.user.user_role.dto.UserRolePageResDto;
import com.example.web_2.user.user_role.dto.UserRoleReqDto;
import com.example.web_2.user.user_role.dto.UserRoleResDto;
import com.example.web_2.user.user_role.exception.UserRoleNotFoundException;
import com.example.web_2.util.PaginationValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private UserRoleRepository userRoleRepository;
    private final ModelMapper mapper;

    public UserRoleServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<UserRoleResDto> allUserRoles() {
        return userRoleRepository.findAll()
                .stream()
                .map(role -> mapper.map(role, UserRoleResDto.class))
                .toList();
    }

    @Override
    public UserRolePageResDto getPage(int pageNumber, int pageSize) {
        long elementsCount = userRoleRepository.count();
        int totalPages = PaginationValidator.validatePagination(pageNumber, pageSize, elementsCount);
        Page<UserRole> page = userRoleRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        List<UserRoleResDto> list = page
                .getContent()
                .stream()
                .map(userRole -> mapper.map(userRole, UserRoleResDto.class))
                .toList();
        return new UserRolePageResDto(pageNumber, totalPages, list);
    }

    @Override
    public UserRoleResDto getById(String id) {
        Optional<UserRole> optionalUserRole = userRoleRepository.findById(UUID.fromString(id));
        if (optionalUserRole.isEmpty())
            throw new UserRoleNotFoundException(String.format("User role with id \"%s\" not found", id));
        return mapper.map(optionalUserRole.get(), UserRoleResDto.class);
    }

    @Override
    public UserRoleReqDto getForUpdate(String id) {
        Optional<UserRole> optionalUserRole = userRoleRepository.findById(UUID.fromString(id));
        if (optionalUserRole.isEmpty())
            throw new UserRoleNotFoundException(String.format("User role with id \"%s\" not found", id));
        return mapper.map(optionalUserRole.get(), UserRoleReqDto.class);
    }

    @Override
    public UserRoleResDto create(int roleValue) {
        UserRoleReqDto userRoleReqDto = new UserRoleReqDto();
        userRoleReqDto.setName(Role.getRoleByValue(roleValue));
        UserRole userRole = mapper.map(userRoleReqDto, UserRole.class);
        userRoleRepository.saveAndFlush(userRole);
        return mapper.map(userRole, UserRoleResDto.class);
    }

    public List<UserRoleResDto> create(List<Integer> roleValues) {
        List<UserRoleReqDto> userRoleReqDtos = roleValues.stream()
                .map(value -> {
                    UserRoleReqDto userRoleReqDto = new UserRoleReqDto();
                    userRoleReqDto.setName(Role.getRoleByValue(value));
                    return userRoleReqDto;
                }).toList();
        List<UserRole> userRoles = userRoleReqDtos.stream()
                .map(dto -> mapper.map(dto, UserRole.class))
                .toList();
        userRoleRepository.saveAllAndFlush(userRoles);
        return userRoles.stream()
                .map(userRole -> mapper.map(userRole, UserRoleResDto.class))
                .toList();
    }

    @Override
    public UserRoleResDto update(String id, int roleValue) {
        UserRoleReqDto userRoleReqDto = new UserRoleReqDto();
        userRoleReqDto.setName(Role.getRoleByValue(roleValue));
        Optional<UserRole> optionalUserRole = userRoleRepository.findById(UUID.fromString(id));
        if (optionalUserRole.isEmpty())
            throw new UserRoleNotFoundException(String.format("User role with id \"%s\" not found", id));
        UserRole userRole = optionalUserRole.get();
        mapper.map(userRoleReqDto, userRole);
        userRoleRepository.saveAndFlush(userRole);
        return mapper.map(userRole, UserRoleResDto.class);
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (userRoleRepository.findById(uuid).isEmpty())
            throw new UserRoleNotFoundException(String.format("User role with id \"%s\" not found", id));
        userRoleRepository.deleteById(uuid);
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
}
