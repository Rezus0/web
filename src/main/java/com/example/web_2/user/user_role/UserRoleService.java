package com.example.web_2.user.user_role;

import com.example.web_2.user.user_role.dto.UserRolePageResDto;
import com.example.web_2.user.user_role.dto.UserRoleReqDto;
import com.example.web_2.user.user_role.dto.UserRoleResDto;

import java.util.List;

public interface UserRoleService {
    List<UserRoleResDto> allUserRoles();
    UserRolePageResDto getPage(int pageNumber, int pageSize);
    UserRoleResDto getById(String id);
    UserRoleReqDto getForUpdate(String id);
    UserRoleResDto create(int roleValue);
    List<UserRoleResDto> create(List<Integer> roleValues);
    UserRoleResDto update(String id, int roleValue);
    void delete(String id);
}
