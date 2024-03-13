package com.example.web_2.user;

import com.example.web_2.user.dto.ProfileUpdateDto;
import com.example.web_2.user.dto.UserPageResDto;
import com.example.web_2.user.dto.UserReqDto;
import com.example.web_2.user.dto.UserResDto;
import com.example.web_2.user.profilePicture.ProfilePictureService;
import com.example.web_2.user.user_role.UserRoleService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private UserRoleService userRoleService;
    private ProfilePictureService profilePictureService;
    private final static Logger LOG = LogManager.getLogger(Controller.class);

    @GetMapping
    public ModelAndView getUserPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            ModelAndView maw
    ) {
        LOG.log(Level.INFO, String.format("Try to show user page %d of size %d", page, pageSize));
        UserPageResDto userPage = userService.getPage(page, pageSize);
        LOG.log(Level.INFO, String.format("Show user page %d of %d", userPage.getPage(), userPage.getTotalPages()));
        maw.addObject("userPage", userPage);
        maw.setViewName("user-page");
        return maw;
    }

    @GetMapping("/profile")
    public ModelAndView profile(Principal principal, ModelAndView maw) {
        LOG.log(Level.INFO, String.format("Show profile for: %s", principal.getName()));
        maw.addObject("userProfile", userService.getUserProfile(principal.getName()));
        maw.addObject("profilePicture", profilePictureService.getProfilePicture(principal.getName()));
        maw.setViewName("profile");
        return maw;
    }

    @PostMapping("/profile/update/picture")
    public ModelAndView profilePictureUpdate(Principal principal, ModelAndView maw,
                                             @RequestParam MultipartFile picture) {
        LOG.log(Level.INFO, String.format("Try to change profile picture for user: %s", principal.getName()));
        maw.addObject("profilePicture",
                profilePictureService.changeProfilePicture(picture, principal.getName()));
        maw.addObject("userProfile", userService.getUserProfile(principal.getName()));
        maw.setViewName("profile");
        return maw;
    }

    @GetMapping("/profile/update")
    public ModelAndView profileUpdate(Principal principal, ModelAndView maw) {
        LOG.log(Level.INFO, String.format("Show profile update-form for: %s", principal.getName()));
        maw.addObject("profileUpdateDto", userService.getProfileForUpdate(principal.getName()));
        maw.setViewName("profile-update");
        return maw;
    }

    @PutMapping("/profile/update")
    public ModelAndView profileUpdate(@Valid ProfileUpdateDto dto, BindingResult bindingResult,
                                      Principal principal, ModelAndView maw) {
        LOG.log(Level.INFO, String.format("Try to update profile for: %s", principal.getName()));
        if (bindingResult.hasFieldErrors("username"))
            bindingResult = userService.validateUniqueName(principal, dto, bindingResult, "profileUpdateDto");
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userRegDto", "password",
                    "Password and confirmation must match"));
            bindingResult.addError(new FieldError("userRegDto", "confirmPassword",
                    "Password and confirmation must match"));
        }
        bindingResult = userService.validateOldPassword(dto.getOldPassword(), principal.getName(), bindingResult);
        if (bindingResult.hasErrors()) {
            maw.addObject("profileUpdateDto", dto);
            maw.addObject("org.springframework.validation.BindingResult.profileUpdateDto",
                    bindingResult);
            maw.setViewName("profile-update");
        } else {
            LOG.log(Level.INFO, String.format("Update profile for: %s", principal.getName()));
            userService.updateUserProfile(dto, principal.getName());
            maw.setViewName("redirect:/user/profile");
        }
        return maw;
    }

    @GetMapping("/{id}")
    public ModelAndView getUserById(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show user by ID: %s for: %s", id, principal.getName()));
        UserResDto user = userService.getById(id);
        maw.addObject("user", user);
        maw.setViewName("user");
        return maw;
    }

    @GetMapping("/add")
    public ModelAndView createUser(ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show user add-form for: %s", principal.getName()));
        maw.addObject("userReqDto", new UserReqDto());
        maw.addObject("roles", userRoleService.allUserRoles());
        maw.setViewName("user-add");
        return maw;
    }

    @PostMapping("/add")
    public ModelAndView createUser(@Valid UserReqDto userReqDto, BindingResult bindingResult,
                                   ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Try to add user for: %s", principal.getName()));
        if (bindingResult.hasErrors()) {
            maw.addObject("userReqDto", userReqDto);
            maw.addObject("roles", userRoleService.allUserRoles());
            maw.addObject("org.springframework.validation.BindingResult.userReqDto",
                    bindingResult);
            maw.setViewName("user-add");
        } else {
            LOG.log(Level.INFO, String.format("Add user by: %s", principal.getName()));
            userService.create(userReqDto);
            maw.setViewName("redirect:/user");
        }
        return maw;
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Show user update-form for: %s", principal.getName()));
        maw.addObject("userReqDto", userService.getForUpdate(id));
        maw.addObject("userId", id);
        maw.addObject("roles", userRoleService.allUserRoles());
        maw.setViewName("user-update");
        return maw;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView updateUser(@PathVariable String id, @Valid UserReqDto userReqDto,
                                   BindingResult bindingResult, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Try to update user %s for: %s", id, principal.getName()));
        if (bindingResult.hasFieldErrors("username")) {
            bindingResult = userService.validateUniqueName(id, userReqDto, bindingResult, "userReqDto");
        }
        if (bindingResult.hasErrors()) {
            maw.addObject("userReqDto", userReqDto);
            maw.addObject("userId", id);
            maw.addObject("roles", userRoleService.allUserRoles());
            maw.addObject("org.springframework.validation.BindingResult.userReqDto", bindingResult);
            maw.setViewName("user-update");
        } else {
            LOG.log(Level.INFO, String.format("Update user %s by: %s", id, principal.getName()));
            userService.update(id, userReqDto);
            maw.setViewName("redirect:/user/{id}");
        }
        return maw;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteUser(@PathVariable String id, ModelAndView maw, Principal principal) {
        LOG.log(Level.INFO, String.format("Delete user %s by: %s", id, principal.getName()));
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

    @Autowired
    public void setProfilePictureService(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }
}
