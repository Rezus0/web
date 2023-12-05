package com.example.web_2.user;

import com.example.web_2.user.dto.UserPageResDto;
import com.example.web_2.user.dto.UserReqDto;
import com.example.web_2.user.dto.UserResDto;
import com.example.web_2.user.exception.UserAlreadyExistsException;
import com.example.web_2.user.exception.UserNotFoundException;
import com.example.web_2.user.user_role.UserRole;
import com.example.web_2.user.user_role.UserRoleRepository;
import com.example.web_2.user.user_role.exception.UserRoleNotFoundException;
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
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private final ModelMapper mapper;

    public UserServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<UserResDto> allUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> mapper.map(user, UserResDto.class))
                .toList();
    }

    @Override
    public UserPageResDto getPage(int pageNumber, int pageSize) {
        long elementsCount = userRepository.count();
        int totalPages = PaginationValidator.validatePagination(pageNumber, pageSize, elementsCount);
        Page<User> page = userRepository.findAll(PageRequest.of(pageNumber - 1, pageSize,
                Sort.by("modified").descending()));
        List<UserResDto> list = page
                .getContent()
                .stream()
                .map(user -> mapper.map(user, UserResDto.class))
                .toList();
        return new UserPageResDto(pageNumber, totalPages, pageSize, list);
    }

    @Override
    public UserResDto getById(String id) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with id \"%s\" not found", id));
        return mapper.map(optionalUser.get(), UserResDto.class);
    }

    @Override
    public UserReqDto getForUpdate(String id) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with id \"%s\" not found", id));
        return mapper.map(optionalUser.get(), UserReqDto.class);
    }

    @Override
    public UserResDto create(UserReqDto userReqDto) {
        String username = userReqDto.getUsername();
        if (userRepository.findUserByUsername(username).isPresent())
            throw new UserAlreadyExistsException(String.format("Username \"%s\" already exists", username));
        User user = mapper.map(userReqDto, User.class);
        validateAndSetUserRole(user, userReqDto.getRoleIdentifier());
        user.setActive(false);
        LocalDateTime current = LocalDateTime.now();
        user.setCreated(current);
        user.setModified(current);
        userRepository.saveAndFlush(user);
        return mapper.map(user, UserResDto.class);
    }

    public List<UserResDto> create(List<UserReqDto> userReqDtos) {
        List<User> users = userReqDtos.stream().map(dto -> {
            User user = mapper.map(dto, User.class);
            validateAndSetUserRole(user, dto.getRoleIdentifier());
            LocalDateTime current = LocalDateTime.now();
            user.setCreated(current);
            user.setModified(current);
            return user;
        }).toList();
        userRepository.saveAllAndFlush(users);
        return users.stream().map(user -> mapper.map(user, UserResDto.class)).toList();
    }

    @Override
    public UserResDto update(String id, UserReqDto userReqDto) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with id \"%s\" not found", id));
        String username = userReqDto.getUsername();
        Optional<User> userForValidation = userRepository.findUserByUsername(username);
        if (userForValidation.isPresent() && !userForValidation.get().getId().toString().equals(id))
            throw new UserAlreadyExistsException(String.format("Username \"%s\" already exists", username));
        User user = optionalUser.get();
        mapper.map(userReqDto, user);
        validateAndSetUserRole(user, userReqDto.getRoleIdentifier());
        user.setModified(LocalDateTime.now());
        userRepository.saveAndFlush(user);
        return mapper.map(user, UserResDto.class);
    }

    @Override
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
        if (userRepository.findById(uuid).isEmpty())
            throw new UserNotFoundException(String.format("User with id \"%s\" not found", id));
        userRepository.deleteById(uuid);
    }

    private void validateAndSetUserRole(User user, String userRoleId) {
        Optional<UserRole> optionalUserRole = userRoleRepository.findById(UUID.fromString(userRoleId));
        if (optionalUserRole.isEmpty())
            throw new UserRoleNotFoundException(String.format("User role with id \"%s\" not found", userRoleId));
        user.setRole(optionalUserRole.get());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
}
