package com.example.web_2.user;

import com.example.web_2.user.dto.UserPageResDto;
import com.example.web_2.user.dto.UserReqDto;
import com.example.web_2.user.dto.UserResDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {
    List<UserResDto> allUsers();
    UserPageResDto getPage(int pageNumber, int pageSize);
    UserResDto getById(String id);
    UserReqDto getForUpdate(String id);
    UserResDto create(UserReqDto userReqDto);
    List<UserResDto> create(List<UserReqDto> userReqDtos);
    UserResDto update(String id, UserReqDto userReqDto);
    void delete(String id);
    BindingResult validateUniqueName(String id, UserReqDto userReqDto, BindingResult bindingResult);
}
