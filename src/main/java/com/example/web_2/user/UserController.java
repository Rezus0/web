package com.example.web_2.user;

import com.example.web_2.user.dto.UserPageResDto;
import com.example.web_2.user.dto.UserReqDto;
import com.example.web_2.user.dto.UserResDto;
import com.example.web_2.user.user_role.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private UserRoleService userRoleService;

    @GetMapping
    public ModelAndView getUserPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            ModelAndView maw
    ) {
        UserPageResDto userPage = userService.getPage(page, pageSize);
        maw.addObject("userPage", userPage);
        maw.setViewName("user-page");
        return maw;
    }

    @GetMapping("/{id}")
    public ModelAndView getUserById(@PathVariable String id, ModelAndView maw) {
        UserResDto user = userService.getById(id);
        maw.addObject("user", user);
        maw.setViewName("user");
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createUser(ModelAndView maw) {
        maw.addObject("userReqDto", new UserReqDto());
        maw.addObject("roles", userRoleService.allUserRoles());
        maw.setViewName("user-add");
        return maw;
    }

    @PostMapping("/add")
    public ModelAndView createUser(@Valid UserReqDto userReqDto, BindingResult bindingResult,
                                    ModelAndView maw) {
        if (bindingResult.hasErrors()) {
            maw.addObject("userReqDto", userReqDto);
            maw.addObject("roles", userRoleService.allUserRoles());
            maw.addObject("org.springframework.validation.BindingResult.userReqDto",
                    bindingResult);
            maw.setViewName("user-add");
        } else {
            userService.create(userReqDto);
            maw.setViewName("redirect:/user");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable String id, ModelAndView maw) {
        maw.addObject("userReqDto", userService.getForUpdate(id));
        maw.addObject("userId", id);
        maw.addObject("roles", userRoleService.allUserRoles());
        maw.setViewName("user-update");
        return maw;
    }

    @PutMapping("/{id}")
    public ModelAndView updateUser(@PathVariable String id, @Valid UserReqDto userReqDto,
                                    BindingResult bindingResult, ModelAndView maw) {
        if (bindingResult.hasFieldErrors("username")) {
            List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                    .filter(fieldError -> !("Username already exists!").equals(fieldError.getDefaultMessage()) ||
                            !userService.getById(id).getUsername().equals(fieldError.getRejectedValue()))
                    .toList();
            bindingResult = new BeanPropertyBindingResult(userReqDto, "userReqDto");
            for (FieldError e:
                 errorsToKeep) {
                bindingResult.addError(e);
            }
        }
        if (bindingResult.hasErrors()) {
            maw.addObject("userReqDto", userReqDto);
            maw.addObject("userId", id);
            maw.addObject("roles", userRoleService.allUserRoles());
            maw.addObject(bindingResult);
            maw.setViewName("user-update");
        } else {
            userService.update(id, userReqDto);
            maw.setViewName("redirect:/user/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteUser(@PathVariable String id, ModelAndView maw) {
        userService.delete(id);
        maw.setViewName("redirect:/user");
        return maw;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserRoleService(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }
}
