package com.example.web_2.user;

import com.example.web_2.user.dto.*;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {
    List<UserResDto> allUsers();
    UserPageResDto getPage(int pageNumber, int pageSize);
    UserResDto getById(String id);
    UserReqDto getForUpdate(String id);
    UserResDto register(UserRegDto userRegDto);
    UserProfileView getUserProfile(String username);
    UserResDto create(UserReqDto userReqDto);
    List<UserResDto> create(List<UserReqDto> userReqDtos);
    UserResDto update(String id, UserReqDto userReqDto);
    void delete(String id);
    BindingResult validateUniqueName(String id, UserReqDto userReqDto, BindingResult bindingResult);
}
