package com.example.web_2.baseEntity;

import com.example.web_2.user.UserService;
import com.example.web_2.user.dto.UserRegDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping("/")
public class AuthController {

    private UserService userService;

    @GetMapping("/register")
    public ModelAndView register(ModelAndView maw) {
        maw.addObject("userRegDto", new UserRegDto());
        maw.setViewName("register");
        return maw;
    }

    @PostMapping("register")
    public ModelAndView register(@Valid UserRegDto userRegDto, BindingResult bindingResult,
                                 HttpServletRequest request, ModelAndView maw) {
        if (!userRegDto.getPassword().equals(userRegDto.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userRegDto", "password",
                    "Password and confirmation must match"));
            bindingResult.addError(new FieldError("userRegDto", "confirmPassword",
                    "Password and confirmation must match"));
        }
        if (bindingResult.hasErrors()) {
            maw.addObject("userRegDto", userRegDto);
            maw.addObject("org.springframework.validation.BindingResult.userRegDto", bindingResult);
            maw.setViewName("register");
            return maw;
        }
        SecurityContext ctx = userService.register(userRegDto);
        request.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY, ctx);
        maw.setViewName("redirect:/");
        return maw;
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView maw) {
        maw.addObject("username", "");
        maw.addObject("badCredentials", false);
        maw.setViewName("login");
        return maw;
    }

    @PostMapping("/login-error")
    public ModelAndView onFailedLogin(@ModelAttribute("username") String username, ModelAndView maw) {
        maw.addObject("username", username);
        maw.addObject("badCredentials", true);
        maw.setViewName("login");
        return maw;
    }

    @GetMapping
    public ModelAndView home(ModelAndView maw) {
        maw.setViewName("index");
        return maw;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
