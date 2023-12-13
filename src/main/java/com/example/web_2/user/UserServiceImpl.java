package com.example.web_2.user;

import com.example.web_2.user.dto.*;
import com.example.web_2.user.exception.UserAlreadyExistsException;
import com.example.web_2.user.exception.UserNotFoundException;
import com.example.web_2.user.user_role.Role;
import com.example.web_2.user.user_role.UserRole;
import com.example.web_2.user.user_role.UserRoleRepository;
import com.example.web_2.user.user_role.exception.UserRoleNotFoundException;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableCaching
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(ModelMapper mapper, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Cacheable(cacheNames = "users")
    public List<UserResDto> allUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> mapper.map(user, UserResDto.class))
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "userPage", key = "#pageNumber + '-' + #pageSize")
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
    public BindingResult validateUniqueName(String id, BaseUserDto baseUserDto,
                                            BindingResult bindingResult, String objectName) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fieldError -> !("Username already exists!").equals(fieldError.getDefaultMessage()) ||
                        !getById(id).getUsername().equals(fieldError.getRejectedValue()))
                .toList();
        bindingResult = new BeanPropertyBindingResult(baseUserDto, objectName);
        for (FieldError e :
                errorsToKeep) {
            bindingResult.addError(e);
        }
        return bindingResult;
    }

    @Override
    public BindingResult validateUniqueName(Principal principal, BaseUserDto baseUserDto,
                                            BindingResult bindingResult, String objectName) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fieldError -> !("Username already exists!").equals(fieldError.getDefaultMessage()) ||
                        !principal.getName().equals(baseUserDto.getUsername()))
                .toList();
        bindingResult = new BeanPropertyBindingResult(baseUserDto, objectName);
        for (FieldError e :
                errorsToKeep) {
            bindingResult.addError(e);
        }
        return bindingResult;
    }

    @Override
    @Cacheable(cacheNames = "users", key = "#id")
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
        User user = optionalUser.get();
        UserReqDto userReqDto = mapper.map(user, UserReqDto.class);
        userReqDto.setPassword("");
        userReqDto.setRoleIdentifier(user.getRole().getId().toString());
        return userReqDto;
    }

    @Override
    public ProfileUpdateDto getProfileForUpdate(String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with username \"%s\" not found", username));
        ProfileUpdateDto profileUpdateDto = mapper.map(optionalUser.get(), ProfileUpdateDto.class);
        profileUpdateDto.setPassword("");
        return profileUpdateDto;
    }

    @Override
    public BindingResult validateOldPassword(String oldPassword, String username, BindingResult bindingResult) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with username \"%s\" not found", username));
        String userPassword = optionalUser.get().getPassword();
        if (!passwordEncoder.matches(oldPassword, userPassword)) {
            bindingResult.addError(new FieldError("profileUpdateDto", "oldPassword",
                    "Old password is invalid"));
        }
        return bindingResult;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "users", allEntries = true),
            @CacheEvict(cacheNames = "userPage", allEntries = true),
            @CacheEvict(cacheNames = "profile", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true)
    })
    public void updateUserProfile(ProfileUpdateDto dto, String username) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with username \"%s\" not found", username));
        String newUsername = dto.getUsername();
        if (!username.equals(newUsername) && userRepository.findUserByUsername(newUsername).isPresent())
            throw new UserAlreadyExistsException(String.format("Username \"%s\" already exists", newUsername));
        User user = optionalUser.get();
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword()))
            throw new IllegalArgumentException("Old password is invalid");
        mapper.map(dto, user);
        user.setModified(LocalDateTime.now());
        userRepository.saveAndFlush(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "users", allEntries = true),
            @CacheEvict(cacheNames = "userPage", allEntries = true),
            @CacheEvict(cacheNames = "profile", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true)
    })
    public SecurityContext register(UserRegDto userRegDto) {
        String rawPassword = userRegDto.getPassword();
        userRegDto.setPassword(passwordEncoder.encode(userRegDto.getPassword()));
        User user = mapper.map(userRegDto, User.class);
        String username = userRegDto.getUsername();
        if (userRepository.findUserByUsername(username).isPresent())
            throw new UserAlreadyExistsException(String.format("Username \"%s\" already exists", username));
        user.setRole(userRoleRepository.findFirstByName(Role.USER).orElseThrow());
        user.setActive(false);
        LocalDateTime current = LocalDateTime.now();
        user.setCreated(current);
        user.setModified(current);
        userRepository.saveAndFlush(user);
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, rawPassword);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(authentication);
        return ctx;
    }

    @Override
    @Cacheable(cacheNames = "profile", key = "#username")
    public UserProfileView getUserProfile(String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with username \"%s\" not found", username));
        return mapper.map(optionalUser, UserProfileView.class);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "users", allEntries = true),
            @CacheEvict(cacheNames = "userPage", allEntries = true),
            @CacheEvict(cacheNames = "profile", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true)
    })
    public UserResDto create(UserReqDto userReqDto) {
        String username = userReqDto.getUsername();
        if (userRepository.findUserByUsername(username).isPresent())
            throw new UserAlreadyExistsException(String.format("Username \"%s\" already exists", username));
        userReqDto.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
        User user = mapper.map(userReqDto, User.class);
        validateAndSetUserRole(user, userReqDto.getRoleIdentifier());
        user.setActive(false);
        LocalDateTime current = LocalDateTime.now();
        user.setCreated(current);
        user.setModified(current);
        userRepository.saveAndFlush(user);
        return mapper.map(user, UserResDto.class);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "users", allEntries = true),
            @CacheEvict(cacheNames = "userPage", allEntries = true),
            @CacheEvict(cacheNames = "profile", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true)
    })
    public List<UserResDto> create(List<UserReqDto> userReqDtos) {
        List<User> users = userReqDtos.stream().map(dto -> {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
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
    @Caching(evict = {
            @CacheEvict(cacheNames = "users", allEntries = true),
            @CacheEvict(cacheNames = "userPage", allEntries = true),
            @CacheEvict(cacheNames = "profile", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true)
    })
    public UserResDto update(String id, UserReqDto userReqDto) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with id \"%s\" not found", id));
        String username = userReqDto.getUsername();
        Optional<User> userForValidation = userRepository.findUserByUsername(username);
        if (userForValidation.isPresent() && !userForValidation.get().getId().toString().equals(id))
            throw new UserAlreadyExistsException(String.format("Username \"%s\" already exists", username));
        User user = optionalUser.get();
        userReqDto.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
        mapper.map(userReqDto, user);
        validateAndSetUserRole(user, userReqDto.getRoleIdentifier());
        user.setModified(LocalDateTime.now());
        userRepository.saveAndFlush(user);
        return mapper.map(user, UserResDto.class);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "users", allEntries = true),
            @CacheEvict(cacheNames = "userPage", allEntries = true),
            @CacheEvict(cacheNames = "profile", allEntries = true),
            @CacheEvict(cacheNames = "userOffers", allEntries = true),
            @CacheEvict(cacheNames = "offerPage", allEntries = true),
            @CacheEvict(cacheNames = "offers", allEntries = true),
            @CacheEvict(cacheNames = "models", allEntries = true),
            @CacheEvict(cacheNames = "brands", allEntries = true)
    })
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
