package com.example.web_2.user.user_role;

import com.example.web_2.user.user_role.dto.UserRolePageResDto;
import com.example.web_2.user.user_role.dto.UserRoleReqDto;
import com.example.web_2.user.user_role.dto.UserRoleResDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user-role")
public class UserRoleController {
    private UserRoleService userRoleService;

    @GetMapping
    public ModelAndView getUserRolePage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            ModelAndView maw
    ) {
        UserRolePageResDto userRolePage = userRoleService.getPage(page, pageSize);
        maw.addObject("userRolePage", userRolePage);
        maw.setViewName("user-role-page");
        return maw;
    }

    @GetMapping("/{id}")
    public ModelAndView getUserRoleById(@PathVariable String id, ModelAndView maw) {
        UserRoleResDto userRole = userRoleService.getById(id);
        maw.addObject("userRole", userRole);
        maw.setViewName("user-role");
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createUserRole(ModelAndView maw) {
        maw.addObject("userRoleReqDto", new UserRoleReqDto());
        maw.setViewName("user-role-add");
        return maw;
    }

    @PostMapping("/add")
    public ModelAndView createUserRole(@Valid UserRoleReqDto userRoleReqDto, BindingResult bindingResult,
                                       ModelAndView maw) {
        if (bindingResult.hasErrors()) {
            maw.addObject("userRoleReqDto", userRoleReqDto);
            maw.addObject("org.springframework.validation.BindingResult.userRoleReqDto",
                    bindingResult);
            maw.setViewName("user-role-add");
        } else {
            userRoleService.create(userRoleReqDto.getName().ordinal());
            maw.setViewName("redirect:/user-role");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateUserRole(@PathVariable String id, ModelAndView maw) {
        maw.addObject("userRoleReqDto", userRoleService.getForUpdate(id));
        maw.addObject("userRoleId", id);
        maw.setViewName("user-role-update");
        return maw;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateUserRole(@PathVariable String id, @Valid UserRoleReqDto userRoleReqDto,
                                    BindingResult bindingResult, ModelAndView maw) {
        if (bindingResult.hasErrors()) {
            maw.addObject("userRoleReqDto", userRoleReqDto);
            maw.addObject("userRoleId", id);
            maw.addObject("org.springframework.validation.BindingResult.userRoleReqDto",
                    bindingResult);
            maw.setViewName("user-role-update");
        } else {
            userRoleService.update(id, userRoleReqDto.getName().getValue());
            maw.setViewName("redirect:/user-role/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteUserRole(@PathVariable String id, ModelAndView maw) {
        userRoleService.delete(id);
        maw.setViewName("redirect:/user-role");
        return maw;
    }

    @Autowired
    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }
}
