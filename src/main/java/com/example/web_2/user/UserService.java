package com.example.web_2.user;

import com.example.web_2.user.dto.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;

public interface UserService {
    List<UserResDto> allUsers();
    UserPageResDto getPage(int pageNumber, int pageSize);
    UserResDto getById(String id);
    UserReqDto getForUpdate(String id);
    ProfileUpdateDto getProfileForUpdate(String username);
    BindingResult validateOldPassword(String oldPassword, String username, BindingResult bindingResult);
    void updateUserProfile(ProfileUpdateDto dto, String username);
    SecurityContext register(UserRegDto userRegDto);
    UserProfileView getUserProfile(String username);
    UserResDto create(UserReqDto userReqDto);
    List<UserResDto> create(List<UserReqDto> userReqDtos);
    UserResDto update(String id, UserReqDto userReqDto);
    void delete(String id);
    BindingResult validateUniqueName(String id, BaseUserDto baseUserDto,
                            BindingResult bindingResult, String objectName);
    BindingResult validateUniqueName(Principal principal, BaseUserDto baseUserDto,
                            BindingResult bindingResult, String objectName);
}
